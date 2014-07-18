package net.gigimoi.zombietc.proxy;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import net.gigimoi.zombietc.ZombieTC;
import net.gigimoi.zombietc.pathfinding.BlockNode;
import net.gigimoi.zombietc.pathfinding.TileNode;
import net.gigimoi.zombietc.pathfinding.TileRendererNode;
import net.gigimoi.zombietc.weapon.ItemWeapon;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;

/**
 * Created by gigimoi on 7/16/2014.
 */
public class ClientProxy extends CommonProxy {
    public static KeyBinding reset;
    @Override
    public void renderers() {
        super.renderers();
        ClientRegistry.bindTileEntitySpecialRenderer(TileNode.class, new TileRendererNode());

        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(BlockNode.instance), new TileNode());
        MinecraftForgeClient.registerItemRenderer(ItemWeapon.radomVis, ItemWeapon.radomVis);
    }
    @Override
    public void keyBinds() {
        reset = new KeyBinding("Reset", Keyboard.KEY_MULTIPLY, "key.categories.zombietc");
        ClientRegistry.registerKeyBinding(reset);
    }
}
