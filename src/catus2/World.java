/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package catus2;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Random;

/**
 *
 * @author raffy
 */
public class World {
   
    // world-config
    
    public double pandemicCoeff = 1.3;
    public double multistrikeDamageMod = 0.3;
    public boolean randomDamageRanges = true;
    public boolean ignoreLocation;
    
    public int RANGE_SELF = 0;
    public int RANGE_COMBAT = 5;
    public int RANGE_SHORTER = 8;
    public int RANGE_SHORT = 20;
    public int RANGE_MEDIUM = 30;
    public int RANGE_LONG = 40;
    public int RANGE_VISION = 100;
    
    public int GCD_FLOOR = 1000;
    public int GCD_SPELL = 1500;
    //public int GCD_ENERGY = 1000;
    //public int GCD_SHAPESHIFT = 1000;
    
    // ---
    
    public ArrayList<UnitList> unitListCache = new ArrayList<>();
    
    public UnitList retainUnitList() {
        if (unitListCache.isEmpty()) {
            return new UnitList(unitList.size());
        } else {
            return unitListCache.remove(unitListCache.size());
        }
    }
    
    public void releaseUnitList(UnitList list) {
        unitListCache.add(list);
        list.clear();
    }
    
    // ---
            
    public final Timeline timeline = new Timeline();
    
    
    public final ArrayList<Unit> unitList = new ArrayList();
    
    public void addUnit(Unit a) {
        a.world = this;
        unitList.add(a);           
    }
    
    public void prepareForCombat() {
        
    }
    
    public void run() {
        prepareForCombat();
        
        
    }
    
    // --- randomness
    
    public final Random rng = new Random();
    
    
    public boolean randomChance(double prob) {
        return prob >= 1 || (prob > 0 && rng.nextDouble() < prob);
    }
    
    public double randomRange(double min, double max) {
        if (randomDamageRanges) {
            return min + rng.nextDouble() * (max - min);
        } else {
            return (max + min) / 2D;
        }
    }
    
    
}
