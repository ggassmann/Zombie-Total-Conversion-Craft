package net.gigimoi.zombietc;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

import java.util.List;

/**
 * Created by gigimoi on 7/15/2014.
 */
public class TileBarricade extends TileEntity {
    public int damage = 0;
    public int ticker = 0;

    public TileBarricade() {
        super();
    }
    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        damage = tag.getInteger("Damage");
        ticker = tag.getInteger("Ticker");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setInteger("Damage", damage);
        tag.setInteger("Ticker", ticker);
    }

    @Override
    public void updateEntity() {
        if(damage < 5) {
            List<EntityZZombie> zombies = worldObj.getEntitiesWithinAABB(EntityZZombie.class, AxisAlignedBB.getBoundingBox(xCoord - 0.5, yCoord - 0.5, zCoord - 0.5, xCoord + 1.5, yCoord + 1.5, zCoord + 1.5));
            if(zombies.size() > 0) {
                ticker++;
                if(ticker > 30) {
                    damage++;
                    ticker = 0;
                }
            }
        }
    }
}
