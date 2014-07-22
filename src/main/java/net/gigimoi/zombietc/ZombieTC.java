package net.gigimoi.zombietc;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.gigimoi.zombietc.event.EditorModeManager;
import net.gigimoi.zombietc.event.GameManager;
import net.gigimoi.zombietc.event.MouseManager;
import net.gigimoi.zombietc.event.NaturalSpawnStopper;
import net.gigimoi.zombietc.pathfinding.BlockNode;
import net.gigimoi.zombietc.pathfinding.ItemNodeLinker;
import net.gigimoi.zombietc.pathfinding.TileNode;
import net.gigimoi.zombietc.proxy.CommonProxy;
import net.gigimoi.zombietc.weapon.ItemWeapon;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = ZombieTC.MODID, version = ZombieTC.VERSION)
public class ZombieTC {
    public static final String MODID = "zombietc";
    public static final String VERSION = "0.1";
    public static final String NETWORK_CHANNEL = "channel_zombie_tc";

    @Mod.Instance("zombietc")
    public static ZombieTC instance;

    public static SimpleNetworkWrapper network;

    public static GameManager gameManager;
    public static EditorModeManager editorModeManager;
    public static MouseManager mouseManager;

    @SidedProxy(clientSide="net.gigimoi.zombietc.proxy.ClientProxy", serverSide="net.gigimoi.zombietc.proxy.CommonProxy")
    public static CommonProxy proxy;

    public static final CreativeTabs tab = new CreativeTabsExt("Zombie Total Conversion");

    private static class CreativeTabsExt extends CreativeTabs {
        public CreativeTabsExt(String lable) {
            super(lable);
        }
        @Override
        public Item getTabIconItem() {
            return ItemSpawnZZombie.instance();
        }
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);

        mouseManager = new MouseManager();
        editorModeManager = new EditorModeManager();
        gameManager = new GameManager();
        FMLCommonHandler.instance().bus().register(editorModeManager);
        FMLCommonHandler.instance().bus().register(gameManager);
        FMLCommonHandler.instance().bus().register(mouseManager);
        MinecraftForge.EVENT_BUS.register(gameManager);
        MinecraftForge.EVENT_BUS.register(mouseManager);
        MinecraftForge.EVENT_BUS.register(editorModeManager);
        MinecraftForge.EVENT_BUS.register(new NaturalSpawnStopper());

        new ItemSpawnZZombie();
        EntityRegistry.registerModEntity(EntityZZombie.class, "Z Zombie", 1, this, 80, 3, true);

        registerItem(ItemSpawnZZombie.instance());
        registerItem(ItemNodeLinker.instance());
        registerItem(ItemWeapon.radomVis);
        registerItem(ItemWeapon.stormRifle);
        registerItem(ItemWeapon.thompson);

        registerBlock(BlockSpawner.zombie);
        registerBlock(BlockNode.instance);

        registerBlock(BlockBarricade.wooden);
        registerBlock(BlockPurchaseItemstack.instance);

        registerTileEntity(TileSpawner.class);
        registerTileEntity(TileBarricade.class);
        registerTileEntity(TileNode.class);
        registerTileEntity(TilePurchaseItemStack.class);

        proxy.renderers();
        proxy.network();
        proxy.keyBinds();
    }

    public void registerTileEntity(Class c) {
        GameRegistry.registerTileEntity(c, c.getCanonicalName());
    }
    public void registerBlock(Block block) {
        block.setCreativeTab(tab);
        block.setBlockTextureName(MODID + ":" + block.getUnlocalizedName().substring(5));
        GameRegistry.registerBlock(block, block.getUnlocalizedName());
    }
    public void registerItem(Item item) {
        item.setCreativeTab(tab);
        item.setTextureName(MODID + ":" + item.getUnlocalizedName().substring(5));
        GameRegistry.registerItem(item, item.getUnlocalizedName(), MODID);
    }
}
