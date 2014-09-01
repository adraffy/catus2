package catus2.buffs;

import catus2.AbstractView;
import catus2.Tick;
import catus2.Timeline;
import catus2.Unit;
import catus2.World;

abstract public class AbstractBuff<M extends AbstractBuffModel,O extends Unit<V>,V extends AbstractView<O>> {
    
    public final M m;
    public final V v;
       
    public AbstractBuff(M model, V view) {
        this.m = model;
        this.v = view;
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
        gotDeactivated();
    }
    
    
    public void activate() { activateForDurationWithStacks(m.default_duration, m.default_stacks); }    
    public void activateForDuration(int dur) { activateForDurationWithStacks(dur, m.default_stacks); }
    public void activateAndAddStacks(int stacks) { 
        activateForDurationWithStacks(m.default_duration, Math.min((current_state ? current_stacks : m.default_stacks) + stacks, m.maximum_stacks)); 
    }
    public void activateForDurationWithStacks(int duration, int stacks) {
        if (stacks < 1 || stacks > m.maximum_stacks) {
            throw new IllegalStateException("Stacks: " + stacks);
        }
        if (duration < 0) {
            throw new IllegalStateException("Duration: " + duration);
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
            gotRefreshed();
        } else {            
            current_state = true;
            if (duration > 0) {
                timeline().schedAt(duration, duration_fader);
            }          
            gotActivated();
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
         
    private double last_frequency;
    private int last_clock;
    
    private void scheduleTick() {
        last_frequency = getFrequency();        
        last_clock = timeline().clock;      
        timeline().schedIn((int)last_frequency, tick_fader);
    }
            
    public void gotActivated() {}
    public void gotRefreshed() {}
    public void gotTick(double fraction) {}
    public void gotDeactivated() {}
    
    public final Tick duration_fader = new Tick() {
        @Override
        public void run() {
            if (timeline().cancel(tick_fader)) {
                ++tick_index;
                gotTick((timeline().clock - last_clock) / last_frequency);
            }      
            gotDeactivated();
        }        
    };
    
    
    public final Tick tick_fader = new Tick() {
        @Override
        public void run() {
            ++tick_index;
            gotTick(1);
            if (current_state) {
                scheduleTick();
            }
        }
    };
    
}
