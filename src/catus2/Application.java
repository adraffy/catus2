package catus2;

public class Application {
    
    static public final int CANNOT_BE_DODGED  = 0b0001;
    static public final int CANNOT_BE_PARRIED = 0b0010;
    static public final int CANNOT_BE_BLOCKED = 0b0100;

    static public final int CANNOT_BE_DODGED_PARRIED_BLOCKED = CANNOT_BE_DODGED | CANNOT_BE_PARRIED | CANNOT_BE_BLOCKED;
 
    static public enum MissType {
        MISS, DODGE, PARRY
    }
    
    public final Object source;
    public final Unit target;
    public final MissType missType;
    public final Origin origin;
    public final School school;
    
    public double base;    
    public double scale = 1;

    // public boolean canMultistrike; // NYI
    
    static public final int DID_CRIT   = 0b00001;
    static public final int DID_BLOCK  = 0b00010;
    static public final int DID_GLANCE = 0b00100;
    static public final int DID_CRUSH  = 0b01000;    
    static public final int DID_MULTI  = 0b10000;
    public int flags;
    
    //public boolean mulitstrike;
    
    public final boolean hit() {
        return missType == null;
    }
    
    public final boolean crit() {
        return (flags & DID_CRIT) == DID_CRIT;
    }
    
    public final boolean heal() {
        switch (origin) {
            case HEAL:
            case HOT:
                return true;
        }
        return false;
    }
        
    public Application(Unit target, Object source, Origin origin, School school, MissType missType) {
        this.source = source;
        this.target = target;
        this.origin = origin;
        this.school = school;
        this.missType = missType;
    }
    
}
