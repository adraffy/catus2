package catus2.feral.generators;

import catus2.AttackT;
import catus2.OriginT;
import catus2.SchoolT;
import catus2.feral.Feral;
import catus2.feral.FeralBleed;
import catus2.feral.FeralView;

public class Rake extends CatGenerator {

    public Rake(Feral owner) {
        super(owner);
    }

    @Override
    public AttackT generate(FeralView target) {
        o.applySavageRoarGlyph();
        double crit = o.getCritChance();
        AttackT atk = o.yellow_melee(target.unit, crit);
        boolean bt = o.buff_bt.tryConsume();
        if (atk.landed) {        
            FeralBleed rake = target.dot_rake;                
            rake.snapshot = o.getSnapshotableDamageMod() * o.getBloodtalonsMod(bt);
            rake.activate();            
            o.applyDamage(rake.getTickDamage(), target.unit, rake, OriginT.BLEED, SchoolT.SCHOOLS_PHYSICAL, atk == AttackT.CRIT);                      
        }
        return atk;
    }
    
}
