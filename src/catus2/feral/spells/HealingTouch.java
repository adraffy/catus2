package catus2.feral.spells;

import catus2.Origin;
import catus2.feral.Feral;
import catus2.School;
import catus2.TargetStyle;
import catus2.Unit;

public class HealingTouch extends FeralSpell {

    public HealingTouch(Feral owner) {
        super(owner, TargetStyle.UNIT);
    }
    
    @Override
    public void willStartCasting() {
        if (!o.buff_ps.isActive() && !o.isManBearPigForm()) {
            o.cancelForm();
        }
    }
    
    @Override
    public int getBaseCastTime() {
        return o.buff_ps.isActive() ? 0 : super.getBaseCastTime();
    }
    
    @Override
    public int getBaseLockoutTime() {
        return o.isCatForm() ? o.world.GCD_FLOOR : super.getBaseLockoutTime();
    }
    
    @Override
    public int getBasePowerCost() {
        return o.buff_ps.isActive() || o.buff_hotw.isActive() ? 0 : super.getBasePowerCost();        
    }
    
    @Override
    public void casted(Unit target, double x, double y, int castTime, int powerCost) {
        double heal = o.getSP(School.NATURE) * o.fgd.HT_HEALING_PER_SP;  // what kind of range is this?   
        if (o.buff_ps.tryConsume()) {
            heal *= o.fgd.PS_HT_HEALING_MOD;
        }         
        double crit = o.getCritChance();        
        if (o.cfg.talent_doc) {
            heal *= o.fgd.DOC_HT_REJUV_HEAL_MOD;
        } 
        double multi = o.getMultistrikeChance();
        o.applyHeal(heal, target, this, Origin.HEAL, School.NATURE, crit, multi);
        if (o != target) {
            o.applyHeal(heal, o, this, Origin.HEAL, School.NATURE,crit, multi);
        }           
        if (o.cfg.talent_bt) {
            o.buff_bt.activate();
        }
    }
    
}
