package catus2;

public class TimeMap extends IntDoubleMap {
    
    static public double DIRTY = Double.NaN;
    static public double T = 50;
    
    public int base;
    
    private double cache = Double.NaN;
    
    public double get() {
        if (cache != cache) {
            double b = base;
            double c = 1;
            for (int i = 0; i < num; i++) {
                double x = vals[i];
                if (Math.abs(x) > T) {
                    b += x;
                } else {
                    c *= x;
                }            
            }
            cache = b * c;
        }
        return cache;
    }

    @Override
    public void clear() {
        super.clear();
        cache = Double.NaN;
    }
    
    @Override
    public void set(int key, double val) {
        super.set(key, val);
        cache = Double.NaN;
    }
    
    @Override
    public boolean remove(int key) {
        boolean b = super.remove(key);
        if (b) {
            cache = Double.NaN;
        }
        return b;
    }
    
}
