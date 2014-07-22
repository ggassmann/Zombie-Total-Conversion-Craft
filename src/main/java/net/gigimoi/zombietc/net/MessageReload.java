package net.gigimoi.zombietc.net;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import net.gigimoi.zombietc.ZombieTC;
import net.gigimoi.zombietc.weapon.ItemWeapon;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;

/**
 * Created by gigimoi on 7/19/2014.
 */
public class MessageReload implements IMessage {
    EntityPlayer reloader;

    public MessageReload() { }
    public MessageReload(EntityPlayer reloader) {
        this.reloader = reloader;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        reloader = (EntityPlayer)ZombieTC.proxy.getEntityByID(buf.readInt());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(reloader.getEntityId());
    }

    public static class MessageReloadHandler implements IMessageHandler<MessageReload, MessageReload> {
        @Override
        public MessageReload onMessage(MessageReload message, MessageContext ctx) {
            ItemStack stack = message.reloader.getHeldItem();
            if(stack != null && stack.getItem().getClass() == ItemWeapon.class) {
                stack.getTagCompound().setInteger("Reload Timer", ((ItemWeapon) message.reloader.getHeldItem().getItem()).reloadTime);
                if(ctx.side == Side.SERVER) {
                    ZombieTC.network.sendToAll(message);
                }
            }
            return null;
        }
    }
}
