package catus2;

public class AbstractView<O extends Unit> {
    
    public final O o;
    public final Unit unit;
    
    public AbstractView(O owner, Unit target) {
        this.o = owner;        
        this.unit = target;
    }
    
    public boolean isBleeding() {
        return false;
    }    
    
    public Application tryApply(Object source, Origin origin, School school) {
        return tryApply(source, origin, school, o.getCritChance(), 0);
    }
    public Application tryApply(Object source, Origin origin, School school, double critChance, int flags) {
        return o.tryApply(unit, source, origin, school, critChance, flags);
    }
    
}
