package catus2.feral.spells;

import catus2.AbstractSpell;
import catus2.TargetStyle;
import catus2.Unit;
import catus2.UnitList;
import catus2.feral.Feral;

public class StampedingRoar extends AbstractSpell<Feral> {

    public StampedingRoar(Feral owner) {
        super(owner, TargetStyle.NONE);
    }
    
    @Override
    public boolean isCastable() {
        return !o.selfView.buff_stampRoar.isActive() && (o.cfg.glyph_stampRoar || o.isCatOrBearForm()) && super.isCastable();
    }

    @Override
    public void casted(Unit target, double x, double y, int castTime, int powerCost) {  
        if (!o.cfg.glyph_stampRoar && !o.isCatOrBearForm()) {
            o.buff_form_bear.activate();
        }        
        UnitList list = o.world.retainUnitList();
        try {
            o.collectUnits(list, false, o.world_x, o.world_y, getRangeMax());
            for (Unit unit: list) {  
                o.getView(unit).buff_stampRoar.activate();
            }
        } finally {
            o.world.releaseUnitList(list);            
        }
    }

}
