package fr.skynnotopia.skyadmin.tabcompleters;

import fr.skynnotopia.skyadmin.Report;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static fr.skynnotopia.skyadmin.Report.getActiveReports;

public class TabCompleterReports implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        final List<String> c = new ArrayList<String>();
        switch (args.length) {
            case 1:
                for (Report reports : getActiveReports()) {
                    c.add("" + reports.getId());
                }
                break;
            case 2:
                c.add("cloturer");
                c.add("assigner");
                break;
        }
        return c;
    }
}
