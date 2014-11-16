package net.gigimoi.zombietc.client.tilerenderer;

import info.jbcs.minecraft.chisel.Chisel;
import net.gigimoi.zombietc.ZombieTC;
import net.gigimoi.zombietc.tile.TileNodeDoor;
import net.gigimoi.zombietc.util.GLHelper;
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
    public static ResourceLocation[] textures = new ResourceLocation[] {
            new ResourceLocation(ZombieTC.MODID, "textures/blocks/doors/lightWood.png"),
            new ResourceLocation(ZombieTC.MODID, "textures/blocks/doors/storage0.png"),
            new ResourceLocation(ZombieTC.MODID, "textures/blocks/doors/storage1.png"),
            new ResourceLocation(ZombieTC.MODID, "textures/blocks/doors/storage2.png"),
            new ResourceLocation(Chisel.MOD_ID, "textures/blocks/factory/circuit.png"),
            new ResourceLocation(Chisel.MOD_ID, "textures/blocks/factory/goldplate.png"),
            new ResourceLocation(Chisel.MOD_ID, "textures/blocks/factory/grinder.png"),
            new ResourceLocation(Chisel.MOD_ID, "textures/blocks/factory/platex.png")
    };

    @Override
    public void renderTileEntityAt(TileEntity tileRaw, double x, double y, double z, float par5) {
        glPushMatrix();
        GL11.glTranslated(x, y, z);
        TileNodeDoor tile = (TileNodeDoor) tileRaw;
        bindTexture(textures[tile.textureID]);
        GLHelper.glRotateDirection(tile.direction);
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