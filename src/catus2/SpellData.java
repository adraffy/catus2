package catus2;

public class SpellData {

    public final int id;    

    public String name;
    public School school;
    
    public SpellData(int id, String name) {
        this.id = id;
        this.name = name;
    }
    
    // final?
    public Origin origin;
    public TargetStyle targetStyle;
    

    // is it a bleed?    
    // is it healing or damage
    
    public String getNameAndId() {
        return String.format("%s<%s>", name, id);
    }
    
}
