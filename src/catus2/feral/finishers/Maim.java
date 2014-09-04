package catus2.feral.finishers;

import catus2.Application;
import catus2.combat.CrowdControl;
import catus2.combat.HitEvent;
import catus2.School;
import catus2.SpellId;
import catus2.SpellData;
import catus2.TargetStyle;
import catus2.Unit;
import catus2.feral.Feral;

public class Maim extends CatFinisher {

    public Maim(Feral owner) {
        super(owner, TargetStyle.UNIT);
    }

    @Override
    public HitEvent finish(Unit target, int powerCost) {        
        HitEvent event = HitEvent.melee(m, o, target, o.getCritChance(), 0);
        if (event.success()) {
            event.base = o.fgd.MAIM_DAMAGE_PER_DPS * o.getNormalizedWeaponDamage();            
            // we should apply a debuff, but there's no need currently
            target.applyCrowdControl(CrowdControl.STUN, (int)(o.power_energy.current * o.fgd.MAIM_STUN_PER_CP));            
        }
        return event;
    }

}
