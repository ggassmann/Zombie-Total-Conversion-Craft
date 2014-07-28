package net.gigimoi.zombietc.net;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import net.gigimoi.zombietc.ZombieTC;
import net.gigimoi.zombietc.block.ITileEntityActivatable;
import net.minecraft.entity.Entity;

/**
 * Created by gigimoi on 7/26/2014.
 */
public class MessageActivateTile implements IMessage {
    int x;
    int y;
    int z;
    Entity activator;
    public MessageActivateTile() { }
    public MessageActivateTile(int xCoord, int yCoord, int zCoord, Entity activator) {
        x = xCoord;
        y = yCoord;
        z = zCoord;
        this.activator = activator;
    }
    @Override
    public void fromBytes(ByteBuf buf) {
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
        activator = ZombieTC.proxy.getWorld(Side.SERVER).getEntityByID(buf.readInt());
    }
    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        buf.writeInt(activator.getEntityId());
    }
    public static class MessageActivateTileHandler implements IMessageHandler<MessageActivateTile, MessageActivateTile> {
        @Override
        public MessageActivateTile onMessage(MessageActivateTile message, MessageContext ctx) {
            ITileEntityActivatable tile = (ITileEntityActivatable) ZombieTC.proxy.getWorld(ctx.side).getTileEntity(message.x, message.y, message.z);
            tile.activate(message.activator, ctx.side);
            if(ctx.side.isServer()) {
                ZombieTC.network.sendToAll(message);
            }
            return null;
        }
    }
}
