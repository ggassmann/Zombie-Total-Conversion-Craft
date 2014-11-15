package net.gigimoi.zombietc.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.gigimoi.zombietc.CommonProxy;
import net.gigimoi.zombietc.ZombieTC;
import net.gigimoi.zombietc.block.BlockBarricade;
import net.gigimoi.zombietc.block.BlockNode;
import net.gigimoi.zombietc.block.BlockNodeDoor;
import net.gigimoi.zombietc.client.event.FogManager;
import net.gigimoi.zombietc.client.event.MainGuiOverrideManager;
import net.gigimoi.zombietc.client.event.PlayerBuffRenderManager;
import net.gigimoi.zombietc.client.tilerenderer.*;
import net.gigimoi.zombietc.client.tilerenderer.purchasable.TileRendererPurchaseItemstack;
import net.gigimoi.zombietc.item.weapon.ItemWeapon;
import net.gigimoi.zombietc.tile.*;
import net.gigimoi.zombietc.tile.purchasable.TilePurchaseItemStack;
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
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(BlockNodeDoor.instance), BlockNodeDoor.instance);
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(BlockNodeDoor.iron), BlockNodeDoor.iron);
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(BlockBarricade.wooden), BlockBarricade.wooden);
        ClientRegistry.bindTileEntitySpecialRenderer(TileNode.class, new TileRendererNode());
        ClientRegistry.bindTileEntitySpecialRenderer(TileNodeDoor.class, new TileRendererDoorNode());
        ClientRegistry.bindTileEntitySpecialRenderer(TilePurchaseItemStack.class, new TileRendererPurchaseItemstack());
        ClientRegistry.bindTileEntitySpecialRenderer(TileBarricade.class, new TileRendererBarricade());
        ClientRegistry.bindTileEntitySpecialRenderer(TileChanceChest.class, new TileRendererChanceChest());
        ClientRegistry.bindTileEntitySpecialRenderer(TileBlockImpassible.class, new TileRendererImpassibleBlock());
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
        ZombieTC.playerBuffRenderManager = new PlayerBuffRenderManager();
        FMLCommonHandler.instance().bus().register(ZombieTC.mainGuiOverrideManager);
        FMLCommonHandler.instance().bus().register(ZombieTC.fogManager);
        FMLCommonHandler.instance().bus().register(ZombieTC.playerBuffRenderManager);
        MinecraftForge.EVENT_BUS.register(ZombieTC.mainGuiOverrideManager);
        MinecraftForge.EVENT_BUS.register(ZombieTC.fogManager);
        MinecraftForge.EVENT_BUS.register(ZombieTC.playerBuffRenderManager);

    }
}
