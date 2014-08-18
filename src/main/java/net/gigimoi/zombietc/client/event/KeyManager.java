package net.gigimoi.zombietc.client.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.gigimoi.zombietc.ZombieTC;
import net.gigimoi.zombietc.client.ClientProxy;

/**
 * Created by gigimoi on 7/22/2014.
 */
public class KeyManager {
    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onTick(TickEvent event) {
        if (event.phase == TickEvent.Phase.END && event.side.isClient()) {
            if (ClientProxy.activate.isPressed()) {
                ZombieTC.gameManager.activating = true;
            } else {
                ZombieTC.gameManager.activating = false;
            }
        }
    }
}
