package catus2.feral;

import catus2.AbstractView;
import catus2.Unit;
import catus2.buffs.Buff;
import catus2.buffs.BuffModel;

public class FeralView extends AbstractView<Feral> {

    public final Buff<BuffModel,Feral,FeralView> hot_rejuv;
    public final Buff<BuffModel,Feral,FeralView> dot_mf;
    public final Buff<BuffModel,Feral,FeralView> dot_mf_cat;
    public final Buff<BuffModel,Feral,FeralView> dot_thrash_bear;   
    public final FeralBleed dot_rip;
    public final FeralBleed dot_rake;
    public final FeralBleed dot_thrash_cat;
    public final Buff debuff_pvp_wod_4pc;

    public FeralView(Feral owner, Unit target) {
        super(owner, target);
        hot_rejuv = new Buff(o.buffModel_rejuv, this);
        dot_mf = new Buff(o.buffModel_mf, this);        
        dot_mf_cat = new Buff(o.buffModel_mf_cat, this);
        dot_thrash_bear = new Buff(o.buffModel_thrash_bear, this);
        dot_rip = new FeralBleed(o.buffModel_rip, this);
        dot_rake = new FeralBleed(o.buffModel_rake, this);
        dot_thrash_cat = new FeralBleed(o.buffModel_thrash_cat, this);
        debuff_pvp_wod_4pc = new Buff(o.buffModel_pvp_wod_4pc, this);
    }

    public double getBleedMod() {
        return debuff_pvp_wod_4pc.isActive() ? 1.1 : 1;
    }

}
