package catus2.procs;

import catus2.Origin;
import catus2.Unit;
import catus2.buffs.Buff;
import catus2.chance.AbstractChance;
import catus2.chance.ChanceFactory;

public class BuffTrigger<C extends AbstractChance> extends Trigger<Unit,C> {

    public final Buff buff;
    
    public BuffTrigger(Unit owner, ChanceFactory<C> chanceFactory, boolean critOnly, int originMask, Buff buff) {
        super(owner, chanceFactory, critOnly, originMask);
        this.buff = buff;
    }

    @Override
    public void triggered(Origin origin, boolean wasCrit) {
        buff.activate();
    }

}
