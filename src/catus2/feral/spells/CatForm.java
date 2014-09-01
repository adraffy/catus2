package catus2.feral.spells;

import catus2.AbstractSpell;
import catus2.TargetStyle;
import catus2.Unit;
import catus2.feral.Feral;

public class CatForm extends AbstractSpell<Feral> {

    /*
    Cat Form	
    Shapeshift
    3.7% of base mana
    Instant
    Requires Druid
    Requires level 6
    Shapeshift into Cat Form, increasing movement speed by 30% and allowing the use of Cat Form abilities.  
    Also protects the caster from Polymorph effects and reduces damage taken from falling.
    The act of shapeshifting frees the caster of movement impairing effects.
    
    Apply Aura: Mod Auto Attack Damage % Value: 100%
    */
    
    public CatForm(Feral owner) {
        super(owner, TargetStyle.NONE);
    }

    @Override
    public void casted(Unit target, double x, double y, int castTime, int powerCost) {
        o.buff_form_cat.activate();
    }    
    
}
