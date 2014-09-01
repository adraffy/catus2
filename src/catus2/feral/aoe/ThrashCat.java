package catus2.feral.aoe;

import catus2.AttackT;
import catus2.OriginT;
import catus2.SchoolT;
import catus2.TargetStyle;
import catus2.Unit;
import catus2.UnitList;
import catus2.feral.Feral;
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
        double mod = o.getBloodtalonsMod(bt); // snapshot damage mod will get automatically applied by school
        double snap = mod * o.getSnapshotableDamageMod();
        for (Unit unit: list) {
            AttackT atk = o.yellow_melee(unit, crit);
            if (atk.landed) {
                FeralView target = o.getView(unit);                
                double dmg = mod * target.getBleedMod() * o.getRazorClawsMod() * o.getAP() * o.fgd.THRASH_CAT_INITIAL_DAMAGE_PER_AP;
                o.applyDamage(dmg, unit, this, OriginT.BLEED, SchoolT.SCHOOLS_PHYSICAL, atk == AttackT.CRIT);
                target.dot_thrash_cat.snapshot = snap;
                target.dot_thrash_cat.activate();                
            }
        }
    }
    
}
