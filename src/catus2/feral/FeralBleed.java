package catus2.feral;

import catus2.buffs.Buff;
import catus2.buffs.BuffModel;
import catus2.combat.HitEvent;

public class FeralBleed extends Buff<BuffModel,Feral,FeralView> {

    public FeralBleed(BuffModel model, FeralView view) {
        super(model, view);
    }
            
    public double snapshot;
    
    @Override
    public void gotTick(double fraction) {
        o.trigger_bonus_t17_2pc();
        HitEvent event = periodic();
        event.base = getTickDamage() * fraction / v.o.getSnapshotableDamageMod();
        v.check_bonus_pvp_wod_4pc(event);
        o.computeAndRecord(event);
    }
    
    public double getTickDamage() {
        return snapshot * getTickDamage(v.o, m);   
    }
    
    static public double getTickDamage(Feral o, BuffModel m) {
        return o.getRazorClawsMod() * o.getAP() * m.param;    
    }
    
}
