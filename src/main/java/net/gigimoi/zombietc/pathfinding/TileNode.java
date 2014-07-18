package net.gigimoi.zombietc.pathfinding;

import net.gigimoi.zombietc.helpers.TextureHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

/**
 * Created by gigimoi on 7/16/2014.
 */
public class TileNode extends TileEntity implements IItemRenderer {
    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        if(type == ItemRenderType.INVENTORY) {
            return true;
        }
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glPushMatrix();
        if(type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
            //EntityPlayer player = (EntityPlayer) data[1];
            GL11.glTranslated(0, 1, 0);
        }
        TextureHelper.bindTexture(TileRendererNode.texture);
        TileRendererNode.model.renderAll();
        GL11.glPopMatrix();
    }
}
