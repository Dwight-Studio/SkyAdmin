package fr.skynnotopia.skyadmin.tabcompleters;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TabCompleterWarn implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        final List<String> c = new ArrayList<String>();
        switch (args.length) {
            case 1:
                for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
                    if (pl.getName().contains(args[0])) {
                        c.add(pl.getName());
                    }
                }
                break;
        }
        return c;
    }
}
