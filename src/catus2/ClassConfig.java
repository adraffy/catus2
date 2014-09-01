package catus2;

public class ClassConfig {

    // racials
    public boolean disable_racials;
    public boolean override_racials; // don't mirror gear racials into cfg
    
    public boolean racial_ne;       // +1% haste night, +1% crit day, -1% nature, +2% dodge, shadowmeld
    public boolean racial_worgen;   // +1% crit, -1% shadow and nature, +15 skin
    public boolean racial_tauren;   // +2% crit damage/healing, endurance (stam), -1% nature damage, war stomp, +15 herb
    public boolean racial_troll;    // berserking 15%, -15% slows, +10% hp regen, 10% hp regen in combat
    
    // bonuses
    public boolean bonus_t13_2pc; // ds
    public boolean bonus_t13_4pc;
    public boolean bonus_t14_2pc; // mv/hof/toes
    public boolean bonus_t14_4pc;
    public boolean bonus_t15_2pc; // tot
    public boolean bonus_t15_4pc;
    public boolean bonus_t16_2pc; // soo
    public boolean bonus_t16_4pc;    
    public boolean bonus_t17_2pc; // bf
    public boolean bonus_t17_4pc;
    public boolean bonus_pvp_wod_2pc; // Interrupting a spell with Skull Bash resets the cooldown of Tiger's Fury.
    public boolean bonus_pvp_wod_4pc; // Shred critical strikes causes the target to take 10% increased damage from your bleed effects for 6 sec.
    
}
