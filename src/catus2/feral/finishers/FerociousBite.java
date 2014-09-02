package catus2.feral.finishers;

import catus2.Application;
import catus2.Origin;
import catus2.School;
import catus2.Unit;
import catus2.feral.Feral;
import catus2.feral.FeralBleed;

public class FerociousBite extends CatFinisher.Offensive {
    
    public FerociousBite(Feral owner) {
        super(owner);
    }

    @Override
    public Application finish(Unit target, int energyCost) {        
        boolean bleeding = target.isBleeding();  
        double crit = o.getCritChance();
        if (o.buff_bonus_t15_4pc.tryConsume()) {
            crit += o.fgd.BONUS_T15_4PC_EXTRA_CRIT;
        }
        if (bleeding) {
            crit += crit;
        }
        boolean bt = o.buff_bt.tryConsume();
        double mod = 1;
        if (!o.cfg.disable_fbExtra) {
            int extra = o.power_energy.partialConsume(o.fgd.FB_EXTRA_COST);        
            mod *= extra * o.fgd.FB_EXTRA_DAMAGE_MOD_PER_ENERGY;
            energyCost += extra;
        }        
        if (o.cfg.glyph_fb) {            
            o.applyHeal_percentHealth(energyCost * o.fgd.FB_GLYPH_PERC_HEALTH_PER_ENERGY, o, this, Origin.HEAL, School.PHYSICAL);
        }        
        Application app = o.tryApply(target, this, Origin.MELEE, School.PHYSICAL, crit, 0);             
        if (app.hit()) {
            app.base = mod * o.getBloodtalonsMod(bt) * o.getAP() * o.fgd.FB_DAMAGE_PER_AP * o.power_combos.getPercent();            
            if (target.getHealthPercent() < o.BITW_PERC) {
                FeralBleed rip = o.getView(target).dot_rip;
                if (rip.isActive()) {
                    rip.activate();
                }
            }                
        }
        return app;
    }

    
    
}
