/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package catus2.feral.spells;

import catus2.feral.Feral;
import catus2.SchoolT;
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
        double heal = o.getSP(SchoolT.NATURE) * o.fgd.HT_HEALING_PER_SP;    
        if (o.buff_ps.tryConsume()) {
            heal *= o.fgd.PS_HT_HEALING_MOD;
        }         
        double crit = o.getCritChance();        
        if (o.cfg.talent_doc) {
            heal *= o.fgd.DOC_HT_REJUV_HEAL_MOD;
        } 
        o.applyHealTo(o.currentTarget.unit, this, SchoolT.NATURE, o.world.randomChance(crit), heal);
        if (o.currentTarget.unit != o) {
            o.applyHealTo(o, this, SchoolT.NATURE, o.world.randomChance(crit), heal);
        }           
        if (o.cfg.talent_bt) {
            o.buff_bt.activate();
        }
    }
    
}
