package catus2.feral.finishers;

import catus2.AttackT;
import catus2.Unit;
import catus2.feral.Feral;
import catus2.feral.FeralBleed;

public class Rip extends CatFinisher.Offensive {

    public Rip(Feral owner) {
        super(owner);
    }

    @Override
    public boolean finish(Unit target, int powerCost) {
        AttackT atk = o.yellow_melee(target, 0);
        boolean bt = o.buff_bt.tryConsume();
        if (atk.landed) {            
            FeralBleed bleed = o.getUnitData(target).dot_rip;    
            bleed.snapshot = o.getBloodtalonsMod(bt) * o.getSnapshotableDamageMod() * o.power_combos.getPercent();
            bleed.activate();
        }             
        return atk.landed;
    }
    
    
}
