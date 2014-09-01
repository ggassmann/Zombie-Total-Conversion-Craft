package net.gigimoi.zombietc.tile;

import net.gigimoi.zombietc.ZombieTC;
import net.gigimoi.zombietc.util.IListenerZTC;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by gigimoi on 8/6/2014.
 */
public class TileNodeDoor extends TileNode implements IListenerZTC {
    public int direction;
    public int animationTime;
    public int animationDirection = -1;

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        direction = tag.getInteger("Direction");
        animationTime = tag.getInteger("Animation Time");
        animationDirection = tag.getInteger("Animation Direction");
        super.readFromNBT(tag);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        tag.setInteger("Direction", direction);
        tag.setInteger("Animation Time", animationTime);
        tag.setInteger("Animation Direction", animationDirection);
        super.writeToNBT(tag);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (ZombieTC.editorModeManager.enabled) {
            animationTime = 0;
            animationDirection = -1;
        }
        animationTime = Math.min(100, Math.max(0, animationTime + animationDirection * 5));
    }

    @Override
    public void onEvent(String event) {
        if(deactivatedUntilEvent && event.equals(eventWaitFor)) {
            isEventTriggering = true;
            deactivated = false;
            animationDirection = 1;
        }
    }
}
