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
    
}
