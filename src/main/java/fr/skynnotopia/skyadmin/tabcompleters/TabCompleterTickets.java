package fr.skynnotopia.skyadmin.tabcompleters;

import fr.skynnotopia.skyadmin.Report;
import fr.skynnotopia.skyadmin.Ticket;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static fr.skynnotopia.skyadmin.Report.getActiveReports;
import static fr.skynnotopia.skyadmin.Ticket.getActiveTickets;

public class TabCompleterTickets implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        final List<String> c = new ArrayList<String>();
        switch (args.length) {
            case 1:
                for (Ticket tickets : getActiveTickets()) {
                    c.add("" + tickets.getId());
                }
                break;
            case 2:
                c.add("cloturer");
                c.add("assigner");
                break;
        }
        return c;    }
}
