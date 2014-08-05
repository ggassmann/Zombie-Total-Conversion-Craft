package net.gigimoi.zombietc.block;

/**
 * Created by gigimoi on 7/27/2014.
 */
public interface ITileEntityPurchasable {
    int getPrice();

    void setPrice(int value);

    boolean getEnabled();

    String getVerb();
}
