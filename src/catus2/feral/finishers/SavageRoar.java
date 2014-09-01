package catus2.feral.finishers;

import catus2.feral.Feral;
import catus2.TargetStyle;
import catus2.Unit;

public class SavageRoar extends CatFinisher {

    public SavageRoar(Feral owner) {
        super(owner, TargetStyle.NONE);
    }
    
    @Override
    public boolean finish(Unit target, int energyCost) {       
        activateWithCombos(o.power_combos.current);
        return true;
    }
    
    public void activateWithCombos(int combos) {
        o.buff_sr.activateForDuration(o.fgd.SR_DURATION_BASE + o.fgd.SR_DURATION_PER_COMBO * combos);
    }

}
