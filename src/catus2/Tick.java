package catus2;

public abstract class Tick {    
    
    int at = -1;    
    
    abstract public void run();
    
    static public Tick wrap(final Runnable r) {
        return new Tick() {
            @Override
            public void run() {
                r.run();
            }            
        };
    }
    
    public final boolean scheduled() {
        return at >= 0;
    }
    
}
