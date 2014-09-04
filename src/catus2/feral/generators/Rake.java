package catus2.feral.generators;

import catus2.Application;
import catus2.Origin;
import catus2.School;
import catus2.SpellId;
import catus2.SpellData;
import catus2.feral.Feral;
import catus2.feral.FeralBleed;
import catus2.feral.FeralView;

public class Rake extends CatGenerator {
    

    public Rake(Feral owner) {
        super(owner);
    }

    @Override
    public Application generate(FeralView target) {
        o.trigger_glyph_sr();
        boolean stealth = o.isStealthed();
        Application app = o.tryApply(target.u, this, Origin.MELEE_BLEED, School.PHYSICAL);
        boolean bt = o.buff_bt.tryConsume();
        if (app.hit()) {        
            FeralBleed rake = target.dot_rake;                
            rake.snapshot = o.getSnapshotableDamageMod() * o.getBloodtalonsMod(bt) * (stealth ? o.fgd.PERK_IMPROVED_RAKE_DAMAGE_MOD : 1);
            rake.activate();  
            app.base = rake.getTickDamage();
            target.check_bonus_pvp_wod_4pc(app);            
        }
        return app;
    }
    
}
