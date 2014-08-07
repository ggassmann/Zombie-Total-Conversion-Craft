package net.gigimoi.zombietc.tile;

import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by gigimoi on 8/6/2014.
 */
public class TileNodeDoor extends TileNode {
    public int direction;
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
