package net.gigimoi.zombietc.block.purchasable;

import cpw.mods.fml.relauncher.Side;
import net.gigimoi.zombietc.ZombieTC;
import net.gigimoi.zombietc.api.ITileEntityActivatable;
import net.gigimoi.zombietc.api.ITileEntityPurchasable;
import net.gigimoi.zombietc.tile.TileZTC;
import net.gigimoi.zombietc.util.IListenerZTC;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by gigimoi on 7/26/2014.
 */
public class TilePurchaseEventLever extends TileZTC implements ITileEntityActivatable, ITileEntityPurchasable, IListenerZTC {
    public String event = "";
    boolean isDown;
    int price = 500;

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
    boolean isEventTriggering = false;
    @Override
    public void onEvent(String event) {
        if(event.equals(this.event)) {
            isEventTriggering = true;
        }
    }
    @Override
    public void updateEntity() {
        if (ZombieTC.editorModeManager.enabled && isDown) {
            activate(null, Side.SERVER);
            isDown = false;
        } else if (isEventTriggering && !isDown) {
            isEventTriggering = false;
            activate(null, worldObj.isRemote ? Side.CLIENT : Side.SERVER);
        }
    }

    @Override
    public void activate(Entity activator, Side side) {
        if (activator != null) {
            ZombieTC.gameManager.invokeEvent(event);
        }
        isDown = true;
        //Copypasta from BlockLever.onBlockActivated
        int i1 = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
        int j1 = i1 & 7;
        int k1 = 8 - (i1 & 8);
        worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, j1 + k1, 3);
        worldObj.playSoundEffect((double) xCoord + 0.5D, (double) yCoord + 0.5D, (double) zCoord + 0.5D, "random.click", 0.3F, k1 > 0 ? 0.6F : 0.5F);
        worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, BlockPurchaseEventLever.instance);

        if (j1 == 1) {
            worldObj.notifyBlocksOfNeighborChange(xCoord - 1, yCoord, zCoord, BlockPurchaseEventLever.instance);
        } else if (j1 == 2) {
            worldObj.notifyBlocksOfNeighborChange(xCoord + 1, yCoord, zCoord, BlockPurchaseEventLever.instance);
        } else if (j1 == 3) {
            worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord - 1, BlockPurchaseEventLever.instance);
        } else if (j1 == 4) {
            worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord + 1, BlockPurchaseEventLever.instance);
        } else if (j1 != 5 && j1 != 6) {
            if (j1 == 0 || j1 == 7) {
                worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord + 1, zCoord, BlockPurchaseEventLever.instance);
            }
        } else {
            worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord - 1, zCoord, BlockPurchaseEventLever.instance);
        }
    }

    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public void setPrice(int value) {
        price = value;
    }

    @Override
    public boolean getEnabled() {
        return !ZombieTC.editorModeManager.enabled && !isDown;
    }

    @Override
    public String getVerb() {
        return "Flip lever";
    }
}
