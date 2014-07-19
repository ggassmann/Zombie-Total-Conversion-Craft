package net.gigimoi.zombietc;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.swing.*;

/**
 * Created by gigimoi on 7/16/2014.
 */
// The magnitude of the bullshit required to make this work compares favorably with that of the national debt.
public class BlockBarricade extends BlockContainer {
    public static BlockBarricade wooden = new BlockBarricade();

    public BlockBarricade() {
        super(Material.rock);
        setBlockName("Barricade");
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileBarricade();
    }

    @Override
    public boolean isBlockSolid(IBlockAccess access, int x, int y, int z, int meta) {
        return false;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess access, int x, int y, int z) {
        super.setBlockBoundsBasedOnState(access, x, y, z);
    }
}
