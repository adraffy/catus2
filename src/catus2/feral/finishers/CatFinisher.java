package catus2.feral.finishers;

import catus2.feral.Feral;
import catus2.TargetStyle;
import catus2.Unit;
import catus2.feral.spells.FeralSpell;

public abstract class CatFinisher extends FeralSpell {

    public CatFinisher(Feral owner, TargetStyle targetStyle) {
        super(owner, targetStyle);
    }
    
    @Override
    public boolean isCastable() {
        return o.isCatForm() && super.isCastable();
    }
    
    @Override
    public boolean hasResource() { 
        return o.power_combos.current > 0 && super.hasResource();
    }

    @Override
    public void casted(Unit target, double x, double y, int castTime, int powerCost) {
        if (finish(target, powerCost)) {            
            int c = o.power_combos.zero();
            if (!o.cfg.disable_ps && o.world.randomChance(c * o.fgd.PS_CHANCE_PER_CP)) {
                o.buff_ps.activate();
            }
            if (o.cfg.talent_sotf && c > 0) {                
                o.power_energy.add(c * o.fgd.SOTF_ENERGY_PER_COMBO);
            }       
            if (o.cfg.bonus_t15_2pc && o.world.randomChance(c * o.fgd.SET_T15_2PC_CHANCE_PER_CP)) {
                o.power_combos.add(o.fgd.SET_T15_2PC_CP_GAIN);
            }  
            if (o.cfg.bonus_t16_4pc && o.buff_bonus_t16_4pc.tryConsume()) {
                o.power_combos.add(o.fgd.SET_T16_4PC_CP_GAIN);
            }
        } else {
            o.refundEnergyCost(powerCost);
        }        
    }
    
    // return true if landed
    public abstract boolean finish(Unit target, int powerCost);
        
    
    static public abstract class Offensive extends CatFinisher {
        
        public Offensive(Feral owner) {
            super(owner, TargetStyle.UNIT);
        }

    }
    
    
}
