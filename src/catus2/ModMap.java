package catus2;

import java.util.Arrays;

public abstract class ModMap {
    
    // fixme: this is an experiment
    // unsorted map with free cache
    // to maximize add/remove speed
    
    static final int INIT_CAP = 8; 
    static final int NULL_KEY = 0;
    
    protected boolean dirty;
    protected int[] keys = new int[INIT_CAP];
    protected int[] free = new int[INIT_CAP];
    protected double[] vals = new double[INIT_CAP];
    private int freeIndex;   
    private int freeAvail;
    private double cached;

    static public class Time extends ModMap {
        static public final double T = 50;
        public double base;
        public Time() { super(); }
        @Override
        protected double compute() {
            double b = base;
            double c = 1;
            for (int i = 0; i < keys.length; i++) {
                if (keys[i] != NULL_KEY) {
                    double x = vals[i];
                    if (Math.abs(x) > T) {
                        b += x;
                    } else {
                        c *= vals[i];
                    }                    
                }                
            }
            return (int)(0.5 + c * b);
        }
    }
    
    static public class Sum extends ModMap {
        public Sum() { super(); }
        @Override
        protected double compute() {
            double sum = 1;
            for (int i = 0; i < keys.length; i++) {
                if (keys[i] != NULL_KEY) {
                    sum += vals[i];
                }                
            }
            return sum;
        }        
    }
    
    static public class Product extends ModMap {
        public Product() { super(); }
        @Override
        protected double compute() {
            double prod = 1;
            for (int i = 0; i < keys.length; i++) {
                if (keys[i] != NULL_KEY) {
                    prod *= vals[i];
                }                
            }
            return prod;
        }
    }
    
    static public class Minimum extends ModMap {
        public Minimum() { super(); }
        @Override
        protected double compute() {
            double min = Double.POSITIVE_INFINITY;
            for (int i = 0; i < keys.length; i++) {
                if (keys[i] != NULL_KEY) {
                    min = Math.min(min, vals[i]);
                }                     
            }
            return min;
        }        
    }
    
    static public class Maximum extends ModMap {
        public Maximum() { super(); }
        @Override
        protected double compute() {
            double max = Double.NEGATIVE_INFINITY;
            for (int i = 0; i < keys.length; i++) {
                if (keys[i] != NULL_KEY) {
                    max = Math.max(max, vals[i]);
                }                     
            }
            return max;
        }        
    }
    
    private ModMap() {
        clear();
    }
    
    public void clear() {
        for (int i = 0; i < free.length; i++) {
            free[i] = i;
        }        
        freeIndex = 0;
        freeAvail = free.length;
        dirty = true;
    }
    
    abstract protected double compute();
    
    public double get() {
        if (dirty) {   
            cached = compute();
            dirty = false;
        }
        return cached;        
    }
    
    public boolean contains(int key) {
        int n = freeAvail;
        for (int i = freeIndex; i < keys.length; i++, n--) {
            if (free[i] == key) {
                return false;
            }            
        }
        for (int i = 0; i < n; i++) {
            if (free[i] == key) {
                return false;
            }
        }
        return true;
    }
    
    public void setOrClear(boolean state, int key, double val) {
        if (state) {
            set(key, val);
        } else {
            remove(key);
        }
    }
    
    public boolean set(int key, double val) {   
        if (key == NULL_KEY) {
            return false;
        }
        int n = keys.length;
        for (int i = 0; i < n; i++) {
            if (keys[i] == key) {
                vals[i] = val;
                dirty = true;    
                return false;
            }
        }
        if (freeAvail == 0) {
            keys = Arrays.copyOf(keys, n * 2);
            vals = Arrays.copyOf(vals, n * 2);      
            free = new int[n * 2];
            freeIndex = 0;
            freeAvail = n;
            for (int i = 0; i < n; i++) {
                free[i] = n + i;
            }
        } 
        --freeAvail;
        int index = free[freeIndex++];
        keys[index] = key;
        vals[index] = val;
        dirty = true;
        return true;
    }
    
    public boolean remove(int key) {
        int n = keys.length;
        for (int i = 0; i < n; i++) {
            if (keys[i] == key) {
                keys[i] = NULL_KEY;
                free[freeIndex + freeAvail++ % free.length] = i;
                dirty = true;    
                return true;
            }
        }
        return false;
    }
    
    
}
