package net.gigimoi.zombietc.api;

import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.Entity;

/**
 * Created by gigimoi on 7/26/2014.
 */
public interface ITileEntityActivatable {
    /*
    This is run when the entity activates an object client side and when the server recieves a valid activation
     */
    void activate(Entity activator, Side side);
}
