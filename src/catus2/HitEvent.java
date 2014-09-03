package catus2;

public class HitEvent {

    public final SpellModel spell;
    public final Unit caster;
    public final Unit target;
    public final int result; 
    public double computed;
    public double base;
    
    public HitEvent(SpellModel spell, Unit caster, Unit target, int result) {
        this.spell = spell;
        this.caster = caster;
        this.target = target;
        this.result = result;
        
    }

    public boolean landed() { return AttackTable.landed(result); }
    public boolean crit()   { return AttackTable.isType(result, AttackTable.CRIT); }
        
    static public HitEvent melee(SpellModel model, Unit caster, Unit target, double critChance, boolean isWhite, int options) {
        return new HitEvent(model, caster, target, AttackTable.melee(caster, target, critChance, isWhite, options));
    }
    
    static public HitEvent help(SpellModel model, Unit caster, Unit target, double critChance) {        
        return new HitEvent(model, caster, target, AttackTable.spell(caster, target, critChance, false));
    }
    
    static public HitEvent periodic(SpellModel model, Unit caster, Unit target, double critChance) {        
        return new HitEvent(model, caster, target, AttackTable.spell(caster, target, critChance, false));
    }
    
    static public HitEvent harm(SpellModel model, Unit caster, Unit target, double critChance, boolean canMiss, boolean canReflect) {        
        int result = AttackTable.spell(caster, target, critChance, canMiss);
        if (canReflect && AttackTable.landed(result) && target.canSpellReflect(caster, model.school)) {            
            result = AttackTable.spell(caster, caster, critChance, canMiss) | AttackTable.REFLECT_BIT;
            target = caster;
        }        
        return new HitEvent(model, caster, target, result);
    }
    
   
    
    
}
