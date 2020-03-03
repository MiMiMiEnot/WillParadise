package me.enot;

import me.enot.bedspawn.BedSpawn;
import me.enot.commands.RGcmd;
import me.enot.configurations.Settings;
import me.enot.configurations.lang.Language;
import me.enot.events.BlockBreak;
import me.enot.events.BlockPlace;
import me.enot.events.EventsInPrivat;
import me.enot.events.PlayerMove;
import me.enot.gui.listener.InventoryEvent;
import me.enot.privat.Builder;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class WellPrivate extends JavaPlugin {
    public static Plugin getPlugin(){
        return WellPrivate.getPlugin(WellPrivate.class);
    }

    @Override
    public void onEnable() {
        Settings.reload();
        Language.reload();
        Builder.loadRGS();

        Bukkit.getPluginManager().registerEvents(new BlockPlace(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerMove(), this);
        Bukkit.getPluginManager().registerEvents(new BlockBreak(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryEvent(), this);
        Bukkit.getPluginManager().registerEvents(new EventsInPrivat(), this);
        Bukkit.getPluginManager().registerEvents(new BedSpawn(), this);
        BedSpawn.load();

        getCommand("rg").setExecutor(new RGcmd());
    }

    @Override
    public void onDisable() {

    }
}
