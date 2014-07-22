package net.gigimoi.zombietc.proxy;

import cpw.mods.fml.client.registry.ClientRegistry;
import net.gigimoi.zombietc.*;
import net.gigimoi.zombietc.pathfinding.TileNode;
import net.gigimoi.zombietc.pathfinding.TileRendererNode;
import net.gigimoi.zombietc.weapon.ItemWeapon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.MinecraftForgeClient;
import org.lwjgl.input.Keyboard;

/**
 * Created by gigimoi on 7/16/2014.
 */
public class ClientProxy extends CommonProxy {
    public static KeyBinding reset;
    public static KeyBinding reload;
    public static KeyBinding activate;

    @Override
    public void renderers() {
        super.renderers();
        ClientRegistry.bindTileEntitySpecialRenderer(TileNode.class, new TileRendererNode());
        ClientRegistry.bindTileEntitySpecialRenderer(TilePurchaseItemStack.class, new TileRendererPurchaseItemstack());
    }
    @Override
    public void keyBinds() {
        reset = new KeyBinding("Toggle Editor Mode", Keyboard.KEY_G, "key.categories.zombietc");
        reload = new KeyBinding("Reload", Keyboard.KEY_R, "key.categories.zombietc");
        activate = new KeyBinding("Activate", Keyboard.KEY_F, "key.categories.zombietc");
        ClientRegistry.registerKeyBinding(reset);
        ClientRegistry.registerKeyBinding(reload);
        ClientRegistry.registerKeyBinding(activate);
    }

    @Override
    public void playSound(String soundName, float x, float y, float z) {
        PositionedSoundRecord snd = PositionedSoundRecord.func_147675_a(new ResourceLocation(ZombieTC.MODID, soundName), x, y, z);
        Minecraft.getMinecraft().getSoundHandler().playSound(snd);
    }

    @Override
    public void registerWeaponRender(ItemWeapon weapon) {
        MinecraftForgeClient.registerItemRenderer(weapon, weapon);
    }

    @Override
    public EntityPlayer getPlayerSafe() {
        return Minecraft.getMinecraft().thePlayer;
    }

    @Override
    public Entity getEntityByID(int id) {
        return Minecraft.getMinecraft().theWorld.getEntityByID(id);
    }

    @Override
    public TileEntity getTileEntity(int x, int y, int z) {
        return Minecraft.getMinecraft().theWorld.getTileEntity(x, y, z);
    }
}
