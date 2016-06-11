/*
 * The MIT License
 *
 * Copyright 2016 kazu0617<kazuyagi19990617@hotmail.co.jp>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package net.kazu0617.afkme;

import java.io.IOException;
import java.util.Collection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;

/**
 *
 * @author kazu0617<kazuyagi19990617@hotmail.co.jp>
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
             plugin.cLog.debug("player: " + p);
             int count;
             //現在のLocationと保存されているLocationの比較.もし同じならAFKをtrueに変更.
             /*if (!plugin.Location.containsKey(p)) {
                 plugin.Location.put(p, L);
                 continue;
             }*/
             Location CL = plugin.Location.get(p); //ココで何故かエラー吐く…
             plugin.cLog.debug("LoadLocation: " + CL);
             plugin.cLog.debug("Location: " + L);
             plugin.cLog.debug("LoadLocation: " + CL);
             plugin.Location.put(p, L);
             if (L == CL) AFK = true;
             plugin.cLog.debug("AFK = " + AFK);
             if(AFK) { //もしAFKがTrueなら,Countの加算.
                 count = plugin.afkcount.get(p);
                 plugin.cLog.debug("count++ = " + count);
                 count++;
                 plugin.cLog.debug("count++ = " + count);
                 plugin.afkcount.put(p, count);
             }
             else if (!AFK) {//もしAFKがFalseなら,Countの初期化.
                 count = plugin.afkcount.get(p);
                 if (count >= afktime) {//もしafktimeよりもcountが大きい時は,離席モードの解除.
                     if (!plugin.DebugMode) {
                         Bukkit.dispatchCommand(p, "me is no longer afk");
                     } else if (plugin.DebugMode) {
                         plugin.cLog.debug("オンライン: " + p);
                     }
                     p.setDisplayName(plugin.DisplayName.get(p));
                 }
                 plugin.afkcount.put(p, 0);
             }
             if (plugin.afkcount.get(p) == afktime) {//countとafktimeが同じであれば,離席モードへ入る.
                 String D = p.getDisplayName();
                 if (!plugin.DebugMode) Bukkit.dispatchCommand(p, "me is afk");
                 if (!plugin.DebugMode) plugin.cLog.debug("離席: " + p);
                 plugin.DisplayName.put(p, D);
                 p.setDisplayName(ChatColor.GRAY + D);
             }
             plugin.cLog.debug("----------");
         }
    }
}
