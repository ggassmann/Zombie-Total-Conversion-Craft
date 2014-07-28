package net.gigimoi.zombietc.net.map;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.gigimoi.zombietc.ZombieTC;
import net.gigimoi.zombietc.pathfinding.Point3;
import net.minecraft.server.MinecraftServer;

/**
 * Created by gigimoi on 7/22/2014.
 */
public class MessageAddBarricade implements IMessage {
    int x;
    int y;
    int z;

    public MessageAddBarricade() { }
    public MessageAddBarricade(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
    }
    public static class MessageAddBarricadeHandler implements IMessageHandler<MessageAddBarricade, MessageAddBarricade> {
        @Override
        public MessageAddBarricade onMessage(MessageAddBarricade message, MessageContext ctx) {
            if(MinecraftServer.getServer() != null && MinecraftServer.getServer().isServerRunning()) {
                return null;
            }
            ZombieTC.gameManager.blockBarricades.add(new Point3(message.x, message.y, message.z));
            return null;
        }
    }
}
