package catus2.procs;

import catus2.Origin;
import catus2.Unit;
import catus2.chance.AbstractChance;
import catus2.chance.ChanceFactory;

abstract public class Trigger<O extends Unit,C extends AbstractChance> {

    public final O o;
    public final C chance;
    public final boolean critOnly;
    public final int originMask;
    
    public Trigger(O owner, ChanceFactory<C> chanceFactory, boolean critOnly, int originMask) {
        this.o = owner;
        this.chance = chanceFactory.create(owner);
        this.critOnly = critOnly;
        this.originMask = originMask;
    }
    
    abstract public void triggered(Origin origin, boolean wasCrit);
    
}
