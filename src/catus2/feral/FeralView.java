package catus2.feral;

import catus2.AbstractView;
import catus2.Unit;
import catus2.buffs.AccumulateBuff;
import catus2.buffs.Buff;
import catus2.buffs.BuffModel;
import catus2.buffs.ModBuff;
import catus2.combat.HitEvent;

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
    public final AccumulateBuff<BuffModel,Feral,FeralView> dot_bonus_t17_4pc;
    public final Buff debuff_bonus_pvp_wod_4pc;

    public FeralView(Feral owner, Unit target) {
        super(owner, target);
        buff_stampRoar = new ModBuff(o.buffModel_stampRoar, this, o.movementSpeed_sum);
        hot_rejuv = new Buff(o.buffModel_rejuv, this);
        dot_mf = new Buff(o.buffModel_mf, this);        
        dot_mf_cat = new Buff(o.buffModel_mf_cat, this);
        dot_thrash_bear = new Buff(o.buffModel_thrash_bear, this);
        dot_rip = new FeralBleed(o.buffModel_rip, this);
        dot_rake = new FeralBleed(o.buffModel_rake, this);
        dot_thrash_cat = new FeralBleed(o.buffModel_thrash_cat, this);
        dot_bonus_t17_4pc = new AccumulateBuff<BuffModel,Feral,FeralView>(o.buffModel_bonus_t17_4pc, this) {
            @Override
            public void gotChunk(double amount) {
                o.trigger_bonus_t17_2pc();   
                HitEvent event = periodic(0); 
                event.base = o.getRazorClawsMod() * amount;                       
                v.check_bonus_pvp_wod_4pc(event);
                // we need to make sure this avoids physical school output modifiers
                o.computeAndRecord(event);
            }
        };
        debuff_ff = new Buff(o.buffModel_ff, this);
        debuff_bonus_pvp_wod_4pc = new Buff(o.buffModel_bonus_pvp_wod_4pc, this);
    }

    // this is a PERSONAL buff, other players do not benefit
    // this does not check if it was actually a bleed
    public void check_bonus_pvp_wod_4pc(HitEvent event) {        
        if (debuff_bonus_pvp_wod_4pc.isActive()) { 
            event.base *= o.fgd.BONUS_WOD_PVP_4PC_BLEED_DAMAGE_MOD;
        }  
    }
    
}
