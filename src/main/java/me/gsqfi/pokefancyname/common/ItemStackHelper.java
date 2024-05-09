package me.gsqfi.pokefancyname.common;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemStackHelper {
    /**
     * 一种我也不确定是否安全的方式给玩家物品
     */
    public static void givePlayerItemStack(Player player, ItemStack itemStack){
        Location location = player.getLocation();
        World world = location.getWorld();
        location.setY(location.getY() + 0.5);
        Item item = world.dropItem(location, itemStack);
        item.setGlowing(true);
        item.setPickupDelay(0);
        item.setGravity(false);
        item.setTicksLived(Integer.MAX_VALUE);
        item.setInvulnerable(true);
        item.setFireTicks(Integer.MAX_VALUE);
    }
}
