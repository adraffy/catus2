package catus2.procs;

import catus2.OriginT;
import catus2.Unit;
import catus2.buffs.AbstractBuff;
import catus2.chance.AbstractChance;
import catus2.chance.ChanceFactory;

public class BuffTrigger<C extends AbstractChance> extends Trigger<Unit,C> {

    public final AbstractBuff buff;
    
    public BuffTrigger(Unit owner, ChanceFactory<C> chanceFactory, boolean critOnly, int originMask, AbstractBuff buff) {
        super(owner, chanceFactory, critOnly, originMask);
        this.buff = buff;
    }

    @Override
    public void triggered(OriginT origin, boolean wasCrit) {
        buff.activate();
    }

}
