package net.gigimoi.zombietc.net.map;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import net.gigimoi.zombietc.ZombieTC;
import net.gigimoi.zombietc.block.TileNode;
import net.minecraft.world.World;

/**
 * Created by gigimoi on 7/26/2014.
 */
public class MessageChangeNodeDisabledUntilEvent implements IMessage {
    public int x;
    public int y;
    public int z;
    public boolean value;

    public MessageChangeNodeDisabledUntilEvent() {
    }

    public MessageChangeNodeDisabledUntilEvent(int x, int y, int z, boolean value) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.value = value;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
        value = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        buf.writeBoolean(value);
    }

    public static class MessageChangeNodeDisabledUntilEventHandler implements IMessageHandler<MessageChangeNodeDisabledUntilEvent, MessageChangeNodeDisabledUntilEvent> {
        @Override
        public MessageChangeNodeDisabledUntilEvent onMessage(MessageChangeNodeDisabledUntilEvent message, MessageContext ctx) {
            World world = ZombieTC.proxy.getWorld(ctx.side);
            TileNode tile = (TileNode) world.getTileEntity(message.x, message.y, message.z);
            tile.deactivatedUntilEvent = message.value;
            if (message.value == false) {
                tile.eventWaitFor = "";
            }
            if (ctx.side == Side.SERVER) {
                ZombieTC.network.sendToAll(message);
            }
            return null;
        }
    }
}
