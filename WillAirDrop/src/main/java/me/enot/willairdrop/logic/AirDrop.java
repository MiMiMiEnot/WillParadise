package me.enot.willairdrop.logic;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import me.enot.willairdrop.WillAirDrop;
import me.enot.willairdrop.configs.Settings;
import me.enot.willairdrop.configs.language.Langs;
import me.enot.willairdrop.configs.language.Language;
import me.enot.willairdrop.configs.language.Replace;
import me.enot.willairdrop.logic.utils.AirDropLogick;
import me.enot.willairdrop.logic.utils.Calculations;
import me.enot.willairdrop.serializer.Loot;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class AirDrop{

    private Loot loot;
    private Location location;
    private boolean opened;
    private Hologram hologram;

    public AirDrop(Loot loot, Location location){
        this(loot, location, false);
    }
    public AirDrop(Loot loot, Location location, boolean opened){
        this.loot = loot;
        this.location = location;
        this.opened = opened;
        /*Location loc = this.location.clone();
        double x = loc.getBlockX() + Settings.getHologramX();
        double y = loc.getBlockY() + Settings.getHologramY();
        double z = loc.getBlockZ() + Settings.getHologramZ();
        loc.setX(x);
        loc.setY(y);
        loc.setZ(z);
        hologram = HologramsAPI.createHologram(WillAirDrop.getPlugin(), loc);
        hologram.getVisibilityManager().setVisibleByDefault(true);*/
    }

    public Loot getLoot() {
        return loot;
    }

    public Location getLocation() {
        return location;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public Hologram getHologram() {
        return hologram;
    }

    public void setHologram(Hologram hologram) {
        this.hologram = hologram;
    }

    public void call(){
        call(null);
    }

    public void call(String adminName){
        int announce = Settings.getAnnounceTime();
        call(announce, adminName);
    }
    public void call(int time, String adminName){
        if(adminName == null) {
            Language.broadcastMessage(Langs.crate__time_to_box,
                    new Replace("{X}", Integer.toString(location.getBlockX())),
                    new Replace("{Z}", Integer.toString(location.getBlockZ())),
                    new Replace("{TIME}", Calculations.seccondsToString(time)));
        } else {
            Language.broadcastMessage(Langs.crate__time_to_box_admin,
                    new Replace("{X}", Integer.toString(location.getBlockX())),
                    new Replace("{Z}", Integer.toString(location.getBlockZ())),
                    new Replace("{TIME}", Calculations.seccondsToString(time)),
                    new Replace("{NICK}", adminName));
        }
        //ItemStack stack = new ItemStack(Settings.getLootMaterial());
        Bukkit.getScheduler().runTaskLater(WillAirDrop.getPlugin(), () -> {
            AirDropLogick.doAnimation(this);
        }, time * 20);
    }

}
