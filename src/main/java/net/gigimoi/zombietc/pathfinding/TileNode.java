package net.gigimoi.zombietc.pathfinding;

import net.gigimoi.zombietc.ZombieTC;
import net.gigimoi.zombietc.block.TileEntitySynced;
import net.gigimoi.zombietc.event.GameManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by gigimoi on 7/16/2014.
 */
public class TileNode extends TileEntitySynced {
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

    @Override
    public void updateEntity() {
        super.updateEntity();
        if(ZombieTC.editorModeManager.enabled) {
            deactivated = deactivatedUntilEvent;
        }
        if(deactivatedUntilEvent && GameManager.isEventTriggering(eventWaitFor)) {
            deactivated = false;
        }
    }
}
