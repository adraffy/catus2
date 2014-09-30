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
    
    //{ Name, Id,Flags,PrjSp,  Sch, Class,  Race,Sca,MSL,SpLv,MxL,MinRange,MaxRange,Cooldown,  GCD,  Cat,  Duration,  RCost, RPG,Stac, PCh,PCr, ProcFlags,EqpCl, EqpInvType,EqpSubclass,CastMn,CastMx,Div,       Scaling,SLv, RplcId, {      Attr1,      Attr2,      Attr3,      Attr4,      Attr5,      Attr6,      Attr7,      Attr8,      Attr9,     Attr10,     Attr11,     Attr12 }, {     Flags1,     Flags2,     Flags3,     Flags4 }, Family, Description, Tooltip, Description Variable, Icon, ActiveIcon, Effect1, Effect2, Effect3 },

}
