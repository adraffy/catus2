package catus2.feral;

import catus2.ActivatorSpell;
import catus2.EnumHelp;
import catus2.GameHelp;
import catus2.OriginT;
import catus2.ProductMap;
import catus2.SchoolT;
import catus2.SpellId;
import catus2.Unit;
import catus2.buffs.Buff;
import catus2.buffs.BuffModel;
import catus2.buffs.ProductBuff;
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
import catus2.feral.spells.HealingTouch;
import catus2.feral.spells.Rejuvenation;
import catus2.feral.spells.StampedingRoar;
import catus2.feral.spells.TigersFury;
import catus2.feral.talents.Incarnation;
import catus2.procs.Trigger;

public class Feral extends Unit<FeralView> {
    
    public FeralConfig cfg;
    public FeralGameData fgd;
    
    // buffs/debuffs
    public final BuffModel buffModel_rejuv = new BuffModel(SpellId.Druid.REJUV);
    public final BuffModel buffModel_mf = new BuffModel(SpellId.Druid.MF);
    public final BuffModel buffModel_mf_cat = new BuffModel(SpellId.Druid.Feral.MF);   
    public final BuffModel buffModel_pvp_wod_4pc = new BuffModel(SpellId.Druid.Feral.Set.PVP_WOD_4PC);
    
    // bleeds
    public final BuffModel buffModel_rip = new BuffModel(SpellId.Druid.Feral.RIP);
    public final BuffModel buffModel_rake = new BuffModel(SpellId.Druid.Feral.RAKE_DOT);
    public final BuffModel buffModel_thrash_cat = new BuffModel(SpellId.Druid.THRASH_CAT);
    public final BuffModel buffModel_thrash_bear = new BuffModel(SpellId.Druid.THRASH_BEAR);
    
    public Feral() {
        super();
    }

    @Override
    public FeralView createView(Unit unit) {
        return new FeralView(this, unit);
    }
        
    public final ProductMap movementSpeedMods_catForm = new ProductMap();
   // public final ProductMap movementSpeed_bearForm_mods = new ProductMap();
    
    @Override
    public double getMovementSpeed() {
        switch (currentForm) {
            case CAT:   return super.getMovementSpeed() * movementSpeedMods_catForm.product();
            //case BEAR:  return super.getMovementSpeed() * movementSpeed_bearForm_mods.product();
            default:    return super.getMovementSpeed();                
        }        
    }
    
    
    public double getNormalizedWeaponDamage() {
        return 1 + getAP() / 14D; // + weaponDPS, +weaponDmg
    }
    
    public double getRazorClawsMod() {
        return 1 + (fgd.CAT_MASTERY_RATING_OFFSET + getMasteryRating()) / fgd.CAT_MASTERY_RATING_PER_UNIT;
    }
    
    public void applyRakeTick(Unit target, double mod) {
        
    }
    
    public void applyRipTick(Unit target, double mod) {
        
        
        
    }
    
    
    
    public void refundEnergyCost(int cost) {
        if (!cfg.disable_refunds) {
            power_energy.add(GameHelp.round(fgd.PP_REFUND_MOD * cost));
        }
    }
    
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

    public final Buff buff_ooc = new Buff<BuffModel,Feral,FeralView>(new BuffModel(SpellId.Druid.Feral.OOC), selfView) {
        @Override
        public void gotActivated() {
            if (v.o.cfg.bonus_t16_2pc) {
                v.o.buff_bonus_t16_2pc.activate();
            }
        }
        @Override
        public void gotRefreshed() {
            gotActivated();
        }        
    };
    
    public final Buff buff_sr = new Buff(new BuffModel(SpellId.Druid.Feral.SR), selfView);        
    public final Buff buff_tf = new Buff(new BuffModel(SpellId.Druid.Feral.TF), selfView);    
    public final Buff buff_bt = new Buff(new BuffModel(SpellId.Druid.Feral.BT), selfView);    
    public final Buff buff_ps = new Buff(new BuffModel(SpellId.Druid.Feral.PS_BUFF), selfView);
    public final Buff buff_prowl = new FeralBuff(new BuffModel(SpellId.Druid.PROWL), selfView);
    public final Buff buff_hotw = new Buff(new BuffModel(SpellId.Druid.Feral.HOTW), selfView);
    
    public final ProductBuff buff_si = new ProductBuff(new BuffModel(SpellId.Druid.SI), selfView, damageTakenMod);    
    public final ProductBuff buff_berserk_cat = new ProductBuff(new BuffModel(SpellId.Druid.BERSERK_CAT), selfView, power_energy.costMods);
    
    
    // ---
        
    // ---
    
    
    // this manages the underlying form
    
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
     
    public void applySavageRoarGlyph() {
        if (cfg.glyph_savageRoar && isProwling()) {
            spell_sr.activateWithCombos(5);                
        }
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
    
    public double getSR_damageMod() {
        return cfg.glyph_savagery ? fgd.SR_GLYPH_DAMAGE_MOD : buff_sr.isActive() ? fgd.SR_DAMAGE_MOD : 1;
    }
     
    
    public final FeralBuff<BuffModel> buff_form_cat = new FeralBuff<BuffModel>(new BuffModel(SpellId.Druid.CAT_FORM), this) {
        @Override
        public void gotActivated() {            
            v.o.shapeshifted();
            v.o.movementSpeedMods_catForm.set(m.id, v.o.fgd.CAT_FORM_SPEED_BONUS);
        }
        @Override
        public void gotRefreshed() {
            v.o.shapeshifted();
        }
        @Override
        public void gotDeactivated() {
            v.o.shapeshifted();
            v.o.movementSpeedMods_catForm.clear(m.id);
        }
    };
    
    public final FeralBuff<BuffModel> buff_form_bear = new FeralBuff<BuffModel>(new BuffModel(SpellId.Druid.BEAR_FORM), this) {
        
    };
    
    public final FeralBuff<BuffModel> buff_form_kotj = new FeralBuff<BuffModel>(new BuffModel(SpellId.Druid.Feral.KOTJ), this) {
        @Override
        public void gotActivated() {            
            v.o.buff_form_cat.activate();
        }
        @Override
        public void gotDeactivated() {
            if (v.o.isCatForm()) {
                v.o.shapeshifted();
            }
        }
    };
    
    public void shapeshifted() {
        // clear roots
    }
    
    
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
    public final ActivatorSpell spell_berserk_cat = new ActivatorSpell(buff_berserk_cat);
    
    // spells
    public final StampedingRoar spell_stampRoar = new StampedingRoar(this);
    public final ActivatorSpell spell_si = new ActivatorSpell(buff_si);
    
    // talents?
    public final Incarnation spell_kotj = new Incarnation(this);
    
    // forms
    public final ActivatorSpell spell_form_cat = new ActivatorSpell(buff_form_cat);
    public final ActivatorSpell spell_form_bear = new ActivatorSpell(buff_form_cat);
    
    // procs
    
    public final Trigger<Feral,PPM> trigger_ooc = new Trigger<Feral,PPM>(this, ChanceFactory.PPM, false, EnumHelp.bits(OriginT.MELEE)) {
        @Override
        public void triggered(OriginT origin, boolean wasCrit) {
            o.buff_ooc.activate();
        }        
    };
    
    public final Trigger<Feral,Probability> trigger_lotp = new Trigger<Feral,Probability>(this, ChanceFactory.PROB, true, EnumHelp.bits(OriginT.MELEE)) {
        @Override
        public void triggered(OriginT origin, boolean wasCrit) {            
            o.applyHeal_percentHealth(o.fgd.LOTP_PERC_HEALTH, o, "LotP", OriginT.HEAL, SchoolT.SCHOOLS_PHYSICAL);            
        }        
    };
    
    // -- 
    
    public double BITW_PERC;
    
    public void setup() {
        
        triggerList.clear();
        
        BITW_PERC = fgd.BITW_PERC; // changed by t13
        
        buff_si.mod = cfg.glyph_si ? fgd.SI_GLYPH_DAMAGE_MOD : fgd.SI_DAMAGE_MOD;
        spell_si.defaultRechargeTime = cfg.glyph_si ? fgd.SI_GLYPH_RECHARGE : fgd.SI_RECHARGE;
        
        if (cfg.glyph_savagery) {
            buff_sr.m.default_duration = 0;
            spell_sr.enabled = false;
        } else {
            spell_sr.enabled = true;
        }
        
        
        if (cfg.disable_ooc) {
            triggerList.remove(trigger_ooc);
        } else {
            triggerList.add(trigger_ooc);
        }
        
        
        
        
    }
    
    @Override
    public void init() {
        super.init();
        
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
        buffModel_rip.scaling = fgd.RIP_TICK_DAMAGE_PER_AP;
        buffModel_rip.pandemic = true;
        
        buffModel_rake.base_frequency = fgd.RAKE_FREQUENCY;
        buffModel_rake.default_duration = fgd.RAKE_DURATION;
        buffModel_rake.scaling = fgd.RAKE_TICK_DAMAGE_PER_AP;
        buffModel_rake.pandemic = true;
        
        buffModel_thrash_cat.base_frequency = fgd.THRASH_CAT_FREQUENCY;
        buffModel_thrash_cat.default_duration = fgd.THRASH_CAT_DURATION;
        buffModel_thrash_cat.pandemic = true;
        buffModel_thrash_cat.scaling = fgd.THRASH_CAT_TICK_DAMAGE_PER_AP;     
        
        buffModel_thrash_bear.base_frequency = fgd.THRASH_BEAR_FREQUENCY;
        buffModel_thrash_bear.default_duration = fgd.THRASH_BEAR_DURATION;
        buffModel_thrash_bear.pandemic = true;
        buffModel_thrash_bear.scaling = fgd.THRASH_BEAR_TICK_DAMAGE_PER_AP;     
        
        
        buff_bt.m.setCharges(fgd.BT_CHARGES);
        buff_bt.m.default_duration = fgd.BT_DURATION;
        
        buff_ps.m.default_duration = fgd.PS_DURATION;
 
        buff_tf.m.default_duration = fgd.TF_DURATION;
        
        buff_ooc.m.default_duration = fgd.OOC_DURATION;
        trigger_ooc.chance.ppm = fgd.OOC_PPM;
        
        spell_tf.defaultCooldownTime = fgd.TF_COOLDOWN;
                
        buff_form_kotj.m.default_duration = fgd.KOTJ_DURATION;   
        
        spell_form_cat.setManaCost(fgd.CAT_FORM_MANA_COST);
               
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
