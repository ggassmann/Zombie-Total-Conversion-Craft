package net.gigimoi.zombietc.proxy;

import cpw.mods.fml.client.registry.ClientRegistry;
import net.gigimoi.zombietc.ZombieTC;
import net.gigimoi.zombietc.pathfinding.BlockNode;
import net.gigimoi.zombietc.pathfinding.TileNode;
import net.gigimoi.zombietc.pathfinding.TileRendererNode;
import net.gigimoi.zombietc.weapon.ItemWeapon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.MinecraftForgeClient;
import org.lwjgl.input.Keyboard;

/**
 * Created by gigimoi on 7/16/2014.
 */
public class ClientProxy extends CommonProxy {
    public static KeyBinding reset;
    public static KeyBinding reload;
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
        reload = new KeyBinding("Reload", Keyboard.KEY_R, "key.categories.zombietc");
        ClientRegistry.registerKeyBinding(reset);
        ClientRegistry.registerKeyBinding(reload);
    }

    @Override
    public void playSound(String soundName, float x, float y, float z) {
        PositionedSoundRecord snd = PositionedSoundRecord.func_147675_a(new ResourceLocation(ZombieTC.MODID, soundName), x, y, z);
        Minecraft.getMinecraft().getSoundHandler().playSound(snd);
    }
}
