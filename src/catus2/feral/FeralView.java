package catus2.feral;

import catus2.AbstractView;
import catus2.Unit;
import catus2.buffs.AbstractBuffModel;

public class FeralView extends AbstractView<Feral> {

    public final FeralBleed dot_rip;
    public final FeralBleed dot_rake;
    public final FeralBleed dot_thrash_cat;
    //public final FeralBleed dot_thrashBear;   
    public final FeralBuff<AbstractBuffModel> dot_mf;
    public final FeralBuff<AbstractBuffModel> dot_mf_cat;
    public final FeralBuff<AbstractBuffModel> hot_rejuv;

    public final FeralBuff debuff_pvp_wod_4pc;

    public FeralView(Feral owner, Unit target) {
        super(owner, target);
   
        debuff_pvp_wod_4pc = new FeralBuff(o.buffModel_pvp_wod_4pc, this);

        hot_rejuv = new FeralBuff<>(o.buffModel_rejuv, this);
        dot_mf = new FeralBuff<>(o.buffModel_mf, this);        
        dot_mf_cat = new FeralBuff<>(o.buffModel_mf_cat, this);

        dot_rip = new FeralBleed(o.buffModel_rip, this);
        dot_rake = new FeralBleed(o.buffModel_rake, this);
        dot_thrash_cat = new FeralBleed(o.buffModel_thrash_cat, this);

    }

    public double getBleedMod() {
        return debuff_pvp_wod_4pc.isActive() ? 1.1 : 1;
    }

}
