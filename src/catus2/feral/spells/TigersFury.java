package catus2.feral.spells;

import catus2.TargetStyle;
import catus2.Unit;
import catus2.feral.Feral;

public class TigersFury extends FeralSpell {

    public TigersFury(Feral owner) {
        super(owner, TargetStyle.NONE);
    }
    
    @Override
    public boolean isCastable() { 
        return o.isCatForm() && super.isCastable(); 
    }

    @Override
    public void casted(Unit target, double x, double y, int castTime, int powerCost) {   
        o.power_energy.add(o.fgd.TF_ENERGY_BONUS);
        o.buff_tf.activate();
    }
    
}
