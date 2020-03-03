package me.enot.privat.logic;

import me.enot.configurations.Settings;

import java.io.File;

public class ConfigDeleting {

    public static boolean delete(Integer id){
        File cfile = new File(Settings.rgdir, + id + ".conf");
        return cfile.delete();
    }
}
