package net.gigimoi.zombietc.client.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.gigimoi.zombietc.ZombieTC;
import net.minecraftforge.client.event.EntityViewRenderEvent;

/**
 * Created by gigimoi on 7/30/2014.
 */
public class FogManager {
    @SubscribeEvent
    public void onFogDensity(EntityViewRenderEvent.FogDensity event) {
        event.setCanceled(true);
        event.density = ZombieTC.editorModeManager.enabled ? 0.0001f : 0.03f;
    }

    @SubscribeEvent
    public void onFogColor(EntityViewRenderEvent.FogColors event) {
        if (!ZombieTC.editorModeManager.enabled) {
            event.red = 0.25f;
            event.blue = 0.25f;
            event.green = 0.25f;
        }
    }
}
