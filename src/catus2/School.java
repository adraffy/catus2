package catus2;

import java.util.Arrays;

public class School {
    

    static public class Idx {
        static public final int PHYSICAL = 0;
        static public final int HOLY     = 1;
        static public final int FIRE     = 2;
        static public final int NATURE   = 3;
        static public final int FROST    = 4;
        static public final int SHADOW   = 5;
        static public final int ARCANE   = 6;
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
    
    static private int mask(int ... indexes) {
        int mask = 0;
        for (int i: indexes) {
            mask |= 1 << i;
        }        
        return mask;
    }
    
    static public final School PHYSICAL    = new School("Physical", Idx.PHYSICAL);
    static public final School ARCANE      = new School("Arcane",   Idx.ARCANE);
    static public final School NATURE      = new School("Nature",   Idx.NATURE);
    static public final School FIRE        = new School("Holy",     Idx.FIRE);
    static public final School FROST       = new School("Holy",     Idx.FROST);
    static public final School HOLY        = new School("Holy",     Idx.HOLY);
    static public final School SHADOW      = new School("Shadow",   Idx.SHADOW);

    static public final School SPELLSTORM  = new School("Spellstorm",   Idx.NATURE, Idx.ARCANE);
    static public final School DIVINE      = new School("Divine",       Idx.HOLY,   Idx.ARCANE);
    static public final School FIRESTORM   = new School("Firestorm",    Idx.FIRE,   Idx.ARCANE);
    static public final School TWILIGHT    = new School("Twilight",     Idx.HOLY,   Idx.SHADOW);
    static public final School FROSTFIRE   = new School("Frostfire",    Idx.FROST,  Idx.FIRE);
    static public final School HOLYFIRE    = new School("Holyfire",     Idx.HOLY,   Idx.FIRE);
    static public final School SHADOWFROST = new School("Shadowfrost",  Idx.SHADOW, Idx.FROST);
    static public final School SHADOWFLAME = new School("Shadowflame",  Idx.SHADOW, Idx.FIRE);
    static public final School PLAGUE      = new School("Plague",       Idx.SHADOW, Idx.NATURE);
    static public final School SHADOWSTRIKE= new School("Shadowstrike", Idx.SHADOW, Idx.PHYSICAL);
    
    static public final School ELEMENTAL   = new School("Elemental", Idx.FIRE, Idx.FROST, Idx.NATURE);
    static public final School MAGIC       = new School("Magic", Idx.NATURE, Idx.ARCANE, Idx.SHADOW, Idx.FIRE, Idx.FROST, Idx.HOLY);
    static public final School CHAOS       = new School("Chaos", Idx.PHYSICAL, Idx.NATURE, Idx.ARCANE, Idx.SHADOW, Idx.FIRE, Idx.FROST, Idx.HOLY);
    
    
    
}
