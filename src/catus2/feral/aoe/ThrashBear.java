package catus2.feral.aoe;

import catus2.TargetStyle;
import catus2.Unit;
import catus2.feral.Feral;
import catus2.feral.spells.FeralSpell;

public class ThrashBear extends FeralSpell {

    public ThrashBear(Feral owner) {
        super(owner, TargetStyle.NONE);
    }

    @Override
    public void casted(Unit target, double x, double y, int castTime, int powerCost) {
        // wut
    }
    
}
