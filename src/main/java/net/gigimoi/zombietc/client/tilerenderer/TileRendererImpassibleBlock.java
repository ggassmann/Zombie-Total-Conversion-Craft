package net.gigimoi.zombietc.client.tilerenderer;

import net.gigimoi.zombietc.ZombieTC;
import net.gigimoi.zombietc.util.TextureHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Gavin on 11/15/2014.
 */
public class TileRendererImpassibleBlock extends TileEntitySpecialRenderer {
    public static ResourceLocation texture = new ResourceLocation(ZombieTC.MODID, "textures/blocks/Node.png");
    public static IModelCustom model = AdvancedModelLoader.loadModel(new ResourceLocation(ZombieTC.MODID, "models/block.obj"));
    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float par5) {
        if (ZombieTC.editorModeManager.enabled) {
            glPushMatrix();
            glTranslated(x + 0.5, y + 0.5, z + 0.5);
            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            TextureHelper.bindTexture(texture);
            glColor4d(1, 1, 1, 0.5);
            model.renderAll();
            glPopMatrix();
        }
    }
}
