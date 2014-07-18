package net.gigimoi.zombietc.net;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import net.gigimoi.zombietc.helpers.SoundHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;

/**
 * Created by gigimoi on 7/17/2014.
 */
public class MessagePlayShootSound implements IMessage {
    Entity at;

    public MessagePlayShootSound() { }
    public MessagePlayShootSound(Entity playAt) {
        at = playAt;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        at = MinecraftServer.getServer().isServerRunning() ?
                MinecraftServer.getServer().getEntityWorld().getEntityByID(buf.readInt()) :
                Minecraft.getMinecraft().thePlayer.worldObj.getEntityByID(buf.readInt());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(at.getEntityId());
    }
    public static class MessagePlayShootSoundHandler implements IMessageHandler<MessagePlayShootSound, MessagePlayShootSound> {

        @Override
        public MessagePlayShootSound onMessage(MessagePlayShootSound message, MessageContext ctx) {
            if(ctx.side == Side.SERVER) {
                SoundHelper.onEntityPlay("pistolShoot", message.at.worldObj, message.at, 2, 1);
            }
            return null;
        }
    }
}
