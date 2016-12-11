package net.kazu0617.afkme;

import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * @author     kazu0617
 * @license    MIT
 * @copyright  Copyright kazu0617 2015
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
     * ことむー氏作成。
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
