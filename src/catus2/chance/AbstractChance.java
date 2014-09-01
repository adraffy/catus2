package catus2.chance;

import catus2.Fmt;
import catus2.Unit;

abstract public class AbstractChance {

    public final Unit o;
    
    public int icd;
    
    public AbstractChance(Unit owner) {
        this.o = owner;
    }
    
    protected final int clock() {
        return o.world.timeline.clock;
    }
    
    protected final boolean chance(double prob) {
        return o.world.randomChance(prob);
    }

    protected int ready;
    
    public void reset() {
        ready = 0;
    }
    
    protected boolean canProc(int t) {
        return t >= ready;
    }
    
    protected void beginCooldown(int t) {  
        ready = t + icd;
    }
        
    public final boolean canProc() {
        return canProc(clock());
    }
    
    public final void beginCooldown() {
        beginCooldown(clock());
    }    
    
    public final boolean tryProc() {
        int t = clock();
        if (canProc(t) && shouldProc(t)) {
            beginCooldown(t);
            return true;
        } else {
            return false;
        }
    }
    
    abstract protected boolean shouldProc(int t);
    
    public final void appendDescTo(StringBuilder sb) {
        appendChanceDescTo(sb);
        if (icd > 0) {
            sb.append(" (");
            sb.append(Fmt.msDur(icd));
            sb.append(" ICD)");
        }  
    }
    
    abstract protected void appendChanceDescTo(StringBuilder sb);
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        appendDescTo(sb);
        return sb.toString();
    }
    
}
