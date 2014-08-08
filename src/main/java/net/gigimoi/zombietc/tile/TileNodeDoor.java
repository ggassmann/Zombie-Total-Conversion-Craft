package net.gigimoi.zombietc.tile;

import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by gigimoi on 8/6/2014.
 */
public class TileNodeDoor extends TileNode {
    public int direction;
    public int animationTime;

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        direction = tag.getInteger("Direction");
        animationTime = tag.getInteger("Animation Time");
        super.readFromNBT(tag);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        tag.setInteger("Direction", direction);
        tag.setInteger("AnimationTime", animationTime);
        super.writeToNBT(tag);
    }
}
