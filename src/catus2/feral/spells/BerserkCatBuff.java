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
        if (!o.cfg.disable_perk_berserk) {
            o.power_energy.
        }
        
        o.spell_rip.powerCostModMap.setOrClear(state, id, mod);
        o.spell_fb.powerCostModMap.setOrClear(state, id, mod);
        o.spell_maim.powerCostModMap.setOrClear(state, id, mod);
        o.spell_sr.powerCostModMap.setOrClear(state, id, mod);        
        o.spell_rake.powerCostModMap.setOrClear(state, id, mod);
        o.spell_shred.powerCostModMap.setOrClear(state, id, mod);
        o.spell_swipe.powerCostModMap.setOrClear(state, id, mod);
        o.spell_thrash_cat.powerCostModMap.setOrClear(state, id, mod);        
    }  
}
