package net.gigimoi.zombietc.proxy;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.gigimoi.zombietc.ZombieTC;
import net.gigimoi.zombietc.block.purchasable.TilePurchaseItemStack;
import net.gigimoi.zombietc.block.purchasable.TileRendererPurchaseItemstack;
import net.gigimoi.zombietc.event.client.FogManager;
import net.gigimoi.zombietc.event.client.MainGuiOverrideManager;
import net.gigimoi.zombietc.pathfinding.BlockNode;
import net.gigimoi.zombietc.pathfinding.TileNode;
import net.gigimoi.zombietc.pathfinding.TileRendererNode;
import net.gigimoi.zombietc.weapon.ItemWeapon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
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
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(BlockNode.instance), BlockNode.instance);
        ClientRegistry.bindTileEntitySpecialRenderer(TileNode.class, new TileRendererNode());
        ClientRegistry.bindTileEntitySpecialRenderer(TilePurchaseItemStack.class, new TileRendererPurchaseItemstack());
    }

    @Override
    public void keyBinds() {
        reset = new KeyBinding("key.binding.toggleEditorMode", Keyboard.KEY_G, "key.categories.zombietc");
        reload = new KeyBinding("key.binding.reload", Keyboard.KEY_R, "key.categories.zombietc");
        activate = new KeyBinding("key.binding.activate", Keyboard.KEY_F, "key.categories.zombietc");
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
    public World getWorld(Side sidePrefered) {
        if (sidePrefered == Side.SERVER) {
            if (Minecraft.getMinecraft().isIntegratedServerRunning()) {
                return MinecraftServer.getServer().getEntityWorld();
            }
        }
        return Minecraft.getMinecraft().theWorld;
    }

    @Override
    public EntityPlayer getPlayer() {
        return Minecraft.getMinecraft().thePlayer;
    }

    @Override
    public void registerGui() {
        ZombieTC.mainGuiOverrideManager = new MainGuiOverrideManager();
        ZombieTC.fogManager = new FogManager();
        FMLCommonHandler.instance().bus().register(ZombieTC.mainGuiOverrideManager);
        FMLCommonHandler.instance().bus().register(ZombieTC.fogManager);
        MinecraftForge.EVENT_BUS.register(ZombieTC.mainGuiOverrideManager);
        MinecraftForge.EVENT_BUS.register(ZombieTC.fogManager);

    }
}
