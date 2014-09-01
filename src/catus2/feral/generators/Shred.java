package catus2.feral.generators;

import catus2.feral.generators.CatGenerator;
import catus2.AttackT;
import catus2.OriginT;
import catus2.SchoolT;
import catus2.feral.Feral;
import catus2.Unit;
import catus2.feral.FeralView;

public class Shred extends CatGenerator {

    public Shred(Feral owner) {
        super(owner);
    }

    @Override
    public AttackT generate(FeralView target) {
        o.applySavageRoarGlyph();
        double mod = 1;
        double crit = o.getCritChance();
        // i dont know the order for these two crit modifiers
        if (o.buff_bonus_t15_4pc.tryConsume()) {
            crit += o.fgd.BONUS_T15_4PC_EXTRA_CRIT;
        }
        if (o.isStealthed()) {
            mod *= o.fgd.SHRED_STEALTH_DAMAGE_MOD;
            crit += crit;            
        }                     
        boolean bt = o.buff_bt.tryConsume();
        AttackT atk = o.yellow_melee(target.unit, crit);
        if (atk.landed) {                
            if (target.unit.isBleeding()) {
                mod *= o.fgd.SHRED_SWIPE_BLEED_BONUS;
            }     
            if (o.cfg.bonus_t14_2pc) {
                mod *= o.fgd.BONUS_T14_2PC_SHRED_DAMAGE_MOD;
            }
            if (o.buff_bonus_t16_2pc.isActive()) {
                mod *= o.fgd.BONUS_T16_2PC_SHRED_SWIPE_DAMAGE_MOD;
            }            
            double dmg = mod * o.getBloodtalonsMod(bt) * o.getNormalizedWeaponDamage() * o.fgd.SHRED_DAMAGE_PER_DPS;         
            boolean didCrit = atk == AttackT.CRIT;
            o.applyDamage(dmg, target.unit, this, OriginT.MELEE, SchoolT.SCHOOLS_PHYSICAL, didCrit);
            if (didCrit && o.cfg.bonus_pvp_wod_4pc) {
                target.debuff_pvp_wod_4pc.activate();
            }
        }    
        return atk;
    }

}
