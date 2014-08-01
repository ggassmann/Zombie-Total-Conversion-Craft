package net.gigimoi.zombietc;

import baubles.api.IBauble;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;

/**
 * Created by gigimoi on 8/1/2014.
 */
public abstract class ItemZombieTCBauble extends Item implements IBauble {
    @Override
    public boolean canEquip(ItemStack stack, EntityLivingBase entityLivingBase) {
        return true;
    }

    @Override
    public boolean canUnequip(ItemStack stack, EntityLivingBase entityLivingBase) {
        System.out.println(entityLivingBase.getClass());
        if(entityLivingBase.getClass() == EntityPlayerMP.class) {
            EntityPlayer entityPlayer = (EntityPlayer) entityLivingBase;
            entityPlayer.addChatMessage(new ChatComponentText(stack.getDisplayName() + " is bound to you"));
        }
        return false;
    }
}
