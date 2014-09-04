package catus2;

import catus2.combat.HitEvent;
import catus2.combat.CrowdControl;
import catus2.buffs.Buff;
import catus2.buffs.BuffModel;
import catus2.procs.Trigger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

abstract public class Unit<O extends Unit<O,V>,V extends AbstractView<O>> {
   
    public final boolean npc;
    
    public final ModMap movementSpeed_sum = new ModMap.Sum(); // wod change
    
    public final ModMap spellPower_product = new ModMap.Product();
    
    public final IntSet incomingDamageImmune_school_set = new IntSet();
    public final ModMap incomingDamageMod_all_product = new ModMap.Product();
    public final ModMap[] incomingDamageMod_school_product;
    
    public final ModMap damageDone_all_product = new ModMap.Product();
    public final ModMap[] damageDone_school_product;
    public final ModMap damageDone_critBonus_sum = new ModMap.Sum();
    
    public final ModMap healingRecv_all_product = new ModMap.Product();
    
    public final ModMap healingDone_all_product = new ModMap.Product();   
    public final ModMap healingDone_critBonus_sum = new ModMap.Sum(); // assumptions

    
    public final int[] cc_resetTime = new int[CrowdControl.N];
    public final int[] cc_stackCount = new int[CrowdControl.N];
    
    
    static final BuffModel buffModel_stunned = new BuffModel(-1);
    static final BuffModel buffModel_silenced = new BuffModel(-1);
    
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
        damageDone_school_product = new ModMap[School.Idx.N];    
        incomingDamageMod_school_product = new ModMap[School.Idx.N];
        for (int i = 0; i < School.Idx.N; i++) {
            damageDone_school_product[i] = new ModMap.Product();
            incomingDamageMod_school_product[i] = new ModMap.Product();
        }         
        stat_raw = new int[UnitStat.NUM];
        stat_product = new ModMap[UnitStat.NUM];  
        for (int i = 0; i < UnitStat.NUM; i++) {
            stat_product[i] = new ModMap.Product();
        }        
        perc_rating = new int[UnitPerc.N];
        perc_perRating = new double[UnitPerc.N];
        perc_rating_product = new ModMap[UnitPerc.N];
        perc_product = new ModMap[UnitPerc.N];
        perc_sum = new ModMap[UnitPerc.N];     
    }
    
    public void prepareForCombat() {
        for (int i = 0; i < UnitPerc.N; i++) {
            perc_sum[i].clear();
            perc_product[i].clear();
            perc_rating_product[i].clear();
        }        
        perc_sum[UnitPerc.DODGE].set(SpellId.Custom.BASE, 0.03);
        perc_sum[UnitPerc.PARRY].set(SpellId.Custom.BASE, 0.03);
        perc_sum[UnitPerc.BLOCK].set(SpellId.Custom.BASE, 0.03);  
    }
    
    // ---
    
    public int getRating(int i) {
        int r = perc_rating[i];
        return r == 0 ? 0 : (int)(0.5 + perc_rating_product[i].fold() * r / perc_perRating[i]);
    }    
    
    public double getPercent(int i) {
        return (getRating(i) + perc_sum[i].fold()) * perc_product[i].fold();
    }
    
    public int getStat(int i, double extra) {
        return (int)(0.5 + (stat_raw[i] + extra) * stat_product[i].fold());        
    }
  
    // ---
    
    int faction;
    
    //public final UnitFilter enemyFilter = (u) -> u.faction != faction;
    
    public double defaultWorldX;
    public double defaultWorldY;
    
    public double frontTargetChance;
    
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
    
    
    public boolean inFrontOf(Unit target) {
        if (world.ignoreLocation) {
            return !world.randomChance(frontTargetChance);
        } else {
            double angleToAttacker = target.angleTo(world_x, world_y);
            double evasionSweep = GameHelp.HALF_PI;
            return !GameHelp.isInsideAngle(angleToAttacker - target.world_dir, evasionSweep);
        }  
    }
    
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
    
    public double getDamageTakenMod(School schoo) {
        return 1;
    }
    
    public double getDamageDoneMod(School school) {
        double mod = damageDone_all_product.fold();
        for (int i : school.indexes) {
            mod *= damageDone_school_product[i].fold();
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
    
    
    
    public double getMastery() {
        return getPercent(UnitPerc.MASTERY);
    }
    
    public double getAttackSpeedMod() {
        return getHasteMod();
    }
    
    public double getHasteMod() {
        return 1;
    }
    
    public boolean chanceMeleeCrit() {
        return world.randomChance(getCritChance());
    }
    
    public double getCritChance() {
        return getPercent(UnitPerc.CRIT);
    }
     
    public double getMultistrikeChance() {
        return getPercent(UnitPerc.MULTI);
    }
    
    public double getMeleeMissChance() {
        return 0.03; // deterence?
    }
    
    public double getSpellMissChance() {
        return 0.06;
    }
    
    /*
    public double getSpellReflectChance() {
        return 0;
    }
    */
    
    public boolean canSpellReflect(Unit caster, School school) {
        // consume charges
        // check school
        return false;
    }
    
    
    public double getDodgeChance() {
        return getPercent(UnitPerc.DODGE); // +0.03;
    }
    
    public double getParryChance() {
        return getPercent(UnitPerc.PARRY); // +0.03;
    }
    
    public double getBlockChance() {
        return getPercent(UnitPerc.BLOCK); // +0.03;
    }
    
    public double getHitChance() { 
        return getPercent(UnitPerc.HIT);
    }
    
    public double getExpChance() { 
        return getPercent(UnitPerc.EXP);
    }
    
    public double getCritHealBonusMod() {
        return 1;
    }
    
    // this fucking equation again:
    // 1 + ((crit effect) * (crit effect mods) - 1) * (crit damage mods)
    // 1 + (2 * (1 + 0.03) - 1) * (1 + 0.2) * (1 + 0.3) 
    // 
    // Expand[1 + (a*b - 1)*c*d] == 1 - c d + a b c d
    // 1 + product(any mod) - product(crit damage mods)
    // 
    public double getCritDamageBonusMod() {
        return 1;
    }
           
    
    public boolean isStealthed() {
        return false;
    }
    
    public int getSP_extra(int schoolIndex) {
        return 0;
    }
    
    public final int getSP(School school) {
        int max = 0;
        for (int i : school.indexes) {
            max = Math.max(max, getSP_extra(i));
        }        
        return getStat(UnitStat.SP, max);
    }
    
    public int getAP_extra() {
        return 0;
    }    
    
    public final int getAP() {
        return getStat(UnitStat.AP, getAP_extra());
    }
    
    //
    
    public void applyRawHeal(double base, Unit target, Object source, Origin origin, School school, int flags) {
        
    }
    
    public void applyHeal_percentHealth(double perc, Unit target, Object source, Origin origin, School school) {
        applyRawHeal(perc * target.getHealthMaximum(), target, source, origin, school, 0);
    }
    
    public void applyHeal(double base, Unit target, Object source, Origin origin, School school, double critChance, double multiChance) {
        double heal = base;
        int flags = 0;
        if (world.randomChance(critChance)) {
            //heal *= getCritHealBonusMod(target);
            //flags |= FLAG_CRIT;
        }
        applyRawHeal(heal, target, source, origin, school, flags);
        if (multiChance > 0) {
            for (int i = 0; i < 2; i++) {
                if (world.randomChance(multiChance)) {
                    heal = base * world.multistrikeDamageMod;
                    //flags = FLAG_MULTI;
                    if (world.randomChance(critChance)) {
                        //heal *= getCritHealBonusMod(target);
                      //  flags |= FLAG_CRIT;
                    }                    
                    applyRawHeal(heal, target, source, origin, school, flags);
                }
            }
        }
    }
    
    public void applyRawDamage(Unit target, Object source, School school, boolean crit, double damage) {
        
        if (crit) {
            damage += damage * getCritDamageBonusMod();
        }                
        damage *= target.getDamageTakenMod(school);
        
    }
    
    public void applyDamage(double damage, Unit target, Object source, Origin origin, School school, boolean crit) {
        boolean blockable = false;
        switch (origin) {
            case MELEE:
            case WHITE:
                blockable = true;                
        }        
        applyDamage(damage, target, source, origin, school, crit, getMultistrikeChance(), blockable);
    }
    public void applyDamage(double damage, Unit target, Object source, Origin origin, School school, boolean crit, double multiChance, boolean blockable) {
        
        damage *= getDamageDoneMod(school);
        applyRawDamage(target, source, school, crit, damage);
        if (multiChance > 0) {
            for (int i = 0; i < 2; i++) {
                if (world.randomChance(multiChance)) {
                    applyRawDamage(target, source, school, crit, damage * world.multistrikeDamageMod);
                }
            }
        }
    }
        
    // -------
    
    public final Power power_combos = new Power();
    public final Power power_energy = new Power();
    public final Power power_mana = new Power();
    public final Power power_rage = new Power();
    
    // ---
    
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
    
    // ---
    
    public void init() {
        // stuff goes here
    }
  
    /*
    public ApplyEvent yellow_spell(Unit target) { return yellow_spell(target, getCritChance()); }
    public ApplyEvent yellow_spell(Unit target, double critChance) {
        double p = world.rng.nextDouble();
        int LD = target.level - level;
        double missChance = target.getSpellMissChance() + 0.03 * LD + 0.08 * Math.max(LD - 3, 0) - getHitChance() - getExpChance();
        if (missChance > 0) {
            p -= missChance;
            if (p < 0) {
                return new ApplyEvent(target, ApplyEvent.MissType.MISS);
            }
        }
        ApplyEvent de = new ApplyEvent(target, null);
        if (critChance > 0) {
            de.crit = p < critChance - getCritSupression(LD);
        }
        return de;
    }
    
    public ApplyEvent yellow_tick(Unit target) { return yellow_tick(target, getCritChance()); }
    public ApplyEvent yellow_tick(Unit target, double critChance) {
        int LD = target.level - level;   
        ApplyEvent de = new ApplyEvent(target, null);
        if (critChance > 0) {
            de.crit = world.randomChance(critChance - getCritSupression(LD));
        }
        return de;
    }
    
    public AttackT yellow_melee(Unit target, double crit) {
        
        crit -= Math.max(0, target.level - level); 
        
        double p = world.rng.nextDouble();
        
        return AttackT.HIT;
    }
    */
      
    // ----    
    // fix me: move to AttackTable class or something 
  
    public void computeAndRecord(HitEvent hit) {
        // normalize it
        
        if (hit.success()) {            
            
            hit.computed = hit.base * hit.caster.getDamageDoneMod(hit.spell.school);
            
            // we need to intercept this
            
            hit.computed *= hit.target.getDamageTakenMod(hit.spell.school);
            
            
        } else {
            hit.computed = 0;
        }    
    }
    
    
    public void record(HitEvent hit) {
        
    }
    
    
    public void didDamage(HitEvent hit) {
        System.out.println(String.format("[%s] %s %s [%s] %.0f %s", hit.caster, hit.spell.getNameAndId(), "hit", hit.target, hit.computed, hit.spell.school));
    }
    
    
    // assumed non-negative
    static double getCritSupression(int defenderLevelDelta) {
        return defenderLevelDelta > 0 ? 0.01 * Math.min(defenderLevelDelta, 10) : 0;
    }
    
    
    
    
    
    public Application tryApply(Unit target, Object source, Origin origin, School school) {
        return tryApply(target, source, origin, school, getCritChance(), 0);
    }
    public Application tryApply(Unit target, Object source, Origin origin, School school, double critChance, int flags) {
        double p = world.rng.nextDouble();
        int LD = target.level - level;        
        boolean isHeal = false;
        boolean isWhite = false; // enable-able via flag?
        boolean canEvade = false; // enable-able via flag?
        double missChance;
        switch (origin) {
            case HEAL:
            case HOT:
                isHeal = true;
            case BLEED:
            case DOT:
                missChance = 0;
                break;                
            case WHITE:
                isWhite = true;
            case MELEE:
            case MELEE_BLEED:
                canEvade = true;
                missChance = world.neverMiss ? 0 : target.getMeleeMissChance() + 0.015 * LD + (target.npc ? 0.015 * Math.max(LD - 3, 0) : 0) - getHitChance();
                break;
            case SPELL:
                missChance = world.neverMiss ? 0 : target.getSpellMissChance() + 0.03 * LD + 0.08 * Math.max(LD - 3, 0) - getHitChance() - getExpChance();
                break;
            default:
                throw new IllegalArgumentException("Unknown origin: " + origin);            
        }
        if (missChance > 0) {
            p -= missChance;
            if (p < 0) {
                return new Application(target, source, origin, school, Application.MissType.MISS);
            }
        }        
        boolean inFront = false;
        if (canEvade) {
            inFront = inFrontOf(target);
            if (inFront) {
                if ((flags & Application.CANNOT_BE_DODGED) == 0) {
                    double dodgeChance = target.getDodgeChance() + 0.015 * LD - getExpChance();
                    if (dodgeChance > 0) {
                        p -= dodgeChance;
                        if (p < 0) {
                            return new Application(target, source, origin, school, Application.MissType.DODGE);
                        }
                    }            
                }        
                if ((flags & Application.CANNOT_BE_PARRIED) == 0) {
                    double parryChance = target.getParryChance() + 0.015 * LD + (target.npc && LD > 2 ? 0.03 : 0) - getExpChance();
                    if (parryChance > 0) {
                        p -= parryChance;
                        if (p < 0) {
                            return new Application(target, source, origin, school, Application.MissType.PARRY);
                        }
                    }            
                }
            }
        }
        Application de = new Application(target, source, origin, school, null);
        found: {
            if (isWhite && target.npc && LD > 3) { // can glance
                double glanceChance = 0.1 + 0.1 * LD;
                p -= glanceChance;
                if (p < 0) {
                    de.coeff = 0.75;
                    de.flags |= Application.DID_GLANCE;
                    break found;
                }            
            }
            if (critChance > 0) { // can crit
                p -= critChance - getCritSupression(LD);
                if (p < 0) {
                    de.coeff = isHeal ? getCritHealBonusMod() : getCritDamageBonusMod();
                    de.flags |= Application.DID_CRIT;
                    break found;
                }
            }
            if (isWhite && npc && LD < -3) { // can crush
                double crushChance = -0.15 + -0.1 * LD;
                p -= crushChance;
                if (p < 0) {
                    de.coeff = 1.5;
                    de.flags |= Application.DID_CRUSH;
                    break found;
                }            
            }        
        }    
        if (inFront && (flags & Application.CANNOT_BE_BLOCKED) == 0) { // can block (second roll)
            double blockChance = getBlockChance() + 0.015 * LD; 
            if (world.randomChance(blockChance)) {
                de.flags |= Application.DID_BLOCK;
            }
        }   
        return de;
    }
        
    public void executeApply(Application app) {  
        double amount = app.amount();
        if (app.heal()) {
            
            
        } else {
            double damage = app.base * app.coeff;
            
        }  
        /*if (app.canMultistrike) {
            double base = app.base * world.multistrikeCoeff;
            for (int i = 0; i < 2; i++) { 
                //...
            }
        }*/
    }
    
    // ---
    // auto-attack scheduling
    
    public int getBaseSwingTime() {
        return 1000;
    }
    
    public int getSwingTime() {
        return (int)(0.5 + getBaseSwingTime() / getAttackSpeedMod());
    }
    
    private boolean swingAuto;
    private boolean swingAvailable;
    private final Tick swing_fader = new Tick() {
        @Override
        public void run() {
            swingAvailable = true;
            trySwing();
        }        
    };
    
    public void restartSwing() {
        swingAvailable = false;
        world.timeline.schedIn(getSwingTime(), swing_fader);    
    } 
    
    public void trySwing() {
        if (currentTarget == null) {
            swingAuto = false; // disable if no-target
            return;
        }
        Unit target = currentTarget.u;
        if (!isEnemy(target)) {
            swingAuto = false; // disable if friendly
            return;
        }            
        if (world.ignoreLocation) {
            swingAt(target); // fk range!
        } else {
            if (target.distanceTo(world_x, world_y) < 5) {
                return; // target out of range
            }
            if (!target.inFrontOf(this)) {
                return; // not facing right direction
            }
            swingAt(target);
        }    
        restartSwing();
    }
    
    private void swingAt(Unit target) {
        
    }
   

    // ---
    
    public boolean isCrowdControlImmune(int cc) {
        if (world.ignoreCrowdControl) {
            return false;
        } else {
            return cc_stackCount[cc] == CrowdControl.MAX_STACKS && world.timeline.clock < cc_resetTime[cc];
        }
    }
    
    // probably should add getExpectedCrowdControlDuration(int cc)
    
    // fist of fury applies a debuff
    // which should be length of applyCrowdControl()
    // if debuff is active, don't call applyCrowdControl() on next punch
    
    public int applyCrowdControl(int cc, int duration) {
        // return duration, 0 = immune
        if (world.ignoreCrowdControl) {
            return duration; // allow it to land, ignore
        }
        if (npc && cc != CrowdControl.STUN) {
            return duration; // only stuns dr for npcs
        }
        if (!npc && duration > CrowdControl.MAX_DURATION) { // too long!
            duration = CrowdControl.MAX_DURATION;
        }
        if (world.timeline.clock > cc_resetTime[cc]) { // it has worn off
            cc_stackCount[cc] = 1;            
        } else if (cc_stackCount[cc] == CrowdControl.MAX_STACKS) { // immune
            // don't update the reset timer
            return 0;
        } else {
            duration = (int)(0.5 + duration * Math.pow(CrowdControl.DR_COEFF, cc_stackCount[cc]++));
        }
        cc_resetTime[cc] = world.timeline.clock + CrowdControl.RESET_DURATION;
        return duration;        
    }
    
    
}
