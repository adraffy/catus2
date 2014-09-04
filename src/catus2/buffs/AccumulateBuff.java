package catus2.buffs;

import catus2.AbstractView;
import catus2.Unit;

abstract public class AccumulateBuff<M extends BuffModel,O extends Unit<O,V>,V extends AbstractView<O>> extends Buff<M,O,V> {

    // this is the model for ignite, feral t17 4pc, etc...        
   
    // warning: 
    // cannot be timeless   
    // cannot be hasted
    
    public AccumulateBuff(M model, V view) {
        super(model, view);
    }
    
    private double tick;
 
    public void accumulate(double amount) {
        if (!isActive()) {
            tick = 0; // shitty cleanup
        }
        activate(); // apply or refresh
        int part = o.world.timeline.clock - last_clock;
        tick = (amount * m.base_frequency + tick * (remainingTime() + part)) / (double)(m.default_duration + part);       
    }
        
    @Override
    public void gotTick(double fraction) {        
        gotChunk(tick * fraction);        
    }
    
    abstract public void gotChunk(double amount);

}
