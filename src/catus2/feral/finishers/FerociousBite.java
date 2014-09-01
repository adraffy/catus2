package catus2.feral.finishers;

import catus2.AttackT;
import catus2.OriginT;
import catus2.SchoolT;
import catus2.Unit;
import catus2.feral.Feral;
import catus2.feral.FeralBleed;

public class FerociousBite extends CatFinisher.Offensive {
    
    //public double BITW_PERC;
    
    public FerociousBite(Feral owner) {
        super(owner);
    }

    @Override
    public boolean finish(Unit target, int energyCost) {        
        boolean bleeding = target.isBleeding();  
        double crit = o.getCritChance();
        if (o.buff_bonus_t15_4pc.tryConsume()) {
            crit += o.fgd.BONUS_T15_4PC_EXTRA_CRIT;
        }
        if (bleeding) {
            crit += crit;
        }
        boolean bt = o.buff_bt.tryConsume();
        double mod = o.getDamageDoneMod(SchoolT.Idx.PHYSICAL);     
        if (!o.cfg.disable_fbExtra) {
            int extra = o.power_energy.partialConsume(o.fgd.FB_EXTRA_COST);        
            mod *= extra * o.fgd.FB_EXTRA_DAMAGE_MOD_PER_ENERGY;
            energyCost += extra;
        }        
        if (o.cfg.glyph_fb) {            
            o.applyHeal_percentHealth(energyCost * o.fgd.FB_GLYPH_PERC_HEALTH_PER_ENERGY, o, this, OriginT.HEAL, SchoolT.SCHOOLS_PHYSICAL);
        }        
        AttackT atk = o.yellow_melee(target, crit);             
        if (atk.landed) {
            double dmg = mod * o.getBloodtalonsMod(bt) * o.getAP() * o.fgd.FB_DAMAGE_PER_AP * o.power_combos.getPercent();            
            target.applyDamage(dmg, target, this, OriginT.MELEE, SchoolT.SCHOOLS_PHYSICAL, atk == AttackT.CRIT);            
            if (target.getHealthPercent() < o.BITW_PERC) {
                FeralBleed rip = o.getView(target).dot_rip;
                if (rip.isActive()) {
                    rip.activate();
                }
            }                
        }
        return atk.landed;
    }

    
    
}
