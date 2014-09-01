package catus2.feral;

import catus2.buffs.AbstractBuff;
import catus2.buffs.AbstractBuffModel;

public class FeralBuff<M extends AbstractBuffModel> extends AbstractBuff<M,Feral,FeralView> {

    public FeralBuff(M model, FeralView view) {
        super(model, view);
    }

    public FeralBuff(M model, Feral owner) {
        super(model, owner.self);
    }

    
}
