package catus2;

import catus2.buffs.Buff;
import catus2.procs.Trigger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public abstract class Unit<O extends Unit<O,V>,V extends AbstractView<O>> {
   
    public final boolean npc;
    
    public final ModMap movementSpeed_sum = ModMap.sum(); // wod change
    
    public final ModMap damageRecv_all_product = ModMap.product();
    public final ModMap damageDone_all_product = ModMap.product();
    public final ModMap spellPower_product = ModMap.product();
    public final ModMap[] damageRecv_school_product;
    public final ModMap[] damageDone_school_product;
    public final ModMap healingRecv_all_product = ModMap.product();
    public final ModMap healingDone_all_product = ModMap.product();
    
    public final ModMap healingCritBonus_sum = ModMap.sum(); // assumptions
    public final ModMap damageCirtBonus_sum = ModMap.sum();

    public final ModMap[] stat_product;
    public final int[] stat_raw;
    
    public final int[] perc_rating;
    public final double[] perc_perRating;
    public final ModMap[] perc_rating_product;
    public final ModMap[] perc_product;
    public final ModMap[] perc_sum;
    
    public final HashMap<Integer,Buff> uniqueBuffMap = new HashMap<>(); 
    
    public final ArrayList<Trigger> triggerList = new ArrayList<>();
    public final ArrayList<Runnable> prepList = new ArrayList<>();
    
    public final V selfView = _createView(this); // not sure when this is executed during constructor
    
    public Unit(boolean npc) {
        this.npc = npc;
        damageDone_school_product = new ModMap[SchoolT.NUM];    
        damageRecv_school_product = new ModMap[SchoolT.NUM];
        for (int i = 0; i < SchoolT.NUM; i++) {
            damageDone_school_product[i] = new ModMap(true);
            damageRecv_school_product[i] = new ModMap(true);
        }         
        stat_raw = new int[UnitStat.NUM];
        stat_product = new ModMap[UnitStat.NUM];  
        for (int i = 0; i < UnitStat.NUM; i++) {
            stat_product[i] = new ModMap(true);
        }        
        perc_rating = new int[UnitPerc.NUM];
        perc_perRating = new double[UnitPerc.NUM];
        perc_rating_product = new ModMap[UnitPerc.NUM];
        perc_product = new ModMap[UnitPerc.NUM];
        perc_sum = new ModMap[UnitPerc.NUM];     
    }
    
    public void prepareForCombat() {
        
    }
    
    public void resetStatsAndRating() {
        
    }
    
        
    public int getRating(int i) {
        return (int)(0.5 + perc_rating_product[i].fold() * perc_rating[i] / perc_perRating[i]);
    }    
    public double getRatingPercent(int i) {
        return (getRating(i) + perc_sum[i].fold()) * perc_product[i].fold();
    }
    public int getStat(int i, double extra) {
        return (int)(0.5 + (stat_raw[i] + extra) * stat_product[i].fold());        
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
        return unit != this; // fix me
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
        return GameData.HP_PER_STA * getStat(UnitStat.STA, 0);
    }
    
    public double getHealthPercent() {
        return 0.5;
    }
    
    public boolean isDead() {
        return false;
    }
    
    // ---
    
    public boolean isBleeding() {
        return true;
    }
    
    public double getDamageTakenMod(int schoolMask) {
        return 1;
    }
    
    public double getDamageDoneMod(int schoolMask) {
        double mod = damageDone_all_product.fold();
        for (int i = 0; i < SchoolT.NUM && schoolMask > 0; i++) {
            int bit = 1 << i;
            if ((bit & schoolMask) == bit) {
                mod *= damageDone_school_product[i].fold();
            }            
        }
        return mod;
    }
    
    // --
    
    public double getMovementSpeed() {
        return movementSpeed_sum.fold();
    }
    
   
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
    
    public double getMeleeMissChance() {
        return 0.03;
    }
    
    public double getSpellMiss() {
        return 0.06;
    }
    
    public double getParryChance() {
        return 0.03;
    }
    
    public double getBlockChance() {
        return 0.03;
    }
    
    public double getHitAndExpertise() { // merged into 1 since this stat is dead    
        return npc ? 0 : 0.15;
    }
    
    public double getCritHealBonusMod(boolean npc) {
        return (npc ? 2 : 1.5);
    }
    
    public double getCritDamageBonusMod() {
        return 1;
    }
    
    
    
    public boolean isStealthed() {
        return false;
    }
    
    public int getSP_extra(int schoolIndex) {
        return 0;
    }
    
    public int getSP(int schoolMask) {
        int max = 0;
        for (int i = 0; i < SchoolT.NUM; i++) {
            if ((schoolMask & (1 << i)) != 0) {
                max = Math.max(max, getSP_extra(i));
            }            
        }        
        return getStat(UnitStat.SP, max);
    }
    
    public int getAP_extra() {
        return 0;
    }    
    
    public int getAP() {
        return getStat(UnitStat.AP, getAP_extra());
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
            //heal *= getCritHealBonusMod(target);
            flags |= FLAG_CRIT;
        }
        applyRawHeal(heal, target, source, origin, schoolMask, flags);
        if (multiChance > 0) {
            for (int i = 0; i < 2; i++) {
                if (world.randomChance(multiChance)) {
                    heal = base * world.multistrikeDamageMod;
                    flags = FLAG_MULTI;
                    if (world.randomChance(critChance)) {
                        //heal *= getCritHealBonusMod(target);
                        flags |= FLAG_CRIT;
                    }                    
                    applyRawHeal(heal, target, source, origin, schoolMask, flags);
                }
            }
        }
    }
    
    public void applyRawDamage(Unit target, Object source, int schoolMask, boolean crit, double damage) {
        
        if (crit) {
            damage += damage * getCritDamageBonusMod();
        }                
        damage *= target.getDamageTakenMod(schoolMask);
        
    }
    
    // these args suck...
    // need access to crit
    public void applyDamage(double damage, Unit target, Object source, OriginT origin, int schoolMask, boolean crit) {
        boolean blockable = false;
        switch (origin) {
            case MELEE:
            case WHITE:
                blockable = true;                
        }        
        applyDamage(damage, target, source, origin, schoolMask, crit, getMultistrikeChance(), blockable);
    }
    public void applyDamage(double damage, Unit target, Object source, OriginT origin, int schoolMask, boolean crit, double multiChance, boolean blockable) {
        
        damage *= getDamageDoneMod(schoolMask);
        applyRawDamage(target, source, schoolMask, crit, damage);
        if (multiChance > 0) {
            for (int i = 0; i < 2; i++) {
                if (world.randomChance(multiChance)) {
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
    public final Power power_rage = new Power();
    
    // -------
    
    public V currentTarget;
    
    /*
    public void setTarget(Unit actor) {
        currentTarget = getUnitData(actor);
    }
    */
    
    private final HashMap<Unit,V> viewMap = new HashMap<>();
    
    protected abstract V _createView(Unit actor); // do not call this function
    
    public V getView(Unit unit) {
        V temp = viewMap.get(unit);
        if (temp == null) {
            temp = _createView(unit);
            viewMap.put(unit, temp);
        }
        return temp;        
    }
    
    // ----    
    // fix me: move to AttackTable class or something 
        
    // base dodge = 3
    // base parry = 3
    // base block = 3
    // base melee miss = 3
    // base spell miss = 6
    // player hit = 7.5
    // player exp = 7.5
    // npc hit = 0
    // npc exp = 0
    
    static double getCritChanceDelta(int defenderLevelDelta) {
        return defenderLevelDelta > 0 ? -0.01 * Math.min(defenderLevelDelta, 10) : 0;
    }
    
    static double getSpellMissChance(int defenderLevelDelta) {
        return 0.11 * Math.max(defenderLevelDelta - 3, 0);
    }
    
    /*
    Hit and Expertise has been removed as a secondary stat.
    Hit and Expertise bonuses on all items and item enhancements (gems, enchants, etc.) have been converted into Critical Strike, Haste, or Mastery.
    All characters now have a 100% chance to hit, 0% chance to be dodged, 3% chance to be parried, and 0% chance for glancing blows, when fighting creatures up to 3 levels higher (bosses included).
    Tanking specializations receive an additional 3% reduction in chance to be parried. Tank attacks now have a 0% chance to be parried vs. creatures up to 3 levels higher.
    Creatures that are 4 or more levels higher than the character still has a chance to avoid attacks in various ways, to discourage fighting enemies that are much stronger.
    Dual Wielding still imposes a 19% chance to miss, to balance it with two-handed weapon use.
    */
    
    public AttackT yellow_spell(Unit target, double critChance) {
        double p = world.rng.nextDouble();
        // miss
        // crit
        // hit
        int LD = target.level - level;
        double missChance = target.getSpellMiss() + 0.03 * LD + 0.08 * Math.max(LD - 3, 0) - getHitAndExpertise();
        if (missChance > 0) {
            p -= missChance;
            if (p < 0) {
                return AttackT.MISS;
            }
        }
        if (critChance > 0) {
            p -= critChance + getCritChanceDelta(LD);
            if (p < 0) {
                return AttackT.CRIT;
            }
        }
        return AttackT.HIT;
    }
    
    public AttackT yellow_melee(Unit target, double crit) {
        crit -= Math.max(0, target.level - level); 
        
        double p = world.rng.nextDouble();
        
        return AttackT.HIT;
    }
    
    public void init() {
        
    }
    
}
