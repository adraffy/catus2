package catus2.internal;

import java.util.Arrays;

public class TinyModMap {

    static public final int ID_MASK  = 0x0007FFFF; // 524288 spells
    static public final int MOD_MASK = 0x7FF80000; // 4096 levels
    static public final int ADD_BIT  = 0x80000000; // add or multiply
    
    static public int encode(int id, boolean add, double mod) {
        if (add) {
        // mod can be [-40.00, 40.00] by 0.01
            return id | ADD_BIT | 
        } else {
            return id | 
        }
        return id | (add ? ADD_BIT : 0) | (int)((mod + 400) * 10);
    }
    
    static public double decode(int encoded) {
        
    }
    
    static public int search(int[] keys, int key, int a, int b) {
        while (a < b) {
            int m = (a + b) >>> 1;
            int k = keys[m] & ID_MASK;
            if (k < key) {
                a = m + 1;
            } else if (k > key) {
                b = m;
            } else {
                return m;
            }            
        }
        return ~a;
    }
    
    private int[] keys = new int[8];
    private int num;
    
    public TinyModMap() {        
    }
    
    public void clear() {
        num = 0;
    }
    
    public boolean any() { return num > 0; }
    public boolean empty() { return num == 0; }
    
    
    
    public boolean add(int key) {
        int i = Arrays.binarySearch(keys, 0, num, key);
        if (i >= 0) {
            return false;
        }        
        i = ~i;
        if (num == keys.length) { 
            int[] copy = new int[num << 1]; // fixme: choose this constant factor so resizes never really happen
            System.arraycopy(keys, 0, copy, 0, i);
            copy[i] = key;
            System.arraycopy(keys, i, copy, i + 1, num - i);
            
        } else {        
            System.arraycopy(keys, i, keys, i + 1, num - i);                
            keys[i] = key;
            return true;
        }
        return true;
       
    }
    
    public boolean remove(int key) {
        int i = Arrays.binarySearch(keys, 0, num, key);
        if (i < 0) {
            return false;
        }
        if (i < --num) {
            System.arraycopy(keys, i + 1, keys, i, num - i);
        }
        return true;
    }

    private double cache;
    private boolean dirty;

    public boolean add(int key) {
        boolean temp = super.add(key);
        dirty |= temp;
        return temp;            
    }


}
