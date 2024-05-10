package me.gsqfi.pokefancyname.listener;

import com.pixelmonmod.pixelmon.api.events.dialogue.DialogueInputEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import me.fullidle.ficore.ficore.common.api.event.ForgeEvent;
import me.gsqfi.pokefancyname.Main;
import me.gsqfi.pokefancyname.PokemonData;
import me.gsqfi.pokefancyname.common.SenderHelper;
import me.gsqfi.pokefancyname.common.StringHelper;
import me.gsqfi.pokefancyname.fancy.renamecard.Card;
import net.minecraft.entity.player.EntityPlayerMP;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CardListener implements Listener {
    public static Map<UUID, UUID> inputData = new HashMap<>();


    @EventHandler
    public void onChat(AsyncPlayerChatEvent e){
        if (Main.getInstance().getConfig().getBoolean("usePokeInput")) {
            return;
        }
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        if (inputData.containsKey(uuid)){
            e.setCancelled(true);
            String message = e.getMessage();
            PokemonData.setPokemonOName(inputData.get(uuid), message,player);
            inputData.remove(uuid);
            SenderHelper.sendConfigMsg(player, "msg.rename_success", string ->
                    StringHelper.replaceColorCode(string.replace("{name}",message)));
        }
    }
    @EventHandler
    public void onForge(ForgeEvent event){
        if (event.getForgeEvent() instanceof DialogueInputEvent.Submitted) {
            if (!Main.getInstance().getConfig().getBoolean("usePokeInput")) {
                return;
            }
            DialogueInputEvent.Submitted e = (DialogueInputEvent.Submitted) event.getForgeEvent();
            Player player = e.getPlayer().getBukkitEntity().getPlayer();
            UUID uuid = player.getUniqueId();
            if (inputData.containsKey(uuid)){
                String input = e.getInput();
                PokemonData.setPokemonOName(inputData.get(uuid), input,player);
                inputData.remove(uuid);
                SenderHelper.sendConfigMsg(player, "msg.rename_success", string ->
                        StringHelper.replaceColorCode(string.replace("{name}",input)));            }
        }
    }
    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent e){
        Entity entity = e.getRightClicked();
        net.minecraft.entity.Entity handle = (net.minecraft.entity.Entity) ((Object) ((CraftEntity) entity).getHandle());
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
        if (!(handle instanceof EntityPixelmon)|| !Card.isCard(itemInMainHand)) {
            return;
        }
        e.setCancelled(true);
        if (e.getHand().equals(EquipmentSlot.OFF_HAND)) return;
        if (inputData.containsKey(uuid)) {
            SenderHelper.sendConfigMsg(player,"msg.already_used_rename_card",StringHelper::replaceColorCode);
            return;
        }
        EntityPixelmon ep = (EntityPixelmon) handle;
        if (!ep.hasOwner()) {
            return;
        }
        Pokemon pokemon = ep.getPokemonData();
        EntityPlayerMP mp = pokemon.getOwnerPlayer();
        if (mp.getBukkitEntity().getPlayer() != e.getPlayer()) {
            return;
        }
        itemInMainHand.setAmount(itemInMainHand.getAmount()-1);
        player.getInventory().setItemInMainHand(itemInMainHand);

        inputData.put(uuid, pokemon.getUUID());
        SenderHelper.sendConfigMsg(player,"msg.use_rename_card",StringHelper::replaceColorCode);
    }
    @EventHandler
    public void quitGiveBack(PlayerQuitEvent e){
        Player player = e.getPlayer();
        if (inputData.containsKey(player.getUniqueId())){
            Card.givePlayerRenameCard(player,1);
            inputData.remove(player.getUniqueId());
        }
    }
}
