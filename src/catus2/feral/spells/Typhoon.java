package catus2.feral.spells;

import catus2.AbstractSpell;
import catus2.GameHelp;
import catus2.TargetStyle;
import catus2.Unit;
import catus2.UnitList;

public class Typhoon extends AbstractSpell {

    public Typhoon(Unit owner) {
        super(owner, TargetStyle.NONE);
    }

    @Override
    public void casted(Unit target, double x, double y, int castTime, int powerCost) {
        UnitList list = o.world.retainUnitList();
        o.collectUnits(list, true, o.world_x, o.world_y, 0, 15, o.world_dir, GameHelp.HALF_PI);
        for (Unit unit: list) {            
            //u.applyKnockback(5);            
        }
        o.world.releaseUnitList(list);
    }
    
}
