package net.gigimoi.zombietc.net.activates;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import net.gigimoi.zombietc.TilePurchaseItemStack;
import net.gigimoi.zombietc.ZombieTC;
import net.gigimoi.zombietc.helpers.NetHelper;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by gigimoi on 7/21/2014.
 */
public class MessageActivatePurchase implements IMessage {
    public int x;
    public int y;
    public int z;
    public EntityPlayer purchaser;

    public MessageActivatePurchase() {}
    public MessageActivatePurchase(EntityPlayer purchaser, int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.purchaser = purchaser;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
        purchaser = (EntityPlayer) NetHelper.getEntityByID(buf.readInt());
    }
    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        buf.writeInt(purchaser.getEntityId());
    }
    public static class MessageActivatePurchaseHandler implements IMessageHandler<MessageActivatePurchase, MessageActivatePurchase> {
        @Override
        public MessageActivatePurchase onMessage(MessageActivatePurchase message, MessageContext ctx) {
            TilePurchaseItemStack tile = (TilePurchaseItemStack)message.purchaser.worldObj.getTileEntity(message.x, message.y, message.z);
            if(ctx.side == Side.SERVER) {
                message.purchaser.inventory.addItemStackToInventory(tile.itemStack.copy());
                ZombieTC.network.sendToAll(message);
            }
            return null;
        }
    }
}
