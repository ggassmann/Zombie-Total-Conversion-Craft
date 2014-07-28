package net.gigimoi.zombietc.block;

import cpw.mods.fml.relauncher.Side;
import net.gigimoi.zombietc.ZombieTC;
import net.gigimoi.zombietc.helpers.Lang;
import net.gigimoi.zombietc.net.MessageActivateTile;
import net.minecraft.client.Minecraft;
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
        System.out.println(activator.getClass());
        if (side.isServer() && activator.getClass() == EntityPlayerMP.class) {
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
        return true;
    }

    @Override
    public void onClientPurchase(EntityPlayer purchaser) {
        ZombieTC.network.sendToServer(new MessageActivateTile(xCoord, yCoord, zCoord, Minecraft.getMinecraft().thePlayer));
    }

    @Override
    public String getVerb() {
        return "Purchase " + Lang.get(itemStack.getUnlocalizedName() + ".name");
    }
}
