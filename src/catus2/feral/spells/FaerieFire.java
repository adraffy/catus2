package catus2.feral.spells;

import catus2.AbstractSpell;
import catus2.AttackT;
import catus2.OriginT;
import catus2.SchoolT;
import catus2.TargetStyle;
import catus2.Unit;
import catus2.feral.Feral;
import catus2.feral.FeralView;

public class FaerieFire extends AbstractSpell<Feral> {

    /*
    Faerie Fire
    35 yd range
    Instant
    Requires Druid (Feral, Guardian)
    Requires level 28
    Faeries surround the target, preventing stealth and invisibility.  Deals (32.5008% of Attack power) damage when cast from Bear Form.
    */
    
    public FaerieFire(Feral owner) {
        super(owner, TargetStyle.UNIT);
    }

    @Override
    public void casted(Unit target, double x, double y, int castTime, int powerCost) {
        if (o.isBearForm()) {            
            double dmg = o.getAP() * o.fgd.FF_BEAR_DAMAGE_PER_AP;            
            AttackT atk = o.yellow_spell(target, o.getCritChance());
            
            
            o.applyDamage(dmg, target, this, OriginT.SPELL, SchoolT.SCHOOLS_PHYSICAL, true);
        }
        FeralView v = o.getView(target);
        
        
    }

}
