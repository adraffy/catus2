package catus2.feral.generators;

import catus2.AttackT;
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
        AttackT atk = generate(o.createView(target));
        if (atk.landed) {                
            int c = 1;
            if (atk == AttackT.CRIT && !o.cfg.disable_primalFury) {
                c++;
            }
            o.power_combos.add(c);                
        } else {
            o.refundEnergyCost(powerCost);
        }            
    }          
    
    public abstract AttackT generate(FeralView target);
    
}
