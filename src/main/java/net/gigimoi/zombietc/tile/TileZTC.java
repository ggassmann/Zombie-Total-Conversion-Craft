package net.gigimoi.zombietc.tile;

import net.gigimoi.zombietc.ZombieTC;
import net.gigimoi.zombietc.util.IListenerZTC;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by gigimoi on 8/8/2014.
 */
public abstract class TileZTC extends TileEntity implements IListenerZTC {
    @Override
    public void updateEntity() {
        super.updateEntity();
        if(!ZombieTC.gameManager.isRegisteredListener(this)) {
            ZombieTC.gameManager.registerListener(this);
        }
    }
    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tagCompound = new NBTTagCompound();
        writeToNBT(tagCompound);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, tagCompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        readFromNBT(pkt.func_148857_g());
    }
}
