package catus2.buffs;

import catus2.AbstractView;
import catus2.ModMap;
import catus2.Unit;

public class ModBuff<M extends BuffModel, O extends Unit<O,V>, V extends AbstractView<O>> extends Buff<M,O,V> {

    public final ModMap map;
    
    public ModBuff(M model, V view, ModMap map) {
        super(model, view);
        this.map = map;
    }
    
    public void activated(boolean refreshed) {
        map.set(m.id, m.param);
    }
    
    public void deactivated() {
        map.remove(m.id);
    }

}
