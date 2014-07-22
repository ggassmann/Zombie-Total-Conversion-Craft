package net.gigimoi.zombietc.net.map;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import net.gigimoi.zombietc.ZombieTC;
import net.gigimoi.zombietc.pathfinding.BlockNode;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Vec3;

/**
 * Created by gigimoi on 7/16/2014.
 */
public class MessageRemoveNode implements IMessage {
    Vec3 pos;
    public MessageRemoveNode() {}
    public MessageRemoveNode(int x, int y, int z) {
        pos = Vec3.createVectorHelper(x, y, z);
    }
    @Override
    public void fromBytes(ByteBuf buf) {
        pos = Vec3.createVectorHelper(buf.readInt(), buf.readInt(), buf.readInt());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt((int)pos.xCoord);
        buf.writeInt((int)pos.yCoord);
        buf.writeInt((int)pos.zCoord);
    }
    public static class MessageRemoveNodeHandler implements IMessageHandler<MessageRemoveNode, MessageRemoveNode> {
        @Override
        public MessageRemoveNode onMessage(MessageRemoveNode message, MessageContext ctx) {
            BlockNode.removeNodeAt(message.pos.xCoord, message.pos.yCoord, message.pos.zCoord);
            if(ctx.side == Side.SERVER) {
                ZombieTC.network.sendToAll(message);
            }
            return null;
        }
    }
}
