package net.gigimoi.zombietc.helpers;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

/**
 * Created by gigimoi on 7/20/2014.
 */
public class NetHelper {
    public static Entity getEntityByID(int id) {
        return getWorldUnsided().getEntityByID(id);
    }
    public static World getWorldUnsided() {
        return (MinecraftServer.getServer() != null && MinecraftServer.getServer().getEntityWorld() != null && MinecraftServer.getServer().isServerRunning()) ?
                MinecraftServer.getServer().getEntityWorld() :
                Minecraft.getMinecraft().thePlayer.worldObj;
    }
    public static World getClientWorld() {
        return Minecraft.getMinecraft().theWorld;
    }
}
