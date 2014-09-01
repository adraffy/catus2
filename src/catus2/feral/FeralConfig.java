/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package catus2.feral;

/**
 *
 * @author raffy
 */
public class FeralConfig {
    
    boolean ignore_location;
    
    boolean disable_racials;
    boolean nightTime; // 1% haste at night, 1% crit at day

    // bonuses
    public boolean bonus_t13_2pc;
    public boolean bonus_t13_4pc;
    public boolean bonus_t14_2pc;
    public boolean bonus_t14_4pc;
    public boolean bonus_t15_2pc;
    public boolean bonus_t15_4pc;
    public boolean bonus_t16_2pc;
    public boolean bonus_t16_4pc;
    public boolean bonus_t17_2pc;
    public boolean bonus_t17_4pc;
    public boolean bonus_pvp_wod_2pc; // Interrupting a spell with Skull Bash resets the cooldown of Tiger's Fury.
    public boolean bonus_pvp_wod_4pc; // Shred critical strikes causes the target to take 10% increased damage from your bleed effects for 6 sec.
    
    // glyphs
    public boolean glyph_catForm;  // +20% healing
    public boolean glyph_cyclone;  // +5 yard range
    public boolean glyph_dash;     // -60sec cooldown
    public boolean glyph_roots;    // -0.5sec cast
    public boolean glyph_ff;       // +10 yard range
    public boolean glyph_fb;       // heal for 1.5% max hp / 10 energy used
    public boolean glyph_maim;     // +100% damage
    public boolean glyph_masterShapeshifter;   // free shifts
    public boolean glyph_9th;      // -10% damage taken in cat
    public boolean glyph_rake;     // +8 yards while stealth
    public boolean glyph_rebirth;  // max hp on rez
    public boolean glyph_savageRoar; // rake/shred under prowl => 5cp
    public boolean glyph_savagery; // passive savage roar, -0.05%
    public boolean glyph_skullBash;// +2 sec interrupt duration, +5sec cooldown duration
    public boolean glyph_stampRoar;// radius = 30 yd, no form requirement
    boolean glyph_si;       // cooldown -40sec, duration = -50%    
    
    // 15
    boolean talent_felineSwiftness;
    boolean talent_displacerBeast;
    boolean talent_wildCharge;
    
    // 30
    boolean talent_yserasGift;
    boolean talent_renewal;
    boolean talent_cenarionWard;
    
    // 45
    boolean talent_faerieSwarm;
    boolean talent_massEntangle;
    boolean talent_typhoon;
        
    // 60
    public boolean talent_sotf;
    public boolean talent_kotj;
    public boolean talent_fon;
    
    // 75
    boolean talent_incapRoar;
    boolean talent_ursolVortex;
    boolean talent_mightyBash;
    
    // 90
    public boolean talent_hotw;
    public boolean talent_doc;
    public boolean talent_nv;
    
    // 100
    boolean talent_li;
    public boolean talent_bt;
    boolean talent_cos;
    
    public boolean disable_ps;
    public boolean disable_primalFury;
    public boolean disable_refunds;
    public boolean disable_fbExtra;
    public boolean disable_ooc;
    
    int initialEnergy;
    
    
}
