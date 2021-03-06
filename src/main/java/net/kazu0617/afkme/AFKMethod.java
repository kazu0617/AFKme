package net.kazu0617.afkme;

import java.util.Collection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;

/**
 * @author     kazu0617
 * @license    MIT
 * @copyright  Copyright kazu0617 2015
 */
public class AFKMethod extends BukkitRunnable{
     Main plugin;
     public AFKMethod(Main instance) {
         this.plugin = instance;
     }
     @Override
     public void run() {
         plugin.cLog.debug("runに入りました"); // スケジュールで実行する処理の内容をここに書きます。
         int afktime = plugin.FileIO.TimeIO(false, 0);
         
         Collection<? extends Player> players = Bukkit.getServer().getOnlinePlayers();
         for (Player p : players) {
             Location L = p.getLocation();
             boolean AFK = false;
             if(!p.hasPermission("AFKme.use")){
                plugin.cLog.Message(p, "権限がありません。");
                continue;
             }
             plugin.cLog.debug("player: " + p + ",Permission: "+ p.hasPermission("AFKme.use"));
             int count;
             //現在のLocationと保存されているLocationの比較.もし同じならAFKをtrueに変更.
             if (!plugin.Location.containsKey(p)) {
                 plugin.Location.put(p, L);
                 continue;
             }
             Location CL = plugin.Location.get(p); //ココで何故かエラー吐く…
             plugin.cLog.debug("LoadLocation: " + CL);
             plugin.cLog.debug("Location: " + L);
             plugin.Location.put(p, L);
             if (L.getX() == CL.getX() 
                     && L.getY() == CL.getY() 
                     && L.getZ() == CL.getZ()
                     && L.getYaw() == CL.getYaw()
                     && L.getPitch() == CL.getPitch()) AFK = true;
             plugin.cLog.debug("AFK = " + AFK);
             if(AFK) { //もしAFKがTrueなら,Countの加算.
                 if (!plugin.afkcount.containsKey(p)) count = 0;
                 else count = plugin.afkcount.get(p);
                 plugin.cLog.debug("count = " + count);
                 count++;
                 plugin.cLog.debug("count++ = " + count);
                 plugin.afkcount.put(p, count);
             }
             else if (!AFK) {//もしAFKがFalseなら,Countの初期化.
                 if (!plugin.afkcount.containsKey(p)) {
                 plugin.afkcount.put(p, 0);
                 continue;
                 }
                 count = plugin.afkcount.get(p);
                 if (count >= afktime) {//もしafktimeよりもcountが大きい時は,離席モードの解除.
                     if (!plugin.DebugMode) {
                         Bukkit.dispatchCommand(p, "me is no longer afk");
                     } else if (plugin.DebugMode) {
                         plugin.cLog.debug("オンライン: " + p);
                     }
                     p.setPlayerListName(plugin.DisplayName.get(p));
                 }
                 plugin.afkcount.put(p, 0);
             }
             if (plugin.afkcount.get(p) == afktime) {//countとafktimeが同じであれば,離席モードへ入る.
                 String D = p.getPlayerListName();
                 if (plugin.DebugMode) plugin.cLog.debug("離席: " + p);
                 else Bukkit.dispatchCommand(p, "me is afk");
                 plugin.DisplayName.put(p, D);
                 p.setPlayerListName(ChatColor.GRAY + D);
             }
             plugin.cLog.debug("----------");
         }
    }
}
