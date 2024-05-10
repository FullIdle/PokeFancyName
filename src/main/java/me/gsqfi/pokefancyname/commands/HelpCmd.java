package me.gsqfi.pokefancyname.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HelpCmd extends SubCmd {
    @Override
    public String getName() {
        return "help";
    }

    public HelpCmd(CMD mainCmd) {
        super(mainCmd,"help");
    }

    public static String[] help = new String[]{
            "HELP========",
            "/pfname rename [player] [slot] [name...] 直接修改名字",
            "/pfname reload 重载配置",
            "/pfname help 帮助",
            "/pfname givecard [1-64] 给与改名卡"
    };

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
