package catus2.combat;

public class CrowdControl {

    /*
    Removed Silence effects from interrupts. Silence effects still exist, but are never attached to an interrupt.
    Removed all Disarms.
    Reduced the number of Diminishing Returns (DR) categories.
    All Roots now share the same DR category.
    Exception: Roots on Charge-type abilities have no DR category, but have a very short duration instead.
    All Stuns now share the same DR category.
    All Incapacitate (sometimes called "mesmerize") effects now share the same DR category and have been merged with the Horror DR category.
    Removed the ability to make cast-time CC spells instant with a cooldown.
    Removed many CC spells entirely, and increased the cooldowns and restrictions on others.
    Pet-cast CC is more limited, and in many cases has been removed.
    Cyclone can now be dispelled by immunities and Mass Dispel.
    PvP trinkets now grant immunity to reapplication of an effect from the same spell cast when they break abilities with persistent effects, like Solar Beam.
    Long fears are now shorter in PvP due to the added benefit of a fear changing the players position.
    */
    
    static public final int MAX_DURATION = 6000;
    
    static public final int MAX_STACKS = 3;
    static public final int RESET_DURATION = 15000;    
    static public final double DR_COEFF = 0.5; 
    
    
    // beta DR thread
    // http://us.battle.net/wow/en/forum/topic/13778668040
    
    static public final int ROOT            = 0; // except roots on Charge
    static public final int STUN            = 1;
    static public final int INCAPACITATE    = 3; // Incapacitate, Horror
    static public final int DISORIENT       = 4; // Cyclone, Fear
    static public final int SILENCE         = 5;
    static public final int AOE_KNOCK       = 6;
    static public final int N               = 7; // important!
    
}
