package catus2.buffs;

import catus2.AbstractView;
import catus2.Unit;

abstract public class AccumulateBuff<M extends BuffModel,O extends Unit<O,V>,V extends AbstractView<O>> extends Buff<M,O,V> {

    // this is the model for ignite, feral t17 4pc, etc...    
    
    // warning: 
    // cannot be timeless   
    // cannot be hasted
    
    /* 
    last @ t=2.0s
    part = t - last = 1.5s
    tick = (add * dur + tick * (end - last)) / (dur + part)
    tick = (300 * 6s + 300 * (6s-2s)) / (6s+1.5s) 

    add = freq/dur * amount        
    => dur * add = dur * freq / dur * amount = freq * amount
    */

    
    public AccumulateBuff(M model, V view) {
        super(model, view);
    }
    
    private double perTick;
 
    public void accumulate(double amount) {
        if (!isActive()) {
            perTick = 0; // shitty cleanup
        }
        activate(); // apply or refresh
        int part = o.world.timeline.clock - last_clock;
        perTick = (amount * m.base_frequency + perTick * (remainingTime() + part)) / (double)(m.default_duration + part);       
    }
        
    @Override
    public void gotTick(double fraction) {        
        gotTickPortion(perTick * fraction);        
    }
    
    abstract public void gotTickPortion(double amount);

}
