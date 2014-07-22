package net.gigimoi.zombietc.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.gigimoi.zombietc.ZombieTC;
import net.gigimoi.zombietc.event.GameManager;
import net.gigimoi.zombietc.proxy.ClientProxy;
import net.minecraft.server.MinecraftServer;

/**
 * Created by gigimoi on 7/22/2014.
 */
public class KeyManager {
    @SubscribeEvent
    public void onTick(TickEvent event) {
        if(event.phase == TickEvent.Phase.END && event.side.isClient()) {
            if(ClientProxy.activate.isPressed()) {
                ZombieTC.gameManager.activating = true;
            } else {
                ZombieTC.gameManager.activating = false;
            }
        }
    }
}
