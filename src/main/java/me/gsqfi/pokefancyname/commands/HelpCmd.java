package me.gsqfi.pokefancyname.commands;

import me.gsqfi.pokefancyname.Main;
import me.gsqfi.pokefancyname.common.StringHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class HelpCmd extends SubCmd {
    @Override
    public String getName() {
        return "help";
    }

    public HelpCmd(CMD mainCmd) {
        super(mainCmd,"help");
        help = StringHelper.replaceColorCode(Main.getInstance().getConfig().getStringList("msg.help")).toArray(new String[0]);
    }

    public final String[] help;

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        commandSender.sendMessage(help);
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return Collections.emptyList();
    }
}
