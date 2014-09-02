package catus2.feral.aoe;

import catus2.AbstractSpell;
import catus2.TargetStyle;
import catus2.Unit;
import catus2.UnitList;
import catus2.feral.Feral;

abstract public class BeastAoE extends AbstractSpell<Feral> {

    public BeastAoE(Feral owner) {
        super(owner, TargetStyle.NONE);
    }

    @Override
    public void casted(Unit target, double x, double y, int castTime, int powerCost) {
        UnitList list = o.world.retainUnitList();
        try {
            o.collectUnits(list, true, o.world_x, o.world_y, getRangeMax());
            applyToUnits(list);
        } finally {
            o.world.releaseUnitList(list);
        }
    }
    
    abstract public void applyToUnits(UnitList list);

}
