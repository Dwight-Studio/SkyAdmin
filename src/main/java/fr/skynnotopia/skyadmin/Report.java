package fr.skynnotopia.skyadmin;

import fr.skynnotopia.skyadmin.exceptions.ReportNotInitiatedException;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import static fr.skynnotopia.skyadmin.Main.logPrefix;

public class Report {
    private boolean isInitiated = false;
    private int id;
    private Player sender;
    private Player player;
    private String date;
    private String reason;
    private Boolean isAssigned;
    private Player assignment;
    private Boolean isClosed;

    public Report(int id){
        if (Config.getInt("lastReportId") > id && id > 0) {
            isInitiated = true;
            this.id = id;
            this.sender = Bukkit.getServer().getPlayer(Config.getString("reports." + id + ".sender"));
            this.player = Bukkit.getServer().getPlayer(Config.getString("reports." + id + ".player"));
            this.date = Config.getString("reports." + id + ".date");
            this.reason = Config.getString("reports." + id + ".reason");
            this.isAssigned = Config.getBoolean("reports." + id + ".isAssigned");
            if (this.isAssigned) {
                this.assignment = Bukkit.getServer().getPlayer(Config.getString("reports." + id + ".assignment"));
            } else {
                assignment = null;
            }
            this.isClosed = Config.getBoolean("reports." + id + ".isClosed");
        }
    }

    public Boolean newReport(Player sender, Player player, String reason) {
        try {
            isInitiated = true;
            this.id = Config.getInt("lastReportId") + 1;
            Config.set("lastReportId", id);
            this.sender = sender;
            this.player = player;
            this.date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
            this.reason = reason;
            this.isAssigned = false;
            this.assignment = null;
            this.isClosed = false;

            Config.set("reports." + id + ".sender", sender.getUniqueId().toString());
            Config.set("reports." + id + ".player", player.getUniqueId().toString());
            Config.set("reports." + id + ".date", date);
            Config.set("reports." + id + ".reason", reason);
            Config.set("reports." + id + ".isAssigned", false);
            Config.set("reports." + id + ".assignment", "");
            Config.set("reports." + id + ".isClosed", false);

        } catch (Exception e) {
            Bukkit.getLogger().log(Level.SEVERE, logPrefix + e.toString());
            isInitiated = false;
            return false;
        }
        return true;
    }

    public Boolean isClosed() throws ReportNotInitiatedException {
        if (!isInitiated) {
            throw new ReportNotInitiatedException();
        }
        return isClosed;
    }

    public Player getAssignment() throws ReportNotInitiatedException {
        if (!isInitiated) {
            throw new ReportNotInitiatedException();
        }
        return assignment;
    }

    public Boolean isAssigned() throws ReportNotInitiatedException {
        if (!isInitiated) {
            throw new ReportNotInitiatedException();
        }
        return isAssigned;
    }

    public String getReason() throws ReportNotInitiatedException {
        if (!isInitiated) {
            throw new ReportNotInitiatedException();
        }
        return reason;
    }

    public String getDate() throws ReportNotInitiatedException {
        if (!isInitiated) {
            throw new ReportNotInitiatedException();
        }
        return date;
    }

    public Player getPlayer() throws ReportNotInitiatedException {
        if (!isInitiated) {
            throw new ReportNotInitiatedException();
        }
        return player;
    }

    public Player getSender() throws ReportNotInitiatedException {
        if (!isInitiated) {
            throw new ReportNotInitiatedException();
        }
        return sender;
    }

    public int getId() throws ReportNotInitiatedException {
        if (!isInitiated) {
            throw new ReportNotInitiatedException();
        }
        return id;
    }

    public void setAssignement(Player player) throws ReportNotInitiatedException {
        if (!isInitiated) {
            throw new ReportNotInitiatedException();
        }
        this.isAssigned = true;
        this.assignment = player;
        Config.set("Reports." + id + ".assignment", player.getUniqueId());
    }

    public void close() throws ReportNotInitiatedException {
        if (!isInitiated) {
            throw new ReportNotInitiatedException();
        }
        this.isClosed = true;
        Config.set("Reports." + id + ".isClosed", player.getUniqueId());
    }

    public static List<Report> getPlayerSentReports(Player player) {
        int lastReportId = Config.getInt("lastReportId");
        List<Report> Reports = new ArrayList<Report>();
        for (int i = 1; i < lastReportId; i++) {
            Bukkit.getServer().getPlayer("Deleranax").sendMessage(""+i);
            Bukkit.getServer().getPlayer("Deleranax").sendMessage(Config.getString("Reports." + i + ".sender"));
            if (Bukkit.getServer().getPlayer(Config.getString("Reports." + i + ".sender")) == player) {
                Reports.add(new Report(i));
            }
        }
        return Reports;
    }

    public static List<Report> getPlayerReports(Player player) {
        int lastReportId = Config.getInt("lastReportId");
        List<Report> Reports = new ArrayList<Report>();
        for (int i = 1; i <= lastReportId; i++) {
            if (Bukkit.getServer().getPlayer(Config.getString("Reports." + i + ".player")) == player) {
                Reports.add(new Report(i));
            }
        }
        return Reports;
    }

    public static int getPlayerActiveSentReportsCount(Player player) {
        int val = 0;
        try {
            for (Report reports : getPlayerSentReports(player)) {
                if (reports.getSender() == player && reports.isClosed() == false) {
                    val++;
                }
            }
        } catch (ReportNotInitiatedException e) {
            Bukkit.getLogger().log(Level.SEVERE, logPrefix + e.toString());
        }
        return val;
    }

    public static int getPlayerActiveReportsCount(Player player) {
        int val = 0;
        try {
            for (Report reports : getPlayerReports(player)) {
                if (reports.getSender() == player && reports.isClosed() == false) {
                    val++;
                }
            }
        } catch (ReportNotInitiatedException e) {
            Bukkit.getLogger().log(Level.SEVERE, logPrefix + e.toString());
        }
        return val;
    }
}
