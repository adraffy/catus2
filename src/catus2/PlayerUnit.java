package catus2;

abstract public class PlayerUnit<O extends Unit<O,V>,V extends AbstractView<O>,C extends ClassConfig> extends Unit<O,V> {
        
    public C cfg;
    
    public PlayerUnit() {
        super(false);
    }
       
    /*
    Hit and Expertise has been removed as a secondary stat.
    Hit and Expertise bonuses on all items and item enhancements (gems, enchants, etc.) have been converted into Critical Strike, Haste, or Mastery.
    All characters now have a 100% chance to hit, 0% chance to be dodged, 3% chance to be parried, and 0% chance for glancing blows, when fighting creatures up to 3 levels higher (bosses included).
    Tanking specializations receive an additional 3% reduction in chance to be parried. 
    Tank attacks now have a 0% chance to be parried vs. creatures up to 3 levels higher.
    Creatures that are 4 or more levels higher than the character still has a chance to avoid attacks in various ways, to discourage fighting enemies that are much stronger.
    Dual Wielding still imposes a 19% chance to miss, to balance it with two-handed weapon use.
    */
          
    // base dodge = 3
    // base parry = 3
    // base block = 3
    // base melee miss = 3
    // base spell miss = 6
    // player hit = 7.5
    // player exp = 7.5
    // npc hit = 0
    // npc exp = 0
    
    @Override
    public void prepareForCombat() {
        super.prepareForCombat();
        
        perc_sum[UnitPerc.HIT].set(SpellId.Custom.BASE, 0.075);            
        perc_sum[UnitPerc.EXP].set(SpellId.Custom.BASE, 0.075); // +0.03 for tanks                  
        perc_sum[UnitPerc.MASTERY].set(SpellId.Custom.BASE, 0.10); // +880
        
        if (!cfg.disable_racials) {
            if (cfg.racial_ne) {
                incomingDamageMod_school_product[School.Idx.NATURE].set(SpellId.Racial.NightElf.NATURE_RESISTANCE, 0.99);
                if (world.nightTime) {
                    perc_sum[UnitPerc.HASTE].set(SpellId.Racial.NightElf.TOUCH_OF_ELUNE_NIGHT, 0.01);
                } else {
                    perc_sum[UnitPerc.CRIT].set(SpellId.Racial.NightElf.TOUCH_OF_ELUNE_DAY, 0.01);
                }
                perc_sum[UnitPerc.DODGE].set(SpellId.Racial.NightElf.QUICKNESS, 0.02);
            }
            if (cfg.racial_tauren) {
                incomingDamageMod_school_product[School.Idx.NATURE].set(SpellId.Racial.Tauren.NATURE_RESISTANCE, 0.99);                
                damageDone_critBonus_sum.set(SpellId.Racial.Tauren.BRAWN, 0.02);
                healingDone_critBonus_sum.set(SpellId.Racial.Tauren.BRAWN, 0.02);
            }
            if (cfg.racial_worgen) {
                perc_sum[UnitPerc.CRIT].set(SpellId.Racial.Worgen.VICIOUSNESS, 0.01);
                incomingDamageMod_school_product[School.Idx.NATURE].set(SpellId.Racial.Worgen.ABERRATION, 0.99);
                incomingDamageMod_school_product[School.Idx.SHADOW].set(SpellId.Racial.Worgen.ABERRATION, 0.99);
            }       
        }
        
    }
    
}
