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
        return !o.buff_stampRoar.isActive() && (o.cfg.glyph_stampRoar || o.isCatOrBearForm()) && super.isCastable();
    }

    @Override
    public void casted(Unit target, double x, double y, int castTime, int powerCost) {  
        if (!o.cfg.glyph_stampRoar && !o.isCatOrBearForm()) {
            o.buff_form_bear.activate();
        }        
        UnitList list = o.world.retainUnitList();
        try {
            o.collectUnits(list, false, o.world_x, o.world_y, o.cfg.glyph_stampRoar ? o.fgd.STAMP_ROAR_GLYPH_RADIUS : o.fgd.STAMP_ROAR_RADIUS);
            for (Unit unit: list) {  
                unit.buff_stampRoar.mod = o.fgd.STAMP_ROAR_SPEED_BONUS;
                unit.buff_stampRoar.activateForDuration(o.fgd.STAMP_ROAR_DURATION);            
            }
        } finally {
            o.world.releaseUnitList(list);            
        }
    }

}
