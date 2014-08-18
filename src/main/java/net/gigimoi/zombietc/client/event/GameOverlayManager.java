package net.gigimoi.zombietc.client.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.gigimoi.zombietc.ZombieTC;
import net.gigimoi.zombietc.item.weapon.ItemWeapon;
import net.gigimoi.zombietc.util.Lang;
import net.gigimoi.zombietc.util.TextAlignment;
import net.gigimoi.zombietc.util.TextRenderHelper;
import net.gigimoi.zombietc.util.TextureHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import java.util.Random;

import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glScaled;

/**
 * Created by gigimoi on 8/16/2014.
 */
public class GameOverlayManager {
    static Random _r = new Random();
    private int activateMessageDuration = 0;
    private String activateMessage;
    public void setActivateMessage(String message) {
        activateMessageDuration = 10;
        activateMessage = message;
    }
    @SuppressWarnings("unused")
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onRenderGameOverlayEvent(RenderGameOverlayEvent.Pre event) {
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
        if (event.type == RenderGameOverlayEvent.ElementType.HOTBAR) {
            if (!ZombieTC.editorModeManager.enabled) {
                //event.setCanceled(true);
                //TODO: Render weapons like a real FPS
            }
        }
    }
    private void renderItemStack(ItemStack stack) {
        if (stack != null && ItemWeapon.class.isAssignableFrom(stack.getItem().getClass())) {
            ItemWeapon stack1weapon = (ItemWeapon) stack.getItem();
            if(stack1weapon.modelGun != null) {
                glPushMatrix();
                glScaled(stack1weapon.inventoryScale, stack1weapon.inventoryScale, stack1weapon.inventoryScale);
                TextureHelper.bindTexture(new ResourceLocation(ZombieTC.MODID, "textures/items/guns/" + stack.getUnlocalizedName().substring(5) + ".png"));
                stack1weapon.modelGun.renderAll();
                glPopMatrix();
            }
        }
    }
}
