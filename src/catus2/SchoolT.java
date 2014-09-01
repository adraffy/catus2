/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package catus2;

/**
 *
 * @author raffy
 */
public class SchoolT {
    
    static public final int NUM = 3;

    static public class Idx {
        static public final int PHYSICAL = 0;
        static public final int NATURE   = 1;
        static public final int ARCANE   = 2;
    }
    
    static public final int SCHOOLS_PHYSICAL    = 1 << Idx.PHYSICAL;
    static public final int SCHOOLS_NATURE      = 1 << Idx.NATURE;
    static public final int SCHOOLS_ARCANE      = 1 << Idx.ARCANE;
    static public final int SCHOOLS_SPELLSTORM  = SCHOOLS_ARCANE | SCHOOLS_NATURE;
    
    
    

}
