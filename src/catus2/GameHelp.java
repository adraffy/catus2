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
public class GameHelp {
    
    static public final int POWER_MULTIPLIER = 10;
    
    static public int energy(int raw) {
        return raw * POWER_MULTIPLIER;
    }
    
    
    static public int round(double value) {
        return (int)(0.5 + value);
    }
    
    
   
    static public final double ONE_PI = Math.PI;
    static public final double TWO_PI = 2 * ONE_PI;  
    static public final double HALF_PI = ONE_PI / 2;
   
   /*
   public static double wrap_angle(double a, double center) {
       return a - TWO_PI * Math.floor((a + Math.PI - center) / TWO_PI);
   }
   */
   
   static public double smallerAngle(double x) {
       double c = Math.abs(x) % TWO_PI;
       return c > ONE_PI ? TWO_PI - c : c;
   }
   
   static public boolean isInsideAngle(double ang, double sweep) {
       return sweep >= ONE_PI || sweep >= smallerAngle(ang);
   }
    
}
