package net.gigimoi.zombietc.net;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import net.gigimoi.zombietc.ZombieTC;
import net.gigimoi.zombietc.block.purchasable.TilePurchaseItemStack;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by gigimoi on 7/21/2014.
 */
public class MessageSetPurchaseItemStackPrice implements IMessage {
    public int x;
    public int y;
    public int z;
    public int price;

    public MessageSetPurchaseItemStackPrice() {
    }

    public MessageSetPurchaseItemStackPrice(int x, int y, int z, int price) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.price = price;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
        price = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        buf.writeInt(price);
    }

    public static class MessageSetPurchaseItemStackPriceHandler implements IMessageHandler<MessageSetPurchaseItemStackPrice, MessageSetPurchaseItemStackPrice> {
        @Override
        public MessageSetPurchaseItemStackPrice onMessage(MessageSetPurchaseItemStackPrice message, MessageContext ctx) {
            TileEntity tileraw = ZombieTC.proxy.getWorld(ctx.side).getTileEntity(message.x, message.y, message.z);
            if (tileraw != null && tileraw.getClass() == TilePurchaseItemStack.class) {
                TilePurchaseItemStack tile = (TilePurchaseItemStack) tileraw;
                tile.setPrice(message.price);
            }
            if (ctx.side == Side.SERVER) {
                ZombieTC.network.sendToAll(message);
            }
            return null;
        }
    }
}
