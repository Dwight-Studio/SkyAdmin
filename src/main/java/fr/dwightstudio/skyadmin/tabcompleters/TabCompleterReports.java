package fr.dwightstudio.skyadmin.tabcompleters;

import fr.dwightstudio.skyadmin.Report;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class TabCompleterReports implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        final List<String> c = new ArrayList<String>();
        switch (args.length) {
            case 1:
                for (Report reports : Report.getActiveReports()) {
                    if (Integer.toString(reports.getId()).contains(args[0])) {
                        c.add(Integer.toString(reports.getId()));
                    }
                }
                break;
            case 2:
                if ("cloturer".contains(args[1])) {
                    c.add("cloturer");
                }
                if ("assigner".contains(args[1])) {
                    c.add("assigner");
                }
                break;
        }
        return c;
    }
}
