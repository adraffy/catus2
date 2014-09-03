package catus2;

public abstract class AbstractSpell<O extends Unit> {
    
    public final O o;
    public final TargetStyle targetStyle;
    public boolean harm;
    public double range_sweep;
    public SpellModel m;
    
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
        if (hit.landed()) {
            hit.base = o.getSP(m.school) * coeff;
        }
        o.computeAndRecord(hit);
    }
    
    
}
