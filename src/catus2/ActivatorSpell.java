package catus2;

import catus2.buffs.Buff;
import catus2.buffs.BuffModel;

public class ActivatorSpell<O extends Unit<O,V>,V extends AbstractView<O>,B extends Buff<? extends BuffModel,O,V>> extends AbstractSpell<O> {

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
