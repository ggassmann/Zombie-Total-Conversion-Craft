package net.gigimoi.zombietc.pathfinding;

import net.gigimoi.zombietc.ZombieTC;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;

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
            if(BlockNode.nodeConnections != null) {
                for(int i = 0; i < BlockNode.nodeConnections.size(); i++) {
                    BlockNode.MCNodePair link = BlockNode.nodeConnections.get(i);
                    if(link.n1.position.distanceTo(new Point3(tile.xCoord, tile.yCoord, tile.zCoord)) < 0.01) {
                        GL11.glLineWidth(5f);
                        GL11.glEnable(GL11.GL_BLEND);
                        GL11.glBegin(GL11.GL_LINES);
                        GL11.glVertex3d(0.0, 0.0, 0.0);
                        GL11.glVertex3d(
                                link.n2.position.xCoord - link.n1.position.xCoord,
                                link.n2.position.yCoord - link.n1.position.yCoord,
                                link.n2.position.zCoord - link.n1.position.zCoord
                        );
                        GL11.glEnd();
                    }
                }
            }
            GL11.glPopMatrix();
        }
    }
}
