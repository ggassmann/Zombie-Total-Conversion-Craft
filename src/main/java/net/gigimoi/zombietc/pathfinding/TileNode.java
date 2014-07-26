package net.gigimoi.zombietc.pathfinding;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by gigimoi on 7/16/2014.
 */
public class TileNode extends TileEntity {
    public boolean deactivatedUntilEvent = false;
    public String eventWaitFor = "";

    public TileNode() {
        super();
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        deactivatedUntilEvent = tag.getBoolean("Deactivated Until Event");
        eventWaitFor = tag.getString("Event Wait For");
        super.readFromNBT(tag);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        tag.setBoolean("Deactivated Until Event", deactivatedUntilEvent);
        tag.setString("Event Wait For", eventWaitFor);
        super.writeToNBT(tag);
    }
    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tagCompound = new NBTTagCompound();
        writeToNBT(tagCompound);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, worldObj.getBlockMetadata(xCoord, yCoord, zCoord), tagCompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        readFromNBT(pkt.func_148857_g());
    }
}
