package net.gigimoi.zombietc;

import net.gigimoi.zombietc.event.WaveEventManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import javax.vecmath.Vector3f;
import java.util.Random;

/**
 * Created by gigimoi on 7/14/2014.
 */
public class TileSpawner extends TileEntity {
    public String entityToSpawn = "";

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        entityToSpawn = tag.getString("entity");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setString("entity", entityToSpawn);
    }

    static Random _r = new Random();
    @Override
    public void updateEntity() {
        if(!worldObj.isRemote) {
            WaveEventManager.spawnPositions.add(new Vector3f(xCoord + 0.5f, yCoord + 1, zCoord + 0.5f));
            WaveEventManager.worldsSpawnedTo.add(worldObj);
        }
    }
}
