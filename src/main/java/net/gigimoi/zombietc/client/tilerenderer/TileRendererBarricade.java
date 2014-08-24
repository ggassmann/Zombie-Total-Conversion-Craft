package net.gigimoi.zombietc.client.tilerenderer;

import net.gigimoi.zombietc.ZombieTC;
import net.gigimoi.zombietc.block.BlockBarricade;
import net.gigimoi.zombietc.tile.TileBarricade;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.common.util.ForgeDirection;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by gigimoi on 8/24/2014.
 */
public class TileRendererBarricade extends TileEntitySpecialRenderer {
    public static IModelCustom model = AdvancedModelLoader.loadModel(new ResourceLocation(ZombieTC.MODID, "models/Wooden Barricade.obj"));
    public static ResourceLocation texture = new ResourceLocation(ZombieTC.MODID, "textures/blocks/Wooden Barricade.png");

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float par5) {
        int damage = ((TileBarricade)tileEntity).damage;
        boolean blockAbove = tileEntity.getWorldObj().getBlock(tileEntity.xCoord, tileEntity.yCoord + 1, tileEntity.zCoord).isSideSolid(tileEntity.getWorldObj(), tileEntity.xCoord, tileEntity.yCoord + 1, tileEntity.zCoord, ForgeDirection.SOUTH);
        boolean blockBelow = tileEntity.getWorldObj().getBlock(tileEntity.xCoord, tileEntity.yCoord - 1, tileEntity.zCoord).isSideSolid(tileEntity.getWorldObj(), tileEntity.xCoord, tileEntity.yCoord - 1, tileEntity.zCoord, ForgeDirection.SOUTH);
        if(BlockBarricade.class.isAssignableFrom(tileEntity.getWorldObj().getBlock(tileEntity.xCoord, tileEntity.yCoord + 1, tileEntity.zCoord).getClass())) {
            blockAbove = false;
        }
        if(BlockBarricade.class.isAssignableFrom(tileEntity.getWorldObj().getBlock(tileEntity.xCoord, tileEntity.yCoord - 1, tileEntity.zCoord).getClass())) {
            blockBelow = false;
        }
        glPushMatrix();
        glTranslated(x, y, z);
        glTranslated(0.5, 0.5, 0.1);
        glScaled(0.7, 0.7, 0.7);
        bindTexture(texture);
        if(damage < 1) { glPushMatrix(); model.renderAll(); glPopMatrix(); }
        if(damage < 2 || blockAbove) { glPushMatrix(); glTranslated(0, 0.5, 0.5); glRotated(90, 1, 0, 0); model.renderAll(); glPopMatrix(); }
        if(damage < 3) { glPushMatrix(); glTranslated(0, 0, 1); model.renderAll(); glPopMatrix(); }
        if(damage < 4) { glPushMatrix(); glTranslated(0.5, 0, 0.5); glRotated(90, 0, 1, 0); model.renderAll(); glPopMatrix(); }
        if(damage < 5) { glPushMatrix(); glTranslated(-0.5, 0, 0.5); glRotated(90, 0, 1, 0); model.renderAll(); glPopMatrix(); }
        if(damage < 6 || blockBelow) { glPushMatrix(); glTranslated(0, -0.5, 0.5); glRotated(90, 1, 0, 0); model.renderAll(); glPopMatrix(); }
        glPopMatrix();
    }
}
