package net.gigimoi.zombietc.block;

import net.gigimoi.zombietc.tile.TileChanceChest;
import net.gigimoi.zombietc.util.DirectionHelper;
import net.minecraft.block.Block;
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
        if(tile.direction == 0 || tile.direction == 2) {
            world.setBlock(x + 1, y, z, BlockChanceChest.instance);
            world.setBlock(x - 1, y, z, BlockChanceChest.instance);
        }
        if(tile.direction == 3 || tile.direction == 1) {
            world.setBlock(x, y, z + 1, BlockChanceChest.instance);
            world.setBlock(x, y, z - 1, BlockChanceChest.instance);
        }
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

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        TileChanceChest tile = (TileChanceChest) world.getTileEntity(x, y, z);
        if(tile.direction == 0 || tile.direction == 2) {
            world.setBlockToAir(x + 1, y, z);
            world.setBlockToAir(x - 1, y, z);
        }
        if(tile.direction == 3 || tile.direction == 1) {
            world.setBlockToAir(x, y, z + 1);
            world.setBlockToAir(x, y, z - 1);
        }
        if(tile.direction == -1) {
            for(int i = x - 1; i <= x + 1; i++) {
                for(int j = z - 1; j <= z + 1; j++) {
                    if(world.getBlock(i, y, j) == this) {
                        world.setBlockToAir(i, y, j);
                    }
                }
            }
        }
        super.breakBlock(world, x, y, z, block, meta);
    }
}
