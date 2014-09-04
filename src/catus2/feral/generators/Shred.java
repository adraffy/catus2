package catus2.feral.generators;

import catus2.Unit;
import catus2.combat.HitEvent;
import catus2.feral.Feral;

public class Shred extends CatGenerator {

    public Shred(Feral owner) {
        super(owner);
    }

    @Override
    public HitEvent generate(Unit target) {
        o.trigger_glyph_sr();
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
        HitEvent event = HitEvent.melee(m, o, target, crit, 0);
        if (event.success()) {                
            if (target.isBleeding()) {
                mod *= o.fgd.SHRED_SWIPE_BLEED_BONUS;
            }     
            if (o.cfg.bonus_t14_2pc) {
                mod *= o.fgd.BONUS_T14_2PC_SHRED_DAMAGE_MOD;
            }
            if (o.buff_bonus_t16_2pc.isActive()) {
                mod *= o.fgd.BONUS_T16_2PC_SHRED_SWIPE_DAMAGE_MOD;
            }            
            event.base = mod * o.getBloodtalonsMod(bt) * o.getNormalizedWeaponDamage() * o.fgd.SHRED_DAMAGE_PER_DPS;         
            if (event.crit() && o.cfg.bonus_pvp_wod_4pc) {
                o.getView(target).debuff_bonus_pvp_wod_4pc.activate();
            }
        }    
        return event;
    }

}
