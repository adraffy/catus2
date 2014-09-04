package catus2;

import java.util.Arrays;

public class IntSet {

    private int[] keys = new int[8];
    private int num;
    
    public IntSet() {        
    }
    
    public void clear() {
        num = 0;
    }
    
    public boolean any() { return num > 0; }
    public boolean empty() { return num == 0; }
    
    public boolean add(int key) {
        int i = Arrays.binarySearch(keys, 0, num, key);
        if (i >= 0) {
            return false;
        }        
        i = ~i;
        if (num == keys.length) { 
            int[] copy = new int[num << 1]; // fixme: choose this constant factor so resizes never really happen
            System.arraycopy(keys, 0, copy, 0, i);
            copy[i] = key;
            System.arraycopy(keys, i, copy, i + 1, num - i);
            
        } else {        
            System.arraycopy(keys, i, keys, i + 1, num - i);                
            keys[i] = key;
            return true;
        }
        return true;
       
    }
    
    public boolean remove(int key) {
        int i = Arrays.binarySearch(keys, 0, num, key);
        if (i < 0) {
            return false;
        }
        if (i < --num) {
            System.arraycopy(keys, i + 1, keys, i, num - i);
        }
        return true;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        appendTo(sb, ", ");
        sb.append("]");
        return sb.toString();
    }
    
    public void appendTo(StringBuilder sb, String sep) {
        if (num == 0) {
            return;
        }
        sb.append(keys[0]);        
        for (int i = 1; i < num; i++) {
            sb.append(sep);
            sb.append(keys[i]);
        }        
    }
    
}
