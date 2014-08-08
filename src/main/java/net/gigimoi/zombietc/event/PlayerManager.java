package net.gigimoi.zombietc.event;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.gigimoi.zombietc.ZombieTC;
import net.gigimoi.zombietc.entity.EntityZZombie;
import net.gigimoi.zombietc.util.TextAlignment;
import net.gigimoi.zombietc.util.TextRenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;

import java.util.List;

/**
 * Created by gigimoi on 8/2/2014.
 */
public class PlayerManager {
    int applyTicker = 0;

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onRenderGameOverlayEvent(RenderGameOverlayEvent event) {
        if (!ZombieTC.editorModeManager.enabled && event.type == RenderGameOverlayEvent.ElementType.TEXT) {
            TextRenderHelper.drawString("Scoreboard", 2, 2, TextAlignment.Left);
            TextRenderHelper.drawString("==========", 2, 10, TextAlignment.Left);
            List players = ZombieTC.proxy.getWorld(Side.CLIENT).playerEntities;
            for (int i = 0; i < players.size(); i++) {
                EntityPlayer player = (EntityPlayer) players.get(i);
                int vim = ZombieTCPlayerProperties.get(player).vim;
                TextRenderHelper.drawString(player.getCommandSenderName() + ": " + vim, 2, 20 + i * 10, TextAlignment.Left);
            }
        }
    }

    @SubscribeEvent
    public void onEntityConstructing(EntityEvent.EntityConstructing event) {
        if (EntityPlayer.class.isAssignableFrom(event.entity.getClass())) {
            event.entity.registerExtendedProperties(ZombieTC.MODID, new ZombieTCPlayerProperties());
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent event) {
        applyTicker++;
        if (applyTicker >= 200) {
            applyTicker = 0;
        }
        if (event.phase == TickEvent.Phase.START) {
            World world = ZombieTC.proxy.getWorld(event.side);
            if (world == null) {
                return;
            }
            for (int i = 0; i < world.playerEntities.size(); i++) {
                EntityPlayer player = (EntityPlayer) world.playerEntities.get(i);
                ZombieTCPlayerProperties playerProperties = ZombieTCPlayerProperties.get(player);
                PotionEffect effect = new PotionEffect(Potion.regeneration.getId(), 10, 3);
                if (playerProperties.timeSinceHit < 140) {
                    playerProperties.timeSinceHit++;
                } else if (player.getHealth() < player.getMaxHealth() && applyTicker == 0) {
                    player.addPotionEffect(effect);
                }
            }
        }
    }

    @SubscribeEvent
    public void onLivingSpawnEvent(LivingSpawnEvent event) {
    }

    @SubscribeEvent
    public void onAllowDespawn(LivingSpawnEvent.AllowDespawn event) {
        if (event.entityLiving.getClass() == EntityZZombie.class) {
            event.setResult(Event.Result.DENY);
        }
    }

    public static class ZombieTCPlayerProperties implements IExtendedEntityProperties {
        public int timeSinceHit;
        public int vim;

        public static ZombieTCPlayerProperties get(EntityPlayer player) {
            return (ZombieTCPlayerProperties) player.getExtendedProperties(ZombieTC.MODID);
        }

        @Override
        public void saveNBTData(NBTTagCompound tag) {
            tag.setInteger("Time Since Hit", timeSinceHit);
            tag.setInteger("Vim", vim);
        }

        @Override
        public void loadNBTData(NBTTagCompound tag) {
            timeSinceHit = tag.getInteger("Time Since Hit");
            vim = tag.getInteger("Vim");
        }

        @Override
        public void init(Entity entity, World world) {
            timeSinceHit = 0;
            vim = 100;
        }
    }
}
