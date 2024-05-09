package me.gsqfi.pokefancyname.listener;

import me.gsqfi.pokefancyname.PlayerData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.UUID;

public class PlayerDataListener implements Listener {
    /**
     * 需要监听的容器
     */

    @EventHandler
    public void joinS(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        cachePlayerData(player);
    }

    public static void  cachePlayerData(Player player){
        String playerName = player.getName();
        FileConfiguration config = PlayerData.config;
        ConfigurationSection section = config.getConfigurationSection(playerName);
        if (section == null) {
            return;
        }
        HashMap<UUID, String> value = new HashMap<>();
        for (String key : section.getKeys(false)) {
            value.put(UUID.fromString(key), section.getString(key));
        }
        PlayerData.map.put(playerName, value);
    }

    @EventHandler
    public void quitS(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        PlayerData.save(player);
        PlayerData.map.remove(player.getName());
    }
}
