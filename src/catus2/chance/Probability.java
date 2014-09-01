package catus2.chance;

import catus2.Unit;

public class Probability extends AbstractChance { //<O extends Unit> extends AbstractChance<O> {

    public double prob;
    
    public Probability(Unit owner) { //O owner, double prob, int icd) {
        super(owner); ///, icd);
        //this.prob = prob;
    }
    
    @Override
    protected boolean shouldProc(int t) {        
        return chance(prob);
    }

    @Override
    protected void appendChanceDescTo(StringBuilder sb) {
        sb.append(String.format("%.2f%% Chance", 100D * prob));
    }

}
