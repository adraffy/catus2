package catus2;

public class SpellData {

    public final int id;    
    public final String name;
    public final School school;
    
    public SpellData(int id, String name, School school) {
        this.id = id;
        this.name = name;
        this.school = school;
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
