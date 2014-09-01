/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package catus2.feral.generators;

import catus2.AttackT;
import catus2.feral.Feral;
import catus2.feral.FeralView;

public class MoonfireCat extends CatGenerator {

    public MoonfireCat(Feral owner) {
        super(owner);
    }

    @Override
    public AttackT generate(FeralView target) {
        return AttackT.CRIT;
    }
    
}
