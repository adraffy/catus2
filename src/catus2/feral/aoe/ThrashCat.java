package catus2.feral.aoe;

import catus2.Application;
import catus2.Origin;
import catus2.School;
import catus2.Unit;
import catus2.UnitList;
import catus2.feral.Feral;
import catus2.feral.FeralBleed;
import catus2.feral.FeralView;

public class ThrashCat extends BeastAoE {

    public ThrashCat(Feral owner) {
        super(owner);
    }

    @Override
    public void applyToUnits(UnitList list) {
        boolean bt = o.buff_bt.tryConsume();
        double crit = o.getCritChance();
        double dmg = o.fgd.THRASH_CAT_INITIAL_TICK_MOD * FeralBleed.getTickDamage(o, o.buffModel_thrash_cat);
        double snap = o.getBloodtalonsMod(bt) * o.getSnapshotableDamageMod();
        for (Unit unit: list) {
            Application de = o.tryApply(unit, this, Origin.MELEE_BLEED, School.PHYSICAL, crit, 0);
            if (de.hit()) {
                FeralView v = o.getView(unit);
                v.dot_thrash_bear.cancel();
                FeralBleed bleed = v.dot_thrash_cat;                
                bleed.snapshot = snap;
                bleed.activate();    
                de.base = dmg;
                v.executeBleed(de);                             
            }
        }
    }
    
}
