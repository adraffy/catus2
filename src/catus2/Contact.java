package catus2;

public class Contact {
    
    // contact flags    
    static public final int CANNOT_DODGE = 0b0001;
    static public final int CANNOT_PARRY = 0b0010;
    static public final int CANNOT_MISS  = 0b0100;
    static public final int WHITE_SWING  = 0b1000;
    
    // contact type
    static public final int MISS    = 0;
    static public final int DODGE   = 1;
    static public final int PARRY   = 2;
    static public final int GLANCE  = 3;
    static public final int CRIT    = 4;
    static public final int CRUSH   = 5;
    static public final int HIT     = 6;

}
