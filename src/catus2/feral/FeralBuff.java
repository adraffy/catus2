package catus2.feral;

import catus2.buffs.Buff;
import catus2.buffs.BuffModel;

public class FeralBuff<M extends BuffModel> extends Buff<M,Feral,FeralView> {

    public FeralBuff(M model, FeralView view) {
        super(model, view);
    }

    public FeralBuff(M model, Feral owner) {
        super(model, owner.selfView);
    }

    
}
