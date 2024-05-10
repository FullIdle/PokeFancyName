package me.gsqfi.pokefancyname;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import me.clip.placeholderapi.PlaceholderAPI;
import me.gsqfi.pokefancyname.commands.CMD;
import me.gsqfi.pokefancyname.fancy.renamecard.Card;
import me.gsqfi.pokefancyname.listener.CardListener;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

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

        getLogger().info("§a插件已启用!");
    }

    @Override
    public void onDisable() {
        PokemonData.save();
    }

    @Override
    public void reloadConfig() {
        saveDefaultConfig();
        super.reloadConfig();
        //PokemonData
        PokemonData.init();
        //计时器任务
        if (timerRunnable != null) timerRunnable.cancel();
        timerRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    for (Pokemon pokemon : Pixelmon.storageManager.getParty(player.getUniqueId()).getAll()) {
                        if (pokemon == null) continue;
                        String path = String.valueOf(pokemon.getUUID());
                        if (PokemonData.config.contains(path))
                            pokemon.setNickname(PlaceholderAPI.setPlaceholders(player,PokemonData.config.getString(path)));
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

}