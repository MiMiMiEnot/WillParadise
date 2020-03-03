package me.enot.bedspawn;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;
import com.typesafe.config.ConfigValueFactory;
import me.enot.configurations.Settings;
import me.enot.configurations.lang.Langs;
import me.enot.configurations.lang.Language;
import me.enot.privat.Corner;
import me.enot.privat.PrivatMember;
import me.enot.privat.WellRegion;
import me.enot.privat.logic.PrivateSetting;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.material.Bed;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class BedSpawn implements Listener {

    private static List<Spawn> spawnlist = new ArrayList<>();
    public static void load(){
        File dir = Settings.bedspawn;
        File[] files = dir.listFiles();
        if(files != null){
            for(File f : files) {
                if (f.getName().endsWith(".conf")) {
                    spawnlist.add(load(f));
                }
            }
        }
    }

    private static Spawn load(File f){
        Config c = ConfigFactory.parseFile(f);
        int x = c.getInt("bed-location.X");
        int y = c.getInt("bed-location.Y");
        int z = c.getInt("bed-location.Z");
        String playername = f.getName().replace(".conf", "");
        Corner corner = new Corner(x, y, z);

        return new Spawn(corner, playername);
    }

    public static void create(Spawn spawn) {
        spawnlist.removeIf(sp -> sp.getPlayer().equalsIgnoreCase(spawn.getPlayer()));
        spawnlist.add(spawn);
        File file = new File(Settings.bedspawn, spawn.getPlayer().toLowerCase() + ".conf");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }

        Config c = ConfigFactory.parseFile(file);
        c = c.withValue("bed-location.X", ConfigValueFactory.fromAnyRef(spawn.getLocation().getX()));
        c = c.withValue("bed-location.Y", ConfigValueFactory.fromAnyRef(spawn.getLocation().getY()));
        c = c.withValue("bed-location.Z", ConfigValueFactory.fromAnyRef(spawn.getLocation().getZ()));

        ConfigRenderOptions cro = ConfigRenderOptions.defaults().setJson(false).setOriginComments(false);
        try {
            Files.write(file.toPath(), c.root().render(cro).getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean delete(String nick){
        File file = new File(Settings.bedspawn, nick.toLowerCase() + ".conf");
        Player p = Bukkit.getPlayer(nick);
        if(!p.isOnline()){
            Settings.rememberAdd(p.getName());
        } else {
            p.sendMessage(Language.get(Langs.spawn__bed_destroyed_if_online));
        }
        spawnlist.removeIf(sp -> sp.getPlayer().equalsIgnoreCase(nick));
        return file.delete();
    }

    private static Spawn getByCoordinates(Block block){
        for(Spawn spawn : spawnlist){

            Corner c1 = new Corner(block.getX(), block.getY(), block.getZ());

            Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GREEN + "Spawn: " + spawn.getLocation().toString());
            Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "Corner1" + c1.toString());
            if(spawn.getLocation().equals(c1)){
                Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "обнаружен с первой попытки");
                return spawn;
            } else {
                Location location = getSecondLocation(block);
                Corner c2 = new Corner(location.getBlockX(), location.getBlockY(), location.getBlockZ());
                Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Corner2" + c2.toString());
                if(spawn.getLocation().equals(c2)){
                    Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "обнаружен с второй попытки");
                    return spawn;
                }
            }
            //if(c.equals(spawn.getLocation()) || c.equals(spawn.getSecondLocation())){
                //return spawn;
            //}
        }
        return null;
    }

    private static Spawn getByName(String playername){
        for(Spawn spawn : spawnlist){
            if(spawn.getPlayer().equalsIgnoreCase(playername)){
                return spawn;
            }
        }
        return null;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        if(Settings.getRemember().contains(e.getPlayer().getName())){
            Settings.getRemember().remove(e.getPlayer().getName());
            Settings.rememberRemove(e.getPlayer().getName());
            e.getPlayer().sendMessage(Language.get(Langs.spawn__bed_destroyed_if_offline));
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBreak(BlockBreakEvent e) {
        if (e.getBlock().getType() == Material.BED_BLOCK) {
            Block block = e.getBlock();
            //Bed bed = (Bed)block.getState().getData();

            Spawn sp = getByCoordinates(block);

            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Координаты сломаного блока " + block.getX() + " " + block.getY() + " " + block.getZ());

            if(sp != null){
                Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "Удаление " + sp.getPlayer() + " " + delete(sp.getPlayer()));
                //delete(sp.getPlayer());
            } else {
                Bukkit.getConsoleSender().sendMessage("Не определено");
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInteract(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player p = e.getPlayer();
            Block b = e.getClickedBlock();
            if (b.getType() == Material.BED_BLOCK) {
                WellRegion wr = PrivateSetting.inPrivat(b.getLocation());
                if (wr != null) {
                    String playername = p.getName();
                    Corner corner = new Corner(b.getX(), b.getY(), b.getZ());
                    if (!wr.getRgowner().equalsIgnoreCase(p.getName())) {
                        PrivatMember member = PrivateSetting.getByNick(wr, p.getName());
                        if (member != null) {
                            if (member.canUseBed()) {
                                create(new Spawn(corner, playername));
                                p.sendMessage(Language.get(Langs.spawn__spawn_created));
                            }
                        }
                    } else {
                        create(new Spawn(corner, playername));
                        p.sendMessage(Language.get(Langs.spawn__spawn_created));
                    }
                } else {
                    Corner corner = new Corner(b.getX(), b.getY(), b.getZ());
                    String playername = p.getName();
                    create(new Spawn(corner, playername));
                    p.sendMessage(Language.get(Langs.spawn__spawn_created));
                }
            }
        }
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onRespawn(PlayerRespawnEvent e){
        Player p = e.getPlayer();
        Spawn spawn = getByName(p.getName());
        if(spawn != null){
            Corner corner = spawn.getLocation();
            e.setRespawnLocation(new Location(p.getWorld(), corner.getX(), corner.getY(), corner.getZ()));
        }
    }

    private static Location getSecondLocation(Block block){
        Bed bed = (Bed) block.getState().getData();
        if(!bed.isHeadOfBed()){
            return block.getRelative(bed.getFacing()).getLocation();
        } else {
            return block.getRelative(bed.getFacing().getOppositeFace()).getLocation();
        }
    }
}
