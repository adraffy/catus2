package catus2;

public class GameData {

    static public final double HP_PER_STA = 60;
    
    static public final double[] L100_RATING_PER_PERC = new double[UnitPerc.NUM];
    static {
        L100_RATING_PER_PERC[UnitPerc.MASTERY] = 88;
        L100_RATING_PER_PERC[UnitPerc.CRIT] = 122;
        L100_RATING_PER_PERC[UnitPerc.HASTE] = 100;
        L100_RATING_PER_PERC[UnitPerc.MULTI] = 66;
        L100_RATING_PER_PERC[UnitPerc.VERSA] = 131;
    }
    
}
