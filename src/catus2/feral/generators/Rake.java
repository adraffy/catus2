package catus2.feral.generators;

import catus2.Application;
import catus2.Origin;
import catus2.School;
import catus2.SpellId;
import catus2.SpellModel;
import catus2.feral.Feral;
import catus2.feral.FeralBleed;
import catus2.feral.FeralView;

public class Rake extends CatGenerator {
    
    static public final SpellModel SPELL = new SpellModel(SpellId.Druid.Feral.RAKE_SPELL, "Rake", School.PHYSICAL);
    static public final SpellModel BLEED = new SpellModel(SpellId.Druid.Feral.RAKE_DOT,   "Rake", School.PHYSICAL);
    
    public Rake(Feral owner) {
        super(owner);
    }

    @Override
    public Application generate(FeralView target) {
        o.applySavageRoarGlyph();
        boolean stealth = o.isStealthed();
        Application app = o.tryApply(target.u, this, Origin.MELEE_BLEED, School.PHYSICAL);
        boolean bt = o.buff_bt.tryConsume();
        if (app.hit()) {        
            FeralBleed rake = target.dot_rake;                
            rake.snapshot = o.getSnapshotableDamageMod() * o.getBloodtalonsMod(bt) * (stealth ? o.fgd.PERK_IMPROVED_RAKE_DAMAGE_MOD : 1);
            rake.activate();  
            app.base = rake.getTickDamage();
            target.executeBleed(app);            
        }
        return app;
    }
    
}
