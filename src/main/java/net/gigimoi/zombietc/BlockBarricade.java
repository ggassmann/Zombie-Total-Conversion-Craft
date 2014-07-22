package net.gigimoi.zombietc;

import net.gigimoi.zombietc.event.GameManager;
import net.gigimoi.zombietc.net.map.MessageRemoveBarricade;
import net.gigimoi.zombietc.net.map.MessageAddBarricade;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by gigimoi on 7/16/2014.
 */
// The magnitude of the bullshit required to make this work compares favorably with that of the national debt.
public class BlockBarricade extends BlockContainer {
    public static BlockBarricade wooden = new BlockBarricade();

    public BlockBarricade() {
        super(Material.rock);
        setBlockName("Barricade");
        setHardness(1.0f);
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileBarricade();
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
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB aabb, List mask, Entity entity) {
        if(entity != null) {
            if(entity.getClass() == EntityZZombie.class) {
                if(((TileBarricade)world.getTileEntity(x, y, z)).damage == 5) {
                    return;
                }
                super.addCollisionBoxesToList(world, x, y, z, aabb, mask, entity);
            } else {
                if(!ZombieTC.editorModeManager.enabled) {
                    super.addCollisionBoxesToList(world, x, y, z, aabb, mask, entity);
                }
            }
        } else {
            super.addCollisionBoxesToList(world, x, y ,z, aabb, mask, entity);
        }
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
    public void onBlockAdded(World world, int x, int y, int z) {
        super.onBlockAdded(world, x, y, z);
        GameManager.blockBarricades.add(Vec3.createVectorHelper(x, y, z));
        ZombieTC.network.sendToAll(new MessageAddBarricade(x, y, z));
    }
    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int side) {
        super.breakBlock(world, x, y, z, block, side);
        for(int i = 0; i < GameManager.blockBarricades.size(); i++) {
            Vec3 vec = GameManager.blockBarricades.get(i);
            if(vec.distanceTo(Vec3.createVectorHelper(x, y, z)) < 0.01) {
                GameManager.blockBarricades.remove(i);
            }
        }
        ZombieTC.network.sendToAll(new MessageRemoveBarricade(x, y, z));
    }
}
