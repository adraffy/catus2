package catus2.feral;

import catus2.OriginT;
import catus2.SchoolT;
import catus2.buffs.AbstractBuffModel;

public class FeralBleed extends FeralBuff<FeralBleed.BuffModel> {

    static public class BuffModel extends AbstractBuffModel { 
        public BuffModel(int id) {
            super(id);
        }
        public double tickDamagePerAP;
    }
    
    public FeralBleed(BuffModel model, FeralView view) {
        super(model, view);
    }
            
    public double snapshot;
    
    @Override
    public void gotTick(double fraction) {
        double dmg = getTickDamage() * fraction / v.o.getSnapshotableDamageMod();
        v.o.applyDamage(dmg, v.unit, this, OriginT.BLEED, SchoolT.SCHOOLS_PHYSICAL, v.o.chanceMeleeCrit());
    }
    
    public double getTickDamage() {
        return v.getBleedMod() * snapshot * v.o.getRazorClawsMod() * v.o.getAP() * m.tickDamagePerAP;        
    }
    
}
