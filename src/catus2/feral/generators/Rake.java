package catus2.feral.generators;

import catus2.Unit;
import catus2.combat.HitEvent;
import catus2.feral.Feral;
import catus2.feral.FeralBleed;
import catus2.feral.FeralView;

public class Rake extends CatGenerator {
    
    public Rake(Feral owner) {
        super(owner);
    }

    @Override
    public HitEvent generate(Unit target) {
        o.trigger_glyph_sr();
        boolean stealth = o.isStealthed();
        boolean bt = o.buff_bt.tryConsume();
        HitEvent event = HitEvent.melee(m, o, target, o.getCritChance(), 0);
        if (event.success()) {    
            FeralView view = o.getView(target);
            FeralBleed rake = view.dot_rake;                
            rake.snapshot = o.getSnapshotableDamageMod() * o.getBloodtalonsMod(bt) * (stealth ? o.fgd.PERK_IMPROVED_RAKE_DAMAGE_MOD : 1);
            rake.activate();  
            event.base = rake.getTickDamage();
            view.check_bonus_pvp_wod_4pc(event);            
        }
        return event;
    }
    
}
