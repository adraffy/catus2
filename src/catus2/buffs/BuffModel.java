package catus2.buffs;

import catus2.SpellData;

public class BuffModel {

    public final SpellData rx; // cooler than sd :p
    
    public boolean enabled;
    public boolean passive;  // activate once
    public boolean unique;   // one per unit
    public boolean pandemic;
    
    public int default_duration;
    public int default_stacks = 1;
    public int maximum_stacks = 1;

    public int base_frequency;
    public boolean hasted;
    
    public double param; // scaling param or modifier
    
    public BuffModel(SpellData sd) {
        this.rx = sd;
    }
    
    public final int id() {
        return rx.id;
    }
    
    public void setCharges(int x) {
        default_stacks = x;
        maximum_stacks = x;
    }
    
}
