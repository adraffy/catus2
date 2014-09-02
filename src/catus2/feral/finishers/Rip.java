package catus2.feral.finishers;

import catus2.Application;
import catus2.Origin;
import catus2.School;
import catus2.Unit;
import catus2.feral.Feral;
import catus2.feral.FeralBleed;

public class Rip extends CatFinisher.Offensive {

    public Rip(Feral owner) {
        super(owner);
    }

    @Override
    public Application finish(Unit target, int powerCost) {
        Application app = o.tryApply(target, this, Origin.MELEE, School.PHYSICAL, 0, 0);
        boolean bt = o.buff_bt.tryConsume();
        if (app.hit()) {            
            FeralBleed bleed = o.getView(target).dot_rip;    
            bleed.snapshot = o.getBloodtalonsMod(bt) * o.getSnapshotableDamageMod() * o.power_combos.getPercent();
            bleed.activate();
        }             
        return app;
    }
    
    
}
