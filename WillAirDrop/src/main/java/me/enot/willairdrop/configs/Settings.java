package me.enot.willairdrop.configs;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;
import com.typesafe.config.ConfigValueFactory;
import me.enot.willairdrop.WillAirDrop;
import me.enot.willairdrop.logic.utils.Calculations;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class Settings {

    private static Plugin plugin = WillAirDrop.getPlugin();
    private static Config settingscnf;
    private static final File file = new File(plugin.getDataFolder(), "settings.conf");
    public static final File lootdir = new File(plugin.getDataFolder(), "loots/");
    private static Settings settings = new Settings();

    private static void create(){
        if(!plugin.getDataFolder().exists()){
            plugin.getDataFolder().mkdir();
        }
        if(!lootdir.exists()){
            lootdir.mkdir();
        }
        try {
            if(!file.exists()) {
                InputStream is = settings.getClass().getResourceAsStream("/settings.conf");
                if (is != null) {
                    Files.copy(is, file.toPath());
                }
            }
            settingscnf = ConfigFactory.parseFile(file).resolve();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static Config getSettings() {
        return settingscnf;
    }


    private static List<String> lootTime;

    private static int maxX;
    private static int maxZ;
    private static int minX;
    private static int minZ;

    private static int announceTime;
    private static String announceTimeString;
    private static String spawnWorld;
    private static Material lootMaterial;

    private static String reloadPermission;
    private static String startPermission;

    private static int minPlayers;

    private static double hologramX;
    private static double hologramY;
    private static double hologramZ;

    private static int radius;

    private static List<Material> blockedMaterials;

    private static Material animationMaterial;
    private static int animationSchedulerTicks;
    private static double animationMoveY;
    private static Color animationColor;

    public static void reload() {
        if (file == null || settingscnf == null) {
            create();
        }
        settingscnf = ConfigFactory.parseFile(file);


        maxX = getSettings().getInt("max-values.X");
        maxZ = getSettings().getInt("max-values.Z");
        minX = getSettings().getInt("min-values.X");
        minZ = getSettings().getInt("min-values.Z");

        lootTime = getSettings().getStringList("loots-time");

        announceTime = Calculations.toSeconds(getSettings().getString("announce-time"));
        announceTimeString = getSettings().getString("announce-time");
        blockedMaterials = getSettings().getEnumList(Material.class, "blocks-what-blocked-to-spawn-generations");
        spawnWorld = getSettings().getString("loot-spawn-world");
        lootMaterial = getSettings().getEnum(Material.class, "loot-material");
        startPermission = getSettings().getString("permissions.start");
        reloadPermission = getSettings().getString("permissions.reload");

        minPlayers = getSettings().getInt("min-players-for-air-drop");

        hologramX = getSettings().getDouble("hologram.X");
        hologramY = getSettings().getDouble("hologram.Y");
        hologramZ = getSettings().getDouble("hologram.Z");

        radius = getSettings().getInt("air-drop-broadcast-radius");

        animationMaterial = getSettings().getEnum(Material.class, "animation.item");
        animationSchedulerTicks = getSettings().getInt("animation.move-every");
        animationMoveY = getSettings().getDouble("animation.move-y");
        animationColor = Color.fromBGR(
                getSettings().getInt("animation.color.red"),
                getSettings().getInt("animation.color.green"),
                getSettings().getInt("animation.color.blue"));

        if (getSettings().getBoolean("generate-example")) {
            File examplefile = new File(lootdir, "example.conf");
            try {
                if (!examplefile.exists()) {
                    InputStream is = settings.getClass().getResourceAsStream("/example.conf");
                    if (is != null) {
                        Files.copy(is, examplefile.toPath());
                    }
                }
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public static int getMaxX() {
        return maxX;
    }

    public static int getMaxZ() {
        return maxZ;
    }

    public static int getMinX() {
        return minX;
    }

    public static int getMinZ() {
        return minZ;
    }

    public static int getAnnounceTime() {
        return announceTime;
    }

    public static String getSpawnWorld() {
        return spawnWorld;
    }

    public static Material getLootMaterial() {
        return lootMaterial;
    }

    public static String getStartPermission() {
        return startPermission;
    }

    public static String getReloadPermission() {
        return reloadPermission;
    }

    public static String getAnnounceTimeString() {
        return announceTimeString;
    }

    public static List<Material> getBlockedMaterials() {
        return blockedMaterials;
    }

    public static List<String> getLootTime() {
        return lootTime;
    }

    public static int getMinPlayers() {
        return minPlayers;
    }

    public static double getHologramX() {
        return hologramX;
    }

    public static double getHologramY() {
        return hologramY;
    }

    public static double getHologramZ() {
        return hologramZ;
    }

    public static int getRadius() {
        return radius;
    }

    public static double getAnimationMoveY() {
        return animationMoveY;
    }

    public static Material getAnimationMaterial() {
        return animationMaterial;
    }

    public static int getAnimationSchedulerTicks() {
        return animationSchedulerTicks;
    }

    public static Color getAnimationColor() {
        return animationColor;
    }

    private static void save(){
        ConfigRenderOptions cro = ConfigRenderOptions.defaults().setJson(false).setOriginComments(false);
        try {
            Files.write(file.toPath(), settingscnf.root().render(cro).getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void add(String path, Object value){
        settingscnf = settingscnf.withValue(path, ConfigValueFactory.fromAnyRef(value));
        save();
    }
    public static void remove(String path){
        settingscnf = settingscnf.withoutPath(path);
        save();
    }

}
