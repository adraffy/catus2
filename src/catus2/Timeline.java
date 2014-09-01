package catus2;

import java.util.PriorityQueue;

public class Timeline {
    
    private final PriorityQueue<Tick> queue = new PriorityQueue<>(100, (a, b) -> a.at - b.at);
    
    public void schedIn(int delta, Tick q) { schedAt(clock + delta, q); }
    public void schedAt(int at, Tick what) {
        if (what.at >= 0) {
            queue.remove(what);
        }     
        if (at < clock) {
            throw new IllegalStateException();
        }
        what.at = at;
        queue.add(what);
    }
    
    public boolean cancel(Tick what) {
        if (what.at < 0) {
            return false;
        }
        what.at = -1;
        queue.remove(what);
        return true;
    }
    
    public int timeUntil(Tick q) {
        return clock - q.at;
    }
    
    public int clock;
    
    public Tick next() {
        Tick q = queue.remove();
        clock = q.at;
        q.at = -1;
        System.out.println("@"+clock);            
        q.run();
        return q;
    }
    
    public void exhaust() {
        while (true) {
            next();
        }        
    }
    
    public void clear() {
        queue.clear();
    }
    
    
}
