package net.gigimoi.zombietc.net;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import net.gigimoi.zombietc.ZombieTC;
import net.gigimoi.zombietc.item.weapon.ItemWeapon;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by gigimoi on 7/17/2014.
 */
public class MessageTryShoot implements IMessage {
    Entity at;

    public MessageTryShoot() {
    }

    public MessageTryShoot(Entity playAt) {
        at = playAt;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        at = ZombieTC.proxy.getWorld(Side.SERVER).getEntityByID(buf.readInt());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(at.getEntityId());
    }

    public static class MessagePlayShootSoundHandler implements IMessageHandler<MessageTryShoot, MessageTryShoot> {
        @Override
        public MessageTryShoot onMessage(MessageTryShoot message, MessageContext ctx) {
            if (ctx.side == Side.SERVER) {
                ItemStack stack = ((EntityLivingBase) message.at).getHeldItem();
                if (stack != null && stack.hasTagCompound()) {
                    NBTTagCompound tag = stack.getTagCompound();
                    if (stack != null && stack.getItem().getClass() == ItemWeapon.class) {
                        ItemWeapon weapon = (ItemWeapon) stack.getItem();
                        tag.setInteger("Rounds", tag.getInteger("Rounds") - 1);
                        tag.setInteger("ShootCooldown", weapon.fireDelay);
                        ZombieTC.network.sendToAll(message);
                    }
                }
            } else {
                ZombieTC.proxy.playSound("pistolShoot", (float) message.at.posX, (float) message.at.posY, (float) message.at.posZ);
            }
            return null;
        }
    }
}