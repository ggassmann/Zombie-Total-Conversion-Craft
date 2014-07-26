package net.gigimoi.zombietc.pathfinding;

import net.gigimoi.zombietc.ZombieTC;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

/**
 * Created by gigimoi on 7/16/2014.
 */
public class TileRendererNode extends TileEntitySpecialRenderer {
    public static ResourceLocation texture = new ResourceLocation(ZombieTC.MODID, "textures/models/node.png");
    public static IModelCustom model = AdvancedModelLoader.loadModel(new ResourceLocation(ZombieTC.MODID, "models/node.obj"));
    @Override
    public void renderTileEntityAt(TileEntity rawTile, double x, double y, double z, float par5) {
        if(ZombieTC.editorModeManager.enabled) {
            TileNode tile = (TileNode)rawTile;
            GL11.glPushMatrix();
            GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);
            GL11.glEnable(GL11.GL_BLEND);
            bindTexture(texture);
            model.renderAll();
            for(int i = 0; i < BlockNode.nodes.size(); i++) {
                MCNode node = BlockNode.nodes.get(i);
                if(node.position.distanceTo(new Point3(tile.xCoord, tile.yCoord, tile.zCoord)) < 0.01) {
                    ArrayList<MCNode> links = (ArrayList<MCNode>)node.neighbors();
                    for(int j = 0; j < links.size(); j++) {
                        GL11.glLineWidth(5f);
                        GL11.glEnable(GL11.GL_BLEND);
                        GL11.glBegin(GL11.GL_LINES);
                        GL11.glVertex3d(0.0, 0.0, 0.0);
                        GL11.glVertex3d(
                                links.get(j).position.xCoord - node.position.xCoord,
                                links.get(j).position.yCoord - node.position.yCoord,
                                links.get(j).position.zCoord - node.position.zCoord
                        );
                        GL11.glEnd();
                    }
                    break;
                }
            }
            GL11.glPopMatrix();
        }
    }
}
