package catus2;

public enum Origin {
    WHITE,          // eg. Melee            // harm
    MELEE,          // eg. Shred            // harm
    DAMAGE,         // eg. Faerie Fire      // harm
    DOT,            // eg. Moonfire         // harm
    HEAL,           // eg. Healing Touch    // help
    HOT             // eg. Rejuv            // help
    
    // fuck, where do i put SPELL_AOE, SPELL_TARGETED 
    // maybe source instanceof Spell and target type...
    
    // this is probably 2 separate things
    
    
    // flags
    // IGNORE_ARMOR
    // OVERTIME
    // HEAL
    // SINGLE_TARGET
    
    // IS_BLEED = IGNORE_ARMOR | OVERTIME
    
    // MELEE_BLEED = MELEE | IS_BLEED
    // HOT = HEAL | OVERTIME
    // 
    
}
