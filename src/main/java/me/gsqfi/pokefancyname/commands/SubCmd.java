package me.gsqfi.pokefancyname.commands;

import lombok.Getter;
import org.bukkit.command.TabExecutor;

@Getter
public abstract class SubCmd implements TabExecutor{
    abstract public String getName();
    private final CMD superCmd;
    public SubCmd(CMD mainCmd,String name){
        mainCmd.sub.put(name,this);
        this.superCmd = mainCmd;
    }
}
