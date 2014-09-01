package catus2.feral.aoe;

import catus2.AttackT;
import catus2.OriginT;
import catus2.SchoolT;
import catus2.TargetStyle;
import catus2.Unit;
import catus2.UnitList;
import catus2.feral.Feral;
import catus2.feral.spells.FeralSpell;

public class Swipe extends FeralSpell {

    public Swipe(Feral owner) {
        super(owner, TargetStyle.NONE);
    }
    
    @Override
    public void casted(Unit target, double x, double y, int castTime, int powerCost) {
        boolean anyHit = false;
        boolean anyCrit = false;
        double crit = o.getCritChance();
        boolean bt = o.buff_bt.tryConsume();
        boolean t16_2pc = o.buff_bonus_t16_2pc.isActive();        
        UnitList list = o.world.retainUnitList();
        o.collectUnits(list, true, o.world_x, o.world_y, getRangeMax());
        double mod = o.getBloodtalonsMod(bt);       
        if (t16_2pc) {
            mod *= o.fgd.BONUS_T16_2PC_SHRED_SWIPE_DAMAGE_MOD;
        }
        for (Unit unit: list) {                     
            AttackT atk = o.yellow_melee(unit, crit);
            if (atk.landed) {
                anyHit = true;
                double dmg = mod * o.getNormalizedWeaponDamage() * o.fgd.SWIPE_DAMAGE_MOD;
                if (unit.isBleeding()) {
                    dmg *= o.fgd.SHRED_SWIPE_BLEED_BONUS;
                }                   
                boolean didCrit = atk == AttackT.CRIT;
                anyCrit |= didCrit;
                o.applyDamage(dmg, unit, this, OriginT.MELEE, SchoolT.SCHOOLS_PHYSICAL, didCrit);                
            }            
        }
        o.world.releaseUnitList(list);
        if (anyHit) {
            o.power_combos.add(anyCrit && !o.cfg.disable_primalFury ? 2 : 1);            
        }        
    }
    
}
