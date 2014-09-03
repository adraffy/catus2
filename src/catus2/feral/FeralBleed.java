package catus2.feral;

import catus2.Application;
import catus2.Origin;
import catus2.School;
import catus2.buffs.Buff;
import catus2.buffs.BuffModel;

public class FeralBleed extends Buff<BuffModel,Feral,FeralView> {

    public FeralBleed(BuffModel model, FeralView view) {
        super(model, view);
    }
            
    public double snapshot;
    
    @Override
    public void gotTick(double fraction) {
        Application app = v.tryApply(this, Origin.BLEED, School.PHYSICAL);
        app.base = getTickDamage() * fraction / v.o.getSnapshotableDamageMod();
        v.executeBleed(app);        
    }
    
    public double getTickDamage() {
        return snapshot * getTickDamage(v.o, m);   
    }
    
    static public double getTickDamage(Feral o, BuffModel m) {
        return o.getRazorClawsMod() * o.getAP() * m.param;    
    }
    
}
