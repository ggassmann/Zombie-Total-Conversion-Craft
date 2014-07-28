package net.gigimoi.zombietc.block;

import cpw.mods.fml.relauncher.Side;
import net.gigimoi.zombietc.ZombieTC;
import net.gigimoi.zombietc.event.GameManager;
import net.gigimoi.zombietc.net.MessageActivateTile;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;

/**
 * Created by gigimoi on 7/26/2014.
 */
public class TilePurchaseEventLever extends TileEntitySynced implements ITileEntityActivatable, ITileEntityPurchasable {
    boolean isDown;
    int price = 500;
    public String event;

    public AxisAlignedBB getPurchaseBounds() {
        return AxisAlignedBB.getBoundingBox(xCoord + 0.1, yCoord - 1, zCoord + 0.1, xCoord + 0.9, yCoord + 1.9, zCoord + 0.9);
    }


    @Override
    public void readFromNBT(NBTTagCompound tag) {
        isDown = tag.getBoolean("Is Down");
        price = tag.getInteger("Price");
        event = tag.getString("Event");
        super.readFromNBT(tag);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        tag.setBoolean("Is Down", isDown);
        tag.setInteger("Price", price);
        tag.setString("Event", event);
        super.writeToNBT(tag);
    }

    @Override
    public void updateEntity() {
        if(ZombieTC.editorModeManager.enabled && isDown) {
            activate(null, Side.SERVER);
            isDown = false;
        }
    }

    @Override
    public void activate(Entity activator, Side side) {
        if(activator != null) {
            GameManager.currentEvents.add(event);
        }
        isDown = true;
        //Copypasta from BlockLever.onBlockActivated
        if (worldObj.isRemote) {
            return;
        }
        else {
            int i1 = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
            int j1 = i1 & 7;
            int k1 = 8 - (i1 & 8);
            worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, j1 + k1, 3);
            worldObj.playSoundEffect((double) xCoord + 0.5D, (double) yCoord + 0.5D, (double) zCoord + 0.5D, "random.click", 0.3F, k1 > 0 ? 0.6F : 0.5F);
            worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, BlockPurchaseEventLever.instance);

            if (j1 == 1) {
                worldObj.notifyBlocksOfNeighborChange(xCoord - 1, yCoord, zCoord, BlockPurchaseEventLever.instance);
            }
            else if (j1 == 2) {
                worldObj.notifyBlocksOfNeighborChange(xCoord + 1, yCoord, zCoord, BlockPurchaseEventLever.instance);
            }
            else if (j1 == 3) {
                worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord - 1, BlockPurchaseEventLever.instance);
            }
            else if (j1 == 4) {
                worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord + 1, BlockPurchaseEventLever.instance);
            }
            else if (j1 != 5 && j1 != 6) {
                if (j1 == 0 || j1 == 7) {
                    worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord + 1, zCoord, BlockPurchaseEventLever.instance);
                }
            }
            else {
                worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord - 1, zCoord, BlockPurchaseEventLever.instance);
            }
        }
    }

    @Override
    public void setPrice(int value) {
        price = value;
    }

    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public boolean getEnabled() {
        return !ZombieTC.editorModeManager.enabled && !isDown;
    }

    @Override
    public void onClientPurchase(EntityPlayer purchaser) {
        ZombieTC.network.sendToServer(new MessageActivateTile(xCoord, yCoord, zCoord, Minecraft.getMinecraft().thePlayer));
    }

    @Override
    public String getVerb() {
        return "Flip lever";
    }
}
