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
