package net.gigimoi.zombietc.net;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import net.gigimoi.zombietc.ZombieTC;
import net.gigimoi.zombietc.pathfinding.BlockNode;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Vec3;

/**
 * Created by gigimoi on 7/16/2014.
 */
public class MessageRemoveNodeConnection implements IMessage {
    Vec3 first;
    Vec3 second;
    public MessageRemoveNodeConnection() {}
    public MessageRemoveNodeConnection(Vec3 v1, Vec3 v2) {
        first = v1;
        second = v2;
    }
    @Override
    public void fromBytes(ByteBuf buf) {
        first = Vec3.createVectorHelper(
                buf.readInt(),
                buf.readInt(),
                buf.readInt()
        );
        second = Vec3.createVectorHelper(
                buf.readInt(),
                buf.readInt(),
                buf.readInt()
        );
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt((int)first.xCoord);
        buf.writeInt((int)first.yCoord);
        buf.writeInt((int)first.zCoord);
        buf.writeInt((int)second.xCoord);
        buf.writeInt((int)second.yCoord);
        buf.writeInt((int)second.zCoord);
    }
    public static class MessageRemoveNodeConnectionHandler implements IMessageHandler<MessageRemoveNodeConnection, MessageRemoveNodeConnection> {

        @Override
        public MessageRemoveNodeConnection onMessage(MessageRemoveNodeConnection message, MessageContext ctx) {
            BlockNode.removeNodeConnection(message.first, message.second);
            if(ctx.side == Side.SERVER && !MinecraftServer.getServer().isSinglePlayer()) {
                ZombieTC.network.sendToAll(message);
            }
            return null;
        }
    }
}
