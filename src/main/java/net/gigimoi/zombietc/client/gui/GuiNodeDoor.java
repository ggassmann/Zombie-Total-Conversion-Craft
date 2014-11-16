package net.gigimoi.zombietc.client.gui;

import net.gigimoi.zombietc.ZombieTC;
import net.gigimoi.zombietc.client.tilerenderer.TileRendererDoorNode;
import net.gigimoi.zombietc.net.MessageChangeNodeDoorTexture;
import net.gigimoi.zombietc.tile.TileNodeDoor;
import net.gigimoi.zombietc.util.*;
import org.lwjgl.input.Mouse;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Gavin on 11/15/2014.
 */
public class GuiNodeDoor extends GuiNode {
    public static final int GUI_ID = 10406;
    public GuiNodeDoor(int x, int y, int z) {
        super(x, y, z);
    }

    public int getTextureMouseOver() {
        double scale = 256 * 0.125 * 0.75;
        for(int i = 0; i < TileRendererDoorNode.textures.length; i++) {
            if(Mouse.getX() - width < -184 + i * scale * 2 + 54 && Mouse.getY() - height < 94 && Mouse.getY() - height > 44) {
                return i;
            }
        }
        return -1;
    }
    @Override
    public void drawScreen(int par1, int par2, float par3) {
        super.drawScreen(par1, par2, par3);
        TextRenderHelper.drawString((Mouse.getY() - height) + "," + Lang.get("ui.nodedoor.texture"), width / 2 - 100, height / 2 - 100 + 48, TextAlignment.Right);
        for(int i = 0; i < TileRendererDoorNode.textures.length; i++) {
            glPushMatrix();
            glTranslated(width / 2 - (15.5 * 0.75f) + i * (33 * 0.75f) - 80, height / 2 - 48, 0);
            if(getTextureMouseOver() == i) {
                glScaled(1.05, 1.05, 1.05);
            }
            glScaled(0.125, 0.125, 0.125);
            glScaled(0.75, 0.75, 0.75);
            TextureHelper.bindTexture(TileRendererDoorNode.textures[i]);
            drawTexturedModalRect(0, 0, 0, 0, 256, 256);
            glPopMatrix();
        }
    }

    @Override
    protected void mouseClicked(int x, int y, int buttonID) {
        super.mouseClicked(x, y, buttonID);
        if(getTextureMouseOver() != -1) {
            TileNodeDoor tile = (TileNodeDoor) this.getTile();
            tile.textureID = getTextureMouseOver();
            ZombieTC.network.sendToServer(new MessageChangeNodeDoorTexture(tile.textureID, tile.xCoord, tile.yCoord, tile.zCoord));
        }
    }
}
