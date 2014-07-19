package net.gigimoi.zombietc.net;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import net.gigimoi.zombietc.EntityZZombie;
import net.gigimoi.zombietc.ZombieTC;
import net.gigimoi.zombietc.helpers.SoundHelper;
import net.gigimoi.zombietc.weapon.ItemWeapon;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

/**
 * Created by gigimoi on 7/17/2014.
 */
public class MessageShoot implements IMessage {
    public Entity shooter;
    public Entity hit;
    public Item weapon;
    public MessageShoot() { }
    public MessageShoot(Entity shooter, Entity hit, Item weapon) {
        this.shooter = shooter;
        this.weapon = weapon;
        this.hit = hit;
    }
    @Override
    public void fromBytes(ByteBuf buf) {
        shooter = MinecraftServer.getServer().isServerRunning() ?
                MinecraftServer.getServer().getEntityWorld().getEntityByID(buf.readInt()) :
                Minecraft.getMinecraft().thePlayer.worldObj.getEntityByID(buf.readInt());
        hit = MinecraftServer.getServer().isServerRunning() ?
                MinecraftServer.getServer().getEntityWorld().getEntityByID(buf.readInt()) :
                Minecraft.getMinecraft().thePlayer.worldObj.getEntityByID(buf.readInt());
        weapon = Item.getItemById(buf.readInt());
    }
    @Override
    public void toBytes(ByteBuf buf) {
        if(shooter != null) {
            buf.writeInt(shooter.getEntityId());
        } else {
            buf.writeInt(-1);
        }
        if(hit != null) {
            buf.writeInt(hit.getEntityId());
        }
        else {
            buf.writeInt(-1);
        }
        buf.writeInt(Item.getIdFromItem(weapon));
    }
    public static class MessageShootHandler implements IMessageHandler<MessageShoot, MessageShoot> {
        @Override
        public MessageShoot onMessage(MessageShoot message, MessageContext ctx) {
            World world = ((EntityLivingBase)message.shooter).worldObj;
            if(message.hit != null && message.shooter != null) {
                message.hit.attackEntityFrom(DamageSource.generic, 2);
            }
            if(ctx.side == Side.SERVER) {
                ZombieTC.network.sendToAll(message);
            } else {
                if(message.shooter.getClass() == EntityPlayer.class) {
                    EntityPlayer player = (EntityPlayer) message.shooter;
                    ItemWeapon.ensureTagCompund(player.getHeldItem());
                }
            }
            return null;
        }
    }
}
