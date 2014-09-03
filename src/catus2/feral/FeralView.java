package catus2.feral;

import catus2.AbstractView;
import catus2.Application;
import catus2.Origin;
import catus2.School;
import catus2.Unit;
import catus2.buffs.Accumulate;
import catus2.buffs.Buff;
import catus2.buffs.BuffModel;
import catus2.buffs.ModBuff;

public class FeralView extends AbstractView<Feral> {

    public final ModBuff buff_stampRoar;
    public final Buff<BuffModel,Feral,FeralView> hot_rejuv;
    public final Buff<BuffModel,Feral,FeralView> dot_mf;
    public final Buff<BuffModel,Feral,FeralView> dot_mf_cat;
    public final Buff<BuffModel,Feral,FeralView> dot_thrash_bear;   
    public final Buff<BuffModel,Feral,FeralView> debuff_ff;   
    public final FeralBleed dot_rip;
    public final FeralBleed dot_rake;
    public final FeralBleed dot_thrash_cat;
    public final Accumulate<BuffModel,Feral,FeralView> dot_bonus_t17_4pc;
    public final Buff debuff_bonus_pvp_wod_4pc;

    public FeralView(Feral owner, Unit target) {
        super(owner, target);
        buff_stampRoar = new ModBuff(o.buffModel_stampRoar, this, owner.movementSpeed_sum);
        hot_rejuv = new Buff(o.buffModel_rejuv, this);
        dot_mf = new Buff(o.buffModel_mf, this);        
        dot_mf_cat = new Buff(o.buffModel_mf_cat, this);
        dot_thrash_bear = new Buff(o.buffModel_thrash_bear, this);
        dot_rip = new FeralBleed(o.buffModel_rip, this);
        dot_rake = new FeralBleed(o.buffModel_rake, this);
        dot_thrash_cat = new FeralBleed(o.buffModel_thrash_cat, this);
        dot_bonus_t17_4pc = new Accumulate<BuffModel,Feral,FeralView>(o.buffModel_bonus_t17_4pc, this) {
            @Override
            public void gotTickPortion(double damage) {
                o.bonus_t17_2pc_trigger();
                Application app = o.tryApply(u, this, Origin.BLEED, School.PHYSICAL, 0, 0);
                app.base = damage;    
                
                // this cannot benefit from any damage modifier stuff
                
                
                
                
            }
        };
        debuff_ff = new Buff(o.buffModel_ff, this);
        debuff_bonus_pvp_wod_4pc = new Buff(o.buffModel_bonus_pvp_wod_4pc, this);
    }

    public void executeBleed(Application app) {        
        if (debuff_bonus_pvp_wod_4pc.isActive()) { 
            // this does not check if it was actually a bleed
            app.base *= o.fgd.BONUS_WOD_PVP_4PC_BLEED_DAMAGE_MOD;
        }  
        o.executeApply(app);
    }
    
}
