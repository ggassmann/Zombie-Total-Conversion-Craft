package net.gigimoi.zombietc.block.purchasable;

import cpw.mods.fml.relauncher.Side;
import net.gigimoi.zombietc.ZombieTC;
import net.gigimoi.zombietc.api.ITileEntityActivatable;
import net.gigimoi.zombietc.api.ITileEntityPurchasable;
import net.gigimoi.zombietc.util.Lang;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by gigimoi on 7/21/2014.
 */
public class TilePurchaseItemStack extends TileEntity implements ITileEntityActivatable, ITileEntityPurchasable {
    public ItemStack itemStack;
    int price = 100;

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        itemStack = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("ItemStack"));
        price = tag.getInteger("Price");
        super.readFromNBT(tag);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        if (itemStack != null) {
            NBTTagCompound stackTag = new NBTTagCompound();
            itemStack.writeToNBT(stackTag);
            tag.setTag("ItemStack", stackTag);
        }
        tag.setInteger("Price", price);
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

    @Override
    public void activate(Entity activator, Side side) {
        if (itemStack != null && side.isServer() && activator.getClass() == EntityPlayerMP.class) {
            EntityPlayer player = (EntityPlayer) activator;
            player.inventory.addItemStackToInventory(itemStack.copy());
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
        return !ZombieTC.editorModeManager.enabled;
    }

    @Override
    public String getVerb() {
        return itemStack != null ?
                "Purchase " + Lang.get(itemStack.getUnlocalizedName() + ".name") :
                Lang.get("ui.purchaseItemStack.setItemInEditor");
    }
}
