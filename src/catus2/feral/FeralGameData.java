package catus2.feral;

public class FeralGameData {
    
    public double PP_REFUND_MOD = 0.8; // primal precision, refund on miss
    
    public double SHARPENED_CLAWS_CRIT_MOD = 1.05;
    public double CRITICAL_STRIKES_CRIT_BONUS = 0.1;
    public double LEATHER_SPECIALIZATION_AGI_MOD = 1.05;
    
    public double PERK_IMPROVED_RAKE_DAMAGE_MOD = 2;
    public int PERK_ENHANCED_BERSERK_ENERGY_CEILING = 50;
    
    public double BERSERK_CAT_POWER_MOD = 0.5;
    
    public double BITW_PERC = 0.25;
    
    public int OOC_DURATION = 15000;
    public double OOC_PPM = 3.5;

    public int FB_ENERGY_COST = 25;
    public int FB_EXTRA_COST = 25;
    public double FB_DAMAGE_PER_AP = 3;
    public double FB_EXTRA_DAMAGE_MOD_PER_ENERGY = 0.04;
    public double FB_GLYPH_PERC_HEALTH_PER_ENERGY = 0.0015;
    
    int MANA_MAX = 32000;
    int ENERGY_MAX = 100;
    
    public double LOTP_PERC_HEALTH = 0.03;
    public int LOTP_COOLDOWN = 6000;
       
    public int SHRED_ENERGY_COST = 40;
    public double SHRED_DAMAGE_PER_DPS = 2.94;
    public double SHRED_STEALTH_DAMAGE_MOD = 1.35;    
    
    public int RAKE_ENERGY_COST = 35;
    public double RAKE_TICK_DAMAGE_PER_AP = 0.475;
    public int RAKE_DURATION = 15;
    public int RAKE_FREQUENCY = 3000;
    
    public int MF_CAT_ENERGY_COST = 30;
    public int MF_CAT_FREQUENCY = 2000;    
    public int MF_CAT_DURATION = 14000;
    
    public double MF_MANA_COST = 0.084;
    public int MF_DURATION = 20;
    public int MF_FREQUENCY = 3000;
    
    public int RIP_ENERGY_COST = 30;
    public double RIP_TICK_DAMAGE_PER_AP = 0.1;
    public int RIP_DURATION = 24;
    public int RIP_FREQUENCY = 2000;
    
    public int WRATH_CAST = 2000;
    public double WRATH_COST = 0.088;
    public double WRATH_DAMAGE_PER_SP = 1.3;
    
    public int MAIM_COST = 35;
    public int MAIM_DAMAGE_PER_DPS = 2;
    public int MAIM_STUN_PER_CP = 1000;
    
    public double THRASH_CAT_TICK_DAMAGE_PER_AP = 0.113886;
    public double THRASH_CAT_INITIAL_TICK_MOD = 1.4;    
    public int THRASH_CAT_ENERGY_COST = 50;
    public int THRASH_CAT_FREQUENCY = 3000;
    public int THRASH_CAT_DURATION = 15000;
    
    public double THRASH_BEAR_TICK_DAMAGE_PER_AP = 0.09375;
    public double THRASH_BEAR_INITIAL_DAMAGE_PER_AP = 3 * THRASH_BEAR_TICK_DAMAGE_PER_AP;    
    public int THRASH_BEAR_FREQUENCY = 2000;
    public int THRASH_BEAR_DURATION = 16000;
    
    public double CAT_MASTERY_PER_DAMAGE_MOD = 3514.5;
    
    public double BT_DAMAGE_MOD = 1.3;
    public int BT_CHARGES = 2;
    public int BT_DURATION = 30 * 1000;
    
    public double TF_DAMAGE_MOD = 1.15;
    public int TF_DURATION = 8000;
    public int TF_COOLDOWN = 30000;
    public int TF_ENERGY_BONUS = 60;
        
    public int SI_CHARGES = 2;
    public int SI_DURATION = 12000;
    public int SI_RECHARGE = 2 * 60000;
    public double SI_DAMAGE_MOD = 0.5;    
    public int SI_GLYPH_RECHARGE_REDUCTION = 40000;
    public double SI_GLYPH_DAMAGE_MOD = 0.4;
       
    public int PS_DURATION = 12000;
    public double PS_CHANCE_PER_CP = 0.2;    
    public double PS_HT_HEALING_MOD = 1.5;
    
    public double HT_MANA_COST = 0.103;
    public int HT_CAST = 2500;
    public double HT_HEALING_PER_SP = 3.6;
    
    public double REJUV_MANA_COST = 0.094;
    public int REJUV_FREQUENCY = 3000;
    public int REJUV_DURATION = 12000;
    
    public double DOC_HT_REJUV_HEAL_MOD = 1.2;

    public double CAT_FORM_MANA_COST = 0.037;
    public double CAT_FORM_SPEED_BONUS = 0.3;
    
    public double BEAR_FORM_MANA_COST = 0.037;
    public int BEAR_FORM_RAGE = 10;
            
    public double TRAVEL_FORM_MANA_COST = 0.056;
    
    public double DASH_SPEED_BONUS = 0.7;
    public int DASH_DURATION = 3 * 60000;
    public int DASH_GLYPH_DURATION_REDUCTION = 60000;
    
    public int FF_DURATION = 6000;
    public int FF_COOLDOWN = 30000;
    public double FF_BEAR_DAMAGE_PER_AP = 0.325;
    
    public int SOTF_ENERGY_PER_COMBO = 4;
    
    public double SHRED_SWIPE_BLEED_BONUS = 1.2;
    
    public int SWIPE_ENERGY_COST = 45;
    public double SWIPE_DAMAGE_MOD = 1.35;
   
    public int KOTJ_DURATION = 30000;
    public int KOTJ_COOLDOWN = 5 * 60000;
        
    public int SR_DURATION_BASE = 12000;
    public int SR_DURATION_PER_COMBO = 6000;
    public int SR_ENERGY_COST = 25;
    public double SR_DAMAGE_MOD = 1.4;
    public double SR_GLYPH_DAMAGE_MOD = 1.35;
    
    public int HOTW_DURATION = 45000;
    public int HOTW_COOLDOWN = 6 * 60000;
    public double HOTW_SPELL_POWER_MOD = 4.2; // +320%
    
    public int STAMP_ROAR_DURATION = 80000;
    public int STAMP_ROAR_COOLDOWN = 2 * 60000;
    public int STAMP_ROAR_RADIUS = 10;
    public double STAMP_ROAR_SPEED_BONUS = 1.6;
    public int STAMP_ROAR_GLYPH_RADIUS = 40;

    public double BONUS_T13_BITW_PERC = 0.60;
    
    public double BONUS_T14_2PC_SHRED_DAMAGE_MOD = 1.05;
    public int BONUS_T14_4PC_DURATION_INCREASE = 4000;
    
    public double BONUS_T15_2PC_CHANCE_PER_CP = 0.15;
    public int BONUS_T15_2PC_CP_GAIN = 1;
    
    public double BONUS_T15_4PC_EXTRA_CRIT = 0.4;
    public int BONUS_T15_4PC_DURATION = 30000;
    public int BONUS_T15_4PC_CHARGES = 3;
    
    public double BONUS_T16_2PC_SHRED_SWIPE_DAMAGE_MOD = 1.5;
    public int BONUS_T16_2PC_DURATION = 6000;
    
    public int BONUS_T16_4PC_CP_GAIN = 3;
    public int BONUS_T16_4PC_DURATION = 12000;
    
    public int BONUS_T17_2PC_ENERGY_BONUS = 3;

    public double BONUS_T17_4PC_EXTRA_BLEED_COEFF = 0.15;
    public int BONUS_T17_4PC_FREQUENCY = 2000; // guess?
    public int BONUS_T17_4PC_DURATION = 6000;
    
    public int BONUS_WOD_PVP_4PC_DURATION = 6000;
    public double BONUS_WOD_PVP_4PC_BLEED_DAMAGE_MOD = 1.1;
    
    
}
