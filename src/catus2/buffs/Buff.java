package catus2.buffs;

import catus2.AbstractView;
import catus2.Tick;
import catus2.Timeline;
import catus2.Unit;
import catus2.World;
import catus2.combat.HitEvent;

public class Buff<M extends BuffModel,O extends Unit<O,V>,V extends AbstractView<O>> {
    
    public final M m;
    public final V v;
    public final O o;
       
    public Buff(M model, V view) {
        this.m = model;
        this.v = view;
        o = v.o; // reduce indirection?
    }
    
    public boolean current_state;
    public int current_stacks;    
    public int current_duration;
    public int tick_index;
    
    public final World world() { return v.o.world; }
    public final Timeline timeline() { return v.o.world.timeline; }
    
    public boolean isActive() { 
        return current_state;
    }
    
    public double getFrequency() {
        return m.hasted ? m.base_frequency / v.o.getHasteMod() : m.base_frequency;
    }
    
    public void cancel() {
        if (current_state) {
            deactivate();
        }
    }
    
    private void deactivate() {
        timeline().cancel(duration_fader);
        timeline().cancel(tick_fader);
        if (m.unique) {
            v.u.uniqueBuffMap.remove(m.id());
        }
        gotDeactivated();
    }
    
    
    public void activate() { activateForDurationWithStacks(m.default_duration, m.default_stacks); }    
    public void activateForDuration(int dur) { activateForDurationWithStacks(dur, m.default_stacks); }
    public void activateAndAddStacks(int stacks) { 
        activateForDurationWithStacks(m.default_duration, Math.min((current_state ? current_stacks : m.default_stacks) + stacks, m.maximum_stacks)); 
    }
    public void activateForDurationWithStacks(int duration, int stacks) {
        if (!m.enabled) {
            return;
        }
        if (stacks < 1 || stacks > m.maximum_stacks) {
            throw new IllegalStateException("Stacks: " + stacks);
        }
        if (duration < 0) {
            throw new IllegalStateException("Duration: " + duration);
        }        
        if (m.unique) {
            Buff buff = (Buff)v.u.uniqueBuffMap.put(m.id(), this); // java generics bug
            if (buff != null && buff != this) {
                buff.cancel();
            }
        }
        current_stacks = stacks;
        if (current_state) {
            if (current_duration == 0) {              
                if (duration > 0) { // infinite -> finite
                    timeline().schedAt(duration, duration_fader);
                }
            } else if (duration == 0) { // finite -> infinite
                timeline().cancel(duration_fader);
            } else { // finite -> finite
                if (m.pandemic) {
                    int remaining = timeline().timeUntil(duration_fader);
                    timeline().schedIn(Math.min((int)(remaining * world().pandemicCoeff), remaining + duration), duration_fader);                
                } else {
                    timeline().schedAt(duration, duration_fader);
                }                
            }
            gotActivated(true);
        } else {            
            current_state = true;
            if (duration > 0) {
                timeline().schedAt(duration, duration_fader);
            }          
            gotActivated(false);
        }     
        if (m.base_frequency > 0) {
            tick_index = 0;
            scheduleTick();
        }  
    }
    
   
    
    public int deltaStacks(int delta) {
        if (!current_state) {
            throw new IllegalStateException("Not active");
        }
        if (delta == 0) {
            return 0;
        }        
        int prev = current_stacks;
        current_stacks = Math.min(m.maximum_stacks, prev + delta);
        return current_stacks - prev;
    }
    
    public boolean tryConsume() {
        return partiallyConsumeStacks(1) > 0;
    }
    
    public int partiallyConsumeStacks(int max) {
        if (current_state) {
            int prev = current_stacks;
            if (prev > max) {
                current_stacks = prev - max;
                return max;
            } else {
                current_stacks = 0;      
                deactivate();
                return prev;
            } 
        }
        return 0;
    }
         
    protected double last_frequency;
    protected int last_clock;
    
    private void scheduleTick() {
        last_frequency = getFrequency();        
        last_clock = timeline().clock;      
        timeline().schedIn((int)last_frequency, tick_fader);
    }
            
    public void gotActivated(boolean refreshed) {}
    public void gotTick(double fraction) {} // guarenteed target is alive
    public void gotDeactivated() {}
    
    public double getRemainingTime() {
        return current_state ? current_duration == 0 ? Double.POSITIVE_INFINITY : remainingTime() : Double.NaN;
    }
    
    // assumes you are scheduled
    protected int remainingTime() {
        return timeline().timeUntil(duration_fader);
    }
    
    protected final Tick duration_fader = new Tick() {
        @Override
        public void run() {
            if (timeline().cancel(tick_fader) && !v.u.isDead()) {
                ++tick_index;
                gotTick((timeline().clock - last_clock) / last_frequency);
            }      
            deactivate();
        }        
    };
        
    protected final Tick tick_fader = new Tick() {
        @Override
        public void run() {
            if (v.u.isDead()) {
                return;
            }
            ++tick_index;
            gotTick(1);
            if (current_state) {
                scheduleTick();
            }
        }
    };
 
    
    // HitEvent helprs
    public HitEvent periodic() { return periodic(o.getCritChance()); }
    public HitEvent periodic(double critChance) { return HitEvent.periodic(m.rx, o, v.o, critChance); }
    
}
