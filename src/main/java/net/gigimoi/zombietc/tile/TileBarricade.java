package net.gigimoi.zombietc.tile;

import net.gigimoi.zombietc.ZombieTC;
import net.gigimoi.zombietc.client.ClientProxy;
import net.gigimoi.zombietc.entity.EntityZZombie;
import net.gigimoi.zombietc.net.activates.MessageActivateRepairBarricade;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.input.Keyboard;

import java.util.List;

/**
 * Created by gigimoi on 7/15/2014.
 */
public class TileBarricade extends TileEntitySynced {
    public int damage = 0;
    public int ticker = 0;
    public int playerTicker = 0;

    public TileBarricade() {
        super();
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        damage = tag.getInteger("Damage");
        ticker = tag.getInteger("Ticker");
        playerTicker = tag.getInteger("Player Ticker");
        super.readFromNBT(tag);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        tag.setInteger("Damage", damage);
        tag.setInteger("Ticker", ticker);
        tag.setInteger("Player Ticker", playerTicker);
        super.writeToNBT(tag);
    }

    public AxisAlignedBB getBoundsAround() {
        return AxisAlignedBB.getBoundingBox(xCoord - 0.5, yCoord - 0.5, zCoord - 0.5, xCoord + 1.5, yCoord + 1.5, zCoord + 1.5);
    }

    @Override
    public void updateEntity() {
        if (damage < 5) {
            List<EntityZZombie> zombies = worldObj.getEntitiesWithinAABB(EntityZZombie.class, getBoundsAround());
            if (zombies.size() > 0) {
                ticker++;
                if (ticker > 30) {
                    damage++;
                    ticker = 0;
                }
            }
        }
        playerTicker = Math.max(0, playerTicker - 1);
        if (damage > 0 && worldObj.isRemote) {
            if (worldObj.getEntitiesWithinAABB(EntityPlayer.class, getBoundsAround()).contains(ZombieTC.proxy.getPlayer())) {
                ZombieTC.gameManager.setActivateMessage("Press [" + Keyboard.getKeyName(ClientProxy.activate.getKeyCode()) + "] to repair" + (playerTicker == 0 ? "" : "..."));
                if (playerTicker == 0 && Keyboard.isKeyDown(ClientProxy.activate.getKeyCode())) {
                    playerTicker = 40;
                    ZombieTC.network.sendToServer(new MessageActivateRepairBarricade(xCoord, yCoord, zCoord));
                }
            }
        }
    }
}
