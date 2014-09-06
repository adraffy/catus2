package catus2.feral;

import catus2.ActivatorSpell;
import catus2.EnumHelp;
import catus2.GameHelp;
import catus2.combat.HitEvent;
import catus2.ModMap;
import catus2.Origin;
import catus2.PlayerUnit;
import catus2.School;
import catus2.SpellId;
import catus2.SpellData;
import catus2.Unit;
import catus2.UnitPerc;
import catus2.UnitStat;
import catus2.buffs.ActivatorBuff;
import catus2.buffs.Buff;
import catus2.buffs.BuffModel;
import catus2.buffs.ModBuff;
import catus2.chance.ChanceFactory;
import catus2.chance.PPM;
import catus2.chance.Probability;
import catus2.feral.aoe.Swipe;
import catus2.feral.aoe.ThrashBear;
import catus2.feral.aoe.ThrashCat;
import catus2.feral.finishers.FerociousBite;
import catus2.feral.finishers.Maim;
import catus2.feral.finishers.Rip;
import catus2.feral.finishers.SavageRoar;
import catus2.feral.generators.MoonfireCat;
import catus2.feral.generators.Rake;
import catus2.feral.generators.Shred;
import catus2.feral.spells.BerserkCatBuff;
import catus2.feral.spells.FaerieFire;
import catus2.feral.spells.HealingTouch;
import catus2.feral.spells.Moonfire;
import catus2.feral.spells.Rejuvenation;
import catus2.feral.spells.StampedingRoar;
import catus2.feral.spells.TigersFury;
import catus2.feral.spells.Wrath;
import catus2.feral.talents.Incarnation;
import catus2.procs.Trigger;

public class Feral extends PlayerUnit<Feral,FeralView,FeralConfig> {
    
    // i don't know where to stick these
    static public final SpellData DEBUFF_T17_4PC = new SpellData(SpellId.Druid.Feral.Bonus.T17_4PC_DEBUFF, "Gushing Wound", School.PHYSICAL);
    static public final SpellData DEBUFF_PVP_WOD_4PC = new SpellData(SpellId.Druid.Feral.Bonus.PVP_WOD_4PC_DEBUFF, "Bloodletting", School.PHYSICAL);
    
    static public final SpellData BUFF_STAMPEDING_ROAR = new SpellData(SpellId.Druid.STAMPEDING_ROAR, "Stampeding Roar", School.PHYSICAL);
    static public final SpellData BUFF_REJUV = new SpellData(SpellId.Druid.REJUV, "Rejuvenation", School.NATURE);
    
    static public final SpellData DEBUFF_MF = new SpellData(SpellId.Druid.MF, "Moonfire", School.ARCANE);
    static public final SpellData DEBUFF_MF_CAT = new SpellData(SpellId.Druid.Feral.MF, "Moonfire", School.ARCANE);
    static public final SpellData DEBUFF_FF = new SpellData(SpellId.Druid.FF, "Faerie Fire", School.NATURE);    
    static public final SpellData DEBUFF_RIP = new SpellData(SpellId.Druid.Feral.RIP, "Rip", School.PHYSICAL);
    static public final SpellData DEBUFF_RAKE = new SpellData(SpellId.Druid.Feral.RAKE_DOT, "Rake", School.PHYSICAL);
    static public final SpellData DEBUFF_THRASH_CAT = new SpellData(SpellId.Druid.THRASH_CAT, "Thrash", School.PHYSICAL);
    static public final SpellData DEBUFF_THRASH_BEAR = new SpellData(SpellId.Druid.THRASH_BEAR, "Thrash", School.PHYSICAL);
    
    static public final SpellData BUFF_BONUS_T15_4PC = new SpellData(SpellId.Druid.Feral.Bonus.T15_4PC_BUFF, "Tiger's Fury", School.NATURE);
    static public final SpellData BUFF_BONUS_T16_2PC = new SpellData(SpellId.Druid.Feral.Bonus.T16_2PC_BUFF, "Feral Fury", School.PHYSICAL);
    static public final SpellData BUFF_BONUS_T16_4PC = new SpellData(SpellId.Druid.Feral.Bonus.T16_4PC_BUFF, "Feral Rage", School.PHYSICAL);
    static public final SpellData BUFF_LOTP = new SpellData(SpellId.Druid.LOTP, "Leader of the Pack", School.PHYSICAL);
    static public final SpellData BUFF_OOC = new SpellData(SpellId.Druid.Feral.OOC_BUFF, "Clearcasting", School.PHYSICAL);    
    static public final SpellData BUFF_SR = new SpellData(SpellId.Druid.Feral.SR, "Savage Roar", School.PHYSICAL);
    static public final SpellData BUFF_TF = new SpellData(SpellId.Druid.Feral.TF, "Tiger's Fury", School.PHYSICAL);
    static public final SpellData BUFF_BT = new SpellData(SpellId.Druid.Feral.BT, "Bloodtalons", School.PHYSICAL);
    static public final SpellData BUFF_PS = new SpellData(SpellId.Druid.Feral.PS_BUFF, "Predatory Swiftness", School.PHYSICAL);
    static public final SpellData BUFF_PROWL = new SpellData(SpellId.Druid.PROWL, "Prowl", School.PHYSICAL);
    static public final SpellData BUFF_HOTW = new SpellData(SpellId.Druid.Feral.HOTW, "Heart of the Wild", School.NATURE);
    static public final SpellData BUFF_SI = new SpellData(SpellId.Druid.SI, "Survival Instincts", School.PHYSICAL);
    static public final SpellData BUFF_BERSERK_CAT = new SpellData(SpellId.Druid.BERSERK_CAT, "Berserk", School.PHYSICAL);
    static public final SpellData BUFF_BERSERK_BEAR = new SpellData(SpellId.Druid.BERSERK_BEAR, "Berserk", School.PHYSICAL);
    static public final SpellData BUFF_DASH = new SpellData(SpellId.Druid.DASH, "Dash", School.PHYSICAL);
    static public final SpellData BUFF_CAT_FORM = new SpellData(SpellId.Druid.CAT_FORM, "Cat Form", School.PHYSICAL);
    static public final SpellData BUFF_BEAR_FORM = new SpellData(SpellId.Druid.BEAR_FORM, "Bear Form", School.PHYSICAL);
    static public final SpellData BUFF_KOTJ = new SpellData(SpellId.Druid.Feral.KOTJ, "Incarnation: King of the Jungle", School.PHYSICAL);

    static public final SpellData SPELL_RAKE = new SpellData(SpellId.Druid.Feral.RAKE_SPELL, "Rake", School.PHYSICAL);
    static public final SpellData SPELL_MAIM = new SpellData(SpellId.Druid.Feral.MAIM, "Maim", School.PHYSICAL);

    public FeralGameData fgd;
    
    // buffs/debuffs
    public final BuffModel buffModel_stampRoar = new BuffModel(BUFF_STAMPEDING_ROAR);
    public final BuffModel buffModel_rejuv = new BuffModel(BUFF_REJUV);
    public final BuffModel buffModel_mf = new BuffModel(DEBUFF_MF);
    public final BuffModel buffModel_mf_cat = new BuffModel(DEBUFF_MF_CAT);   
    public final BuffModel buffModel_ff = new BuffModel(DEBUFF_FF);
    
    // bonuses
    public final BuffModel buffModel_bonus_pvp_wod_4pc = new BuffModel(DEBUFF_PVP_WOD_4PC);
    public final BuffModel buffModel_bonus_t17_4pc = new BuffModel(DEBUFF_PVP_WOD_4PC);
    
    // bleeds
    public final BuffModel buffModel_rip = new BuffModel(DEBUFF_RIP);
    public final BuffModel buffModel_rake = new BuffModel(DEBUFF_RAKE);
    public final BuffModel buffModel_thrash_cat = new BuffModel(DEBUFF_THRASH_CAT);
    public final BuffModel buffModel_thrash_bear = new BuffModel(DEBUFF_THRASH_CAT);
    
    public Feral() {
        super();
    }

    @Override
    public FeralView _createView(Unit unit) {
        return new FeralView(this, unit);
    }
        
    public final ModMap movementSpeed_cat_sum = new ModMap.Sum();
   // public final ProductMap movementSpeed_bearForm_mods = new ProductMap();
    
    @Override
    public double getMovementSpeed() {
        switch (currentForm) {
            case CAT:   return super.getMovementSpeed() * movementSpeed_cat_sum.get();
            //case BEAR:  return super.getMovementSpeed() * movementSpeed_bearForm_mods.product();
            default:    return super.getMovementSpeed();                
        }        
    }    
    
    // ---
    
    @Override
    public final double getParryChance() {
        return 0;
    }
    
    @Override
    public final double getBlockChance() {
        return 0;
    }
    
    @Override
    public int getAP_extra() {
        return getStat(UnitStat.AGI, 0); 
    }
    
    @Override
    public int getSP_extra(int schoolIndex) {
        return super.getSP_extra(schoolIndex) + (schoolIndex == School.Idx.NATURE ? getStat(UnitStat.AGI, 0) : 0);
    }
    
    // ---
    
    /*
    Weapon Damage values on all weapons have been reduced by 50%.
    Attack Power now increases Weapon Damage at a rate of 1 DPS per 3.5 Attack Power (up from 1 DPS per 14 Attack Power).
    Attack Power, Spell Power, or Weapon Damage now affect the entire healing or damage throughput of player spells.
    */
    public double getNormalizedWeaponDamage() {
        return 1 + getAP() / 3.5D; // + weaponDPS, +weaponDmg
    }
    
    public double getRazorClawsMod() {
        return 1 + getMastery() / fgd.CAT_MASTERY_PER_DAMAGE_MOD;
    }
    
    // ---
        
    // maybe intercept this using didDamage() using spell id
    public void trigger_bonus_t17_2pc() {
        // Cat thrash, cat rake, cat rip, and t17 4p bonus all proc the energy. 
        // Bear thrash and the new bleed enchant, do NOT proc the energy.
        if (cfg.bonus_t17_2pc) {
            power_energy.add(fgd.BONUS_T17_2PC_ENERGY_BONUS);
        }  
    }
    
    public void trigger_glyph_sr() {
        if (cfg.glyph_savageRoar && isProwling()) {
            spell_sr.activateWithCombos(5);                
        }
    }
    
    
    // ---
    
    public void breakRootsAndSnares() {
        // duh
    }
    
    public void addGeneratedCombo(boolean crit) {
        power_combos.add(crit && !cfg.disable_primalFury ? 2 : 1);  
    }
    
    public void refundEnergyCost(int cost) {
        if (!cfg.disable_refunds) {
            power_energy.add(GameHelp.round(fgd.PP_REFUND_MOD * cost));
        }
    }
    
    
    // --- 
    
    public final Buff buff_bonus_t15_4pc = new Buff(new BuffModel(BUFF_BONUS_T15_4PC), selfView);
    public final Buff buff_bonus_t16_2pc = new Buff(new BuffModel(BUFF_BONUS_T16_2PC), selfView);    
    public final Buff buff_bonus_t16_4pc = new Buff(new BuffModel(BUFF_BONUS_T16_4PC), selfView);
    
    public final Buff buff_lotp = new Buff<BuffModel,Feral,FeralView>(new BuffModel(BUFF_LOTP), selfView) {
        // do later
        // idea:
        // auras have infinite range
        // auras are stored in a party/raid object
    };
    
    public final Buff buff_ooc = new Buff<BuffModel,Feral,FeralView>(new BuffModel(BUFF_OOC), selfView) {
        @Override
        public void gotActivated(boolean refreshed) {
            if (v.o.cfg.bonus_t16_2pc) {
                v.o.buff_bonus_t16_2pc.activate();
            }
        }    
    };
    
    
    public final Buff buff_sr = new Buff(new BuffModel(BUFF_SR), selfView);        
    public final Buff buff_tf = new Buff(new BuffModel(BUFF_TF), selfView);    
    public final Buff buff_bt = new Buff(new BuffModel(BUFF_BT), selfView);    
    public final Buff buff_ps = new Buff(new BuffModel(BUFF_PS), selfView);
    public final Buff buff_prowl = new Buff(new BuffModel(BUFF_PROWL), selfView);
    public final ActivatorBuff buff_hotw = new ActivatorBuff<BuffModel,Feral,FeralView>(new BuffModel(BUFF_HOTW), selfView) {
        @Override
        public void stateChanged(boolean state) {
            int id = m.id();
            v.o.spellPower_product.setOrClear(state, id, fgd.HOTW_SPELL_POWER_MOD);
            v.o.spell_mf.powerCostModMap.setOrClear(state, id, 0);
            v.o.spell_wrath.powerCostModMap.setOrClear(state, id, 0);
            v.o.spell_rejuv.powerCostModMap.setOrClear(state,id, 0);
            v.o.spell_ht.powerCostModMap.setOrClear(state, id, 0);  
        }
    };
    
    public final ModBuff buff_si = new ModBuff(new BuffModel(BUFF_SI), selfView, incomingDamageMod_all_product);    
    public final BerserkCatBuff buff_berserk_cat = new BerserkCatBuff(this);
    public final Buff buff_berserk_bear = new ModBuff(new BuffModel(BUFF_BERSERK_BEAR), selfView, power_energy.costMods);
        
    // ---
    
    // this manages the underlying form
    // fix me
    
    public FeralFormT currentForm;    
    public void cancelForm() { setForm(FeralFormT.HUMAN); }    
    public void setForm(FeralFormT form) {
        if (currentForm != form) {   
            // we need to cancel the current form
            switch (currentForm) {
                case CAT:
                    buff_catForm.cancel();
                    break;
                case BEAR:
                    buff_bearForm.cancel();
                    break;
            }
            currentForm = form;
        }
    }
    
    // ---
    
    public boolean isProwling() {
        return buff_prowl.isActive();
    }
    
    public boolean isCatOrBearForm() {
        return isCatForm() || isBearForm();
    }
    
    public boolean isCatForm() {
        return true;
    }
    
    public boolean isManBearPigForm() {
        return isCatForm() && cfg.talent_cos;
    }
    
    public boolean isBearForm() {
        return false;
    }
     
    // --- 
    
    public double getBloodtalonsMod(boolean bt) {
        return bt ? fgd.BT_DAMAGE_MOD : 1;
    }
       
    public double getSnapshotableDamageMod() {
        return (buff_tf.isActive() ? fgd.TF_DAMAGE_MOD : 1);                
    }
    
    
    /*
    @Override
    public double getDamageDoneMod(int schoolMask) {
        if ((schoolMask & SchoolT.SCHOOLS_PHYSICAL) != 0) {
            return getTotalPhysicalDamageMod(false);
        } else {
            return super.getDamageDoneMod(schoolMask);
        }        
    }
    */
    
    public final Buff<BuffModel,Feral,FeralView> buff_dash = new Buff<BuffModel,Feral,FeralView>(new BuffModel(BUFF_DASH), selfView) {
        @Override
        public void gotActivated(boolean refreshed) {
            v.o.buff_catForm.activate();
            v.o.movementSpeed_cat_sum.set(m.id(), v.o.fgd.DASH_SPEED_BONUS);
        }
        @Override
        public void gotDeactivated() {
            v.o.movementSpeed_cat_sum.remove(m.id());
        }
    };
    
    public final Buff<BuffModel,Feral,FeralView> buff_catForm = new Buff<BuffModel,Feral,FeralView>(new BuffModel(BUFF_CAT_FORM), selfView) {
        @Override
        public void gotActivated(boolean refreshed) {            
            v.o.breakRootsAndSnares();
            v.o.movementSpeed_cat_sum.set(m.id(), v.o.fgd.CAT_FORM_SPEED_BONUS);
        }
        @Override
        public void gotDeactivated() {
            v.o.breakRootsAndSnares();
            v.o.movementSpeed_cat_sum.remove(m.id());
        }
    };
    
    public final Buff<BuffModel,Feral,FeralView> buff_bearForm = new Buff<BuffModel,Feral,FeralView>(new BuffModel(BUFF_BEAR_FORM), selfView) {
        @Override
        public void gotActivated(boolean refreshed) {            
            v.o.breakRootsAndSnares();
            v.o.power_rage.set(v.o.fgd.BEAR_FORM_RAGE);
        }
    };
    
    public final Buff<BuffModel,Feral,FeralView> buff_form_kotj = new Buff<BuffModel,Feral,FeralView>(new BuffModel(BUFF_KOTJ), selfView) {
        @Override
        public void gotActivated(boolean refreshed) {            
            v.o.buff_catForm.activate();
        }
        @Override
        public void gotDeactivated() {
            if (v.o.isCatForm()) {
                v.o.breakRootsAndSnares();
            }
        }
    };
    
    // generators
    public final Shred spell_shred = new Shred(this);
    public final Rake spell_rake = new Rake(this);
    public final MoonfireCat spell_mf_cat = new MoonfireCat(this);
    
    // aoe
    public final Swipe spell_swipe = new Swipe(this);
    public final ThrashCat spell_thrash_cat = new ThrashCat(this);
    public final ThrashBear spell_thrash_bear = new ThrashBear(this);
    
    // finishers
    public final SavageRoar spell_sr = new SavageRoar(this);
    public final FerociousBite spell_fb = new FerociousBite(this);
    public final Rip spell_rip = new Rip(this);
    public final Maim spell_maim = new Maim(this);
    
    // spells
    public final HealingTouch spell_ht = new HealingTouch(this);
    public final Rejuvenation spell_rejuv = new Rejuvenation(this);
    public final Wrath spell_wrath = new Wrath(this);
    public final Moonfire spell_mf = new Moonfire(this);
    
    // cat stuff    
    public final TigersFury spell_tf = new TigersFury(this);
    public final FaerieFire spell_ff = new FaerieFire(this);
    public final ActivatorSpell spell_berserk_cat = new ActivatorSpell(buff_berserk_cat);
    public final ActivatorSpell spell_berserk_bear = new ActivatorSpell(buff_berserk_bear);
    public final StampedingRoar spell_stampRoar = new StampedingRoar(this);
    public final ActivatorSpell spell_si = new ActivatorSpell(buff_si);
    public final ActivatorSpell spell_dash = new ActivatorSpell(buff_dash);
    
    // talents?
    public final Incarnation spell_kotj = new Incarnation(this);
    public final ActivatorSpell spell_hotw = new ActivatorSpell(buff_hotw);
    
    // forms
    public final ActivatorSpell spell_catForm = new ActivatorSpell(buff_catForm);
    public final ActivatorSpell spell_bearForm = new ActivatorSpell(buff_bearForm);
    
    // procs
    
    public final Trigger<Feral,PPM> trigger_ooc = new Trigger<Feral,PPM>(this, ChanceFactory.PPM, false, EnumHelp.bits(Origin.MELEE)) {
        @Override
        public void triggered(Origin origin, boolean wasCrit) {
            o.buff_ooc.activate();
        }        
    };
    
    public final Trigger<Feral,Probability> trigger_lotp = new Trigger<Feral,Probability>(this, ChanceFactory.PROB, true, EnumHelp.bits(Origin.MELEE)) {
        @Override
        public void triggered(Origin origin, boolean wasCrit) {            
            o.applyHeal_percentHealth(o.fgd.LOTP_PERC_HEALTH, o, "LotP", Origin.HEAL, School.PHYSICAL);            
        }        
    };

    @Override
    public void didDamage(HitEvent hit) {
        super.didDamage(hit);
        if (cfg.bonus_t17_4pc && buff_berserk_cat.isActive()) { 
            switch (hit.spell.id) {
                //case SpellId.Druid.Feral.RAKE_DOT:
                //case SpellId.Druid.Feral.RIP:
                //case SpellId.Druid.THRASH_CAT:
                case SpellId.Druid.FB:
                case SpellId.Druid.SHRED:
                case SpellId.Druid.Feral.SWIPE:
                    getView(hit.target).dot_bonus_t17_4pc.accumulate(hit.computed);
            }
        }                       
    }
    
    // -- 
    
    @Override
    public void prepareForCombat() {
        super.prepareForCombat();
        
        perc_sum[UnitPerc.CRIT].set(SpellId.PrimaryStat.Agility.CRITICAL_STRIKES, fgd.CRITICAL_STRIKES_CRIT_BONUS);
        perc_rating_product[UnitPerc.CRIT].set(SpellId.Druid.Feral.SHARPENED_CLAWS, fgd.SHARPENED_CLAWS_CRIT_MOD);
        
        stat_product[UnitStat.AGI].set(SpellId.Druid.Feral.LEATHER_SPECIALIZATION, fgd.LEATHER_SPECIALIZATION_AGI_MOD);
        
      

    }
    
    public double BITW_PERC;
    public boolean hasArmorSpecialization;
    
    public void setup() {        
        triggerList.clear();
     
        BITW_PERC = cfg.bonus_t13_2pc ? fgd.BONUS_T13_BITW_PERC : fgd.BITW_PERC; 
        
        buff_si.m.param = cfg.glyph_si ? fgd.SI_GLYPH_DAMAGE_MOD : fgd.SI_DAMAGE_MOD;        
        spell_si.defaultRechargeTime = fgd.SI_RECHARGE - (cfg.glyph_si ? fgd.SI_GLYPH_RECHARGE_REDUCTION : 0);

        spell_dash.defaultCooldownTime = fgd.DASH_DURATION - (cfg.glyph_dash ? fgd.DASH_GLYPH_DURATION_REDUCTION : 0);
        
        buffModel_rip.default_duration = fgd.RIP_DURATION + (cfg.bonus_t14_4pc ? fgd.BONUS_T14_4PC_DURATION_INCREASE : 0);
        
        spell_stampRoar.range_max = cfg.glyph_stampRoar ? fgd.STAMP_ROAR_GLYPH_RADIUS : fgd.STAMP_ROAR_RADIUS;
        
        if (cfg.glyph_savagery) {
            buff_sr.m.param = fgd.SR_GLYPH_DAMAGE_MOD;
            buff_sr.m.default_duration = 0;
            buff_sr.m.passive = true;
        } else {
            buff_sr.m.param = fgd.SR_DAMAGE_MOD;
            buff_sr.m.passive = false;
        }
        
        buff_ooc.m.enabled = !cfg.disable_ooc;
        if (!cfg.disable_ooc) {
            triggerList.add(trigger_ooc);
        }
        
        buff_lotp.m.enabled = !cfg.disable_lotp;        
        if (!cfg.disable_lotp) {
            triggerList.add(trigger_lotp);
        }
        
        if (cfg.bonus_t14_2pc) {
            spell_shred.directDoneMod.set(SpellId.Druid.Feral.Bonus.T14_2PC_BUFF, fgd.BONUS_T14_2PC_SHRED_DAMAGE_MOD);
        }
        
        if (cfg.bonus_t14_4pc) {
            
        }
        
        
    }
    
    @Override
    public void init() {
        super.init();
        
        buff_sr.m.pandemic = true; // fixed on beta
        
        buff_dash.m.default_duration = fgd.DASH_DURATION;
        
        buffModel_bonus_pvp_wod_4pc.default_duration = fgd.BONUS_WOD_PVP_4PC_DURATION;
        buffModel_bonus_pvp_wod_4pc.param = fgd.BONUS_WOD_PVP_4PC_BLEED_DAMAGE_MOD;
        
        buffModel_bonus_t17_4pc.base_frequency = fgd.BONUS_T17_4PC_FREQUENCY;
        buffModel_bonus_t17_4pc.default_duration = fgd.BONUS_T17_4PC_DURATION;
        buffModel_bonus_t17_4pc.param = fgd.BONUS_T17_4PC_EXTRA_BLEED_COEFF;
        
        buffModel_ff.unique = true;
        buffModel_ff.default_duration = fgd.FF_DURATION;
        spell_ff.defaultCooldownTime = fgd.FF_COOLDOWN;
        
        buffModel_rejuv.base_frequency = fgd.REJUV_FREQUENCY;
        buffModel_rejuv.default_duration = fgd.REJUV_DURATION;
        buffModel_rejuv.pandemic = true;
        buffModel_rejuv.hasted = true;
        
        buffModel_mf.base_frequency = fgd.MF_FREQUENCY;
        buffModel_mf.default_duration = fgd.MF_DURATION;
        buffModel_mf.pandemic = true;
        buffModel_mf.hasted = true;
        
        buffModel_mf_cat.base_frequency = fgd.MF_CAT_FREQUENCY;
        buffModel_mf_cat.default_duration = fgd.MF_CAT_DURATION;
        buffModel_mf_cat.pandemic = true;
        buffModel_mf_cat.hasted = true;
        
        buffModel_rip.base_frequency = fgd.RIP_FREQUENCY;
        buffModel_rip.default_duration = fgd.RIP_DURATION;        
        buffModel_rip.param = fgd.RIP_TICK_DAMAGE_PER_AP;
        buffModel_rip.pandemic = true;
        
        buffModel_rake.base_frequency = fgd.RAKE_FREQUENCY;
        buffModel_rake.default_duration = fgd.RAKE_DURATION;
        buffModel_rake.param = fgd.RAKE_TICK_DAMAGE_PER_AP;
        buffModel_rake.pandemic = true;
        
        buffModel_thrash_cat.base_frequency = fgd.THRASH_CAT_FREQUENCY;
        buffModel_thrash_cat.default_duration = fgd.THRASH_CAT_DURATION;
        buffModel_thrash_cat.pandemic = true;
        buffModel_thrash_cat.param = fgd.THRASH_CAT_TICK_DAMAGE_PER_AP;     
        
        buffModel_thrash_bear.base_frequency = fgd.THRASH_BEAR_FREQUENCY;
        buffModel_thrash_bear.default_duration = fgd.THRASH_BEAR_DURATION;
        buffModel_thrash_bear.pandemic = true;
        buffModel_thrash_bear.param = fgd.THRASH_BEAR_TICK_DAMAGE_PER_AP;     
        
        buffModel_stampRoar.default_duration = fgd.STAMP_ROAR_DURATION;
        buffModel_stampRoar.unique = true;
        buffModel_stampRoar.param = fgd.STAMP_ROAR_SPEED_BONUS;        
        
        buff_bt.m.setCharges(fgd.BT_CHARGES);
        buff_bt.m.default_duration = fgd.BT_DURATION;
        
        buff_ps.m.default_duration = fgd.PS_DURATION;
 
        buff_tf.m.default_duration = fgd.TF_DURATION;
        
        buff_ooc.m.default_duration = fgd.OOC_DURATION;
        trigger_ooc.chance.ppm = fgd.OOC_PPM;
        
        spell_tf.defaultCooldownTime = fgd.TF_COOLDOWN;
                
        buff_form_kotj.m.default_duration = fgd.KOTJ_DURATION;   
        
        spell_catForm.setManaCost(fgd.CAT_FORM_MANA_COST);
               
        buff_si.m.default_duration = fgd.SI_DURATION;
        spell_si.defaultCooldownTime = fgd.SI_DURATION;
        
        spell_shred.setEnergyCost(fgd.SHRED_ENERGY_COST);
        spell_rake.setEnergyCost(fgd.RAKE_ENERGY_COST);
        spell_mf_cat.setEnergyCost(fgd.MF_CAT_ENERGY_COST);
        
        spell_sr.setEnergyCost(fgd.SR_ENERGY_COST);
        spell_fb.setEnergyCost(fgd.FB_ENERGY_COST); 
        spell_rip.setEnergyCost(fgd.RIP_ENERGY_COST);
        
        trigger_lotp.chance.prob = 1;
        trigger_lotp.chance.icd = fgd.LOTP_COOLDOWN;
        
        
        spell_stampRoar.defaultCooldownTime = fgd.STAMP_ROAR_COOLDOWN;
        
        spell_swipe.setEnergyCost(fgd.SWIPE_ENERGY_COST);
        spell_thrash_cat.setEnergyCost(fgd.THRASH_CAT_ENERGY_COST);
        
        
        buff_bonus_t15_4pc.m.default_duration = fgd.BONUS_T16_4PC_DURATION;
        buff_bonus_t15_4pc.m.setCharges(fgd.BONUS_T15_4PC_CHARGES);

        buff_bonus_t16_4pc.m.default_duration = fgd.BONUS_T16_4PC_DURATION;
        
        buff_hotw.m.default_duration = fgd.HOTW_DURATION;
            

     
        
        
        
        
    }
    
}
