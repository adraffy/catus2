package catus2.chance;

import catus2.Unit;

public class RPPM extends AbstractChance { //<O extends Unit> extends AbstractChance<O> {

    static public final int RPPM_IDLE_TIME = 2 * 60000; // standard boss rppm reset
    
    static final int MAX_TIME_SINCE_LAST = 10000;
    static final int MAX_TIME_SINCE_PROC = 1000 * 1000;
    
    public double rppm;
    public boolean hasted;
        
    public RPPM(Unit owner) { //O owner, double rppm, int icd, boolean hasted) {
        super(owner); //, icd);
        //this.rppm = rppm;
        //this.hasted = hasted;       
    }
    
    private int lastTime;
    private int lastProc;
    
    @Override
    public void reset() {
        super.reset();
        lastTime = -RPPM_IDLE_TIME;
    }
    
    public double rppm() {
        return hasted ? rppm * o.getHasteMod() : rppm;
    }    
    
    @Override
    protected void beginCooldown(int t) {
        super.beginCooldown(t);
        lastTime = lastProc = t;
    }
    
    @Override
    protected boolean canProc(int t) {
        return super.canProc() && t > lastTime;
    }
    
    @Override
    protected boolean shouldProc(int t) {
        // http://us.battle.net/wow/en/forum/topic/8197741003?page=1        
        // Calculate the proc frequency as normal. 
        // Based on that, you can figure out the expected average proc interval.
        // We also now keep track of time since the last successful proc,
        // (this is different from the time since last chance to proc), capped at 1000 sec. 
        // Multiply the proc chance by MAX(1, 1+((TimeSinceLastSuccessfulProc/AverageProcInterval)-1.5)*3). 
        // For example, if a proc has an average proc interval of 45 sec, 
        // and it’s been 72 sec since your last successful proc, you’ll get a 1.3x multiplier to your proc chance. 
        // If you’ve been out of combat for a few min, and it’s been 5 min since your last successful proc,
        // you’ll get a whopping 16.5x multiplier to your proc chance.
        // http://us.battle.net/wow/en/forum/topic/6893549789#1
        // It can proc from any damage/healing event. It keeps track of the last time it had a chance to proc for that enchant.
        // It calculates the difference in time since the last chance to proc. It uses that time to determine the chance for that event to trigger a proc.
        // For example, if you have 22% Haste, it was 1.4sec since the last chance to proc, and you’ve got Windsong, then the chance to proc is 2(ppm) * 1.22(haste) * 1.4(time since last chance) / 60 (sec per min) = 5.693%.
        // The ‘time since the last chance to proc’ is capped at 10sec, so that your first attack of a fight isn’t a guaranteed proc. 
        int sinceLast = t - lastTime;
        lastTime = t;           
        double hppm = rppm();
        double norm = Math.min(MAX_TIME_SINCE_LAST, sinceLast) / 60000D;
        double tslp = Math.min(MAX_TIME_SINCE_PROC, t - lastProc);      
        double avpi = Math.max(icd, 60000D / hppm);
         //MAX(1, 1+((TimeSinceLastSuccessfulProc/AverageProcInterval)-1.5)*3)        
        double wait = Math.max(1, 3 * tslp / avpi - 3.5);
        double prob = hppm * wait * norm;                     
        return chance(prob);
    }

    @Override
    protected void appendChanceDescTo(StringBuilder sb) {
        sb.append(String.format("%.2f", rppm()));
        if (hasted) {
            sb.append(" Hasted (");
            sb.append(String.format("%.2f", rppm));
            sb.append(")");
        }
        sb.append(" RPPM");
    }    

}

