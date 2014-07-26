package net.gigimoi.zombietc.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockLever;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import static net.minecraftforge.common.util.ForgeDirection.*;
import static net.minecraftforge.common.util.ForgeDirection.DOWN;

/**
 * Created by gigimoi on 7/26/2014.
 */
public class BlockPurchaseEventLever extends BlockContainer {
    public static final BlockPurchaseEventLever instance = new BlockPurchaseEventLever();
    public BlockPurchaseEventLever() {
        super(Material.circuits);
        setBlockName("Purchase Event Lever");
        setHardness(0.5f);
        setStepSound(soundTypeWood);
        setBlockTextureName("lever");
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_)
    {
        return null;
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean isOpaqueCube()
    {
        return false;
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return 12;
    }

    /**
     * checks to see if you can place this block can be placed on that side of a block: BlockLever overrides
     */
    public boolean canPlaceBlockOnSide(World world, int x, int y, int z, int side)
    {
        ForgeDirection dir = ForgeDirection.getOrientation(side);
        return (dir == DOWN  && world.isSideSolid(x, y + 1, z, DOWN )) ||
                (dir == UP    && world.isSideSolid(x, y - 1, z, UP   )) ||
                (dir == NORTH && world.isSideSolid(x, y, z + 1, NORTH)) ||
                (dir == SOUTH && world.isSideSolid(x, y, z - 1, SOUTH)) ||
                (dir == WEST  && world.isSideSolid(x + 1, y, z, WEST )) ||
                (dir == EAST  && world.isSideSolid(x - 1, y, z, EAST ));
    }

    public boolean canPlaceBlockAt(World world, int x, int y, int z)
    {
        return world.isSideSolid(x - 1, y, z, EAST ) ||
                world.isSideSolid(x + 1, y, z, WEST ) ||
                world.isSideSolid(x, y, z - 1, SOUTH) ||
                world.isSideSolid(x, y, z + 1, NORTH) ||
                world.isSideSolid(x, y - 1, z, UP   ) ||
                world.isSideSolid(x, y + 1, z, DOWN );
    }

    public int onBlockPlaced(World p_149660_1_, int p_149660_2_, int p_149660_3_, int p_149660_4_, int p_149660_5_, float p_149660_6_, float p_149660_7_, float p_149660_8_, int p_149660_9_)
    {
        int k1 = p_149660_9_ & 8;
        int j1 = p_149660_9_ & 7;
        byte b0 = -1;

        if (p_149660_5_ == 0 && p_149660_1_.isSideSolid(p_149660_2_, p_149660_3_ + 1, p_149660_4_, DOWN))
        {
            b0 = 0;
        }

        if (p_149660_5_ == 1 && p_149660_1_.isSideSolid(p_149660_2_, p_149660_3_ - 1, p_149660_4_, UP))
        {
            b0 = 5;
        }

        if (p_149660_5_ == 2 && p_149660_1_.isSideSolid(p_149660_2_, p_149660_3_, p_149660_4_ + 1, NORTH))
        {
            b0 = 4;
        }

        if (p_149660_5_ == 3 && p_149660_1_.isSideSolid(p_149660_2_, p_149660_3_, p_149660_4_ - 1, SOUTH))
        {
            b0 = 3;
        }

        if (p_149660_5_ == 4 && p_149660_1_.isSideSolid(p_149660_2_ + 1, p_149660_3_, p_149660_4_, WEST))
        {
            b0 = 2;
        }

        if (p_149660_5_ == 5 && p_149660_1_.isSideSolid(p_149660_2_ - 1, p_149660_3_, p_149660_4_, EAST))
        {
            b0 = 1;
        }

        return b0 + k1;
    }

    /**
     * Called when the block is placed in the world.
     */
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase placer, ItemStack stack)
    {
        int l = world.getBlockMetadata(x, y, z);
        int i1 = l & 7;
        int j1 = l & 8;

        if (i1 == invertMetadata(1))
        {
            if ((MathHelper.floor_double((double) (placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 1) == 0)
            {
                world.setBlockMetadataWithNotify(x, y, z, 5 | j1, 2);
            }
            else
            {
                world.setBlockMetadataWithNotify(x, y, z, 6 | j1, 2);
            }
        }
        else if (i1 == invertMetadata(0))
        {
            if ((MathHelper.floor_double((double)(placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 1) == 0)
            {
                world.setBlockMetadataWithNotify(x, y, z, 7 | j1, 2);
            }
            else
            {
                world.setBlockMetadataWithNotify(x, y, z, 0 | j1, 2);
            }
        }
    }

    public static int invertMetadata(int p_149819_0_)
    {
        switch (p_149819_0_)
        {
            case 0:
                return 0;
            case 1:
                return 5;
            case 2:
                return 4;
            case 3:
                return 3;
            case 4:
                return 2;
            case 5:
                return 1;
            default:
                return -1;
        }
    }

    public void onNeighborBlockChange(World world, int x, int y, int z, Block neighborBlock)
    {
        if (this.func_149820_e(world, x, y, z))
        {
            int l = world.getBlockMetadata(x, y, z) & 7;
            boolean flag = false;

            if (!world.isSideSolid(x - 1, y, z, EAST) && l == 1)
            {
                flag = true;
            }

            if (!world.isSideSolid(x + 1, y, z, WEST) && l == 2)
            {
                flag = true;
            }

            if (!world.isSideSolid(x, y, z - 1, SOUTH) && l == 3)
            {
                flag = true;
            }

            if (!world.isSideSolid(x, y, z + 1, NORTH) && l == 4)
            {
                flag = true;
            }

            if (!world.isSideSolid(x, y - 1, z, UP) && l == 5)
            {
                flag = true;
            }

            if (!world.isSideSolid(x, y - 1, z, UP) && l == 6)
            {
                flag = true;
            }

            if (!world.isSideSolid(x, y + 1, z, DOWN) && l == 0)
            {
                flag = true;
            }

            if (!world.isSideSolid(x, y + 1, z, DOWN) && l == 7)
            {
                flag = true;
            }

            if (flag)
            {
                this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
                world.setBlockToAir(x, y, z);
            }
        }
    }

    private boolean func_149820_e(World p_149820_1_, int p_149820_2_, int p_149820_3_, int p_149820_4_)
    {
        if (!this.canPlaceBlockAt(p_149820_1_, p_149820_2_, p_149820_3_, p_149820_4_))
        {
            this.dropBlockAsItem(p_149820_1_, p_149820_2_, p_149820_3_, p_149820_4_, p_149820_1_.getBlockMetadata(p_149820_2_, p_149820_3_, p_149820_4_), 0);
            p_149820_1_.setBlockToAir(p_149820_2_, p_149820_3_, p_149820_4_);
            return false;
        }
        else
        {
            return true;
        }
    }

    public void setBlockBoundsBasedOnState(IBlockAccess access, int x, int y, int z)
    {
        int l = access.getBlockMetadata(x, y, z) & 7;
        float f = 0.1875F;

        if (l == 1)
        {
            this.setBlockBounds(0.0F, 0.2F, 0.5F - f, f * 2.0F, 0.8F, 0.5F + f);
        }
        else if (l == 2)
        {
            this.setBlockBounds(1.0F - f * 2.0F, 0.2F, 0.5F - f, 1.0F, 0.8F, 0.5F + f);
        }
        else if (l == 3)
        {
            this.setBlockBounds(0.5F - f, 0.2F, 0.0F, 0.5F + f, 0.8F, f * 2.0F);
        }
        else if (l == 4)
        {
            this.setBlockBounds(0.5F - f, 0.2F, 1.0F - f * 2.0F, 0.5F + f, 0.8F, 1.0F);
        }
        else if (l != 5 && l != 6)
        {
            if (l == 0 || l == 7)
            {
                f = 0.25F;
                this.setBlockBounds(0.5F - f, 0.4F, 0.5F - f, 0.5F + f, 1.0F, 0.5F + f);
            }
        }
        else
        {
            f = 0.25F;
            this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.6F, 0.5F + f);
        }
    }

    public void breakBlock(World world, int x, int y, int z, Block block, int meta)
    {
        if ((meta & 8) > 0)
        {
            world.notifyBlocksOfNeighborChange(x, y, z, this);
            int i1 = meta & 7;

            if (i1 == 1)
            {
                world.notifyBlocksOfNeighborChange(x - 1, y, z, this);
            }
            else if (i1 == 2)
            {
                world.notifyBlocksOfNeighborChange(x + 1, y, z, this);
            }
            else if (i1 == 3)
            {
                world.notifyBlocksOfNeighborChange(x, y, z - 1, this);
            }
            else if (i1 == 4)
            {
                world.notifyBlocksOfNeighborChange(x, y, z + 1, this);
            }
            else if (i1 != 5 && i1 != 6)
            {
                if (i1 == 0 || i1 == 7)
                {
                    world.notifyBlocksOfNeighborChange(x, y + 1, z, this);
                }
            }
            else
            {
                world.notifyBlocksOfNeighborChange(x, y - 1, z, this);
            }
        }

        super.breakBlock(world, x, y, z, block, meta);
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TilePurchaseEventLever();
    }
}
