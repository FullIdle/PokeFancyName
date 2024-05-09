package me.gsqfi.pokefancyname;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import me.clip.placeholderapi.PlaceholderAPI;
import me.gsqfi.pokefancyname.commands.CMD;
import me.gsqfi.pokefancyname.fancy.renamecard.Card;
import me.gsqfi.pokefancyname.listener.CardListener;
import me.gsqfi.pokefancyname.listener.PlayerDataListener;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class Main extends JavaPlugin {
    private static Main plugin;
    private static BukkitRunnable timerRunnable;

    @Override
    public void onLoad() {
        plugin = this;
    }

    @Override
    public void onEnable() {
        reloadConfig();

        CMD cmd = new CMD();
        PluginCommand command = getCommand(this.getDescription().getName().toLowerCase());
        command.setExecutor(cmd);
        command.setTabCompleter(cmd);

        getServer().getPluginManager().registerEvents(new CardListener(),this);
        getServer().getPluginManager().registerEvents(new PlayerDataListener(),this);

        getLogger().info("§a插件已启用!");
    }

    @Override
    public void onDisable() {
        PlayerData.saveAll();
        PlayerData.map.clear();
    }

    @Override
    public void reloadConfig() {
        saveDefaultConfig();
        super.reloadConfig();
        PlayerData.init();
        //计时器任务
        if (timerRunnable != null) timerRunnable.cancel();
        timerRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<String, Map<UUID, String>> entry : Lists.newArrayList(PlayerData.map.entrySet())) {
                    Player player = Bukkit.getPlayer(entry.getKey());
                    if (player == null) {
                        PlayerData.map.remove(entry.getKey());
                        return;
                    }
                    PlayerPartyStorage party = Pixelmon.storageManager.getParty(player.getUniqueId());
                    for (Map.Entry<UUID, String> entry1 : entry.getValue().entrySet()) {
                        //精灵在背包内不进行处理
                        int slot;
                        if ((slot = party.getSlot(entry1.getKey())) == -1) {
                            continue;
                        }
                        Pokemon pokemon;
                        if ((pokemon = party.get(slot)) != null) pokemon.setNickname(PlaceholderAPI.setPlaceholders(player,entry1.getValue()));
                    }
                }
            }
        };
        timerRunnable.runTaskTimerAsynchronously(this,0,getConfig().getInt("updateTick"));


        //Other
        Card.init();
    }

    public static Main getInstance() {
        return plugin;
    }

    /**
     * 颜色代码替换
     */
    public static String replaceColorCode(String str){
        return str.replace('&','§');
    }

    public static List<String> replaceColorCode(List<String> list){
        return list.stream().map(Main::replaceColorCode).collect(Collectors.toList());
    }
}