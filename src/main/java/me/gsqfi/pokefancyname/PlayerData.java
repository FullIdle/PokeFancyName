package me.gsqfi.pokefancyname;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.IStorageManager;
import com.pixelmonmod.pixelmon.api.storage.PCStorage;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import lombok.SneakyThrows;
import me.gsqfi.pokefancyname.listener.PlayerDataListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;

public class PlayerData {
    public static File file;
    /**
     * 新数据来自监听器,玩家加入服务器才尝试添加缓存
     * 只有占位符变量的宝可梦名会被存入player_data.yml
     */
    public static Map<String,Map<UUID,String>> map = new HashMap<>();
    public static FileConfiguration config;

    @SneakyThrows
    public static void init(){
        clear();
        //player_data.yml文件
        Main plugin = Main.getInstance();
        file = new File(plugin.getDataFolder(), "player_data.yml");
        if (!file.exists()) {
            file.createNewFile();
        }
        config = YamlConfiguration.loadConfiguration(file);
        //加载在线玩家
        for (Player p : Bukkit.getOnlinePlayers()) {
            PlayerDataListener.cachePlayerData(p);
        }
    }

    /**
     * 清楚缓存
     */
    private static void clear(){
        map.clear();
    }

    /**
     * 获取本插件因玩家赋予给宝可梦的占位符名
     */
    public static String getPlayerPokeOName(Player player, UUID uuid){
        Map<UUID, String> map1 = map.get(player.getName());
        if (map1 == null) {
            return null;
        }
        return map1.get(uuid);
    }

    /**
     * 给玩家指定UUID的宝可梦记录并设置占位符名
     * 这不会自动记录持久化数据中,需要你执行一次save方法才行
     * 非占位符名就只设置宝可梦名字,不会被记录
     */
    public static void setPlayerPokeOName(Player player,UUID uuid,String name){
        name = Main.replaceColorCode(name);
        Map<UUID, String> map1 = map.computeIfAbsent(player.getName(), k -> new HashMap<>());
        if (!isPapi(name)){
            //不是占位符名的情况下只修改宝可梦名
            Pokemon poke = getPlayerPokeWithUUID(player, uuid);
            poke.setNickname(name);
            map1.remove(uuid);
            return;
        }
        //添加到缓存内,有计时器去进行修改名字
        map1.put(uuid,name);
    }

    /**
     * 获取玩家指定uuid的宝可梦
     */
    public static Pokemon getPlayerPokeWithUUID(Player player,UUID uuid){
        IStorageManager manager = Pixelmon.storageManager;
        UUID playerUuid = player.getUniqueId();
        PlayerPartyStorage party = manager.getParty(playerUuid);
        int slot = party.getSlot(uuid);
        return slot == -1 ? getPCPokeWithUUID(manager.getPCForPlayer(playerUuid),uuid) : party.get(slot);
    }

    /**
     * 对PC内获取指定uuid的宝可梦
     */
    public static Pokemon getPCPokeWithUUID(PCStorage pc,UUID uuid){
        ArrayList<Pokemon> list = Lists.newArrayList(pc.getAll());
        list.removeIf(Objects::isNull);
        for (Pokemon pokemon : list) {
            if (pokemon.getUUID() == uuid) {
                return pokemon;
            }
        }
        return null;
    }

    /**
     * 持久化存储
     */
    @SneakyThrows
    public static void save(Player player){
        String playerName = player.getName();
        if (!map.containsKey(playerName)) return;
        Map<UUID, String> map1 = map.get(playerName);
        config.set(playerName,null);
        for (Map.Entry<UUID, String> entry : map1.entrySet()) {
            config.set(playerName+"."+ entry.getKey(),entry.getValue());
        }
        config.save(file);
    }

    /**
     * 保存所有玩家缓存数据
     */
    public static void saveAll(){
        for (Map.Entry<String, Map<UUID, String>> entry : map.entrySet()) {
            save(Bukkit.getPlayer(entry.getKey()));
        }
    }

    /**
     * 有两个%
     */
    public static boolean isPapi(String str){
        int i;
        if ((i = str.indexOf('%')) == -1) {
            return false;
        }
        return str.substring(i+1).contains("%");
    }
}
