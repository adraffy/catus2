package catus2.feral;

import catus2.ClassConfig;

public class FeralConfig extends ClassConfig {
    
    // features
    public boolean disable_ps;
    public boolean disable_primalFury;
    public boolean disable_refunds;
    public boolean disable_fbExtra;
    public boolean disable_ooc;
    public boolean disable_lotp;
    
    //int initialEnergy;
    
    // glyphs
    public boolean glyph_catForm;           // +20% healing
    public boolean glyph_cyclone;           // +5 yard range
    public boolean glyph_dash;              // -60sec cooldown
    public boolean glyph_roots;             // -0.5sec cast
    public boolean glyph_ff;                // +10 yard range
    public boolean glyph_fb;                // heal for 1.5% max hp / 10 energy used
    public boolean glyph_maim;              // +100% damage
    public boolean glyph_masterShapeshifter;// free shifts
    public boolean glyph_9th;               // -10% damage taken in cat
    public boolean glyph_rake;              // +8 yards while stealth
    public boolean glyph_rebirth;           // max hp on rez
    public boolean glyph_savageRoar;        // rake/shred under prowl => 5cp
    public boolean glyph_savagery;          // passive savage roar, -0.05%
    public boolean glyph_skullBash;         // +2 sec interrupt duration, +5sec cooldown duration
    public boolean glyph_stampRoar;         // radius = 30 yd, no form requirement
    public boolean glyph_si;                // cooldown -40sec, duration = -50%    
    
    // 15
    public boolean talent_felineSwiftness;
    public boolean talent_displacerBeast;
    public boolean talent_wildCharge;
    
    // 30
    public boolean talent_yserasGift;
    public boolean talent_renewal;
    public boolean talent_cenarionWard;
    
    // 45
    public boolean talent_faerieSwarm;
    public boolean talent_massEntangle;
    public boolean talent_typhoon;
        
    // 60
    public boolean talent_sotf;
    public boolean talent_kotj;
    public boolean talent_fon;
    
    // 75
    public boolean talent_incapRoar;
    public boolean talent_ursolVortex;
    public boolean talent_mightyBash;
    
    // 90
    public boolean talent_hotw;
    public boolean talent_doc;
    public boolean talent_nv;
    
    // 100
    public boolean talent_li;
    public boolean talent_bt;
    public boolean talent_cos;
    
}
