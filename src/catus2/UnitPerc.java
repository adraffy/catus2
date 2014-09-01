package catus2;

public class UnitPerc {

    // allow inlining
    static public final int CRIT    = 0;
    static public final int HASTE   = 1;
    static public final int MASTERY = 2;
    static public final int MULTI   = 3;
    static public final int VERSA   = 4;
    static public final int LEECH   = 5;
    static public final int AVOID   = 6;    
    static public final int DODGE   = 7;
    static public final int PARRY   = 8;
    static public final int BLOCK   = 9;
    static public final int NUM     = 10; // be right, don't be wrong

    public final int index;
    public final String longName;
    public final String shortName;
    private UnitPerc(int index, String name) {
        this.index = index;
        int pos = name.indexOf('|');
        if (pos >= 0) {
            longName = name.substring(0, pos);
            shortName = name.substring(pos + 1);
        } else {
            longName = shortName = name;
        }
    }

    static private UnitPerc make(int i) {
        switch (i) {
            case CRIT:      return new UnitPerc(i, "Critical Strike|Crit");
            case HASTE:     return new UnitPerc(i, "Haste");
            case MASTERY:   return new UnitPerc(i, "Mastery");
            case MULTI:     return new UnitPerc(i, "Multistrike|Multi");
            case VERSA:     return new UnitPerc(i, "Versatility|Versa");
            case LEECH:     return new UnitPerc(i, "Leech");
            case AVOID:     return new UnitPerc(i, "Avoidance|Avoid");            
            default: throw new IllegalArgumentException();
        }
    }    
    
    static public final UnitPerc[] A;
    static {
        A = new UnitPerc[NUM];
        for (int i = 0; i < NUM; i++) {
            A[i] = make(i);
        }
    }
    
}