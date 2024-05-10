package me.gsqfi.pokefancyname;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.IStorageManager;
import com.pixelmonmod.pixelmon.api.storage.PCStorage;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import lombok.SneakyThrows;
import me.gsqfi.pokefancyname.common.EssTypeHelper;
import me.gsqfi.pokefancyname.common.StringHelper;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;

public class PokemonData {
    public static File file;
    public static FileConfiguration config;

    @SneakyThrows
    public static void init() {
        clear();

        Main plugin = Main.getInstance();
        {//文件和配置
            file = new File(plugin.getDataFolder(), "pokemon_data.yml");
            if (!file.exists()) {
                if (!file.getParentFile().exists()) {
                    file.mkdirs();
                }
                file.createNewFile();
            }
            config = YamlConfiguration.loadConfiguration(file);
        }
    }

    public static void clear(){

    }

    @SneakyThrows
    public static void save(){
        config.save(file);
    }


    /**
     * 通过uuid来设置宝可梦的名字
     * 如果name不是占位符名的话则会尝试在players对象中搜出宝可梦并设置name
     *
     * @param players 非占位符名搜索对象
     */
    @SneakyThrows
    public static void setPokemonOName(UUID uuid, String name, Player... players) {
        name = StringHelper.replaceColorCode(name);
        if (EssTypeHelper.isPapi(name)) {
            config.set(String.valueOf(uuid),name);
            config.save(file);
            return;
        }
        for (Player player : players) {
            Pokemon poke;
            if ((poke = getPlayerPokeWithUUID(player, uuid)) == null) {
                continue;
            }
            poke.setNickname(name);
        }
    }

    /**
     * 从指定的数个玩家内搜索宝可梦名字
     */
    public static Map<Player,String> getPokemonOName(UUID uuid, Player... players) {
        HashMap<Player, String> map = new HashMap<>();
        for (Player player : players) {
            Pokemon poke;
            if ((poke = getPlayerPokeWithUUID(player,uuid)) == null) {
                continue;
            }
            String path = String.valueOf(poke.getUUID());
            map.put(player,config.contains(path)?config.getString(path):poke.getNickname());
        }
        return map;
    }

    /**
     * 获取玩家指定uuid的宝可梦
     */
    public static Pokemon getPlayerPokeWithUUID(Player player, UUID uuid){
        IStorageManager manager = Pixelmon.storageManager;
        UUID playerUuid = player.getUniqueId();
        PlayerPartyStorage party = manager.getParty(playerUuid);
        int slot = party.getSlot(uuid);
        return slot == -1 ? getPCPokeWithUUID(manager.getPCForPlayer(playerUuid),uuid) : party.get(slot);
    }

    /**
     * 对PC内获取指定uuid的宝可梦
     */
    public static Pokemon getPCPokeWithUUID(PCStorage pc, UUID uuid){
        ArrayList<Pokemon> list = Lists.newArrayList(pc.getAll());
        list.removeIf(Objects::isNull);
        for (Pokemon pokemon : list) {
            if (pokemon.getUUID() == uuid) {
                return pokemon;
            }
        }
        return null;
    }
}
