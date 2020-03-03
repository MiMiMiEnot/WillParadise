package me.enot.configurations.lang;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;
import me.enot.WellPrivate;
import me.enot.configurations.Settings;
import me.enot.privat.WellRegion;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class Language {
    private static Plugin plugin = WellPrivate.getPlugin();
    private static Config languagecnf;
    private static File file = new File(plugin.getDataFolder(), "language.conf");
    private static Settings settings = new Settings();

    private static void create(){
        if(!plugin.getDataFolder().exists()){
            plugin.getDataFolder().mkdir();
        }
        try {
            if(!file.exists()) {
                InputStream is = settings.getClass().getResourceAsStream("/language.conf");
                if (is != null) {
                    Files.copy(is, file.toPath());
                }
            }
            languagecnf = ConfigFactory.parseFile(file).resolve();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static Config getLanguage() {
        return languagecnf;
    }

    public static void reload(){
        if(file == null || languagecnf == null){
            create();
        }
        languagecnf = ConfigFactory.parseFile(file);
    }

    public static void save(){
        ConfigRenderOptions cro = ConfigRenderOptions.defaults().setJson(false).setOriginComments(false);
        try {
            Files.write(file.toPath(), languagecnf.root().render(cro).getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static String get(Langs langs){
        return toString(getLanguage().getString(langs.getPath()));
    }

    public static String toString(String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static List<String> toStringList(List<String> list){
        for(int i = 0; i < list.size(); i++){
            list.set(i, toString(list.get(i)));
        }

        return list;
    }

    public static List<String> getList(Langs langs){
        List<String> list = getLanguage().getStringList(langs.getPath());
        list.forEach(string -> ChatColor.translateAlternateColorCodes('&', string));
        return list;
    }
}
