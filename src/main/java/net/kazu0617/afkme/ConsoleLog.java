package net.kazu0617.afkme;

import java.util.logging.Logger;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ConsoleLog {

    Main plugin;

    public ConsoleLog(Main instance) {
        this.plugin = instance;
    }
    public static final Logger log = Logger.getLogger("Minecraft");
    

    public void info(String Mess) {
        log.info(plugin.Pluginname + Mess);
    }

    public void debug(String Mess) {
        if (plugin.DebugMode) {
            log.info(plugin.Pluginname + "[Debug] " + Mess);
        }
    }

    public void warn(String Mess) {
        log.warning(plugin.Pluginname + Mess);
    }

    public void Message(CommandSender sender, String Text) {
        sender.sendMessage(plugin.Pluginprefix + Text);
        return;
    }

    public void Message(Player p, String Text) {
        p.sendMessage(plugin.Pluginprefix + Text);
        return;
    }

    public void BroadCast(String Text) {
        plugin.getServer().broadcastMessage(plugin.Pluginprefix + Text);
        return;
    }
}
