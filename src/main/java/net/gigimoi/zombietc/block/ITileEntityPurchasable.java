package net.gigimoi.zombietc.block;

import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by gigimoi on 7/27/2014.
 */
public interface ITileEntityPurchasable {
    int getPrice();

    void setPrice(int value);

    boolean getEnabled();

    void onClientPurchase(EntityPlayer purchaser);

    String getVerb();
}
