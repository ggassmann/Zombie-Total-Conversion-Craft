package net.gigimoi.zombietc.client.tilerenderer;

import net.gigimoi.zombietc.ZombieTC;
import net.gigimoi.zombietc.block.BlockNode;
import net.gigimoi.zombietc.tile.TileNode;
import net.gigimoi.zombietc.util.Point3;
import net.gigimoi.zombietc.util.pathfinding.MCNode;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by gigimoi on 7/16/2014.
 */
public class TileRendererNode extends TileEntitySpecialRenderer {
    public static ResourceLocation texture = new ResourceLocation(ZombieTC.MODID, "textures/blocks/Node.png");
    public static IModelCustom model = AdvancedModelLoader.loadModel(new ResourceLocation(ZombieTC.MODID, "models/node.obj"));

    public static void renderModel(TileEntity rawTile, double x, double y, double z) {
        if (ZombieTC.editorModeManager.enabled) {
            TileNode tile = (TileNode) rawTile;
            glPushMatrix();
            glTranslated(x + 0.5, y + 0.5, z + 0.5);
            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            if (tile.deactivated) {
                glColor3b((byte) 0, (byte) 0, (byte) 255);
            }
            model.renderAll();
            for (int i = 0; i < BlockNode.nodes.size(); i++) {
                MCNode node = BlockNode.nodes.get(i);
                if (node.position.distanceTo(new Point3(tile.xCoord, tile.yCoord, tile.zCoord)) < 0.01) {
                    ArrayList<MCNode> links = node.linksTo;
                    for (int j = 0; j < links.size(); j++) {
                        glPushMatrix();
                        if (links.get(j).isDisabled() || node.isDisabled() || !((List) node.neighbors()).contains(links.get(j))) {
                            glColor3b((byte) 0, (byte) 0, (byte) 255);
                            glTranslated(0, 0.1, 0);
                        } else {
                            glColor3d(1, 1, 1);
                        }
                        glLineWidth(5f);
                        glEnable(GL_BLEND);
                        glBegin(GL_LINES);
                        glVertex3d(0.0, 0.0, 0.0);
                        glVertex3d(
                                links.get(j).position.xCoord - node.position.xCoord,
                                links.get(j).position.yCoord - node.position.yCoord,
                                links.get(j).position.zCoord - node.position.zCoord
                        );
                        glEnd();
                        glPopMatrix();
                    }
                    break;
                }
            }
            glPopMatrix();
        }
    }

    @Override
    public void renderTileEntityAt(TileEntity rawTile, double x, double y, double z, float par5) {
        bindTexture(texture);
        renderModel(rawTile, x, y, z);
    }
}
