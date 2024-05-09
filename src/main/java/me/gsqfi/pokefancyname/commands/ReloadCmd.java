package me.gsqfi.pokefancyname.commands;

import me.gsqfi.pokefancyname.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class ReloadCmd extends SubCmd{
    public ReloadCmd(CMD mainCmd) {
        super(mainCmd,"reload");
    }

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public boolean onCommand(CommandSender sender,Command command,String label,String[] args) {
        Main.getInstance().reloadConfig();
        sender.sendMessage("§aPlugin Reloaded!");
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(CommandSender sender,Command command,String label,String[] args) {
        return Collections.emptyList();
    }
}
