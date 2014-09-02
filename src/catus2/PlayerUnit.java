package catus2;

abstract public class PlayerUnit<O extends Unit<O,V>,V extends AbstractView<O>> extends Unit<O,V> {
        
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
        
    }
    
}
