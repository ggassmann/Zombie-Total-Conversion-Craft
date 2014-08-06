package net.gigimoi.zombietc.net;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import net.gigimoi.zombietc.entity.EntityZZombie;
import net.gigimoi.zombietc.ZombieTC;
import net.gigimoi.zombietc.event.PlayerManager;
import net.gigimoi.zombietc.item.weapon.ItemWeapon;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

/**
 * Created by gigimoi on 7/17/2014.
 */
public class MessageShoot implements IMessage {
    public Entity shooter;
    public Entity hit;
    public Item weapon;

    public MessageShoot() {
    }

    public MessageShoot(Entity shooter, Entity hit, Item weapon) {
        this.shooter = shooter;
        this.weapon = weapon;
        this.hit = hit;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        shooter = ZombieTC.proxy.getWorld(Side.SERVER).getEntityByID(buf.readInt());
        hit = ZombieTC.proxy.getWorld(Side.SERVER).getEntityByID(buf.readInt());
        weapon = Item.getItemById(buf.readInt());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        if (shooter != null) {
            buf.writeInt(shooter.getEntityId());
        } else {
            buf.writeInt(-1);
        }
        if (hit != null) {
            buf.writeInt(hit.getEntityId());
        } else {
            buf.writeInt(-1);
        }
        buf.writeInt(Item.getIdFromItem(weapon));
    }

    public static class MessageShootHandler implements IMessageHandler<MessageShoot, MessageShoot> {
        @Override
        public MessageShoot onMessage(MessageShoot message, MessageContext ctx) {
            World world = ((EntityLivingBase) message.shooter).worldObj;
            //TODO: Server side raytracing
            if(!message.hit.isDead) {
                if (message.hit != null && message.shooter != null) {
                    EntityPlayer player = null;
                    if(EntityPlayer.class.isAssignableFrom(message.shooter.getClass())) {
                        player = ZombieTC.proxy.getWorld(ctx.side).getPlayerEntityByName(message.shooter.getCommandSenderName());
                    }
                    if(player != null) {
                        PlayerManager.ZombieTCPlayerProperties.get(player).vim += 10;
                        message.hit.attackEntityFrom(DamageSource.generic, ((ItemWeapon) message.weapon).getBulletDamage());
                        if(EntityZZombie.class.isAssignableFrom(message.hit.getClass())) {
                            EntityZZombie zombie = (EntityZZombie)message.hit;
                            if(zombie.getHealth() <= 0) {
                                PlayerManager.ZombieTCPlayerProperties.get(player).vim += 90;
                            }
                        }
                        if (ctx.side == Side.SERVER) {
                            ZombieTC.network.sendToAll(message);
                        }
                    }
                }
            }
            return null;
        }
    }
}