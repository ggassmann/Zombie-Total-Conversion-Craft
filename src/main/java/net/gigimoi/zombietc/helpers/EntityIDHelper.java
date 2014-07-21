package net.gigimoi.zombietc.helpers;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;

/**
 * Created by gigimoi on 7/20/2014.
 */
public class EntityIDHelper {
    public static Entity getEntityID(int id) {
        return (MinecraftServer.getServer() != null && MinecraftServer.getServer().getEntityWorld() != null && MinecraftServer.getServer().isServerRunning()) ?
                MinecraftServer.getServer().getEntityWorld().getEntityByID(id) :
                Minecraft.getMinecraft().thePlayer.worldObj.getEntityByID(id);
    }
}
