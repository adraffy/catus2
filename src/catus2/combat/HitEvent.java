package catus2.combat;

import catus2.SpellData;
import catus2.Unit;
import catus2.combat.AttackTable;

public class HitEvent {

    public final SpellData spell;
    public final Unit caster;
    public final Unit target;
    public final int result; 
    public final double mod;
    public double computed;
    public double base;
    
    public HitEvent(SpellData spell, Unit caster, Unit target, int result) {
        this.spell = spell;
        this.caster = caster;
        this.target = target;
        this.result = result;
        mod = success() ? crit() ? caster.getCritDamageBonusMod() : 1 : 0;
    }

    public boolean success() { return AttackTable.landed(result); }
    public boolean crit()   { return AttackTable.isType(result, AttackTable.CRIT); }
        
    static public HitEvent melee(SpellData model, Unit caster, Unit target, double critChance, int options) {
        
        return new HitEvent(model, caster, target, AttackTable.melee(caster, target, critChance, options));
    }
    
    static public HitEvent self_apply(SpellData spell, Unit caster) {
        return new HitEvent(spell, caster, caster, AttackTable.HIT);
    }
    
    static public HitEvent help(SpellData spell, Unit caster, Unit target, double critChance) {        
        return new HitEvent(spell, caster, target, AttackTable.spell(caster, target, critChance, false));
    }
    
    static public HitEvent periodic(SpellData spell, Unit caster, Unit target, double critChance) {        
        return new HitEvent(spell, caster, target, AttackTable.spell(caster, target, critChance, false));
    }
    
    static public HitEvent harm(SpellData spell, Unit caster, Unit target, double critChance, boolean canMiss, boolean canReflect) {        
        int result = AttackTable.spell(caster, target, critChance, canMiss);
        if (canReflect && AttackTable.landed(result) && target.canSpellReflect(caster, spell.school)) {            
            result = AttackTable.spell(caster, caster, critChance, canMiss) | AttackTable.REFLECT_BIT;
            target = caster;
        }        
        return new HitEvent(spell, caster, target, result);
    }
    
   
    
    
}
