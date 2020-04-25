package me.enot.willairdrop.serializer;

import org.bukkit.inventory.Inventory;

public class Loot {

    private String lootName;
    private Inventory inventory;
    private int timeForOpen;

    public Loot(String lootName, Inventory inventory, int timeForOpen){
        this.lootName = lootName;
        this.inventory = inventory;
        this.timeForOpen = timeForOpen;
    }

    public String getLootName() {
        return lootName;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public int getTimeForOpen() {
        return timeForOpen;
    }
}
