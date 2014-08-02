package net.gigimoi.zombietc.event;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.gigimoi.zombietc.EntityZZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;

/**
 * Created by gigimoi on 8/2/2014.
 */
public class LivingManager {
    @SubscribeEvent
    public void onLivingSpawnEvent(LivingSpawnEvent event) {
        if (EntityPlayer.class.isAssignableFrom(event.entityLiving.getClass())) {
            EntityPlayer player = (EntityPlayer)event.entityLiving;
            NBTTagCompound tag = new NBTTagCompound();
            player.writeToNBT(tag);
            tag.setInteger("Time Since Hit", 0);
            player.readFromNBT(tag);
        }
    }
    @SubscribeEvent
    public void onAllowDespawn(LivingSpawnEvent.AllowDespawn event) {
        if(event.entityLiving.getClass() == EntityZZombie.class) {
            event.setResult(Event.Result.DENY);
        }
    }
}
