package me.gsqfi.pokefancyname.commands;

import com.google.common.collect.Lists;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.*;
import java.util.stream.Collectors;

public class CMD implements TabExecutor {
    public final Map<String, SubCmd> sub = new HashMap<>();
    public final ArrayList<String> subCmd = new ArrayList<>();

    public CMD(){
        new HelpCmd(this);
        new ReloadCmd(this);
        new RenameCmd(this);
        new GiveCardCmd(this);
        subCmd.addAll(sub.keySet());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage("你不是op!");
            return false;
        }

        if (args.length > 0){
            String lowerCase = args[0].toLowerCase();
            if (subCmd.contains(lowerCase)){
                ArrayList<String> list = Lists.newArrayList(args);
                list.remove(0);
                return sub.get(lowerCase).onCommand(sender, command, label, list.toArray(new String[0]));
            }
        }
        sub.get("help").onCommand(sender, command, label, args);
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) return subCmd;
        if (args.length == 1) return subCmd.stream().filter(s -> s.startsWith(args[0])).
                collect(Collectors.toList());
        String lowerCase = args[0].toLowerCase();
        if (Lists.newArrayList(subCmd).contains(lowerCase)){
            ArrayList<String> list = Lists.newArrayList(args);
            list.remove(0);
            return sub.get(lowerCase).onTabComplete(sender, command, label, list.toArray(new String[0]));
        }
        return Collections.emptyList();
    }
}
