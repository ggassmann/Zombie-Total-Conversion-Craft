package net.gigimoi.zombietc.client.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.gigimoi.zombietc.ZombieTC;
import net.gigimoi.zombietc.util.TextureHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by gigimoi on 8/1/2014.
 */
public class PlayerBuffRenderManager {
    private static final ResourceLocation textureRingOfHealth = new ResourceLocation(ZombieTC.MODID, "textures/misc/ringOfHealth.png");
    private static final IModelCustom model = AdvancedModelLoader.loadModel(new ResourceLocation(ZombieTC.MODID, "models/playerBuffShadow.obj"));

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onRenderPlayerEventPost(RenderLivingEvent.Post event) {
        if (event.entity != Minecraft.getMinecraft().thePlayer) {
            glPushMatrix();
            glEnable(GL_BLEND);
            glTranslated(event.x, event.y, event.z);
            if (EntityPlayer.class.isAssignableFrom(event.entity.getClass())) {
                /*IInventory baubles = BaublesApi.getBaubles((EntityPlayer) event.entity);
                if (baubles.getStackInSlot(2) != null) {*/
                    TextureHelper.bindTexture(textureRingOfHealth);
                    model.renderAll();
                //} TODO: Baubles replacement
            }
            glDisable(GL_BLEND);
            glPopMatrix();
        }
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onRenderWorldLastEvent(RenderWorldLastEvent event) {
        glPushMatrix();
        glTranslated(0, -1.6, 0);
        glEnable(GL_BLEND);
        //IInventory baubles = BaublesApi.getBaubles(Minecraft.getMinecraft().thePlayer);
        //if (baubles.getStackInSlot(2) != null) {
            TextureHelper.bindTexture(textureRingOfHealth);
            model.renderAll();
        //} TODO: Baubles replacement
        glDisable(GL_BLEND);
        glPopMatrix();
    }
}
