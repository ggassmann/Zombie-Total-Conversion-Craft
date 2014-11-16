package net.gigimoi.zombietc.block;

import net.gigimoi.zombietc.ZombieTC;
import net.gigimoi.zombietc.client.gui.GuiNodeDoor;
import net.gigimoi.zombietc.client.tilerenderer.TileRendererDoorNode;
import net.gigimoi.zombietc.entity.EntityZZombie;
import net.gigimoi.zombietc.tile.TileNodeDoor;
import net.gigimoi.zombietc.util.DirectionHelper;
import net.gigimoi.zombietc.util.TextureHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by gigimoi on 8/5/2014.
 */
public class BlockNodeDoor extends BlockNode {
    public static final BlockNodeDoor iron = new BlockNodeDoor();

    public BlockNodeDoor() {
        setBlockName("Node Door");
        setHardness(1.0f);
        guiToOpen = GuiNodeDoor.GUI_ID;
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
        if (entity != null) {
            if (entity.getClass() == EntityZZombie.class) {
                return;
            } else {
                if (!ZombieTC.editorModeManager.enabled && !(getTile(world, x, y, z).animationTime > 70)) {
                    super.addCollisionBoxesToList(world, x, y, z, aabb, mask, entity);
                    super.addCollisionBoxesToList(world, x, y + 1, z, aabb, mask, entity);
                }
            }
        } else {
            super.addCollisionBoxesToList(world, x, y, z, aabb, mask, entity);
            super.addCollisionBoxesToList(world, x, y + 1, z, aabb, mask, entity);
        }
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }
    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int x, int y, int z) {
        return Blocks.stone.getCollisionBoundingBoxFromPool(par1World, x, y, z).expand(0, 1, 0);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess access, int x, int y, int z) {
        if(!ZombieTC.editorModeManager.enabled) {
            setBlockBounds(0, 0, 0, 0, 0, 0);
            return;
        }
        setBlockBounds(0, 0, 0, 1, 2, 1);
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        glPushMatrix();
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glScaled(0.7, 0.7, 0.7);
        glTranslated(0, -1, 0);
        glRotated(40, 0, 1, 0);
        if(type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
            glTranslated(0, 2, 0);
            glRotated(90, 0, 1, 0);
        }
        TextureHelper.bindTexture(TileRendererDoorNode.textures[0]);
        TileRendererDoorNode.model.renderAll();
        glPopMatrix();
        glPushMatrix();
        if(type != ItemRenderType.EQUIPPED_FIRST_PERSON) {
            glTranslated(0, -0.3, 0);
        }
        super.renderItem(type, item, data);
        glPopMatrix();
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