package catus2;

import catus2.combat.HitEvent;

public abstract class AbstractSpell<O extends Unit> {
    
    public final O o;
    public final TargetStyle targetStyle;
    public boolean harm;
    public double range_sweep;
    public SpellData m;
    
    /*
    enum property_type_t {
      P_GENERIC           = 0,
      P_DURATION          = 1,
      P_THREAT            = 2,
      P_EFFECT_1          = 3,
      P_STACK             = 4,
      P_RANGE             = 5,
      P_RADIUS            = 6,
      P_CRIT              = 7,
      P_UNKNOWN_1         = 8, // Unknown
      P_PUSHBACK          = 9,
      P_CAST_TIME         = 10,
      P_COOLDOWN          = 11,
      P_EFFECT_2          = 12,
      P_UNUSED_1          = 13,
      P_RESOURCE_COST     = 14,
      P_CRIT_DAMAGE       = 15,
      P_PENETRATION       = 16,
      P_TARGET            = 17,
      P_PROC_CHANCE       = 18, // Unconfirmed
      P_TICK_TIME         = 19, // Unknown
      P_TARGET_BONUS      = 20, // Improved Revenge, Glyph of Chain Heal, ...
      P_GCD               = 21, // Only used for flat modifiers?
      P_TICK_DAMAGE       = 22,
      P_EFFECT_3          = 23, // Glyph of Killing Spree, Glyph of Revealing Strike (both +% damage increases)
      P_SPELL_POWER       = 24,
      P_UNUSED_2          = 25,
      P_PROC_FREQUENCY    = 26,
      P_DAMAGE_TAKEN      = 27,
      P_DISPEL_CHANCE     = 28,
      P_EFFECT_4          = 32,
      P_MAX               = 29,
    };
    */
    
    
    // Apply Aura: Modifies Buff Duration (1) 
    //-50% = 0.5
    public final TimeMap durTime = new TimeMap(); // Apply Aura: Modifies Buff Duration (1)
    
    
    public final TimeMap gcdTime = new TimeMap(); // Apply Aura: (21)    
    public final TimeMap castTime = new TimeMap();

    //Apply Aura: Modifies Cooldown (11) -40000
    
    public final TimeMap cdTime = new TimeMap();
    
    //Apply Aura: Mod Increase Maximum Power - % (Mana)
    //Apply Aura: Mod Increase Maximum Power - % (Mana)
    //Apply Aura: Modifies Max Power - Flat (Energy)
    //Apply Aura: Increase Max Power - Flat (Chi)
    
    // Apply Aura: Modifies Power Cost (14)
    // 2014-09-03: confirmed multiplicative using mage (arcane power, arcane blast)
    // 5000 mana * (1 + 150% * stacks) * (1 + 10%)
    public final ModMap powerCostModMap = new ModMap.Product();    
    
    
    public final ModMap directDoneMod = new ModMap.Product();   // Apply Aura: Modifies Damage/Healing Done
    public final ModMap periodicDoneMod = new ModMap.Product(); // Apply Aura: Modifies Periodic Damage/Healing Done (22)
    
    public int schoolMask;
    
    public AbstractSpell(O owner, TargetStyle targetStyle) {
        this.o = owner;        
        this.targetStyle = targetStyle;        
    }
    
    String name;
    
    
    final Tick cooldownFader = new Tick() {
        @Override
        public void run() {
            
        }        
    };
    
    final Tick recharger = new Tick() {
        @Override
        public void run() {
            setCharges(charges + 1);
        }        
    };
    
    public void setCharges(int amt) {
        if (amt < 0 || amt > maxCharges) {
            throw new IllegalStateException();
        } 
        charges = amt;
        if (amt == maxCharges) {
            o.timeline.cancel(recharger);
        } else if (!recharger.scheduled()) {
            o.timeline.schedIn(getBaseRechargeTime(), recharger);
        }
    }
    
    public void willStartCasting() {} // event
    
    public double locationX;
    public double locationY;
    
    
    public boolean enabled;
    public int defaultLockoutTime;
    public int defaultCastTime;
    public int defaultRechargeTime;
    public int defaultCooldownTime;
    public Power power;
    public int defaultPowerCost;
    //public int defaultChannelTickCount;
    int maxCharges;
    int charges;
    
    public int range_min;
    public int range_max;
    
    final World _world() { return o.world; }
    

    public void setEnergyCost(int amt) {
        power = o.power_energy;
        defaultPowerCost = amt;
    }
    
    public void setManaCost(double perc) {
        power = o.power_mana;
        defaultPowerCost = o.power_mana.percentOfBase(perc);
    }

    /*
    public void setCostDirect(Power power, int amt) {
        this.power = power;
        defaultPowerCost = amt;
    }
    
    public void setCostPercent(Power power, double perc) {
        this.power = power;
        defaultPowerCost = power.percentOfBase(perc);
    }
    */
    
    public int getBaseRechargeTime()    { return defaultRechargeTime; }
    public int getBaseCooldownTime()    { return defaultCooldownTime; }
    public int getBaseLockoutTime()     { return defaultLockoutTime; }   
    public int getBaseCastTime()        { return defaultCastTime; }    
    public int getBasePowerCost()       { return defaultPowerCost; }
    
    public int getPowerCost() { return power == null ? 0 : power.adjust(getBasePowerCost()); }
    
    
    // corresponds to the "state"
    // in cat-form
    public boolean isCastable() { return enabled; }
    
    // corresponds to the "trigger"
    public boolean isReady() { return !cooldownFader.scheduled(); }    
    public int timeUntilReady() { return o.timeline.timeUntil(cooldownFader); }    
    
    public boolean hasPower() {
        return power == null || power.current >= getPowerCost();
    }
    
    public boolean hasCharge() {
        return charges > 0;
    }
    
    // corresponds to the "resource"
    public boolean hasResource() { 
        if (!hasPower()) {
            return false;
        }
        if (maxCharges > 0 && charges == 0) {
            return false;
        }
        return true;
    }
    int timeUntilChargeAvailable() {
        return maxCharges > 0 ? o.world.timeline.timeUntil(recharger) : 0;
    }
    
    public final int getLockoutTime() { return o.adjustCastTime(getBaseLockoutTime()); }    
    public final int getCastTime() { return o.adjustCastTime(getBaseCastTime()); }

    public boolean tryCastOnCurrentTarget() {
        return tryCast(o.currentTargetUnit(), 0, 0);
    }
    
    public boolean tryCastAtLocation(double x, double y) {
        return tryCast(null, x, y);
    }
    
    
    public boolean tryCast(Unit target, double x, double y) {
        if (!isReady()) {
            return false;
        }
        if (!hasCharge()) {
            return false;
        }
        int cost = getPowerCost();
        if (cost > 0 && power.current < cost) {
            return false;
        }
        if (!hasTarget(target, x, y)) {
            return false;
        }        
        int castTime = getCastTime();
        if (castTime == 0) {         
            power.current -= cost;
            casted(target, x, y, 0, cost);   
            return true;
        }           
        o.beginCasting(this, castTime);
        return true;
    }
    
    public void setMeleeRange() {
        range_min = 0;
        range_max = o.world.RANGE_COMBAT;
        range_sweep = Math.PI;
    }
    
    public double getRangeMin() {
        return range_min;
    }
    
    public double getRangeMax() {
        return range_max;
    }
    
    
    public boolean isInsideRange(double dist) {
        return (dist >= getRangeMin() && dist <= getRangeMax());
    }
    
    private boolean checkRange(double x, double y) {
        if (o.world.ignoreLocation) {
            return true;
        }
        if (!isInsideRange(o.distanceTo(x, y))) {
            return false;
        }
        if (!GameHelp.isInsideAngle(o.world_dir - o.angleTo(x, y), range_sweep)) {
            return false;
        }      
        return true;
    }
    
    public boolean hasTarget(Unit target, double x, double y) {
        if (targetStyle == TargetStyle.NONE) {
            return true;
        }
        switch (targetStyle) {
            case NONE: return true;
            case UNIT: {
                if (target == null) {
                    return false;
                }   
                return checkRange(target.world_x, target.world_y);
            }
            case TARGETED_LOCATION: {
                return checkRange(x, y);
            }
            default:
                throw new IllegalStateException();
        }
    }
    
    public void deductCost() {
        if (power != null) {            
            int cost = getBasePowerCost();
            power.add(-getBasePowerCost());
        }
    }
    
    boolean castComplete() {
        if (o.castingTime > 0) {
            if (!hasResource()) {
                return false;
            }
        }  
        return true;
    }
    
    public abstract void casted(Unit target, double x, double y, int castTime, int powerCost);
    
    void castStopped() {
    }
    
    
    public boolean checkGCD() {
        return getBaseLockoutTime() == 0 || !o.lockout.scheduled();
    }
    
    public boolean checkCasting() {
        return usableWhileCasting || !o.casting.scheduled();
    }
    
    public boolean checkTarget() {
        switch (targetStyle) {
            case HARM:
                return o.enemyTarget != null;
            case HELP:
                return false;
            default:
                return true;
        }
    }
    
    public boolean isUsable() {
        return checkGCD() && checkCasting() && isReady() && checkTarget() && resource.isConsumable();
    }
    
    void invokeGCD() {
        o.lockout(getBaseLockoutTime());
    }
    
    void beginCooldown() {
        if (cooldownTime > 0) {
            o.timeline.schedIn(cooldownTime, cooldownFader);
        }
    }
    
    void invokeSpell() {
        beginCooldown();
        if (resource.isConsumable()) {
            resource.consume();
            casted();       
        }
    }
    
    public void channelTicked() {}

    
    
    public void simpleSpellDamage(Unit target, double coeff) {
        HitEvent hit = HitEvent.harm(m, o, target, o.getCritChance(), true, true);
        if (hit.success()) {
            hit.base = o.getSP(m.school) * coeff;
        }
        o.computeAndRecord(hit);
    }
    
    
}
