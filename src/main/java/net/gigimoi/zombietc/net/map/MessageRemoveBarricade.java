package net.gigimoi.zombietc.net.map;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.gigimoi.zombietc.event.GameManager;
import net.gigimoi.zombietc.util.Point3;
import net.minecraft.server.MinecraftServer;

/**
 * Created by gigimoi on 7/22/2014.
 */
public class MessageRemoveBarricade implements IMessage {
    public int x;
    public int y;
    public int z;

    public MessageRemoveBarricade() {
    }

    public MessageRemoveBarricade(int x, int y, int z) {
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

    public static class MessageRemoveBarricadeHandler implements IMessageHandler<MessageRemoveBarricade, MessageRemoveBarricade> {
        @Override
        public MessageRemoveBarricade onMessage(MessageRemoveBarricade message, MessageContext ctx) {
            if (MinecraftServer.getServer() != null && MinecraftServer.getServer().isServerRunning()) {
                return null;
            }
            for (int i = 0; i < GameManager.blockBarricades.size(); i++) {
                Point3 vec = GameManager.blockBarricades.get(i);
                if (vec.distanceTo(new Point3(message.x, message.y, message.z)) < 0.01) {
                    GameManager.blockBarricades.remove(i);
                }
            }
            return null;
        }
    }
}
