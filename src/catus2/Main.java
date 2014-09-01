/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package catus2;

import catus2.feral.Feral;

/**
 *
 * @author raffy
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        World w = new World();
        
        Feral feral = new Feral();        
        
        w.addUnit(feral);
        
        
        
    }
    
}
