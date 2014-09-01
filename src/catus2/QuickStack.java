package catus2;

import java.util.Arrays;

public class QuickStack {
    
    static public class Slot {
        final QuickStack stack;
        final int index;   
        private Slot(QuickStack stack, int index) {
            this.stack = stack;
            this.index = index;
        }
        public void set(double value) {
            stack.slots[index] = value;
        }
        public void clear() {
            set(stack.clearedSlot);
        }
    }

    final boolean product;
    final double clearedSlot;
    
    public QuickStack(boolean product) {
        this.product = product;
        clearedSlot = product ? 1 : 0;
        base = clearedSlot;
    } 
    
    double base;
    
    private int count = 0;
    private double[] slots = new double[10];
    private boolean dirty;
    private double cached;
    
    public void clear() {
        dirty = false;
        cached = base;
    }
    
    public double fold() {
        if (dirty) {
            double acc = base;
            if (product) {
                for (int i = 0; i < count; i++) {
                    acc *= slots[i];
                }   
            } else {
                for (int i = 0; i < count; i++) {
                    acc += slots[i];
                }            
            }
            cached = acc;            
        }
        return cached;        
    }
    
    public Slot reserve() {
        Slot slot = new Slot(this, count++);
        if (count > slots.length) {
            slots = Arrays.copyOf(slots, count);
        }
        return slot;
    }
    
}
