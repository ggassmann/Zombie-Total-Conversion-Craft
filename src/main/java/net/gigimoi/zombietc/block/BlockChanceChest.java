package net.gigimoi.zombietc.block;

import net.gigimoi.zombietc.tile.TileChanceChest;
import net.gigimoi.zombietc.util.DirectionHelper;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by gigimoi on 8/30/2014.
 */
public class BlockChanceChest extends BlockContainerZTC {
    public static BlockChanceChest instance = new BlockChanceChest();
    protected BlockChanceChest() {
        super(Material.rock);
        setHardness(0.2f);
        setResistance(2f);
        setBlockName("Chance Chest");
    }
    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack) {
        TileChanceChest tile = (TileChanceChest) world.getTileEntity(x, y, z);
        tile.direction = DirectionHelper.getPlayerDirection((EntityPlayer) player);
    }

    @Override
    public int getRenderType() {
        return -1;
    }
    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }
    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        TileChanceChest tileChanceChest = new TileChanceChest();
        tileChanceChest.direction = -1;
        return tileChanceChest;
    }
}
