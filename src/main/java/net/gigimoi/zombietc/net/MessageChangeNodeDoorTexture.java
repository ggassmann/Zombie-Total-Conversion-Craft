package net.gigimoi.zombietc.net;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import net.gigimoi.zombietc.ZombieTC;
import net.gigimoi.zombietc.tile.TileNodeDoor;
import net.minecraft.server.MinecraftServer;

/**
 * Created by Gavin on 11/15/2014.
 */
public class MessageChangeNodeDoorTexture implements IMessage {
    public int id;
    public int x;
    public int y;
    public int z;
    public MessageChangeNodeDoorTexture() {}
    public MessageChangeNodeDoorTexture(int textureID, int x, int y, int z) {
        this.id = textureID;
        this.x = x;
        this.y = y;
        this.z = z;
    }
    @Override
    public void fromBytes(ByteBuf buf) {
        id = buf.readInt();
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(id);
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
    }
    public static class MessageChangeNodeDoorTextureHandler implements IMessageHandler<MessageChangeNodeDoorTexture, MessageChangeNodeDoorTexture> {
        @Override
        public MessageChangeNodeDoorTexture onMessage(MessageChangeNodeDoorTexture message, MessageContext ctx) {
            TileNodeDoor door = (TileNodeDoor)ZombieTC.proxy.getWorld(ctx.side).getTileEntity(message.x, message.y, message.z);
            if(door != null) {
                door.textureID = message.id;
            }
            if(ctx.side == Side.SERVER) {
                ZombieTC.network.sendToAll(message);
            }
            return null;
        }
    }
}
