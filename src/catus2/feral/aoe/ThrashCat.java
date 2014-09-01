package catus2.feral.aoe;

import catus2.AttackT;
import catus2.OriginT;
import catus2.SchoolT;
import catus2.TargetStyle;
import catus2.Unit;
import catus2.UnitList;
import catus2.feral.Feral;
import catus2.feral.FeralBleed;
import catus2.feral.FeralView;
import catus2.feral.spells.FeralSpell;

public class ThrashCat extends FeralSpell {

    public ThrashCat(Feral owner) {
        super(owner, TargetStyle.NONE);
    }

    @Override
    public void casted(Unit target, double x, double y, int castTime, int powerCost) {
        UnitList list = o.world.retainUnitList();
        try {
            o.collectUnits(list, true, o.world_x, o.world_y, getRangeMax());
            applyToUnits(list);
        } finally {
            o.world.releaseUnitList(list);
        }
    }
    
    public void applyToUnits(UnitList list) {
        double crit = o.getCritChance();
        boolean bt = o.buff_bt.tryConsume();
        double snap = o.getBloodtalonsMod(bt) * o.getSnapshotableDamageMod();
        for (Unit unit: list) {
            AttackT atk = o.yellow_melee(unit, crit);
            if (atk.landed) {
                FeralView v = o.getView(unit);
                v.dot_thrash_bear.cancel();
                FeralBleed bleed = v.dot_thrash_cat;                
                bleed.snapshot = snap;
                bleed.activate();                
                o.applyDamage(o.fgd.THRASH_CAT_INITIAL_TICK_MOD * bleed.getTickDamage(), unit, this, OriginT.BLEED, SchoolT.SCHOOLS_PHYSICAL, atk == AttackT.CRIT);                                
            }
        }
    }
    
}
