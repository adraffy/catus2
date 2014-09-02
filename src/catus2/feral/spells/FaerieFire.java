package catus2.feral.spells;

import catus2.AbstractSpell;
import catus2.Application;
import catus2.Origin;
import catus2.School;
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
    Faeries surround the target, preventing stealth and invisibility.  
    Deals (32.5008% of Attack power) damage when cast from Bear Form.
    */
    
    public FaerieFire(Feral owner) {
        super(owner, TargetStyle.UNIT);
    }

    @Override
    public void casted(Unit target, double x, double y, int castTime, int powerCost) {
        Application app = o.tryApply(target, this, Origin.SPELL, School.PHYSICAL, 0, 0);
        if (app.hit()) {                
            if (o.isBearForm()) {                                    
                app.base = o.getAP() * o.fgd.FF_BEAR_DAMAGE_PER_AP;                    
            }
            o.getView(target).debuff_ff.activate();        
        }
        o.executeApply(app);
    }        

}
