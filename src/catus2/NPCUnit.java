package catus2;

abstract public class NPCUnit<O extends Unit<O,V>,V extends AbstractView<O>> extends Unit<O,V> {

    public NPCUnit() {
        super(true);
    }

    @Override
    public void prepareForCombat() {
        super.prepareForCombat();
        //perc_sum[UnitPerc.HIT].set(SpellId.Custom.BASE, 0);            
        //perc_sum[UnitPerc.EXP].set(SpellId.Custom.BASE, 0);    
    }
  
}
