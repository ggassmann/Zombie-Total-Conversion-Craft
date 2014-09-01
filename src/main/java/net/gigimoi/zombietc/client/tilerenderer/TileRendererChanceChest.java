package net.gigimoi.zombietc.client.tilerenderer;

import net.gigimoi.zombietc.ZombieTC;
import net.gigimoi.zombietc.tile.TileChanceChest;
import net.gigimoi.zombietc.util.GLHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by gigimoi on 8/31/2014.
 */
public class TileRendererChanceChest extends TileEntitySpecialRenderer {
    public static final ResourceLocation texture = new ResourceLocation(ZombieTC.MODID, "textures/Blocks/Chance Chest.png");
    public static final IModelCustom model = AdvancedModelLoader.loadModel(new ResourceLocation(ZombieTC.MODID, "models/chest.obj"));
    @Override
    public void renderTileEntityAt(TileEntity rawTile, double x, double y, double z, float par5) {
        TileChanceChest tile = (TileChanceChest) rawTile;
        if(tile.direction == -1) {
            return;
        }
        glPushMatrix();
        glTranslated(x, y, z);
        GLHelper.glRotateDirection(tile.direction);
        glScaled(0.5, 0.5, 0.5);
        glTranslated(0, 1, 0);
        bindTexture(texture);
        model.renderPart("ChestBody_Cube");
        glRotated(- 90, 1, 0, 0);
        model.renderPart("ChestTop_Cylinder");
        glPopMatrix();
    }
}
