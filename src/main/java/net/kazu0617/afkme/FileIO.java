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

import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 *
 * @author kazu0617<kazuyagi19990617@hotmail.co.jp>
 */
class FileIO {
     Main plugin;
     public FileIO(Main instance)
     {
         this.plugin = instance;
     }
     public int getTime() {
         return TimeIO(false,0);
     }
    public int TimeIO(boolean Save, int count) { 
        File file = new File(plugin.folder + "AFK.yml");
        FileConfiguration yaml = new YamlConfiguration();
        try {
            yaml.load(file);
        } catch (IOException | InvalidConfigurationException ex) {
            plugin.cLog.info("該当するファイルがありませんでした。新規作成します");
            yaml.set("time", 300);
            SettingFiles(yaml, file, true);
        }
        if(Save==true)
        {
            yaml.set("time", count);
            SettingFiles(yaml, file, true);
            return count;
        }
        else if(Save==false)
        {
            int afktime = yaml.getInt("time");
            if (afktime<10){
                afktime = 300;
                yaml.set("time",afktime);
            }
            return afktime;
        }
        return -1;
    }  
    /**
     * ファイルの保存
     *
     * 新規作成だけで、保存しない場合はsaveをfalseに
     * 保存&新規作成の場合はtrueでもfalseでも
     * 上書きの時は必ずtrueに
     *
     * @param fileconfiguration ファイルコンフィグを指定
     * @param file ファイル指定
     * @param save 上書きをするかリセットするか(trueで上書き)
     */
    public void SettingFiles(FileConfiguration fileconfiguration, File file, boolean save)
    {
        if(!file.exists() || save)
        {
            try {
                fileconfiguration.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
