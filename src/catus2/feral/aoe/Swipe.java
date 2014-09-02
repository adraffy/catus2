package catus2.feral.aoe;

import catus2.Application;
import catus2.Origin;
import catus2.School;
import catus2.Unit;
import catus2.UnitList;
import catus2.feral.Feral;

public class Swipe extends BeastAoE {

    public Swipe(Feral owner) {
        super(owner);
    }

    @Override
    public void applyToUnits(UnitList list) {
        boolean anyHit = false;
        boolean anyCrit = false;
        double crit = o.getCritChance();
        if (o.buff_bonus_t15_4pc.tryConsume()) {
            crit += o.fgd.BONUS_T15_4PC_EXTRA_CRIT;
        }
        boolean bt = o.buff_bt.tryConsume();
        double dmg = o.getBloodtalonsMod(bt) * o.getNormalizedWeaponDamage() * o.fgd.SWIPE_DAMAGE_MOD;       
        if (o.buff_bonus_t16_2pc.isActive()) {
            dmg *= o.fgd.BONUS_T16_2PC_SHRED_SWIPE_DAMAGE_MOD;
        }
        for (Unit unit: list) {                     
            Application app = o.tryApply(unit, this, Origin.MELEE, School.PHYSICAL, crit, 0);
            if (app.hit()) {
                anyHit = true;
                app.base = dmg;
                if (unit.isBleeding()) {
                    app.base *= o.fgd.SHRED_SWIPE_BLEED_BONUS;
                }                   
                anyCrit |= app.crit();
                o.executeApply(app);
            }            
        }
        if (anyHit) {
            o.addGeneratedCombo(anyCrit);
        }  
    }
    
}
