package catus2;

public enum Origin {
    WHITE,          // eg. Melee
    MELEE,          // eg. Shred
    BLEED,          // eg. Rip
    MELEE_BLEED,    // eg. Rake (initial)
    SPELL,          // eg. Faerie Fire
    DOT,            // eg. Moonfire
    HEAL,           // eg. Healing Touch
    HOT             // eg. Rejuv
    
    // fuck, where do i put SPELL_AOE, SPELL_TARGETED 
    // maybe source instanceof Spell and target type...
}
