package net.kazu0617.afkme;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author     kazu0617
 * @license    MIT
 * @copyright  Copyright kazu0617 2015
 */
public class ConsoleLog {

    Main plugin;
    String Pluginname = null;

    public ConsoleLog(Main instance) {
        this.plugin = instance;
        String Pluginname = plugin.Pluginname;
    }
    public static final Logger log = Logger.getLogger("Minecraft");
    private String cPrefix = "["+Pluginname+"] ";
    private String Pluginprefix = "[" + ChatColor.GREEN + Pluginname + ChatColor.RESET +"] ";
    private final String pError = "[" + ChatColor.RED+ "ERROR" + ChatColor.RESET+"] ";
    private final String pInfo =  "[" + ChatColor.RED+ "Info" + ChatColor.RESET+"] ";

    public void info(String Mess) {
        log.log(Level.INFO, "{0}{1}", new Object[]{plugin.Pluginname, Mess});
    }

    public void debug(String Mess) {
        if (plugin.DebugMode) {
            log.log(Level.INFO, "{0}[Debug] {1}", new Object[]{plugin.Pluginname, Mess});
        }
    }

    public void warn(String Mess) {
        log.log(Level.WARNING, "{0}{1}", new Object[]{plugin.Pluginname, Mess});
    }

    public void Message(CommandSender sender, String Text) {
        Message(sender, Text, 0);
    }
    public void Message(CommandSender sender, String Text, int type) {
        switch (type) {
            case 0:
                sender.sendMessage(Pluginprefix + Text);
                break;
            case 1:
                sender.sendMessage(cPrefix + pInfo + Text);
                break;
            case 2:
                sender.sendMessage(cPrefix + pError + Text);
                break;
            default:
                break;
        }
    }

    public void Message(Player p, String Text) {
        Message(p, Text, 0);
    }
    public void Message(Player p, String Text,int type) {
        switch (type) {
            case 0:
                p.sendMessage(Pluginprefix + Text);
                break;
            case 1:
                p.sendMessage(cPrefix + pInfo + Text);
                break;
            case 2:
                p.sendMessage(cPrefix + pError + Text);
                break;
            case 3:
                if(plugin.DebugMode) p.sendMessage(cPrefix + "[debug]" + Text);
                break;
            default:
                break;
        }
    }

    public void BroadCast(String Text) {
        plugin.getServer().broadcastMessage(Pluginprefix + Text);
    }
}
