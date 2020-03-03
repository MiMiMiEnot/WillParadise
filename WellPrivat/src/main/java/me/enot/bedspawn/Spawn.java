package me.enot.bedspawn;

import me.enot.privat.Corner;
import org.bukkit.entity.Player;

public class Spawn {

    private Corner location;
    private String player;
    public Spawn(Corner location, String player){
        this.player = player;
        this.location = location;
    }

    public Corner getLocation() {
        return location;
    }

    public String getPlayer() {
        return player;
    }

}
