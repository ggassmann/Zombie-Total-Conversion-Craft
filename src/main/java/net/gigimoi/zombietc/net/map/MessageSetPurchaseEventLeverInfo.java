package net.gigimoi.zombietc.net.map;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import net.gigimoi.zombietc.ZombieTC;
import net.gigimoi.zombietc.block.TilePurchaseEventLever;
import net.gigimoi.zombietc.helpers.ByteBufHelper;

/**
 * Created by gigimoi on 7/27/2014.
 */
public class MessageSetPurchaseEventLeverInfo implements IMessage {
    int x;
    int y;
    int z;
    String event;
    int price;

    public MessageSetPurchaseEventLeverInfo() {
    }

    public MessageSetPurchaseEventLeverInfo(int x, int y, int z, String event, int price) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.event = event;
        this.price = price;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
        event = ByteBufHelper.readString(buf);
        price = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        ByteBufHelper.writeString(event, buf);
        buf.writeInt(price);
    }

    public static class MessageSetPurchaseEventLeverInfoHandler implements IMessageHandler<MessageSetPurchaseEventLeverInfo, MessageSetPurchaseEventLeverInfo> {
        @Override
        public MessageSetPurchaseEventLeverInfo onMessage(MessageSetPurchaseEventLeverInfo message, MessageContext ctx) {
            TilePurchaseEventLever leverTile = (TilePurchaseEventLever) ZombieTC.proxy.getWorld(ctx.side).getTileEntity(message.x, message.y, message.z);
            leverTile.setPrice(message.price);
            leverTile.event = message.event;
            if (ctx.side == Side.SERVER) {
                ZombieTC.network.sendToAll(message);
            }
            return null;
        }
    }
}
