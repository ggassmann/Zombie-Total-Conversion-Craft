package net.gigimoi.zombietc.block;

import net.gigimoi.zombietc.tile.TileNodeDoor;
import net.gigimoi.zombietc.util.DirectionHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by gigimoi on 8/5/2014.
 */
public class BlockNodeDoor extends BlockNode {
    public static final BlockNodeDoor iron = new BlockNodeDoor();

    public BlockNodeDoor() {
        setBlockName("Node Door");
        setHardness(1.0f);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileNodeDoor();
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack) {
        TileNodeDoor tile = getTile(world, x, y, z);
        tile.direction = DirectionHelper.getPlayerDirection((EntityPlayer) player);
    }

    private TileNodeDoor getTile(World world, int x, int y, int z) {
        return (TileNodeDoor) world.getTileEntity(x, y, z);
    }

    @Override
    public boolean isBlockSolid(IBlockAccess access, int x, int y, int z, int meta) {
        return false;
    }

    @Override
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB aabb, List mask, Entity entity) {
        super.addCollisionBoxesToList(world, x, y, z, aabb, mask, entity);
        /*
        if (entity != null) {
            if (entity.getClass() == EntityZZombie.class) {
                super.addCollisionBoxesToList(world, x, y, z, aabb, mask, entity);
            } else {
                if (!ZombieTC.editorModeManager.enabled) {
                    super.addCollisionBoxesToList(world, x, y, z, aabb, mask, entity);
                }
            }
        } else {
            super.addCollisionBoxesToList(world, x, y, z, aabb, mask, entity);
        }
        */
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
}