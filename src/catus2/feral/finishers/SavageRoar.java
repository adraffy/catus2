package catus2.feral.finishers;

import catus2.Application;
import catus2.combat.HitEvent;
import catus2.Origin;
import catus2.School;
import catus2.SpellId;
import catus2.SpellData;
import catus2.TargetStyle;
import catus2.Unit;
import catus2.feral.Feral;

public class SavageRoar extends CatFinisher {
    
    static public final SpellData SPELL = new SpellData(SpellId.Druid.Feral.SR, "Savage Roar", School.PHYSICAL);

    public SavageRoar(Feral owner) {
        super(owner, TargetStyle.NONE);
    }
    
    @Override
    public HitEvent finish(Unit target, int energyCost) {       
        activateWithCombos(o.power_combos.current);   
        return HitEvent.self_apply(m, o);
    }
    
    public void activateWithCombos(int combos) {
        o.buff_sr.activateForDuration(o.fgd.SR_DURATION_BASE + o.fgd.SR_DURATION_PER_COMBO * combos);
    }

}
