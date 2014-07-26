package net.gigimoi.zombietc.net.map;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import net.gigimoi.zombietc.ZombieTC;
import net.gigimoi.zombietc.pathfinding.BlockNode;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Vec3;

/**
 * Created by gigimoi on 7/16/2014.
 */
public class MessageAddNodeConnection implements IMessage {
    Vec3 first;
    Vec3 second;
    public MessageAddNodeConnection() {}
    public MessageAddNodeConnection(Vec3 v1, Vec3 v2) {
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
    public static class MessageAddNodeConnectionHandler implements IMessageHandler<MessageAddNodeConnection, MessageAddNodeConnection> {

        @Override
        public MessageAddNodeConnection onMessage(MessageAddNodeConnection message, MessageContext ctx) {
            BlockNode.addNodeConnection(message.first, message.second);
            ZombieTC.gameManager.regeneratePathMap();
            if(ctx.side == Side.SERVER) {
                System.out.println("Server recieved add node connection");
                ZombieTC.network.sendToAll(message);
            }
            return null;
        }
    }
}
