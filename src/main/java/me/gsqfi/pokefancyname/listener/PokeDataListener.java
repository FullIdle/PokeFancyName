package me.gsqfi.pokefancyname.listener;

import com.pixelmonmod.pixelmon.api.events.pokemon.SetNicknameEvent;
import me.fullidle.ficore.ficore.common.api.event.ForgeEvent;
import me.gsqfi.pokefancyname.PokemonData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PokeDataListener implements Listener {
    @EventHandler
    public void onForge(ForgeEvent event){
        if (event.getForgeEvent() instanceof SetNicknameEvent){
            SetNicknameEvent e = (SetNicknameEvent) event.getForgeEvent();
            PokemonData.removePokemonOName(e.pokemon.getUUID());
        }
    }
}
