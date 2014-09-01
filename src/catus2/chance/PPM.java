package catus2.chance;

import catus2.Unit;

public class PPM extends AbstractChance { //<O extends Unit> extends AbstractChance<O> {

    public double ppm;
    
    public PPM(Unit owner) { //O owner, double ppm, int icd) {
        super(owner); //, icd);
        //this.ppm = ppm;
    }
    
    @Override
    protected boolean shouldProc(int t) {
        // http://us.battle.net/wow/en/forum/topic/6893549789
        // 3(PPM) * 3.6(weaponspeed) / 60 (sec per min) = 18% chance
        return o.world.randomChance(ppm * o.getBaseSwingTime() / 60000D);
    }

    @Override
    protected void appendChanceDescTo(StringBuilder sb) {
        sb.append(String.format("%.2f PPM", ppm));
    }

    
    
}
