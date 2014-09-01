package catus2;

public class EnumHelp {
    
    static public <E extends Enum> int bits(E ... a) {
        int bits = 0;
        for (E x: a) {
            bits |= 1 << x.ordinal();
        }        
        return bits;
    }

}
