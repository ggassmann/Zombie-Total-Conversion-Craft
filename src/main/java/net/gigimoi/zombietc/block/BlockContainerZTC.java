package net.gigimoi.zombietc.block;

import net.gigimoi.zombietc.ZombieTC;
import net.gigimoi.zombietc.util.IListenerZTC;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;

/**
 * Created by gigimoi on 8/8/2014.
 */
public abstract class BlockContainerZTC extends BlockContainer {
    protected BlockContainerZTC(Material material) {
        super(material);
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        super.onBlockAdded(world, x, y, z);
        if(IListenerZTC.class.isAssignableFrom(world.getTileEntity(x, y, z).getClass())) {
            IListenerZTC listenerZTC = (IListenerZTC) world.getTileEntity(x, y, z);
            ZombieTC.gameManager.registerListener(listenerZTC);
        }
    }

    @Override
    public void onBlockPreDestroy(World world, int x, int y, int z, int meta) {
        super.onBlockPreDestroy(world, x, y, z, meta);
        if(IListenerZTC.class.isAssignableFrom(world.getTileEntity(x, y, z).getClass())) {
            IListenerZTC listenerZTC = (IListenerZTC) world.getTileEntity(x, y, z);
            ZombieTC.gameManager.unregisterListener(listenerZTC);
        }
    }
}
