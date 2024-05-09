package me.gsqfi.pokefancyname.commands;

import me.gsqfi.pokefancyname.common.ItemStackHelper;
import me.gsqfi.pokefancyname.fancy.renamecard.Card;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GiveCardCmd extends SubCmd{
    public GiveCardCmd(CMD mainCmd) {
        super(mainCmd, "givecard");
    }

    @Override
    public String getName() {
        return "givecard";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        int length = args.length;
        if (length > 0) {
            //获取玩家
            Player other = Bukkit.getPlayer(args[0]);
            if (other == null) {
                sender.sendMessage("玩家不存在!");
                return false;
            }


            //只有玩家参数时
            if (length == 1){
                Card.givePlayerRenameCard(other,1);
                sender.sendMessage("给与玩家 "+ Card.itemStack.getItemMeta().getDisplayName() +" *1");
                return false;
            }


            //大于0且不等于1那就是大于等于2 有玩家和数量参数
            if (!RenameCmd.isInteger(args[1])){
                sender.sendMessage("非法参数,非数字!");
                return false;
            }
            int amount = Integer.parseInt(args[1]);
            Card.givePlayerRenameCard(other,amount);
            sender.sendMessage("给与玩家 "+ Card.itemStack.getItemMeta().getDisplayName() +" *"+amount);
            return false;
        }
        getSuperCmd().sub.get("help").onCommand(sender, cmd, label, args);
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        int length = args.length;
        if (length == 1) return null;
        if (length > 1) return IntStream.range(0,65).boxed().map(String::valueOf).collect(Collectors.toList());
        return Collections.emptyList();
    }
}
