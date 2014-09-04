package catus2.feral.spells;

import catus2.AbstractSpell;
import catus2.TargetStyle;
import catus2.Unit;
import catus2.feral.Feral;

public class Moonfire extends AbstractSpell<Feral> {

    public Moonfire(Feral owner) {
        super(owner, TargetStyle.UNIT);
    }

    @Override
    public void casted(Unit target, double x, double y, int castTime, int powerCost) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
