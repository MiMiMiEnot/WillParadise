package me.enot.privat;

import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.List;

public class WellRegion {

    private Integer id;
    private String privatname;
    private Corner corner1;
    private Corner corner2;
    private String rgowner;
    private List<PrivatMember> playermembers;
    private Corner privatblock;

    public WellRegion(Integer id, String privatname, Corner corner1, Corner corner2,
                      String rgowner, List<PrivatMember> playermembers, Corner privatblock){
        this.id = id;
        this.privatname = privatname;
        this.corner1 = corner1;
        this.corner2 = corner2;
        this.rgowner = rgowner;
        this.playermembers = playermembers;
        this.privatblock = privatblock;
    }

    public Integer getId() {
        return id;
    }

    public String getPrivatname() {
        return privatname;
    }

    public Corner getCorner1() {
        return corner1;
    }

    public Corner getCorner2() {
        return corner2;
    }

    public String getRgowner() {
        return rgowner;
    }

    public List<PrivatMember> getPlayerMembers() {
        return playermembers;
    }

    public Corner getPrivatblock() {
        return privatblock;
    }

    public void setPlayermembers(List<PrivatMember> playermembers) {
        this.playermembers = playermembers;
    }
}
