package catus2;

import catus2.buffs.AbstractBuff;
import catus2.buffs.AbstractBuffModel;

public class ActivatorSpell<O extends Unit<V>,V extends AbstractView<O>,B extends AbstractBuff<? extends AbstractBuffModel,O,V>> extends AbstractSpell<O> {

    public final B buff;
    
    public ActivatorSpell(B buff) {
        super(buff.v.o, TargetStyle.NONE);
        this.buff = buff;
    }

    @Override
    public void casted(Unit target, double x, double y, int castTime, int powerCost) {
        buff.activate();
    }

}
