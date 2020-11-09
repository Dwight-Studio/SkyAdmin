package fr.dwightstudio.skyadmin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Ticket {
    private int id;
    private Player sender;
    private String date;
    private String reason;
    private Boolean isAssigned;
    private Player assignedTo;
    private String assignmentDate;
    private Boolean isClosed;
    private Player closedBy;
    private String closingDate;

    public Ticket(int id) {
        if (Config.getString("tickets." + id + ".sender") != null) {
            this.id = id;
            this.sender = Bukkit.getServer().getPlayer(UUID.fromString(Config.getString("tickets." + id + ".sender")));
            this.date = Config.getString("tickets." + id + ".date");
            this.reason = Config.getString("tickets." + id + ".reason");
            this.isAssigned = Config.getBoolean("tickets." + id + ".isAssigned");
            if (this.isAssigned) {
                this.assignedTo = Bukkit.getServer().getPlayer(UUID.fromString(Config.getString("tickets." + id + ".assignedTo")));
                this.assignmentDate = Config.getString("tickets." + id + ".assignmentDate");
            } else {
                this.assignedTo = null;
                this.assignmentDate = null;
            }
            this.isClosed = Config.getBoolean("tickets." + id + ".isClosed");
            if (this.isClosed) {
                this.closedBy = Bukkit.getServer().getPlayer(UUID.fromString(Config.getString("tickets."+ id + ".closedBy")));
                this.closingDate = Config.getString("tickets." + id + ".closingDate");
            } else {
                this.closedBy = null;
                this.closingDate = null;
            }
        } else {
            throw new NullPointerException("Ticket (Id:" + id + ") does't exist");
        }
    }

    public Ticket(Player sender, String reason) {
        this.id = Config.getInt("lastTicketId") + 1;
        Config.set("lastTicketId", id);
        this.sender = sender;
        this.date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
        this.reason = reason;
        this.isAssigned = false;
        this.assignedTo = null;
        this.isClosed = false;

        Config.set("tickets." + id + ".sender", sender.getUniqueId().toString());
        Config.set("tickets." + id + ".date", date);
        Config.set("tickets." + id + ".reason", reason);
        Config.set("tickets." + id + ".isAssigned", false);
        Config.set("tickets." + id + ".isClosed", false);
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

    public Player getSender() {
        return sender;
    }

    public int getId() {
        return id;
    }

    public void setAssignement(Player player) {
        this.isAssigned = true;
        this.assignedTo = player;
        Config.set("tickets." + id + ".assignedTo", player.getUniqueId().toString());
        Config.set("tickets." + id + ".isAssigned", true);
        assignmentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
        Config.set("tickets." + id + ".assignmentDate", assignmentDate);
    }

    public void close(Player closingPlayer) {
        this.isClosed = true;
        Config.set("tickets." + id + ".isClosed", true);
        Config.set("tickets." + id + ".closedBy", closingPlayer.getUniqueId().toString());
        closingDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
        Config.set("tickets." + id + ".closingDate", closingDate);
    }

    public Player getClosingPlayer() {
        return closedBy;
    }

    public static List<Ticket> getPlayerTickets(Player player) {
        int lastTicketId = Config.getInt("lastTicketId");
        List<Ticket> Tickets = new ArrayList<Ticket>();
        for (int i = 1; i <= lastTicketId; i++) {
            try {
                if (Bukkit.getServer().getPlayer(UUID.fromString(Config.getString("tickets." + i + ".sender"))) == player) {
                    Tickets.add(new Ticket(i));
                }
            } catch (NullPointerException e) {
                break;
            }
        }
        return Tickets;
    }


    public static List<Ticket> getPlayerActiveTickets(Player player) {
        List<Ticket> activeTickets = new ArrayList<Ticket>();
        for (Ticket tickets : getPlayerTickets(player)) {
            if (!tickets.isClosed()) {
                activeTickets.add(tickets);
            }
        }
        return activeTickets;
    }

    public static List<Ticket> getActiveTickets() {
        int lastTicketId = Config.getInt("lastTicketId");
        List<Ticket> Tickets = new ArrayList<Ticket>();
        for (int i = 1; i <= lastTicketId; i++) {
            try {
                Ticket ticket = new Ticket(i);
                if (!ticket.isClosed()) {
                    Tickets.add(ticket);
                }
            } catch (NullPointerException e) {
                break;
            }
        }
        return Tickets;
    }

    public static List<Ticket> getUnassignedTickets() {
        int lastTicketId = Config.getInt("lastTicketId");
        List<Ticket> Tickets = new ArrayList<Ticket>();
        for (int i = 1; i <= lastTicketId; i++) {
            try {
                Ticket ticket = new Ticket(i);
                if (!ticket.isAssigned() && !ticket.isClosed()) {
                    Tickets.add(ticket);
                }
            } catch (NullPointerException e) {
                break;
            }
        }
        return Tickets;
    }

    public static List<Ticket> getAssignedTickets() {
        int lastTicketId = Config.getInt("lastTicketId");
        List<Ticket> Tickets = new ArrayList<Ticket>();
        for (int i = 1; i <= lastTicketId; i++) {
            try {
                Ticket ticket = new Ticket(i);
                if (ticket.isAssigned() && !ticket.isClosed()) {
                    Tickets.add(ticket);
                }
            } catch (NullPointerException e) {
                break;
            }
        }
        return Tickets;
    }

    public static List<Ticket> getAssignedTickets(Player assignment) {
        int lastTicketId = Config.getInt("lastTicketId");
        List<Ticket> Tickets = new ArrayList<Ticket>();
        for (int i = 1; i <= lastTicketId; i++) {
            try {
                Ticket ticket = new Ticket(i);
                if (ticket.isAssigned() && !ticket.isClosed() && ticket.getAssignment() == assignment) {
                    Tickets.add(ticket);
                }
            } catch (NullPointerException e) {
                break;
            }
        }
        return Tickets;
    }

    public static List<Ticket> getClosedTickets() {
        int lastTicketId = Config.getInt("lastTicketId");
        List<Ticket> Tickets = new ArrayList<Ticket>();
        for (int i = 1; i <= lastTicketId; i++) {
            try {
                Ticket ticket = new Ticket(i);
                if (ticket.isClosed()) {
                    Tickets.add(ticket);
                }
            } catch (NullPointerException e) {
                break;
            }
        }
        return Tickets;
    }

    public static List<Ticket> getClosedTickets(Player closingPlayer) {
        int lastTicketId = Config.getInt("lastTicketId");
        List<Ticket> Tickets = new ArrayList<Ticket>();
        for (int i = 1; i <= lastTicketId; i++) {
            try {
                Ticket ticket = new Ticket(i);
                if (ticket.isClosed() && ticket.getClosingPlayer() == closingPlayer) {
                    Tickets.add(ticket);
                }
            } catch (NullPointerException e) {
                break;
            }
        }
        return Tickets;
    }

    public String getAssignmentDate() {
        return assignmentDate;
    }

    public String getClosingDate() {
        return closingDate;
    }

    public static List<Ticket> getTickets() {
        int lastTicketId = Config.getInt("lastTicketId");
        List<Ticket> Tickets = new ArrayList<Ticket>();
        for (int i = 1; i <= lastTicketId; i++) {
            try {
                Tickets.add(new Ticket(i));
            } catch (NullPointerException e) {
                break;
            }
        }
        return Tickets;
    }
}
