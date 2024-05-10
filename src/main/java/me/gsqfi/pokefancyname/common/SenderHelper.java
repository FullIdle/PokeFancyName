package me.gsqfi.pokefancyname.common;

import me.gsqfi.pokefancyname.Main;
import org.bukkit.command.CommandSender;

import java.util.function.Function;

public class SenderHelper {
    /**
     * 发送config.yml内指定的path信息
     */
    public static void sendConfigMsg(CommandSender sender, String path, Function<String,String> function){
        String msg = Main.getInstance().getConfig().getString(path);
        sender.sendMessage(function.apply(msg));
    }
}
