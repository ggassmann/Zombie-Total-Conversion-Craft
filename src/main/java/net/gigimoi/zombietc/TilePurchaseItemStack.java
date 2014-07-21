package net.gigimoi.zombietc;

import net.gigimoi.zombietc.net.activates.MessageActivatePurchase;
import net.gigimoi.zombietc.net.activates.MessageActivateRepairBarricade;
import net.gigimoi.zombietc.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.input.Keyboard;

/**
 * Created by gigimoi on 7/21/2014.
 */
public class TilePurchaseItemStack extends TileEntity {
    public ItemStack itemStack;


    public AxisAlignedBB getPurchaseBounds() {
        return AxisAlignedBB.getBoundingBox(xCoord, yCoord - 1, zCoord, xCoord + 1, yCoord + 1, zCoord + 1);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        itemStack = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("ItemStack"));
        super.readFromNBT(tag);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        NBTTagCompound stackTag = new NBTTagCompound();
        itemStack.writeToNBT(stackTag);
        tag.setTag("ItemStack", stackTag);
        System.out.println(itemStack);
        super.writeToNBT(tag);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if(itemStack != null && !ZombieTC.editorModeManager.enabled && worldObj.isRemote && worldObj.getEntitiesWithinAABB(EntityPlayer.class, getPurchaseBounds()).contains(Minecraft.getMinecraft().thePlayer)) {
            ZombieTC.gameManager.setActivateMessage("Press [" + Keyboard.getKeyName(ClientProxy.activate.getKeyCode()) + "] to purchase ");
            if(ClientProxy.activate.isPressed()) {
                ZombieTC.network.sendToServer(new MessageActivatePurchase(Minecraft.getMinecraft().thePlayer, xCoord, yCoord, zCoord));
            }
        }
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
