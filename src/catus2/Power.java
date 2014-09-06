package catus2;

public class Power {
    
    int base;
    int maximum;
    public int current;  
    
    public final ModMap costMods = new ModMap.Product(); // no idea
    
    public int percentOfBase(double perc) {
        return (int)(perc * base);
    }
    
    public void set(int amt) {
        if (amt < 0) {
            throw new IllegalStateException();
        }
        current = Math.min(amt, maximum);
    }
    
    public int add(int amt) {
        int prev = current;
        current = Math.min(current + amt, maximum);
        return current - prev;
    }
    
    public int adjust(int amt) {
        return (int)(costMods.get() * amt + 0.5);
    }
    
    
    public int partialConsume(int max) {
        int part = Math.max(current, max);
        current -= part;
        return part;
    }
    
    public double getPercent() {
        return current / (double)maximum;
    }
    
    public int zero() {
        int prev = current;
        current = 0;
        return prev;
    }
    
    
}
