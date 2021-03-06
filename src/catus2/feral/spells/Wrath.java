package catus2.feral.spells;

import catus2.AbstractSpell;
import catus2.combat.HitEvent;
import catus2.School;
import catus2.SpellId;
import catus2.SpellData;
import catus2.TargetStyle;
import catus2.Unit;
import catus2.feral.Feral;

public class Wrath extends AbstractSpell<Feral> {
    
    static public final SpellData spellModel = new SpellData(SpellId.Druid.WRATH, "Wrath", School.NATURE);

    public Wrath(Feral owner) {
        super(owner, TargetStyle.UNIT);
    }

    @Override
    public void casted(Unit target, double x, double y, int castTime, int powerCost) {
        HitEvent hit = HitEvent.harm(m, o, target, o.getCritChance(), true, true);
        if (hit.success()) {
            hit.base = o.getSP(m.school) * o.fgd.WRATH_DAMAGE_PER_SP;
        }
        o.computeAndRecord(hit);
    }

}
