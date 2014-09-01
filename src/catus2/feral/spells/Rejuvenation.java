package catus2.feral.spells;

import catus2.AbstractSpell;
import catus2.TargetStyle;
import catus2.Unit;
import catus2.feral.Feral;
import catus2.feral.FeralView;

public class Rejuvenation extends AbstractSpell<Feral> {

    public Rejuvenation(Feral owner) {
        super(owner, TargetStyle.UNIT);
    }
    
    @Override
    public int getBaseLockoutTime() {
        return o.isCatForm() ? o.world.GCD_FLOOR : super.getBaseLockoutTime();
    }
    
    @Override
    public int getBasePowerCost() {
        return o.buff_hotw.isActive() ? 0 : super.getBasePowerCost();        
    }
    
    public void applyToUnit(Unit target) {
        FeralView view = o.getView(target);        
        view.hot_rejuv.activate();
    } 

    @Override
    public void casted(Unit target, double x, double y, int castTime, int powerCost) {        
        applyToUnit(target);
         if (o.cfg.talent_doc && target != o) {
            applyToUnit(o);
        }
        
    }
    
}
