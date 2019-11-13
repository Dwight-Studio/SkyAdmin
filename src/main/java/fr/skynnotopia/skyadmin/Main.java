package fr.skynnotopia.skyadmin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    static String logPrefix = "[SkyChat] ";
    public static String messagePrefix = ChatColor.DARK_GREEN + "[SkyChat] " + ChatColor.RESET;

    @Override
    public void onEnable() {
        // Plugin startup logic
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
