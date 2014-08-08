package net.gigimoi.zombietc.net.map;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import net.gigimoi.zombietc.ZombieTC;
import net.gigimoi.zombietc.tile.TileNode;
import net.gigimoi.zombietc.util.ByteBufHelper;
import net.minecraft.world.World;

/**
 * Created by gigimoi on 7/26/2014.
 */
public class MessageChangeNodeEventWaitFor implements IMessage {
    int x;
    int y;
    int z;
    String event;

    public MessageChangeNodeEventWaitFor() {
    }

    public MessageChangeNodeEventWaitFor(int x, int y, int z, String event) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.event = event;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
        event = ByteBufHelper.readString(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        ByteBufHelper.writeString(event, buf);
    }

    public static class MessageChangeNodeEventWaitForHandler implements IMessageHandler<MessageChangeNodeEventWaitFor, MessageChangeNodeEventWaitFor> {
        @Override
        public MessageChangeNodeEventWaitFor onMessage(MessageChangeNodeEventWaitFor message, MessageContext ctx) {
            World world = ZombieTC.proxy.getWorld(ctx.side);
            TileNode tile = (TileNode) world.getTileEntity(message.x, message.y, message.z);
            tile.eventWaitFor = message.event;
            if (message.event == "") {
                tile.deactivatedUntilEvent = true;
            }
            if (ctx.side == Side.SERVER) {
                ZombieTC.network.sendToAll(message);
            }
            return null;
        }
    }
}
