package catus2.buffs;

import catus2.AbstractView;
import catus2.Unit;

abstract public class AccDoT<M extends BuffModel,O extends Unit<O,V>,V extends AbstractView<O>> extends Buff<M,O,V> {

    public AccDoT(M model, V view) {
        super(model, view);
    }
    
    private double perTick;
 
    public void accumulate(double damage) {
        if (damage > 0) {
            double coeff = m.base_frequency / m.default_duration;          
            perTick = coeff * damage + (isActive() ? perTick / coeff : 0);
            activate();            
        }
    }
        
    @Override
    public void gotTick(double fraction) {        
        gotAccDamage(perTick * fraction);        
    }
    
    /*
    @Override
    public void gotDeactivated() {
        perTick = 0;
    }
    */
    
    abstract public void gotAccDamage(double damage);

}
