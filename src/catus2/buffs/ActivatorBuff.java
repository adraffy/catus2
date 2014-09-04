package catus2.buffs;

import catus2.AbstractView;
import catus2.Unit;

abstract public class ActivatorBuff<M extends BuffModel,O extends Unit<O,V>,V extends AbstractView<O>> extends Buff<M,O,V> {

    /*
    @FunctionalInterface
    static public interface ActivateListener {
        void stateChanged(boolean state);
    }
    
    public final ActivateListener listener;
    */
    
    public ActivatorBuff(M model, V view) { //, ActivateListener listener) {
        super(model, view);
        //this.listener = listener;        
    }
    
    /*
    @Override
    public void gotActivated(boolean ignore) {
        listener.stateChanged(true);
    }
    
    @Override
    public void gotDeactivated() {
        listener.stateChanged(false);
    }
    */
    
    @Override
    public void gotActivated(boolean ignore) {
        stateChanged(true);
    }
    
    @Override
    public void gotDeactivated() {
        stateChanged(false);
    }
    
    public abstract void stateChanged(boolean state);
    
}
