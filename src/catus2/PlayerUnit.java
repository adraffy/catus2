package catus2;

abstract public class PlayerUnit<O extends Unit<O,V>,V extends AbstractView<O>> extends Unit<O,V> {
        
    public PlayerUnit() {
        super(false);
    }
   
}
