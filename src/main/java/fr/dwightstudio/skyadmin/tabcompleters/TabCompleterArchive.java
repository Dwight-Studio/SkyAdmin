package fr.dwightstudio.skyadmin.tabcompleters;

import fr.dwightstudio.skyadmin.Report;
import fr.dwightstudio.skyadmin.Ticket;
import fr.dwightstudio.skyadmin.Warn;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static fr.dwightstudio.skyadmin.Ticket.getTickets;
import static fr.dwightstudio.skyadmin.Warn.getWarns;

public class TabCompleterArchive implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        final List<String> c = new ArrayList<String>();
        switch (args.length) {
            case 1:
                if ("warns".contains(args[0])) {
                    c.add("warns");
                }
                if ("reports".contains(args[0])) {
                    c.add("reports");
                }
                if ("tickets".contains(args[0])) {
                    c.add("tickets");
                }
                if ("joueur".contains(args[0])) {
                    c.add("joueur");
                }
                break;
            case 2:
                if (args[0].equalsIgnoreCase("warns")) {
                    for (Warn warn : getWarns()) {
                        if (Integer.toString(warn.getId()).contains(args[1])) {
                            c.add(Integer.toString(warn.getId()));
                        }
                    }
                }
                if (args[0].equalsIgnoreCase("reports")) {
                    for (Report report : Report.getReports()) {
                        if (Integer.toString(report.getId()).contains(args[1])) {
                            c.add(Integer.toString(report.getId()));
                        }
                    }
                }
                if (args[0].equalsIgnoreCase("tickets")) {
                    for (Ticket ticket : getTickets()) {
                        if (Integer.toString(ticket.getId()).contains(args[1])) {
                            c.add(Integer.toString(ticket.getId()));
                        }
                    }
                }
                if (args[0].equalsIgnoreCase("joueur")) {
                    for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                        if (player.getName().contains(args[1])) {
                            c.add(player.getName());
                        }
                    }
                }
                break;
        }
        return c;
    }
}
