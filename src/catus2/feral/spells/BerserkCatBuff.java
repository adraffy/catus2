package catus2.feral.spells;

import catus2.buffs.ActivatorBuff;
import catus2.buffs.BuffModel;
import catus2.feral.Feral;
import catus2.feral.FeralView;

public class BerserkCatBuff extends ActivatorBuff<BuffModel,Feral,FeralView> {

    // model.param = BERSERK_CAT_POWER_MOD
        
    public BerserkCatBuff(Feral owner) {
        super(new BuffModel(Feral.BUFF_BERSERK_CAT), owner.selfView);
    }
    
    @Override
    public void stateChanged(boolean state) {    
        int id = m.id();
        double mod = o.fgd.BERSERK_CAT_POWER_MOD;
        o.spell_rip.powerCostMod.setOrClear(state, id, mod);
        o.spell_fb.powerCostMod.setOrClear(state, id, mod);
        o.spell_maim.powerCostMod.setOrClear(state, id, mod);
        o.spell_sr.powerCostMod.setOrClear(state, id, mod);        
        o.spell_rake.powerCostMod.setOrClear(state, id, mod);
        o.spell_shred.powerCostMod.setOrClear(state, id, mod);
        o.spell_swipe.powerCostMod.setOrClear(state, id, mod);
        o.spell_thrash_cat.powerCostMod.setOrClear(state, id, mod);        
    }  
}
