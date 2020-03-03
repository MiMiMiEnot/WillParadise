package me.enot.privat.logic;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;
import com.typesafe.config.ConfigValueFactory;
import me.enot.WellPrivate;
import me.enot.configurations.Settings;
import me.enot.privat.Builder;
import me.enot.privat.Corner;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

public class ConfigCreating {
    private static Plugin plugin = WellPrivate.getPlugin();
    private static File dir = Settings.rgdir;

    public static boolean create(String privatname, Corner corner1, Corner corner2, String rgowner, Block block) {
        File file = new File(Settings.rgdir, Settings.getSettings().getInt("id") + ".conf");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }
        Config c = ConfigFactory.parseFile(file);
        c = c.withValue("privat-name", ConfigValueFactory.fromAnyRef(privatname));
        c = c.withValue("corners.first.X", ConfigValueFactory.fromAnyRef(corner1.getX()));
        c = c.withValue("corners.first.Y", ConfigValueFactory.fromAnyRef(corner1.getY()));
        c = c.withValue("corners.first.Z", ConfigValueFactory.fromAnyRef(corner1.getZ()));
        c = c.withValue("corners.second.X", ConfigValueFactory.fromAnyRef(corner2.getX()));
        c = c.withValue("corners.second.Y", ConfigValueFactory.fromAnyRef(corner2.getY()));
        c = c.withValue("corners.second.Z", ConfigValueFactory.fromAnyRef(corner2.getZ()));
        c = c.withValue("owner-name", ConfigValueFactory.fromAnyRef(rgowner));
        c = c.withValue("privat-members", ConfigValueFactory.fromAnyRef(new ArrayList<String>()));
        c = c.withValue("privat-block.X", ConfigValueFactory.fromAnyRef(block.getX()));
        c = c.withValue("privat-block.Y", ConfigValueFactory.fromAnyRef(block.getY()));
        c = c.withValue("privat-block.Z", ConfigValueFactory.fromAnyRef(block.getZ()));

        ConfigRenderOptions cro = ConfigRenderOptions.defaults().setJson(false).setOriginComments(false);
        try {
            Files.write(file.toPath(), c.root().render(cro).getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            Settings.addID();
            Builder.loadRG(file);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}

