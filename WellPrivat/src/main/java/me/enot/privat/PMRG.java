package me.enot.privat;

public class PMRG {

    private PrivatMember privatMember;
    private WellRegion wellRegion;

    public PMRG(PrivatMember privatMember, WellRegion wellRegion){
        this.privatMember = privatMember;
        this.wellRegion = wellRegion;
    }

    public PrivatMember getPrivatMember() {
        return privatMember;
    }

    public WellRegion getWellRegion() {
        return wellRegion;
    }
}
