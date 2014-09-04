package catus2.combat;

import catus2.Unit;

public class AttackTable {
    
    static double getCritSupression(int LD) {
        return LD > 0 ? 0.01 * Math.min(LD, 10) : 0;
    }
    
    // melee flags
    static public final int CANNOT_MISS  = 0b0001;
    static public final int CANNOT_DODGE = 0b0010;
    static public final int CANNOT_PARRY = 0b0100;
    static public final int CANNOT_BLOCK = 0b1000;
    
    // melee mods
    static public final double BLOCK_MOD = 0.7;
    static public final double CRUSH_MOD = 1.5;    
    static public final double GLANCE_MOD = 0.75;
    
    // raw types
    static public final int MISS    = 0;
    static public final int DODGE   = 1;
    static public final int PARRY   = 2;
    static public final int CRIT    = 3;
    static public final int HIT     = 4;  
    
    static public final int CRUSH   = 5; // don't think these are worth implementing
    static public final int GLANCE  = 6; // since they only apply outside of [-3,+3]
    
    static public final int TYPE_MASK   = 0b000111; // Ceiling@Log2[1+( 5 )] => 3 bits  
    static public final int HIT_BIT     = 0b001000;
    static public final int BLOCK_BIT   = 0b010000;
    static public final int REFLECT_BIT = 0b100000; // external to this logic since it requires target change
    

    
    static public boolean landed(int bits) {
        return (bits & HIT_BIT) == HIT_BIT;
    }
    
    static public int getType(int bits) {
        return bits & TYPE_MASK;
    }
    
    static public boolean isType(int bits, int type) {
        return getType(bits) == type;
    }
    
    static public double getTypeMod(int bits) {
        int type = getType(bits);
        if (type < GLANCE) {
            return 0;
        }
        double mod = (BLOCK_BIT & bits) == BLOCK_BIT ? BLOCK_MOD : 1;
        if (type == GLANCE) {
            return mod * GLANCE_MOD;
        } else if (type == CRUSH) {
            return mod * CRUSH_MOD;
        } else {
            return mod;
        }
    }
   
    static public int melee(Unit caster, Unit target, double critChance, /*boolean isWhite,*/ int options) {
        double p = caster.world.rng.nextDouble();
        int LD = target.level - caster.level;  
        if ((options & CANNOT_MISS) == 0) {
            double missChance = target.getMeleeMissChance() + 0.015 * LD + (target.npc ? 0.015 * Math.max(LD - 3, 0) : 0) - caster.getHitChance();
            if (missChance > 0) {
                p -= missChance;
                if (p < 0) {
                    return MISS;
                }
            }
        }           
        boolean inFront = caster.inFrontOf(target);
        if (inFront) {
            if ((options & CANNOT_DODGE) == 0) {
                double dodgeChance = target.getDodgeChance() + 0.015 * LD - caster.getExpChance();
                if (dodgeChance > 0) {
                    p -= dodgeChance;
                    if (p < 0) {
                        return DODGE;
                    }
                }            
            }        
            if ((options & CANNOT_PARRY) == 0) {
                double parryChance = target.getParryChance() + 0.015 * LD + (target.npc && LD > 2 ? 0.03 : 0) - caster.getExpChance();
                if (parryChance > 0) {
                    p -= parryChance;
                    if (p < 0) {
                        return PARRY;
                    }
                }            
            }
        }
        /*
        int bits = HIT | HIT_BIT; // this should constify
        found: {
            /*if (isWhite && target.npc && LD > 3) { // can glance
                double glanceChance = 0.1 + 0.1 * LD;
                p -= glanceChance;
                if (p < 0) {
                    bits = GLANCE | HIT_BIT;
                    break found;
                }
            if (critChance > 0) { // can crit
                p -= critChance - getCritSupression(LD);
                if (p < 0) {
                    bits = CRIT | HIT_BIT;
                    break found;
                }
            }
            if (isWhite && caster.npc && LD < -3) { // can crush
                double crushChance = -0.15 + -0.1 * LD;
                p -= crushChance;
                if (p < 0) {
                    bits = CRUSH | HIT_BIT;
                    break found;
                }            
            }
        }
        */
        int bits = critChance > 0 && critChance - getCritSupression(LD) < p ? (CRIT | HIT_BIT) : (HIT | HIT_BIT);
        if (inFront && (options & CANNOT_BLOCK) == 0) { // can block (second roll)
            double blockChance = target.getBlockChance() + 0.015 * LD;
            if (caster.world.randomChance(blockChance)) {
                bits |= BLOCK_BIT;
            }
        }    
        return bits;
    }
    
    //static public int periodic(Unit caster, Unit target, double critChance) { return spell(caster, target, critChance, false); }    
    //static public int heal(Unit caster, Unit target, double critChance) { return spell(caster, target, critChance, false); }    
    
    static public int spell(Unit caster, Unit target, double critChance, boolean canMiss) {
        int LD = target.level - caster.level;  
        if (canMiss) {
            double missChance = caster.world.neverMiss ? 0 : target.getSpellMissChance() + 0.03 * LD + 0.08 * Math.max(LD - 3, 0) - caster.getHitChance() - caster.getExpChance();
            if (caster.world.randomChance(missChance)) {
                return MISS;
            }
        }
        return caster.world.randomChance(critChance - getCritSupression(LD)) ? (CRIT | HIT_BIT) : (HIT | HIT_BIT);
    }
}
