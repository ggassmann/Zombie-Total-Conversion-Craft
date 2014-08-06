package net.gigimoi.zombietc.block.purchasable;

import net.gigimoi.zombietc.ZombieTC;
import net.gigimoi.zombietc.client.gui.GuiPurchaseItemStack;
import net.gigimoi.zombietc.item.weapon.ItemWeapon;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

/**
 * Created by gigimoi on 7/21/2014.
 */
public class BlockPurchaseItemstack extends BlockContainer {
    public static final BlockPurchaseItemstack instance = new BlockPurchaseItemstack();

    public BlockPurchaseItemstack() {
        super(Material.rock);
        setBlockName("Purchase Itemstack");
        setHardness(0.5f);
    }

    @Override
    public int getRenderType() {
        return -1;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TilePurchaseItemStack();
    }

    @Override
    public boolean isBlockSolid(IBlockAccess access, int x, int y, int z, int meta) {
        return false;
    }

    @Override
    public int getLightOpacity() {
        return 0;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean canPlaceBlockOnSide(World world, int x, int y, int z, int sideraw) {
        ForgeDirection side = ForgeDirection.getOrientation(sideraw);
        if (side == ForgeDirection.UP || side == ForgeDirection.DOWN) {
            return false;
        }
        return true;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (ZombieTC.editorModeManager.enabled) {
            player.openGui(ZombieTC.instance, GuiPurchaseItemStack.GUI_ID, world, x, y, z);
            return true;
        }
        return false;
    }

    @Override
    public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int meta) {
        return side;
    }

    @Override
    public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player) {
        super.onBlockClicked(world, x, y, z, player);
        if (ZombieTC.editorModeManager.enabled) {
            if (player.getHeldItem() == null || (player.getHeldItem() != null)) {
                TilePurchaseItemStack tile = (TilePurchaseItemStack) world.getTileEntity(x, y, z);
                if (player.getHeldItem() == null) {
                    tile.itemStack = null;
                    return;
                }
                tile.itemStack = player.getHeldItem().copy();
                if(tile.itemStack.hasTagCompound() && tile.itemStack.getItem().getClass() == ItemWeapon.class) {
                    tile.itemStack.getTagCompound().setBoolean("InSights", false);
                }
            }
        }
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        ForgeDirection side = ForgeDirection.getOrientation(world.getBlockMetadata(x, y, z));
        if (side == ForgeDirection.NORTH) {
            setBlockBounds(0, 0.2f, 1f, 1f, 1f, 0.9f);
        } else if (side == ForgeDirection.WEST) {
            setBlockBounds(0.9f, 0.2f, 0f, 1f, 1f, 1f);
        } else if (side == ForgeDirection.EAST) {
            setBlockBounds(0f, 0.2f, 0f, 0.1f, 1f, 1f);
        } else if (side == ForgeDirection.SOUTH) {
            setBlockBounds(0, 0.2f, 0.1f, 1f, 1f, 0f);
        }
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB aabb, List bounds, Entity entity) {
    }
}
