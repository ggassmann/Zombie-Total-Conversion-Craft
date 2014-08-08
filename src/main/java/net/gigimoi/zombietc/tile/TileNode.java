package net.gigimoi.zombietc.tile;

import net.gigimoi.zombietc.ZombieTC;
import net.gigimoi.zombietc.util.IListenerZTC;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by gigimoi on 7/16/2014.
 */
public class TileNode extends TileZTC implements IListenerZTC {
    public boolean deactivatedUntilEvent = false;
    public String eventWaitFor = "";
    public boolean deactivated;

    public TileNode() {
        super();
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        deactivatedUntilEvent = tag.getBoolean("Deactivated Until Event");
        eventWaitFor = tag.getString("Event Wait For");
        deactivated = tag.getBoolean("Deactivated");
        super.readFromNBT(tag);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        tag.setBoolean("Deactivated Until Event", deactivatedUntilEvent);
        tag.setString("Event Wait For", eventWaitFor);
        tag.setBoolean("Deactivated", deactivated);
        super.writeToNBT(tag);
    }

    boolean isEventTriggering = false;

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (ZombieTC.editorModeManager.enabled) {
            deactivated = deactivatedUntilEvent;
        }
        if (isEventTriggering) {
            deactivated = false;
        }
    }

    @Override
    public void onEvent(String event) {
        if(deactivatedUntilEvent && event.equals(eventWaitFor)) {
            isEventTriggering = true;
        }
    }
}
