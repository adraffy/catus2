package catus2.feral.finishers;

import catus2.combat.HitEvent;
import catus2.Unit;
import catus2.feral.Feral;
import catus2.feral.FeralBleed;

public class Rip extends CatFinisher.Offensive {

    public Rip(Feral owner) {
        super(owner);
    }

    @Override
    public HitEvent finish(Unit target, int powerCost) {
        HitEvent event = HitEvent.melee(m, o, target, 0, 0);
        boolean bt = o.buff_bt.tryConsume();
        if (event.success()) {            
            FeralBleed bleed = o.getView(target).dot_rip;    
            bleed.snapshot = o.getBloodtalonsMod(bt) * o.getSnapshotableDamageMod() * o.power_combos.getPercent();
            bleed.activate();
        }             
        return event;
    }
    
    
}
