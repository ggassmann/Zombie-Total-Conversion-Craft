package net.gigimoi.zombietc.client.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.gigimoi.zombietc.ZombieTC;
import net.gigimoi.zombietc.item.weapon.ItemWeapon;
import net.gigimoi.zombietc.util.Lang;
import net.gigimoi.zombietc.util.TextAlignment;
import net.gigimoi.zombietc.util.TextRenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

/**
 * Created by gigimoi on 8/16/2014.
 */
public class GameOverlayManager {
    private int activateMessageDuration = 0;
    private String activateMessage;
    public void setActivateMessage(String message) {
        activateMessageDuration = 10;
        activateMessage = message;
    }
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onRenderGameOverlayEvent(RenderGameOverlayEvent event) {
        if (event.type == RenderGameOverlayEvent.ElementType.CROSSHAIRS && !ZombieTC.editorModeManager.enabled) {
            event.setCanceled(true);
            return;
        }
        if (event.type == RenderGameOverlayEvent.ElementType.FOOD) {
            event.setCanceled(true);
            return;
        }
        if (event.type == RenderGameOverlayEvent.ElementType.CHAT) {
            if (ZombieTC.editorModeManager.enabled) {
                TextRenderHelper.drawString(Lang.get("ui.overlay.editorModeEnabled"), 2, 2, TextAlignment.Left);
            }
            TextRenderHelper.drawString(Lang.get("ui.wave") + ": " + ZombieTC.gameManager.wave, 2, event.resolution.getScaledHeight() - 10, TextAlignment.Left);
            TextRenderHelper.drawString(Lang.get("ui.overlay.zombiesLeft") + ": " + (ZombieTC.gameManager.zombiesToSpawn + ZombieTC.gameManager.zombiesAlive), 2, event.resolution.getScaledHeight() - 20, TextAlignment.Left);
            ItemStack heldItem = ZombieTC.proxy.getPlayer().getHeldItem();
            if (heldItem != null && heldItem.getItem().getClass() == ItemWeapon.class) {
                ((ItemWeapon) heldItem.getItem()).drawUIFor(heldItem, event);
            }
            if (activateMessageDuration > 0) {
                TextRenderHelper.drawString(activateMessage, event.resolution.getScaledWidth() / 2, event.resolution.getScaledHeight() - 90, TextAlignment.Center);
                activateMessageDuration--;
            }
        }
    }
}
