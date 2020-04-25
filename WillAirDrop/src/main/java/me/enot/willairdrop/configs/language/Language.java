package me.enot.willairdrop.configs.language;

import com.typesafe.config.*;
import me.enot.willairdrop.WillAirDrop;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Language {

    private static Plugin plugin = WillAirDrop.getPlugin();
    private static Config languagecnf;
    private static File file = new File(plugin.getDataFolder(), "language.conf");
    private static Language language = new Language();

    private static void create(){
        if(!plugin.getDataFolder().exists()){
            plugin.getDataFolder().mkdir();
        }
        try {
            if(!file.exists()) {
                InputStream is = language.getClass().getResourceAsStream("/language.conf");
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

    public static void sendMessage(Player p, Langs langs){
        ConfigValue value = getLanguage().getValue(langs.convert());
        ConfigValueType type = value.valueType();
        if(type == ConfigValueType.STRING){
            String s = getLanguage().getString(langs.convert());
            p.sendMessage(toString(s));
        } else if(type == ConfigValueType.LIST){
            List<String> list = toStringList(getLanguage().getStringList(langs.convert()));
            list.forEach(p::sendMessage);
        }
    }
    public static void sendMessage(ConsoleCommandSender consoleCommandSender, Langs langs){
        ConfigValue value = getLanguage().getValue(langs.convert());
        ConfigValueType type = value.valueType();
        if(type == ConfigValueType.STRING){
            String s = getLanguage().getString(langs.convert());
            consoleCommandSender.sendMessage(toString(s));
        } else if(type == ConfigValueType.LIST){
            List<String> list = toStringList(getLanguage().getStringList(langs.convert()));
            list.forEach(consoleCommandSender::sendMessage);
        }
    }
    public static void sendMessage(Player p, Langs langs, Replace... replaces){
        ConfigValue value = getLanguage().getValue(langs.convert());
        ConfigValueType type = value.valueType();
        if(type == ConfigValueType.STRING){
            String s = getLanguage().getString(langs.convert());
            p.sendMessage(toString(s, replaces));
        } else if(type == ConfigValueType.LIST){
            List<String> list = toStringList(getLanguage().getStringList(langs.convert()), replaces);
            list.forEach(p::sendMessage);
        }
    }
    public static void sendMessage(ConsoleCommandSender consoleCommandSender, Langs langs, Replace... replaces){
        ConfigValue value = getLanguage().getValue(langs.convert());
        ConfigValueType type = value.valueType();
        if(type == ConfigValueType.STRING){
            String s = getLanguage().getString(langs.convert());
            consoleCommandSender.sendMessage(toString(s, replaces));
        } else if(type == ConfigValueType.LIST){
            List<String> list = toStringList(getLanguage().getStringList(langs.convert()), replaces);
            list.forEach(consoleCommandSender::sendMessage);
        }
    }

    public static List<String> getMessage(Langs langs, Replace... replaces){
        String conv = langs.convert();
        Object value = getLanguage().getValue(conv).unwrapped();
        if(value instanceof String){
            return Collections.singletonList(toString((String) value, replaces));
        } else if (value instanceof List){
            return toStringList((List<String>) value, replaces);
        }
        return Arrays.asList("");
    }
    public static List<String> getMessage(Langs langs){
        String conv = langs.convert();
        Object value = getLanguage().getValue(conv).unwrapped();
        if(value instanceof String){
            return Collections.singletonList(toString((String) value));
        } else if (value instanceof List){
            return toStringList((List<String>) value);
        }
        return Arrays.asList("");
    }


    public static void broadcastMessage(Langs langs){
        ConfigValue value = getLanguage().getValue(langs.convert());
        ConfigValueType type = value.valueType();
        if(type == ConfigValueType.STRING){
            String s = getLanguage().getString(langs.convert());
            Bukkit.broadcastMessage(toString(s));
        } else if(type == ConfigValueType.LIST){
            List<String> list = toStringList(getLanguage().getStringList(langs.convert()));
            list.forEach(string -> Bukkit.broadcastMessage(toString(string)));
        }
    }
    public static void broadcastMessage(Langs langs, Replace... replaces){
        ConfigValue value = getLanguage().getValue(langs.convert());
        ConfigValueType type = value.valueType();
        if(type == ConfigValueType.STRING){
            String s = getLanguage().getString(langs.convert());
            Bukkit.broadcastMessage(toString(s, replaces));
        } else if(type == ConfigValueType.LIST){
            List<String> list = toStringList(getLanguage().getStringList(langs.convert()));
            list.forEach(string -> Bukkit.broadcastMessage(toString(string, replaces)));
        }
    }
    public static void sendMessage(CommandSender commandSender, Langs langs, Replace... replaces){
        ConfigValue value = getLanguage().getValue(langs.convert());
        ConfigValueType type = value.valueType();
        if(type == ConfigValueType.STRING){
            String s = getLanguage().getString(langs.convert());
            commandSender.sendMessage(toString(s, replaces));
        } else if(type == ConfigValueType.LIST){
            List<String> list = toStringList(getLanguage().getStringList(langs.convert()), replaces);
            list.forEach(commandSender::sendMessage);
        }
    }
    public static void sendMessage(CommandSender commandSender, Langs langs){
        ConfigValue value = getLanguage().getValue(langs.convert());
        ConfigValueType type = value.valueType();
        if(type == ConfigValueType.STRING){
            String s = getLanguage().getString(langs.convert());
            commandSender.sendMessage(toString(s));
        } else if(type == ConfigValueType.LIST){
            List<String> list = toStringList(getLanguage().getStringList(langs.convert()));
            list.forEach(commandSender::sendMessage);
        }
    }

    public static String toString(String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }
    public static List<String> toStringList(List<String> slist){
        for(int i = 0; i < slist.size(); i++){
            slist.set(i, toString(slist.get(i)));
        }
        return slist;
    }
    public static String toString(String s, Replace... replaces){
        for(Replace r : replaces){
            s = s.replace(r.getWhat(), r.getTo());
        }
        return toString(s);
    }
    public static List<String> toStringList(List<String> slist, Replace... replaces){
        for(int i = 0; i < slist.size(); i++){
            slist.set(i, toString(slist.get(i), replaces));
        }
        return slist;
    }

}
