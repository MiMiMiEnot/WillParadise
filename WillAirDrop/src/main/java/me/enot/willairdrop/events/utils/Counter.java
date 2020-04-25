package me.enot.willairdrop.events.utils;

import me.enot.willairdrop.WillAirDrop;
import me.enot.willairdrop.configs.Settings;
import me.enot.willairdrop.configs.language.Langs;
import me.enot.willairdrop.configs.language.Language;
import me.enot.willairdrop.configs.language.Replace;
import me.enot.willairdrop.logic.AirDrop;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Counter {

    private Player player;
    private AirDrop drop;
    private int seconds;
    private Integer scheduleID;

    public Counter(Player player, AirDrop drop, int seconds){
        this.player = player;
        this.drop = drop;
        this.seconds = seconds;
        this.scheduleID = null;
    }

    public Counter(Player player, AirDrop drop){
        this(player, drop, drop.getLoot().getTimeForOpen());
    }

    public Player getPlayer() {
        return player;
    }

    public AirDrop getDrop() {
        return drop;
    }

    public int getSeconds() {
        return seconds;
    }

    public Integer getScheduleID() {
        return scheduleID;
    }

    public void start (){
        this.drop.getHologram().clearLines();
        for(String s : Language.getMessage(Langs.hologram__air_drop_counter, new Replace("{X}", Integer.toString(this.seconds)))) {
            this.drop.getHologram().appendTextLine(s);
        }
        this.scheduleID = Bukkit.getScheduler().scheduleSyncRepeatingTask(WillAirDrop.getPlugin(), () -> {
            if(this.drop.isOpened()) {
                stop();
                return;
            }
            if (this.seconds > 0){
                this.seconds--;
                // this.seconds = this.seconds--;
                this.drop.getHologram().clearLines();
                for(String s : Language.getMessage(Langs.hologram__air_drop_counter, new Replace("{X}", Integer.toString(this.seconds)))) {
                    this.drop.getHologram().appendTextLine(s);
                }
            } else {
                stop();
            }
        }, 0, 20);
    }
    public void stop() {
        Bukkit.getScheduler().cancelTask(this.scheduleID);
        this.drop.setOpened(true);
        Language.broadcastMessage(Langs.crate__air_drop_opened,
                new Replace("{X}", Integer.toString(drop.getLocation().getBlockX())),
                new Replace("{Z}", Integer.toString(drop.getLocation().getBlockZ())));
        this.drop.getHologram().clearLines();
        ;
        for (String s : Language.getMessage(Langs.hologram__airdrop_opened)) {
            this.drop.getHologram().appendTextLine(s);
        }
    }

    public void stopWB() {
        Bukkit.getScheduler().cancelTask(this.scheduleID);
        Language.sendMessage(this.player, Langs.crate__air_drop_opening_cancelled);

        int radius = Settings.getRadius();
        int x1 = this.drop.getLocation().getBlockX() + radius;
        int x2 = this.drop.getLocation().getBlockX() - radius;

        int z1 = this.drop.getLocation().getBlockZ() + radius;
        int z2 = this.drop.getLocation().getBlockZ() - radius;

        int maxx = Math.max(x1, x2);
        int minx = Math.max(x1, x2);
        int maxz = Math.max(z1, z2);
        int minz = Math.max(z1, z2);


        for (Player p : this.drop.getLocation().getWorld().getPlayers()) {
            if (p.getLocation().getBlockX() >= minx && p.getLocation().getBlockX() <= maxx) {
                if (p.getLocation().getBlockZ() >= minz && p.getLocation().getBlockZ() <= maxz) {
                    Language.sendMessage(p, Langs.crate__airdrop_broadcat_cancelled_message);
                }
            }
        }

        this.drop.getHologram().clearLines();
        for (String s : Language.getMessage(Langs.hologram__airdrop_cancelled)) {
            this.drop.getHologram().appendTextLine(s);
        }
    }
}
