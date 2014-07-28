package net.gigimoi.zombietc.block;

import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by gigimoi on 7/27/2014.
 */
public interface ITileEntityPurchasable {
    void setPrice(int value);
    int getPrice();
    boolean getEnabled();
    void onClientPurchase(EntityPlayer purchaser);
    String getVerb();
}
