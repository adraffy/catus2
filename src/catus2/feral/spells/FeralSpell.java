package catus2.feral.spells;

import catus2.AbstractSpell;
import catus2.TargetStyle;
import catus2.feral.Feral;

public abstract class FeralSpell extends AbstractSpell<Feral> {

    public boolean consumes_omen;
    
    public FeralSpell(Feral owner, TargetStyle targetStyle) {
        super(owner, targetStyle);
    }
    
    
    @Override
    public int getPowerCost() {
        return consumes_omen && o.buff_ooc.isActive() ? 0 : super.getPowerCost();
    }
    
    public void consumeOmen() {
        if (getBasePowerCost() > 0) {
            o.buff_ooc.tryConsume();
        }
    }
        
    
}
