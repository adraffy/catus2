package catus2;

import java.util.Arrays;

public class IntDoubleMap {

    static final int INIT_CAP = 8;
    
    protected int num;
    protected int[] keys = new int[INIT_CAP];
    protected double[] vals = new double[INIT_CAP];

    public void clear() {
        num = 0;
    }
    
    protected int at(int key) {
        return Arrays.binarySearch(keys, key, 0, num);
    }
    
    public boolean contains(int key) {
        return at(key) >= 0;        
    }
    
    public void setOrClear(boolean state, int key, double val) {
        if (state) {
            set(key, val);
        } else {
            remove(key);
        }
    }
    
    public void set(int key, double val) {   
        int i = at(key);
        if (i >= 0) {
            vals[i] = val;
            return;
        }       
        i = ~i;
        if (num == keys.length) { 
            int new_size = num << 1;
            int[] new_keys = new int[new_size]; 
            double[] new_vals = new double[new_size];
            System.arraycopy(keys, 0, new_keys, 0, i);
            System.arraycopy(vals, 0, new_vals, 0, i);
            new_keys[i] = key;
            System.arraycopy(keys, i, new_keys, i + 1, num - i);            
            System.arraycopy(vals, i, new_vals, i + 1, num - i);    
            keys = new_keys;
            vals = new_vals;
        } else {        
            System.arraycopy(keys, i, keys, i + 1, num - i);                
            System.arraycopy(vals, i, vals, i + 1, num - i);                
            keys[i] = key;
        }      
    }
    
    public boolean remove(int key) {
        int i = at(key);
        if (i < 0) {
            return false;
        }    
        if (i < --num) {
            System.arraycopy(keys, i + 1, keys, i, num - i);
            System.arraycopy(vals, i + 1, vals, i, num - i);
        }
        return true;
    }
}
