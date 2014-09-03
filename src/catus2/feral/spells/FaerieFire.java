package catus2.feral.spells;

import catus2.AbstractSpell;
import catus2.HitEvent;
import catus2.School;
import catus2.SpellId;
import catus2.SpellModel;
import catus2.TargetStyle;
import catus2.Unit;
import catus2.feral.Feral;

public class FaerieFire extends AbstractSpell<Feral> {

    static public final SpellModel SPELL = new SpellModel(SpellId.Druid.FF, "Faerie Fire", School.NATURE);
    
    /*
    Faerie Fire
    35 yd range
    Instant
    Requires Druid (Feral, Guardian)
    Requires level 28
    Faeries surround the target, preventing stealth and invisibility.  
    Deals (32.5008% of Attack power) damage when cast from Bear Form.
    */
    
    public FaerieFire(Feral owner) {
        super(owner, TargetStyle.UNIT);
    }

    @Override
    public void casted(Unit target, double x, double y, int castTime, int powerCost) {      
        boolean damaging = o.isBearForm(); 
        HitEvent hit = HitEvent.harm(m, o, target, damaging ? o.getCritChance() : 0, true, true);
        if (hit.landed()) {
            if (damaging) {
                hit.base = o.getAP() * o.fgd.FF_BEAR_DAMAGE_PER_AP;   
            }
            o.getView(hit.target).debuff_ff.activate();   
        }
        o.computeAndRecord(hit);
    }        

}
