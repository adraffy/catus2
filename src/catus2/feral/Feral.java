package catus2.feral;

import catus2.ActivatorSpell;
import catus2.EnumHelp;
import catus2.GameHelp;
import catus2.HitEvent;
import catus2.ModMap;
import catus2.Origin;
import catus2.PlayerUnit;
import catus2.School;
import catus2.SpellId;
import catus2.SpellModel;
import catus2.Unit;
import catus2.UnitPerc;
import catus2.UnitStat;
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
import catus2.feral.finishers.Rip;
import catus2.feral.finishers.SavageRoar;
import catus2.feral.generators.MoonfireCat;
import catus2.feral.generators.Rake;
import catus2.feral.generators.Shred;
import catus2.feral.spells.FaerieFire;
import catus2.feral.spells.HealingTouch;
import catus2.feral.spells.Rejuvenation;
import catus2.feral.spells.StampedingRoar;
import catus2.feral.spells.TigersFury;
import catus2.feral.talents.Incarnation;
import catus2.procs.Trigger;

public class Feral extends PlayerUnit<Feral,FeralView,FeralConfig> {
    
    
    public FeralGameData fgd;
    
    // buffs/debuffs
    public final BuffModel buffModel_stampRoar = new BuffModel(SpellId.Druid.STAMPEDING_ROAR);
    public final BuffModel buffModel_rejuv = new BuffModel(SpellId.Druid.REJUV);
    public final BuffModel buffModel_mf = new BuffModel(SpellId.Druid.MF);
    public final BuffModel buffModel_mf_cat = new BuffModel(SpellId.Druid.Feral.MF);   
    public final BuffModel buffModel_ff = new BuffModel(SpellId.Druid.FF);
    
    // bonuses
    public final BuffModel buffModel_bonus_pvp_wod_4pc = new BuffModel(SpellId.Druid.Feral.Set.PVP_WOD_4PC_DEBUFF);
    public final BuffModel buffModel_bonus_t17_4pc = new BuffModel(SpellId.Druid.Feral.Set.T17_4PC_DEBUFF);
    
    // bleeds
    public final BuffModel buffModel_rip = new BuffModel(SpellId.Druid.Feral.RIP);
    public final BuffModel buffModel_rake = new BuffModel(SpellId.Druid.Feral.RAKE_DOT);
    public final BuffModel buffModel_thrash_cat = new BuffModel(SpellId.Druid.THRASH_CAT);
    public final BuffModel buffModel_thrash_bear = new BuffModel(SpellId.Druid.THRASH_BEAR);
    
    public Feral() {
        super();
    }

    @Override
    public FeralView _createView(Unit unit) {
        return new FeralView(this, unit);
    }
        
    public final ModMap movementSpeed_cat_sum = new ModMap(true);
   // public final ProductMap movementSpeed_bearForm_mods = new ProductMap();
    
    @Override
    public double getMovementSpeed() {
        switch (currentForm) {
            case CAT:   return super.getMovementSpeed() * movementSpeed_cat_sum.fold();
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
        
    public void bonus_t17_2pc_trigger() {
        /*
        Cat thrash, cat rake, cat rip, and t17 4p bonus all proc the energy. 
        Bear thrash and the new bleed enchant, do NOT proc the energy.
        3 may be too large of a number, still working through some tuning on these.
        */
        if (cfg.bonus_t17_2pc) {
            power_energy.add(fgd.BONUS_T17_2PC_ENERGY_BONUS);
        }  
    }
    
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
    
    public void applySavageRoarGlyph() {
        if (cfg.glyph_savageRoar && isProwling()) {
            spell_sr.activateWithCombos(5);                
        }
    }
    
    // --- 
    
    // Tiger's Fury
    // http://wow.wowhead.com/spell=138358
    // Increases critical strike chance by 40% for next 3 uses of Mangle, Shred, Ferocious Bite, Ravage, and Swipe.  Lasts 30 sec.
    public final Buff buff_bonus_t15_4pc = new Buff(new BuffModel(SpellId.Druid.Feral.Set.T15_4PC_BUFF), selfView);
    
    // Feral Fury
    // http://www.wowhead.com/spell=144865
    // Omen of Clarity increases damage of Shred, Mangle, Swipe, and Ravage by 50% for 6 sec.
    public final Buff buff_bonus_t16_2pc = new Buff(new BuffModel(SpellId.Druid.Feral.Set.T16_2PC_BUFF), selfView);
    
    // Feral Rage
    // http://www.wowhead.com/spell=146874
    // After using Tiger's Fury, your next finishing move will restore 3 combo points on your current target after being used.
    public final Buff buff_bonus_t16_4pc = new Buff(new BuffModel(SpellId.Druid.Feral.Set.T16_4PC_BUFF), selfView);
    
    public final Buff buff_lotp = new Buff<BuffModel,Feral,FeralView>(new BuffModel(SpellId.Druid.LOTP), selfView) {
        // do later
        // idea:
        // auras have infinite range
        // auras are stored in a party/raid object
    };
    
    public final Buff buff_ooc = new Buff<BuffModel,Feral,FeralView>(new BuffModel(SpellId.Druid.Feral.OOC), selfView) {
        @Override
        public void gotActivated(boolean refreshed) {
            if (v.o.cfg.bonus_t16_2pc) {
                v.o.buff_bonus_t16_2pc.activate();
            }
        }    
    };
    
    public final Buff buff_sr = new Buff(new BuffModel(SpellId.Druid.Feral.SR), selfView);        
    public final Buff buff_tf = new Buff(new BuffModel(SpellId.Druid.Feral.TF), selfView);    
    public final Buff buff_bt = new Buff(new BuffModel(SpellId.Druid.Feral.BT), selfView);    
    public final Buff buff_ps = new Buff(new BuffModel(SpellId.Druid.Feral.PS_BUFF), selfView);
    public final Buff buff_prowl = new Buff(new BuffModel(SpellId.Druid.PROWL), selfView);
    public final Buff buff_hotw = new Buff(new BuffModel(SpellId.Druid.Feral.HOTW), selfView) {
        @Override
        public void gotActivated(boolean refreshed) {
            v.o.spellPower_product.set(m.id, fgd.HOTW_SPELL_POWER_MOD);
        }
        @Override
        public void gotDeactivated() {
            v.o.spellPower_product.remove(m.id);
        }
    };
    
    public final ModBuff buff_si = new ModBuff(new BuffModel(SpellId.Druid.SI), selfView, damageRecv_all_product);    
    public final ModBuff buff_berserk_cat = new ModBuff(new BuffModel(SpellId.Druid.BERSERK_CAT), selfView, power_energy.costMods);
        
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
                    buff_form_cat.cancel();
                    break;
                case BEAR:
                    buff_form_bear.cancel();
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
    
    public final Buff<BuffModel,Feral,FeralView> buff_dash = new Buff<BuffModel,Feral,FeralView>(new BuffModel(SpellId.Druid.CAT_FORM), selfView) {
        @Override
        public void gotActivated(boolean refreshed) {
            v.o.buff_form_cat.activate();
            v.o.movementSpeed_cat_sum.set(m.id, v.o.fgd.DASH_SPEED_BONUS);
        }
        @Override
        public void gotDeactivated() {
            v.o.movementSpeed_cat_sum.remove(m.id);
        }
    };
    
    public final Buff<BuffModel,Feral,FeralView> buff_form_cat = new Buff<BuffModel,Feral,FeralView>(new BuffModel(SpellId.Druid.CAT_FORM), selfView) {
        @Override
        public void gotActivated(boolean refreshed) {            
            v.o.breakRootsAndSnares();
            v.o.movementSpeed_cat_sum.set(m.id, v.o.fgd.CAT_FORM_SPEED_BONUS);
        }
        @Override
        public void gotDeactivated() {
            v.o.breakRootsAndSnares();
            v.o.movementSpeed_cat_sum.remove(m.id);
        }
    };
    
    public final Buff<BuffModel,Feral,FeralView> buff_form_bear = new Buff<BuffModel,Feral,FeralView>(new BuffModel(SpellId.Druid.BEAR_FORM), selfView) {
        @Override
        public void gotActivated(boolean refreshed) {            
            v.o.breakRootsAndSnares();
            v.o.power_rage.set(v.o.fgd.BEAR_FORM_RAGE);
        }
    };
    
    public final Buff<BuffModel,Feral,FeralView> buff_form_kotj = new Buff<BuffModel,Feral,FeralView>(new BuffModel(SpellId.Druid.Feral.KOTJ), selfView) {
        @Override
        public void gotActivated(boolean refreshed) {            
            v.o.buff_form_cat.activate();
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
    
    // heals
    public final HealingTouch spell_ht = new HealingTouch(this);
    public final Rejuvenation spell_rejuv = new Rejuvenation(this);
    
    // cat stuff    
    public final TigersFury spell_tf = new TigersFury(this);
    public final FaerieFire spell_ff = new FaerieFire(this);
    public final ActivatorSpell spell_berserk_cat = new ActivatorSpell(buff_berserk_cat);
    public final StampedingRoar spell_stampRoar = new StampedingRoar(this);
    public final ActivatorSpell spell_si = new ActivatorSpell(buff_si);
    public final ActivatorSpell spell_dash = new ActivatorSpell(buff_dash);
    
    // talents?
    public final Incarnation spell_kotj = new Incarnation(this);
    
    // forms
    public final ActivatorSpell spell_catForm = new ActivatorSpell(buff_form_cat);
    public final ActivatorSpell spell_bearForm = new ActivatorSpell(buff_form_cat);
    
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
                case SpellId.Druid.Feral.RAKE_DOT:
                case SpellId.Druid.Feral.FB:
                case SpellId.Druid.Feral.RIP:
                case SpellId.Druid.THRASH_CAT:
                case SpellId.Druid.SHRED:
                    getView(hit.target).dot_bonus_t17_4pc.accumulate(hit.computed);
            }
        }                       
    }
    
    // -- 
    
    @Override
    public void prepareForCombat() {
        super.prepareForCombat();
        
        perc_sum[UnitPerc.CRIT].set(SpellId.DPS_Agility.CRITICAL_STRIKES, fgd.CRITICAL_STRIKES_CRIT_BONUS);
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
        
    }
    
    @Override
    public void init() {
        super.init();
        
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
