package catus2.feral.aoe;

import catus2.AttackT;
import catus2.OriginT;
import catus2.SchoolT;
import catus2.TargetStyle;
import catus2.Unit;
import catus2.UnitList;
import catus2.feral.Feral;
import catus2.feral.spells.FeralSpell;
import java.util.ArrayList;

public class Swipe extends FeralSpell {

    public Swipe(Feral owner) {
        super(owner, TargetStyle.NONE);
    }

    final ArrayList<Unit> buf = new ArrayList<>();
    
    @Override
    public void casted(Unit target, double x, double y, int castTime, int powerCost) {
        UnitList list = o.world.retainUnitList();
        boolean anyHit = false;
        boolean anyCrit = false;
        double crit = o.getCritChance();
        buf.clear();        
        o.collectUnits(buf, true, o.world_x, o.world_y, getRangeMin(), getRangeMax(), o.world_dir, range_sweep);
        for (Unit unit: buf) {                     
            AttackT atk = o.yellow_melee(unit, crit);
            if (atk.landed) {
                anyHit = true;
                double mod = 1;
                if (unit.isBleeding()) {
                    mod *= o.fgd.SHRED_SWIPE_BLEED_BONUS;
                }   
                double dmg = mod * o.getNormalizedWeaponDamage() * o.fgd.SWIPE_DAMAGE_MOD;
                boolean didCrit = atk == AttackT.CRIT;
                anyCrit |= didCrit;
                o.applyDamage(dmg, unit, this, OriginT.MELEE, SchoolT.SCHOOLS_PHYSICAL, didCrit);                
            }            
        }
        buf.clear();
        if (anyCrit) {
            o.power_combos.add(1);
        }
    }
    
}
