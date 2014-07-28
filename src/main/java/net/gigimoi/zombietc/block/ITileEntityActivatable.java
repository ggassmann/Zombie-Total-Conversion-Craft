package net.gigimoi.zombietc.block;

import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.Entity;

/**
 * Created by gigimoi on 7/26/2014.
 */
public interface ITileEntityActivatable {
    void activate(Entity activator, Side side);
}
