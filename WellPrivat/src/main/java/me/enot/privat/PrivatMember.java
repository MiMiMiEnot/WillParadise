package me.enot.privat;

public class PrivatMember {

    private String playername;
    private boolean isOwner;
    private boolean canBlockBreak;
    private boolean canChestOpen;
    private boolean canBlockPlace;
    private boolean canUseCharka;
    private boolean canUseAnvil;
    private boolean canUseDoors;
    private boolean canUsePech;
    private boolean canUseHopers;
    private boolean canUseWorkBench;
    private boolean canUseBed;
    private boolean canUsePlity;

    public PrivatMember(String playername, boolean isOwner, boolean canBlockBreak,
                        boolean canChestOpen, boolean canBlockPlace, boolean canUseCharka,
                        boolean canUseAnvil, boolean canUseDoors, boolean canUsePech,
                        boolean canUseHopers, boolean canUseWorkBench, boolean canUseBed,
                        boolean canUsePlity){
        this.playername = playername;
        this.isOwner = isOwner;
        this.canBlockBreak = canBlockBreak;
        this.canChestOpen = canChestOpen;
        this.canBlockPlace = canBlockPlace;
        this.canUseCharka = canUseCharka;
        this.canUseAnvil = canUseAnvil;
        this.canUseDoors = canUseDoors;
        this.canUsePech = canUsePech;
        this.canUseHopers = canUseHopers;
        this.canUseWorkBench = canUseWorkBench;
        this.canUseBed = canUseBed;
        this.canUsePlity = canUsePlity;
    }

    public String getPlayername() {
        return playername;
    }

    public boolean isOwner(){
        return isOwner;
    }

    public boolean canBlockBreak() {
        return canBlockBreak;
    }

    public boolean canChestOpen() {
        return canChestOpen;
    }

    public boolean canBlockPlace() {
        return canBlockPlace;
    }

    public boolean canUseCharka() {
        return canUseCharka;
    }

    public boolean canUseAnvil() {
        return canUseAnvil;
    }

    public boolean canUseDoors() {
        return canUseDoors;
    }

    public boolean canUsePech() {
        return canUsePech;
    }

    public boolean canUseHopers() {
        return canUseHopers;
    }

    public boolean canUseWorkBench() {
        return canUseWorkBench;
    }

    public boolean canUseBed() {
        return canUseBed;
    }

    public boolean canUsePlity() {
        return canUsePlity;
    }

    public void setCanBlockBreak(boolean canBlockBreak) {
        this.canBlockBreak = canBlockBreak;
    }

    public void setCanBlockPlace(boolean canBlockPlace) {
        this.canBlockPlace = canBlockPlace;
    }

    public void setCanChestOpen(boolean canChestOpen) {
        this.canChestOpen = canChestOpen;
    }

    public void setCanUseAnvil(boolean canUseAnvil) {
        this.canUseAnvil = canUseAnvil;
    }

    public void setCanUseBed(boolean canUseBed) {
        this.canUseBed = canUseBed;
    }

    public void setCanUseCharka(boolean canUseCharka) {
        this.canUseCharka = canUseCharka;
    }

    public void setCanUseDoors(boolean canUseDoors) {
        this.canUseDoors = canUseDoors;
    }

    public void setCanUseHopers(boolean canUseHopers) {
        this.canUseHopers = canUseHopers;
    }

    public void setOwner(boolean owner) {
        isOwner = owner;
    }

    public void setCanUsePech(boolean canUsePech) {
        this.canUsePech = canUsePech;
    }

    public void setCanUsePlity(boolean canUsePlity) {
        this.canUsePlity = canUsePlity;
    }

    public void setCanUseWorkBench(boolean canUseWorkBench) {
        this.canUseWorkBench = canUseWorkBench;
    }

}
