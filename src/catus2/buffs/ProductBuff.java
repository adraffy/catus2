package catus2.buffs;

import catus2.AbstractView;
import catus2.ProductMap;
import catus2.Unit;

public class ProductBuff<M extends BuffModel, O extends Unit<V>, V extends AbstractView<O>> extends Buff<M,O,V> {

    public final ProductMap map;
    
    public double mod;
    
    public ProductBuff(M model, V view, ProductMap map) {
        super(model, view);
        this.map = map;
    }
    
    public void activated(boolean refreshed) {
        map.set(m.id, mod);
    }
    
    public void deactivated() {
        map.clear(m.id);
    }

}
