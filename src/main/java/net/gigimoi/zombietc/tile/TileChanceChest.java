package net.gigimoi.zombietc.tile;

import cpw.mods.fml.relauncher.Side;
import net.gigimoi.zombietc.api.ITileEntityActivatable;
import net.gigimoi.zombietc.api.ITileEntityPurchasable;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by gigimoi on 8/30/2014.
 */
public class TileChanceChest extends TileZTC implements ITileEntityPurchasable, ITileEntityActivatable {
    public int direction;
    @Override
    public void onEvent(String event) {
    }

    @Override
    public void activate(Entity activator, Side side) {
        System.out.println("Activated chance chest on" + side);
    }

    @Override
    public int getPrice() {
        return 950;
    }

    @Override
    public void setPrice(int value) {
        System.out.println("Tried to set price of chance chest, Cannot set price of ChanceChest. Stop fucking with the chance chest.");
    }

    @Override
    public boolean getEnabled() {
        return true;
    }

    @Override
    public String getVerb() {
        return "open Chance Chest";
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        direction = tag.getInteger("Direction");
        super.readFromNBT(tag);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        tag.setInteger("Direction", direction);
        super.writeToNBT(tag);
    }
}
