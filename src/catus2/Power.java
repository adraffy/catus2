/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package catus2;

/**
 *
 * @author raffy
 */
public class Power {
    
    int base;
    int maximum;
    public int current;  
    
    public final ProductMap costMods = new ProductMap();
    
    public int percentOfBase(double perc) {
        return (int)(perc * base);
    }
    
    public int add(int amt) {
        int prev = current;
        current = Math.min(current + amt, maximum);
        return current - prev;
    }
    
    public int adjust(int amt) {
        return (int)(costMods.product() * amt + 0.5);
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
