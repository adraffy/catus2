package catus2;

import catus2.buffs.AbstractBuffModel;
import catus2.buffs.ProductBuff;
import catus2.procs.Trigger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public abstract class Unit<V extends AbstractView<? extends Unit>> {
    
    public final ProductMap damageTakenMod = new ProductMap();
    public final ProductMap[] damageDoneMods = new ProductMap[SchoolT.NUM];
    
    public final ArrayList<Trigger> triggerList = new ArrayList<>();
    
    public final V self = createView(this);    
    
    public Unit() {
        createStacks();        
    }
    
    private void createStacks() {
        for (int i = 0; i < SchoolT.NUM; i++) {
            damageDoneMods[i] = new ProductMap();
        }
    }
    
    int faction;
    
    //public final UnitFilter enemyFilter = (u) -> u.faction != faction;
    
    public double defaultWorldX;
    public double defaultWorldY;
    
    public double behindTargetProb;
    
    public World world;
    
    public int level;
    
    //final ArrayList<Proc>
    
    public double world_x;
    public double world_y;
    public double world_dir;
    
    
    public final Timeline timeline() { return world.timeline; }
    
    /*
    public double unitRadius;
    public boolean moving;    
    public double getUnitRadius() {
        return unitRadius + (moving ? 2 : 0);
    }
    */
    
    public double distanceTo(double x, double y) {
        return Math.hypot(x - world_x, y - world_y);
    }
    
    public double angleTo(double x, double y) {
        return Math.atan2(x - world_x, y - world_y);        
    }
    
    public boolean isInsideDoughnut(double x, double y, double r0, double r1) {
        if (world.ignoreLocation) {
            return true;
        }
        double dist = distanceTo(x, y);
        /*if (dist >= r0 && dist <= r1) {
            return true;
        }        
        return dist < radius + getUnitRadius();*/
        return dist >= r0 && dist <= r1;
    }
    
    public boolean isInsideCone(double x, double y, double dir, double sweep) {
        if (world.ignoreLocation) {
            return true;
        }
        double ang = angleTo(x, y);        
        return GameHelp.isInsideAngle(ang - dir, sweep);
    }
    
    public boolean isEnemy(Unit unit) {
        return unit != this;
    }
    
    public void collectUnits(Collection<Unit> units, boolean isEnemy, double x, double y, double r1) {
        collectUnits(units, isEnemy, x, y, 0, r1, 0, GameHelp.ONE_PI);
    }
    public void collectUnits(Collection<Unit> units, boolean isEnemy, double x, double y, double r0, double r1, double dir, double sweep) {
        for (Unit u: world.unitList) {
            if (isEnemy(u) == isEnemy && u.isInsideDoughnut(x, y, r0, r1)) {                
                if (sweep >= GameHelp.ONE_PI || u.isInsideCone(x, y, dir, sweep)) {
                    units.add(u);
                }                
            }
        }
    }
    
    // ---
    
    final Tick lockout = new Tick() {
        @Override
        public void run() {
            
        }        
    };
    
    int castingTime;
    AbstractSpell castingSpell;
    
    final Tick casting = new Tick() {
        @Override
        public void run() {
            if (!castingSpell.castComplete()) {
                // for some reason, we couldn't cast the spell
                // even tho we did the cast
                timeline().cancel(lockout);
            }
        }        
    };
        
    void beginCasting(AbstractSpell spell, int castTime) {
        if (casting.scheduled()) {
            throw new IllegalStateException();
        }
        castingSpell = spell;
        castingTime = castTime;
        timeline().schedIn(castTime, casting);
        timeline().schedIn(spell.getLockoutTime(), lockout);
        spell.willStartCasting(); 
    }
    
    
    Runnable channelCallback;
    int channelTicks;
    int channelFreq;
    
    
    final Tick channelTick = new Tick() {
        @Override
        public void run() {
            if (channelTicks > 1) {
                channelTicks--;
                schedChannel();
            }
            castingSpell.channelTicked();            
        }
    };
    
    private void schedChannel() {
        world.timeline.schedIn(adjustCastTime(channelFreq), casting);
    }
    
    public void beginChannel(AbstractSpell spell, int ticks, int freq) {
        if (isCasting()) {
            throw new IllegalStateException();
        }
        channelTicks = ticks;
        channelFreq = freq;
        if (ticks > 0) {
            schedChannel();
        }
    }
    
    public boolean isCasting() {
        return casting.scheduled() || channelTick.scheduled();
    }
    
    public boolean tryInterrupt() {
        return false;
    }
    
    public void stopCasting() {
        if (timeline().cancel(casting)) {    
            castingSpell.castStopped();
            timeline().cancel(lockout);            
        } else if (timeline().cancel(channelTick)) {
            castingSpell.castStopped();
        }
    }
    
    public int adjustCastTime(int t) {
        if (t > world.GCD_FLOOR) {
            return Math.max(world.GCD_FLOOR, (int)(t / getHasteMod()));
        } else {
            return t;
        }
    }
    
    // ---
    
    public int getBaseSwingTime() {
        return 1000;
    }
    
    public double getHealthMaximum() {
        return 1000000;
    }
    
    public double getHealthPercent() {
        return 0.5;
    }
    
    public boolean isDead() {
        return false;
    }
    
    // ---
    
    int bleedCounter;
    
    public boolean isBleeding() {
        return bleedCounter > 0;
    }
    
    
    public double getDamageTakenMod(int schoolMask) {
        return 1;
    }
    
    public double getDamageDoneMod(int schoolMask) {
        double mod = 1;
        for (int i = 0; i < SchoolT.NUM && schoolMask > 0; i++) {
            int bit = 1 << i;
            if ((bit & schoolMask) == bit) {
                mod *= damageDoneMods[i].product();
            }            
        }
        return mod;
    }
    
    // --
    
    public final ProductMap movementSpeedMods = new ProductMap();
    
    public double getMovementSpeed() {
        return movementSpeedMods.product();
    }
    
    public final ProductBuff buff_stampRoar = new ProductBuff(new AbstractBuffModel(SpellId.Druid.STAMPEDING_ROAR), self, movementSpeedMods);
    
    /*
    public boolean hasMovementSpeed(int id) {
        return movementSpeed_mods.contains(id);
    }
    */
    
    
    public int getMasteryRating() {
        return 0;
    }
    
    public double getHasteMod() {
        return 1;
    }
    
    public boolean chanceMeleeCrit() {
        return world.randomChance(getCritChance());
    }
    
    public double getCritChance() {
        return 0.5;
    }
    
    public double getBlockChance() {
        return 0;
    }
    
    public double getCritHealMod() {
        return 2;
    }
    
    public double getCritDamageMod() {
        return 2;
    }
    
    public boolean isStealthed() {
        return false;
    }
    
    public int getSP(int schoolIndex) {
        return 0;
    }
    
    public int getAP() {
        return 1;
    }
    
    public double getMultistrikeChance() {
        return 0.1;
    }
    
    //
    
    static public final int FLAG_CRIT  = 0b0001;
    static public final int FLAG_MULTI = 0b0010;
   
    public void applyRawHeal(double base, Unit target, Object source, OriginT origin, int schoolMask, int flags) {
        
    }
    
    public void applyHeal_percentHealth(double perc, Unit target, Object source, OriginT origin, int schoolMask) {
        applyRawHeal(perc * target.getHealthMaximum(), target, source, origin, schoolMask, 0);
    }
    
    public void applyHeal(double base, Unit target, Object source, OriginT origin, int schoolMask, double critChance, double multiChance) {
        double heal = base;
        int flags = 0;
        if (world.randomChance(critChance)) {
            heal *= getCritHealMod();
            flags |= FLAG_CRIT;
        }
        applyRawHeal(heal, target, source, origin, schoolMask, flags);
        if (multiChance > 0) {
            for (int i = 0; i < 2; i++) {
                if (world.randomChance(multiChance)) {
                    heal = base * world.multistrikeDamageMod;
                    flags = FLAG_MULTI;
                    if (world.randomChance(critChance)) {
                        heal *= getCritHealMod();
                        flags |= FLAG_CRIT;
                    }                    
                    applyRawHeal(heal, target, source, origin, schoolMask, flags);
                }
            }
        }
    }
    
    public void applyRawDamage(Unit target, Object source, int schoolMask, boolean crit, double damage) {
        
        if (crit) {
            damage *= getCritDamageMod();
        }        
        
    }
    
    
    public void applyDamage(double damage, Unit target, Object source, OriginT origin, int schoolMask, boolean crit) {
        applyDamage(damage, target, source, origin, schoolMask, crit, getMultistrikeChance(), true);
    }
    public void applyDamage(double damage, Unit target, Object source, OriginT origin, int schoolMask, boolean crit, double multistrikeChance, boolean canGetBlocked) {
        damage *= getDamageDoneMod(schoolMask);
        applyRawDamage(target, source, schoolMask, crit, damage);
        if (multistrikeChance > 0) {
            for (int i = 0; i < 2; i++) {
                if (world.randomChance(multistrikeChance)) {
                    applyRawDamage(target, source, schoolMask, crit, damage * world.multistrikeDamageMod);
                }
            }
        }
    }
    
    
    // ---
    
    
    public double getBlockMod(Unit attacker) {  
        boolean blocked;
        if (world.ignoreLocation) {
            blocked = world.randomChance(attacker.behindTargetProb);
        } else {
            double angleToAttacker = angleTo(attacker.world_x, attacker.world_y);
            double evasionSweep = GameHelp.HALF_PI;
            blocked = GameHelp.isInsideAngle(angleToAttacker - world_dir, evasionSweep) && world.randomChance(getBlockChance());
        }        
        return blocked ? 0.7 : 1;
    }       
    
    // -------
    
    public final Power power_combos = new Power();
    public final Power power_energy = new Power();
    public final Power power_mana = new Power();
    
    // -------
    
    public V currentTarget;
    
    /*
    public void setTarget(Unit actor) {
        currentTarget = getUnitData(actor);
    }
    */
    
    private final HashMap<Unit,V> viewMap = new HashMap<>();
    
    public abstract V createView(Unit actor);
    
    public V getUnitData(Unit actor) {
        V temp = viewMap.get(actor);
        if (temp == null) {
            temp = createView(actor);
            viewMap.put(actor, temp);
        }
        return temp;        
    }
    
    public AttackT yellow_melee(Unit target, double crit) {
        crit -= Math.max(0, target.level - level); 
        
        double p = world.rng.nextDouble();
        
        return AttackT.HIT;
    }
    
    public void init() {
        
    }
    
}
