package net.gigimoi.zombietc.block;

import net.gigimoi.zombietc.tile.TileSpawner;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by gigimoi on 7/14/2014.
 */
public class BlockSpawner extends BlockContainerZTC {
    public static final BlockSpawner zombie = new BlockSpawner();

    protected BlockSpawner() {
        super(Material.dragonEgg);
        setBlockName("Spawner");
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileSpawner();
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }
}
