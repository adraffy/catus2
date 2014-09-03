package catus2;

public class AbstractView<O extends Unit> {
    
    public final O o;
    public final Unit u;
    
    public AbstractView(O owner, Unit target) {
        this.o = owner;        
        this.u = target;
    }
    
    public boolean isBleeding() {
        return false;
    }    
    
    public Application tryApply(Object source, Origin origin, School school) {
        return tryApply(source, origin, school, o.getCritChance(), 0);
    }
    public Application tryApply(Object source, Origin origin, School school, double critChance, int flags) {
        return o.tryApply(u, source, origin, school, critChance, flags);
    }
    
}
