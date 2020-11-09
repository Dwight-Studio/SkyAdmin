package fr.dwightstudio.skyadmin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Warn {
    private int id;
    private Player sender;
    private Player player;
    private String date;
    private String reason;
    private int currentWarnCount;

    public Warn(int id) {
        if (Config.getString("warns." + id + ".sender") != null) {
            this.id = id;
            this.sender = Bukkit.getServer().getPlayer(UUID.fromString(Config.getString("warns." + id + ".sender")));
            this.player = Bukkit.getServer().getPlayer(UUID.fromString(Config.getString("warns." + id + ".player")));
            this.date = Config.getString("warns." + id + ".date");
            this.reason = Config.getString("warns." + id + ".reason");
            this.currentWarnCount = Config.getInt("warns." + id + ".currentWarnCount");
        } else {
            throw new NullPointerException("Warn (Id:" + id + ") does't exist");
        }
    }

    public Warn(Player player, Player sender, String reason) {
        this.id = Config.getInt("lastWarnId") + 1;
        Config.set("lastWarnId", id);
        this.sender = sender;
        this.player = player;
        this.date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
        this.reason = reason;

        Config.set("warns." + id + ".sender", sender.getUniqueId().toString());
        Config.set("warns." + id + ".player", player.getUniqueId().toString());
        Config.set("warns." + id + ".date", date);
        Config.set("warns." + id + ".reason", reason);
        Config.set("warns." + id + ".currentWarnCount", getPlayerWarns(player).size());
    }

    public String getReason() {
        return reason;
    }

    public Player getPlayer() {
        return player;
    }

    public String getDate() {
        return date;
    }

    public Player getSender() {
        return sender;
    }

    public int getId() {
        return id;
    }

    public static List<Warn> getPlayerWarns(Player player) {
        int lastWarnId = Config.getInt("lastWarnId");
        List<Warn> Warns = new ArrayList<Warn>();
        for (int i = 1; i <= lastWarnId; i++) {
            try {
                if (Bukkit.getServer().getPlayer(UUID.fromString(Config.getString("warns." + i + ".player"))) == player) {
                    Warns.add(new Warn(i));
                }
            } catch (NullPointerException e) {
                break;
            }
        }
        return Warns;
    }

    public static List<Warn> getPlayerSentWarns(Player player) {
        int lastWarnId = Config.getInt("lastWarnId");
        List<Warn> Warns = new ArrayList<Warn>();
        for (int i = 1; i <= lastWarnId; i++) {
            try {
                if (Bukkit.getServer().getPlayer(UUID.fromString(Config.getString("warns." + i + ".sender"))) == player) {
                    Warns.add(new Warn(i));
                }
            } catch (NullPointerException e) {
                break;
            }
        }
        return Warns;
    }

    public static List<Warn> getWarns() {
        int lastWarnId = Config.getInt("lastWarnId");
        List<Warn> Warns = new ArrayList<Warn>();
        for (int i = 1; i <= lastWarnId; i++) {
            try {
                Warns.add(new Warn(i));
            } catch (NullPointerException e) {
                break;
            }
        }
        return Warns;
    }

    public int getCurrentWarnCount() {
        return currentWarnCount;
    }
}
