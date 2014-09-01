package catus2.buffs;

public class AbstractBuffModel {

    public final int id;
    
    public boolean pandemic;
    
    public int default_duration;
    public int default_stacks = 1;
    public int maximum_stacks = 1;

    public int base_frequency;
    public boolean hasted;
    
    public double param;
    
    public AbstractBuffModel(int id) {
        this.id = id;
    }
    
    public void setCharges(int x) {
        default_stacks = x;
        maximum_stacks = x;
    }
    
}
