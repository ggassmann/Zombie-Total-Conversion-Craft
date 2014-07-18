package net.gigimoi.zombietc.helpers;

import net.gigimoi.zombietc.pathfinding.TileRendererNode;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;

/**
 * Created by gigimoi on 7/17/2014.
 */
public class TextureHelper {
    public static void bindTexture(ResourceLocation location) {
        if(TileEntityRendererDispatcher.instance != null && TileEntityRendererDispatcher.instance.field_147553_e != null) {
            TileEntityRendererDispatcher.instance.field_147553_e.bindTexture(location);
        }
    }
}
