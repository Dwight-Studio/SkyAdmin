package fr.skynnotopia.skyadmin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Report {
    private int id;
    private Player sender;
    private Player player;
    private String date;
    private String reason;
    private Boolean isAssigned;
    private Player assignedTo;
    private String assignmentDate;
    private Boolean isClosed;
    private Player closedBy;
    private String closingDate;

    public Report(int id) {
        if (Config.getString("reports." + id + ".sender") != null) {
            this.id = id;
            this.sender = Bukkit.getServer().getPlayer(UUID.fromString(Config.getString("reports." + id + ".sender")));
            this.player = Bukkit.getServer().getPlayer(UUID.fromString(Config.getString("reports." + id + ".player")));
            this.date = Config.getString("reports." + id + ".date");
            this.reason = Config.getString("reports." + id + ".reason");
            this.isAssigned = Config.getBoolean("reports." + id + ".isAssigned");
            if (this.isAssigned) {
                this.assignedTo = Bukkit.getServer().getPlayer(UUID.fromString(Config.getString("reports." + id + ".assignedTo")));
                this.assignmentDate = Config.getString("reports." + id + ".assignmentDate");
            } else {
                this.assignedTo = null;
                this.assignmentDate = null;
            }
            this.isClosed = Config.getBoolean("reports." + id + ".isClosed");
            if (this.isClosed) {
                this.closedBy = Bukkit.getServer().getPlayer(UUID.fromString(Config.getString("reports."+ id + ".closedBy")));
                this.closingDate = Config.getString("reports." + id + ".closingDate");
            } else {
                this.closedBy = null;
                this.closingDate = null;
            }
        } else {
            throw new NullPointerException("Report (Id:" + id + ") does't exist");
        }
    }

    public Report(Player sender, Player player, String reason) {
        this.id = Config.getInt("lastReportId") + 1;
        Config.set("lastReportId", id);
        this.sender = sender;
        this.player = player;
        this.date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
        this.reason = reason;
        this.isAssigned = false;
        this.assignedTo = null;
        this.isClosed = false;

        Config.set("reports." + id + ".sender", sender.getUniqueId().toString());
        Config.set("reports." + id + ".player", player.getUniqueId().toString());
        Config.set("reports." + id + ".date", date);
        Config.set("reports." + id + ".reason", reason);
        Config.set("reports." + id + ".isAssigned", false);
        Config.set("reports." + id + ".isClosed", false);
    }

    public Boolean isClosed() {
        return isClosed;
    }

    public Player getAssignment() {
        return assignedTo;
    }

    public Boolean isAssigned() {
        return isAssigned;
    }

    public String getReason() {
        return reason;
    }

    public String getDate() {
        return date;
    }

    public Player getPlayer() {
        return player;
    }

    public Player getSender() {
        return sender;
    }

    public int getId() {
        return id;
    }

    public void setAssignement(Player player) {
        this.isAssigned = true;
        this.assignedTo = player;
        Config.set("reports." + id + ".assignedTo", player.getUniqueId().toString());
        Config.set("reports." + id + ".isAssigned", true);
        assignmentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
        Config.set("reports." + id + ".assignmentDate", assignmentDate);

    }

    public void close(Player closingPlayer) {
        this.isClosed = true;
        Config.set("reports." + id + ".isClosed", true);
        Config.set("reports." + id + ".closedBy", closingPlayer.getUniqueId().toString());
        closingDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
        Config.set("reports." + id + ".closingDate", closingDate);
    }

    public Player getClosingPlayer() {
        return closedBy;
    }

    public static List<Report> getPlayerSentReports(Player player) {
        int lastReportId = Config.getInt("lastReportId");
        List<Report> Reports = new ArrayList<Report>();
        for (int i = 1; i <= lastReportId; i++) {
            try {
                if (Bukkit.getServer().getPlayer(UUID.fromString(Config.getString("reports." + i + ".sender"))) == player) {
                    Reports.add(new Report(i));
                }
            } catch (NullPointerException e) {
                break;
            }
        }
        return Reports;
    }

    public static List<Report> getPlayerReports(Player player) {
        int lastReportId = Config.getInt("lastReportId");
        List<Report> Reports = new ArrayList<Report>();
        for (int i = 1; i <= lastReportId; i++) {
            try {
                if (Bukkit.getServer().getPlayer(UUID.fromString(Config.getString("reports." + i + ".player"))) == player) {
                    Reports.add(new Report(i));
                }
            } catch (NullPointerException e) {
                break;
            }
        }
        return Reports;
    }

    public static List<Report> getPlayerActiveSentReports(Player player) {
        List<Report> activeReports = new ArrayList<Report>();
        for (Report reports : getPlayerSentReports(player)) {
            if (!reports.isClosed()) {
                activeReports.add(reports);
            }
        }
        return activeReports;
    }

    public static List<Report> getPlayerActiveReports(Player player) {
        List<Report> activeReports = new ArrayList<Report>();
        for (Report reports : getPlayerReports(player)) {
            if (!reports.isClosed()) {
                activeReports.add(reports);
            }
        }
        return activeReports;
    }

    public static List<Report> getActiveReports() {
        int lastReportId = Config.getInt("lastReportId");
        List<Report> Reports = new ArrayList<Report>();
        for (int i = 1; i <= lastReportId; i++) {
            try {
                Report report = new Report(i);
                if (!report.isClosed()) {
                    Reports.add(report);
                }
            } catch (NullPointerException e) {
                break;
            }
        }
        return Reports;
    }

    public static List<Report> getUnassignedReports() {
        int lastReportId = Config.getInt("lastReportId");
        List<Report> Reports = new ArrayList<Report>();
        for (int i = 1; i <= lastReportId; i++) {
            try {
                Report report = new Report(i);
                if (!report.isAssigned() && !report.isClosed()) {
                    Reports.add(report);
                }
            } catch (NullPointerException e) {
                break;
            }
        }
        return Reports;
    }

    public static List<Report> getAssignedReports() {
        int lastReportId = Config.getInt("lastReportId");
        List<Report> Reports = new ArrayList<Report>();
        for (int i = 1; i <= lastReportId; i++) {
            try {
                Report report = new Report(i);
                if (report.isAssigned() && !report.isClosed()) {
                    Reports.add(report);
                }
            } catch (NullPointerException e) {
                break;
            }
        }
        return Reports;
    }

    public static List<Report> getAssignedReports(Player assignment) {
        int lastReportId = Config.getInt("lastReportId");
        List<Report> Reports = new ArrayList<Report>();
        for (int i = 1; i <= lastReportId; i++) {
            try {
                Report report = new Report(i);
                if (report.isAssigned() && !report.isClosed() && report.getAssignment() == assignment) {
                    Reports.add(report);
                }
            } catch (NullPointerException e) {
                break;
            }
        }
        return Reports;
    }

    public static List<Report> getClosedReports() {
        int lastReportId = Config.getInt("lastReportId");
        List<Report> Reports = new ArrayList<Report>();
        for (int i = 1; i <= lastReportId; i++) {
            try {
                Report report = new Report(i);
                if (report.isClosed()) {
                    Reports.add(report);
                }
            } catch (NullPointerException e) {
                break;
            }
        }
        return Reports;
    }

    public static List<Report> getClosedReports(Player closingPlayer) {
        int lastReportId = Config.getInt("lastReportId");
        List<Report> Reports = new ArrayList<Report>();
        for (int i = 1; i <= lastReportId; i++) {
            try {
                Report report = new Report(i);
                if (report.isClosed() && report.getClosingPlayer() == closingPlayer) {
                    Reports.add(report);
                }
            } catch (NullPointerException e) {
                break;
            }
        }
        return Reports;
    }

    public String getAssignmentDate() {
        return assignmentDate;
    }

    public String getClosingDate() {
        return closingDate;
    }

    public static List<Report> getReports() {
        int lastReportId = Config.getInt("lastReportId");
        List<Report> Reports = new ArrayList<Report>();
        for (int i = 1; i <= lastReportId; i++) {
            try {
                Reports.add(new Report(i));
            } catch (NullPointerException e) {
                break;
            }
        }
        return Reports;
    }
}