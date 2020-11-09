package fr.dwightstudio.skyadmin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

import static fr.dwightstudio.skyadmin.Main.logPrefix;

public class Config {

    private static JavaPlugin plugin;

    public Config(JavaPlugin p) {
        p.saveDefaultConfig();
        plugin = p;
    }

    public static boolean getBoolean(String s) throws NullPointerException {
        return plugin.getConfig().getBoolean(s);
    }

    public static int getInt(String s) throws NullPointerException {
        return plugin.getConfig().getInt(s);
    }

    public static String getString(String s) throws NullPointerException {
        return plugin.getConfig().getString(s);
    }

    public static java.util.List<String> getStringList(String s) throws NullPointerException {
        return plugin.getConfig().getStringList(s);
    }

    public static void set(String s, Object o) throws NullPointerException {
        plugin.getConfig().set(s, o);
        plugin.saveConfig();
    }

    public static boolean reload() {
        boolean r = true;
        try {
            plugin.reloadConfig();
        } catch (Exception e) {
            Bukkit.getLogger().log(Level.SEVERE,logPrefix + e.getMessage());
            r = false;
        }
        return r;
    }


}