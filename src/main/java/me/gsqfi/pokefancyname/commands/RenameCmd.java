package me.gsqfi.pokefancyname.commands;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import me.gsqfi.pokefancyname.PokemonData;
import me.gsqfi.pokefancyname.common.EssTypeHelper;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RenameCmd extends SubCmd {
    public RenameCmd(CMD mainCmd) {
        super(mainCmd, "rename");
    }

    @Override
    public String getName() {
        return "rename";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        int argLength = args.length;
        if (argLength > 1) {
            //直接通过指令设置宝可梦名字
            //玩家是否在线
            Player other = Bukkit.getPlayer(args[0]);
            if (other == null) {
                sender.sendMessage("玩家不在线!");
                return false;
            }

            //获取数字
            if (!EssTypeHelper.isInteger(args[1])) {
                sender.sendMessage("非法参数,非数字!");
                return false;
            }
            int slot = Integer.parseInt(args[1]);


            //获取总名字
            StringBuilder builder = new StringBuilder();
            for (int i = 2; i < argLength; i++) {
                builder.append(args[i]);
                if (i != argLength - 1) {
                    builder.append(" ");
                }
            }
            //设置名字
            String name = builder.toString();
            setPokeName(other,slot,name);
            sender.sendMessage("已尝试设置玩家:"+other.getName()+"背包中第"+(slot+1)+"只精灵的名字为"+ name);
            return false;
        }
        getSuperCmd().sub.get("help").onCommand(sender, command, label, args);
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 2) return IntStream.range(0,6).boxed().map(String::valueOf).collect(Collectors.toList());
        return null;
    }

    /**
     * 通过背包位置获取宝可梦并设置宝可梦名字
     */
    public static void setPokeName(Player player, int slot, String name) {
        Pokemon poke = Pixelmon.storageManager.getParty(player.getUniqueId()).get(slot);
        assert poke != null;
        PokemonData.setPokemonOName(poke.getUUID(), name);
    }
}
