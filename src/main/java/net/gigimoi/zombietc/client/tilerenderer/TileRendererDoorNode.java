package net.gigimoi.zombietc.client.tilerenderer;

import net.gigimoi.zombietc.ZombieTC;
import net.gigimoi.zombietc.tile.TileNodeDoor;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by gigimoi on 8/6/2014.
 */
public class TileRendererDoorNode extends TileEntitySpecialRenderer {
    public static IModelCustom model = AdvancedModelLoader.loadModel(new ResourceLocation(ZombieTC.MODID, "models/door.obj"));
    public static ResourceLocation texture = new ResourceLocation(ZombieTC.MODID, "textures/blocks/Node Door.png");

    @Override
    public void renderTileEntityAt(TileEntity tileRaw, double x, double y, double z, float par5) {
        glPushMatrix();
        bindTexture(texture);
        GL11.glTranslated(x, y, z);
        TileNodeDoor tile = (TileNodeDoor) tileRaw;
        if (tile.direction == 0) {
            GL11.glTranslated(0.5, 0, 0.5);
            GL11.glRotated(180, 0, 1, 0);
        } else if (tile.direction == 2) {
            GL11.glTranslated(0.5, 0, 0.5);
        } else if (tile.direction == 3) {
            GL11.glRotated(90, 0, 1, 0);
            GL11.glTranslated(-0.5, 0, 0.5);
            GL11.glRotated(180, 0, 1, 0);
        } else {
            GL11.glRotated(90, 0, 1, 0);
            GL11.glTranslated(-0.5, 0, 0.5);
        }
        GL11.glTranslated(0, tile.animationTime / 49f, 0);
        GL11.glColor3d(1, 1, 1);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        model.renderAll();
        glPopMatrix();

        renderNode(tileRaw, x, y, z);
    }

    private void renderNode(TileEntity tileRaw, double x, double y, double z) {
        bindTexture(TileRendererNode.texture);
        TileRendererNode.renderModel(tileRaw, x, y, z);
    }
}