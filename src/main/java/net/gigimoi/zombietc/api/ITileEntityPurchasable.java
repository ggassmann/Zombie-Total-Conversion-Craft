package net.gigimoi.zombietc.api;

/**
 * Created by gigimoi on 7/27/2014.
 */
public interface ITileEntityPurchasable {
    /*
    Gets the cost of purchase
     */
    int getPrice();

    /*
    Sets the cost of purchase, used by GUIs
     */
    void setPrice(int value);

    /*
    Returns if the tile entity can be purchased, if it cannot, no message will be displayed
     */
    boolean getEnabled();

    /*
    Press [F] to ...
     */
    String getVerb();
}
