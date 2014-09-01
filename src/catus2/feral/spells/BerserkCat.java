/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package catus2.feral.spells;

import catus2.TargetStyle;
import catus2.Unit;
import catus2.feral.Feral;

/**
 *
 * @author raffy
 */
public class BerserkCat extends FeralSpell {

    public BerserkCat(Feral owner) {
        super(owner, TargetStyle.NONE);
    }

    @Override
    public void casted(Unit target, double x, double y, int castTime, int powerCost) {
        o.buff_berserk_cat.activate();
    }
    
}
