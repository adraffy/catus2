package catus2.feral.generators;

import catus2.Application;
import catus2.TargetStyle;
import catus2.Unit;
import catus2.feral.Feral;
import catus2.feral.FeralView;
import catus2.feral.spells.FeralSpell;

public abstract class CatGenerator extends FeralSpell {

    public CatGenerator(Feral owner) {
        super(owner, TargetStyle.UNIT);            
        consumes_omen = true;        
    }
    
    @Override
    public boolean isCastable() { 
        return o.isCatForm() && super.isCastable(); 
    }
    
    @Override
    public void casted(Unit target, double x, double y, int castTime, int powerCost) {
        Application app = generate(o.getView(target));
        o.executeApply(app);
        if (app.hit()) {                
            o.addGeneratedCombo(app.crit());
        } else {
            o.refundEnergyCost(powerCost);
        }            
    }          
    
    public abstract Application generate(FeralView target);
    
}
