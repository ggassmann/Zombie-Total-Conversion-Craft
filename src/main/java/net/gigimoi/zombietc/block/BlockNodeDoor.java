package net.gigimoi.zombietc.block;

import net.gigimoi.zombietc.tile.TileNodeDoor;
import net.gigimoi.zombietc.util.DirectionHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by gigimoi on 8/5/2014.
 */
public class BlockNodeDoor extends BlockNode {
    public static final BlockNodeDoor iron = new BlockNodeDoor();
    public BlockNodeDoor() {
        setBlockName("Node Door");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileNodeDoor();
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack) {
        TileNodeDoor tile = getTile(world, x, y, z);
        tile.direction = DirectionHelper.getPlayerDirection((EntityPlayer)player);
    }
    private TileNodeDoor getTile(World world, int x, int y, int z) {
        return (TileNodeDoor)world.getTileEntity(x, y, z);
    }
}