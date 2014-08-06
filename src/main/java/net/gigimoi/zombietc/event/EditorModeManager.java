package net.gigimoi.zombietc.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import net.gigimoi.zombietc.ZombieTC;
import net.gigimoi.zombietc.net.MessageChangeEditorMode;
import net.gigimoi.zombietc.client.ClientProxy;
import net.minecraftforge.event.entity.player.PlayerEvent;

/**
 * Created by gigimoi on 7/15/2014.
 */
public class EditorModeManager {
    public boolean enabled = false;

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (((ClientProxy) ZombieTC.proxy).reset.isPressed()) {
            ZombieTC.network.sendToServer(new MessageChangeEditorMode(!ZombieTC.editorModeManager.enabled));
        }
    }

    @SubscribeEvent
    public void getBreakSpeed(PlayerEvent.BreakSpeed event) {
        if (!enabled) {
            event.setCanceled(true);
        }
    }

}
