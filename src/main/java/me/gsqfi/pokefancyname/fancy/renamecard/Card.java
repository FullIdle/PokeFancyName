package me.gsqfi.pokefancyname.fancy.renamecard;

import me.gsqfi.pokefancyname.Main;
import me.gsqfi.pokefancyname.commands.RenameCmd;
import me.gsqfi.pokefancyname.common.ItemStackHelper;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class Card {
    public static ItemStack itemStack;

    /**
     * 判断物品是否是改名卡
     */
    public static boolean isCard(ItemStack item) {
        net.minecraft.server.v1_12_R1.ItemStack nmsCopy = CraftItemStack.asNMSCopy(item);
        if (nmsCopy.getTag() == null) {
            return false;
        }
        return nmsCopy.getTag().hasKey(Card.class.getName());
    }

    public static void init() {
        Main plugin = Main.getInstance();

        clear();

        {//加载物品
            Pair<Material, Short> maPair = getMaterialAndDurability(plugin.getConfig().getString("card.material"));
            itemStack = new ItemStack(maPair.getKey(),1,maPair.getValue());
            String name = plugin.getConfig().getString("card.name");
            List<String> list = plugin.getConfig().getStringList("card.lore");
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(Main.replaceColorCode(name));
            itemMeta.setLore(Main.replaceColorCode(list));
            itemStack.setItemMeta(itemMeta);
            net.minecraft.server.v1_12_R1.ItemStack nmsCopy = CraftItemStack.asNMSCopy(itemStack);
            NBTTagCompound tag = nmsCopy.getTag() == null?new NBTTagCompound():nmsCopy.getTag();
            tag.setByte(Card.class.getName(), (byte) 0);
            nmsCopy.setTag(tag);
            itemStack = CraftItemStack.asBukkitCopy(nmsCopy);
        }
    }

    public static void clear() {

    }

    /**
     * 材质和短数据 一对
     */
    public static Pair<Material, Short> getMaterialAndDurability(String str) {
        Main plugin = Main.getInstance();
        String materialData = plugin.getConfig().getString("card.material");
        if (!materialData.contains(":")) {
            Material material = !RenameCmd.isInteger(materialData) ? Material.getMaterial(str) :
                    Material.getMaterial(Integer.parseInt(str));
            return Pair.of(material, (short) 0);
        }
        String[] split = materialData.split(":");
        int i = Integer.parseInt(split[0]);
        int i1 = Integer.parseInt(split[1]);
        return Pair.of(Material.getMaterial(i), (short) i1);
    }

    public static void givePlayerRenameCard(Player player,int amount){
        ItemStack clone = itemStack.clone();
        clone.setAmount(amount);
        ItemStackHelper.givePlayerItemStack(player,clone);
    }
}
