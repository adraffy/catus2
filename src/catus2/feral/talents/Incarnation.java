package catus2.feral.talents;

import catus2.TargetStyle;
import catus2.Unit;
import catus2.feral.Feral;
import catus2.feral.spells.FeralSpell;

public class Incarnation extends FeralSpell {

    public Incarnation(Feral owner) {
        super(owner, TargetStyle.NONE);
    }

    @Override
    public void casted(Unit target, double x, double y, int castTime, int powerCost) {
        o.buff_catForm.activate();
        o.buff_form_kotj.activate();
    }

}
