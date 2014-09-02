package catus2;

import java.util.Arrays;

public class School {
    

    static public class Idx {
        static public final int PHYSICAL = 0;
        static public final int NATURE   = 1;
        static public final int ARCANE   = 2;
        static public final int SHADOW   = 3;
        static public final int FIRE     = 4;
        static public final int FROST    = 5;
        static public final int HOLY     = 6;
        static public final int NUM      = 7; // grr
    }

    public final int[] indexes;
    public final int mask;
    public final String name;
    private School(String name, int ... indexes) {
        this.name = name;
        Arrays.sort(indexes); // assume we own this
        this.indexes = indexes;        
        this.mask = mask(indexes);        
    }
    
    static public int mask(int ... indexes) {
        int mask = 0;
        for (int i: indexes) {
            mask |= 1 << i;
        }        
        return mask;
    }
    
    static public final School PHYSICAL    = new School("Physical",   Idx.PHYSICAL);
    static public final School ARCANE      = new School("Arcane",     Idx.ARCANE);
    static public final School NATURE      = new School("Nature",     Idx.NATURE);
    static public final School SPELLSTORM  = new School("Spellstorm", Idx.NATURE, Idx.ARCANE);
    
    
    
    
}
