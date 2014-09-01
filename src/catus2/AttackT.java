package catus2;

public enum AttackT {

    MISS        ("Miss",  false),
    DODGE       ("Dodge", false),
    PARRY       ("Parry", false),
    HIT         ("Hit",   true),  
    CRIT        ("Crit",  true);
    
    public final String name;
    public final boolean landed;
    
    private AttackT(String name, boolean landed) { 
        this.name = name;
        this.landed = landed;
    }
    
    static public AttackT damaged(boolean crit) {
        return crit ? CRIT : HIT;
    }
    
                
}
