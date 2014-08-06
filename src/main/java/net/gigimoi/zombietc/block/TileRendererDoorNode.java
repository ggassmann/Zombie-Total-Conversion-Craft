package net.gigimoi.zombietc.block;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by gigimoi on 8/6/2014.
 */
public class TileRendererDoorNode extends TileEntitySpecialRenderer {
    TileRendererNode internalNodeRenderer = new TileRendererNode();
    @Override
    public void renderTileEntityAt(TileEntity tileRaw, double x, double y, double z, float par5) {
        internalNodeRenderer.renderTileEntityAt(tileRaw, x, y, z, par5);
    }
}
