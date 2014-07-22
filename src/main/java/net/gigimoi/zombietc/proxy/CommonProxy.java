package net.gigimoi.zombietc.proxy;

import cpw.mods.fml.common.network.IGuiHandler;
import net.gigimoi.zombietc.ZombieTC;
import net.gigimoi.zombietc.gui.GuiPurchaseItemStack;
import net.gigimoi.zombietc.weapon.ItemWeapon;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by gigimoi on 7/16/2014.
 */
public class CommonProxy implements IGuiHandler {
    public void renderers() {
    }
    public void keyBinds() {
    }
    public void playSound(String soundName, float x, float y, float z) {
    }
    public void registerWeaponRender(ItemWeapon weapon) {
    }
    public EntityPlayer getPlayerSafe() {
        System.out.println("ERROR: Tried to get player on server");
        return null;
    }
    public Entity getEntityByID(int id) {
        return MinecraftServer.getServer().getEntityWorld().getEntityByID(id);
    }
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if ( ID == GuiPurchaseItemStack.GUI_ID )
            return new GuiPurchaseItemStack(x, y, z);
        return null;
    }

    public TileEntity getTileEntity(int x, int y, int z) {
        return MinecraftServer.getServer().getEntityWorld().getTileEntity(x, y, z);
    }
}
