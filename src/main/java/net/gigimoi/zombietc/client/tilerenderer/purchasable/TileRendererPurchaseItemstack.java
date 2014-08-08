package net.gigimoi.zombietc.client.tilerenderer.purchasable;

import net.gigimoi.zombietc.ZombieTC;
import net.gigimoi.zombietc.block.purchasable.TilePurchaseItemStack;
import net.gigimoi.zombietc.item.weapon.ItemWeapon;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

/**
 * Created by gigimoi on 7/21/2014.
 */
public class TileRendererPurchaseItemstack extends TileEntitySpecialRenderer {
    private static RenderItem renderItem = new RenderItem() {
        @Override
        public boolean shouldBob() {
            return false;
        }
    };

    @Override
    public void renderTileEntityAt(TileEntity tileraw, double x, double y, double z, float par5) {
        TilePurchaseItemStack tile = (TilePurchaseItemStack) tileraw;
        ForgeDirection side = ForgeDirection.getOrientation(tile.getWorldObj().getBlockMetadata(tile.xCoord, tile.yCoord, tile.zCoord));
        GL11.glPushMatrix();
        GL11.glTranslated(x, y + 0.6, z);
        if (side == ForgeDirection.NORTH) {
            GL11.glRotated(90, 0, 1, 0);
            GL11.glTranslated(-0.9, 0, 0.5);
        }
        if (side == ForgeDirection.WEST) {
            GL11.glTranslated(0.9, 0, 0.5);
        }
        if (side == ForgeDirection.EAST) {
            GL11.glTranslated(0.1, 0, 0.5);
        }
        if (side == ForgeDirection.SOUTH) {
            GL11.glRotated(-90, 0, 1, 0);
            GL11.glTranslated(0.1, 0, -0.5);
        }
        if (tile.itemStack != null && ItemWeapon.class == tile.itemStack.getItem().getClass()) {
            GL11.glRotated(45, 0, 1, 0);
            ItemWeapon itemWeapon = (ItemWeapon) tile.itemStack.getItem();
            GL11.glScaled(itemWeapon.inventoryScale, itemWeapon.inventoryScale, itemWeapon.inventoryScale);
            itemWeapon.renderItem(IItemRenderer.ItemRenderType.ENTITY, tile.itemStack);
        } else {
            if (tile.itemStack != null) {
                EntityItem eItem = new EntityItem(tile.getWorldObj());
                eItem.hoverStart = 0.0F;
                eItem.setEntityItemStack(tile.itemStack);

                GL11.glPushMatrix();
                GL11.glRotated(90, 0, 1, 0);

                renderItem.setRenderManager(RenderManager.instance);
                renderItem.doRender(eItem, 0, 0, 0, 0, 0);
                GL11.glPopMatrix();
            } else if (ZombieTC.editorModeManager.enabled) {
                GL11.glScaled(0.1, 0.1, 0.1);
                GL11.glRotated(90, 0, 0, 1);
                GL11.glRotated(-90, 0, 1, 0);
                if (side == ForgeDirection.WEST) {
                    GL11.glRotated(180, 0, 0, 1);
                }
                bindTexture(new ResourceLocation(ZombieTC.MODID, "textures/blocks/Purchase Itemstack.png"));
                AdvancedModelLoader.loadModel(new ResourceLocation(ZombieTC.MODID, "models/purchaseUnset.obj")).renderAll();
            }
        }

        GL11.glPopMatrix();
    }
}
