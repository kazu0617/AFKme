package net.kazu0617.afkme;

import java.io.File;
import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author     kazu0617
 * @license    MIT
 * @copyright  Copyright kazu0617 2015
 */
public class Main extends JavaPlugin implements Listener{
    //Todo AFK時の仕様をもう少し共有化出来るようにする。
     String Pluginname = getDescription().getName();
    public ConsoleLog cLog = new ConsoleLog(this);
    public AFKMethod AFKMethod = new AFKMethod(this);
    public FileIO FileIO = new FileIO(this);
    public boolean DebugMode = false;
    HashMap<Player,Location> Location;
    HashMap<Player,String> DisplayName;
    HashMap<Player,Integer> afkcount;
    String folder = getDataFolder() + File.separator;
    
    @Override
    public void onEnable()
    {
        getServer().getPluginManager().registerEvents(this, this);
        this.getServer().getScheduler().runTaskTimer(this, AFKMethod, 0L, 20L);
        int afktime = FileIO.getTime();
        cLog.debug("AFKTime = " + afktime);
        Location = new HashMap<>();
        DisplayName = new HashMap<>();
        afkcount = new HashMap<>();
    }
    @EventHandler
    public void onLogin(PlayerJoinEvent e) {
        Location.put(e.getPlayer(), e.getPlayer().getLocation());
        afkcount.put(e.getPlayer(), 0);
        DisplayName.put(e.getPlayer(), e.getPlayer().getDisplayName());
    }
    @EventHandler
    public void onLogout(PlayerQuitEvent e){
        Location.remove(e.getPlayer());
        afkcount.remove(e.getPlayer());
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if ((args.length >= 1) && ("DebugMode").startsWith(args[0])) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (!p.hasPermission("afkme.DebugMode")) {
                    cLog.Message(p, "権限がありません…", 2);
                    return true;
                }
            }
            if (DebugMode) {
                DebugMode = false;
            } else if (!DebugMode) {
                DebugMode = true;
            }
            cLog.BroadCast("DebugModeが" + DebugMode + "に変更されました");
            return true;
        } else if ((args.length == 0) && "afk".equalsIgnoreCase(label) && sender instanceof Player) {
            Player p = (Player) sender;
            Location L = p.getLocation();
            if(!p.hasPermission("afkme.use")){
                cLog.Message(p, "権限がありません。");
                return true;
            }
            boolean AFK = false;
            int count = afkcount.get(p);
            int afktime = FileIO.getTime();
            if (count < afktime) {
                Bukkit.dispatchCommand(p, "me is afk");
                DisplayName.get(p);
                p.setPlayerListName(ChatColor.GRAY + p.getDisplayName());
                Location.put(p, L);
                count = afktime + 1;
                afkcount.put(p, count);

            } else if (count >= afktime) {
                Bukkit.dispatchCommand(p, "me is no longer afk");
                p.setPlayerListName(DisplayName.get(p));
                afkcount.put(p, 0);

            }
        } else {
            sender.sendMessage("コマンドを確認してください。");
        }
        return true;
    }
}