package net.gigimoi.zombietc.block;

import net.gigimoi.zombietc.ZombieTC;
import net.gigimoi.zombietc.entity.EntityZZombie;
import net.gigimoi.zombietc.tile.TileBarricade;
import net.gigimoi.zombietc.tile.TileBlockImpassible;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by Gavin on 11/15/2014.
 */
public class BlockPlayerImpassible extends BlockContainerZTC {
    public final static BlockPlayerImpassible instance = new BlockPlayerImpassible();
    protected BlockPlayerImpassible() {
        super(Material.rock);
        setBlockName("Player Impassible Block");
    }

    @Override
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB aabb, List mask, Entity entity) {
        if (entity == null || entity.getClass() == EntityZZombie.class) {
            return;
        }
        super.addCollisionBoxesToList(world, x, y, z, aabb, mask, entity);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileBlockImpassible();
    }

    @Override
    public int getLightOpacity() {
        return 0;
    }

    @Override
    public boolean isBlockSolid(IBlockAccess access, int x, int y, int z, int meta) {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess access, int x, int y, int z) {
        super.setBlockBoundsBasedOnState(access, x, y, z);
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean getBlocksMovement(IBlockAccess access, int x, int y, int z) {
        return false;
    }
    @Override
    public int getRenderType() {
        return -1;
    }
}
