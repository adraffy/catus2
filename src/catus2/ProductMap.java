/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package catus2;

import java.util.Arrays;

/**
 *
 * @author raffy
 */
public class ProductMap {
    
    static final int INIT_CAP = 5;
    static final int NULL_KEY = 0;
    
    private boolean dirty;
    private int[] keys = new int[INIT_CAP];
    private int[] free = new int[INIT_CAP];
    private double[] vals = new double[INIT_CAP];
    private int freeIndex;   
    private int freeAvail;
    private double product;
    
    public ProductMap() {
        for (int i = 0; i < free.length; i++) {
            free[i] = i;
        }        
        freeIndex = 0;
        freeAvail = free.length;
        
    }
    
    public double product() {
        if (dirty) {            
            double p = 1;            
            for (int i = 0; i < keys.length; i++) {
                if (keys[i] != NULL_KEY) {
                    p *= vals[i];
                }                
            }
            product = p;
        }
        return product;        
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
    
    
    public void set(int key, double val) {   
        if (key == NULL_KEY) {
            return;
        }
        clear(key);
        if (val == 1) {
            return;
        }
        if (freeAvail == 0) {
            int n = keys.length;
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
    }
    
    public boolean clear(int id) {
        int n = keys.length;
        for (int i = 0; i < n; i++) {
            if (keys[i] == id) {
                keys[i] = NULL_KEY;
                free[freeIndex + freeAvail++ % free.length] = i;
                dirty = true;    
                return true;
            }
        }
        return false;
    }
    
    
}
