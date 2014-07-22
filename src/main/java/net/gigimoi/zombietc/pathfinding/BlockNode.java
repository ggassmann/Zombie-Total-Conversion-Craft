package net.gigimoi.zombietc.pathfinding;

import cpw.mods.fml.client.registry.RenderingRegistry;
import net.gigimoi.zombietc.ZombieTC;
import net.gigimoi.zombietc.net.map.MessageAddNode;
import net.gigimoi.zombietc.net.map.MessageRemoveNode;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gigimoi on 7/16/2014.
 */
public class BlockNode extends BlockContainer {

    public static class MCNodePair {
        public MCNode n1;
        public MCNode n2;
        public MCNodePair(MCNode p1, MCNode p2) {
            n1 = p1;
            n2 = p2;
        }
    }
    public static MCNode getClosestToPosition(World world, Vec3 position, boolean raytrace) {
        double shortestDistance = Double.MAX_VALUE;
        MCNode closest = null;
        for(int i = 0; i < BlockNode.nodes.size(); i++) {
            MCNode node = BlockNode.nodes.get(i);
            if(raytrace) {
                MovingObjectPosition trace = world.rayTraceBlocks(node.position.toVec3(), position, true);
                if(trace != null && trace.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                    continue;
                }
            }
            double distance = position.distanceTo(node.position.toVec3());
            if(distance < shortestDistance) {
                shortestDistance = distance;
                closest = node;
            }
        }
        return closest;
    }
    public static List<MCNode> nodes = new ArrayList<MCNode>();
    public static List<MCNodePair> nodeConnections = new ArrayList<MCNodePair>();
    public static final BlockNode instance = new BlockNode();
    private static int renderID = -1;
    public BlockNode() {
        super(Material.rock);
        setHardness(1f);
        setBlockName("Node");
        if(renderID < 0) {
            renderID = RenderingRegistry.getNextAvailableRenderId();
        }
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileNode();
    }
    @Override
    public int getRenderType() {
        return -1;
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
    public boolean isBlockSolid(IBlockAccess p_149747_1_, int p_149747_2_, int p_149747_3_, int p_149747_4_, int p_149747_5_) {
        return false;
    }
    @Override
    public boolean isOpaqueCube() {
        return false;
    }
    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
        return null;
    }
    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        if(ZombieTC.editorModeManager.enabled) {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }
        else {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        }
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        super.onBlockAdded(world, x, y, z);
        if(!world.isRemote) {
            nodes.add(new MCNode(Vec3.createVectorHelper(x, y, z)));
            if(!MinecraftServer.getServer().isSinglePlayer()) {
                ZombieTC.network.sendToAll(new MessageAddNode(x, y, z));
            }
        }
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        super.breakBlock(world, x, y, z, block, meta);
        if(!world.isRemote) {
            BlockNode.removeNodeAt(x, y, z);
            if(!MinecraftServer.getServer().isSinglePlayer()) {
                ZombieTC.network.sendToAll(new MessageRemoveNode(x, y, z));
            }
        }
    }

    public static void removeNodeAt(double x, double y, double z) {
        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i).position.distanceTo(new Point3((int)x, (int)y, (int)z)) < 0.001) {
                nodes.remove(i);
            }
        }
        while(true) {
            boolean shouldReloop = false;
            for (int i = 0; i < nodeConnections.size(); i++) {
                MCNodePair link = nodeConnections.get(i);
                if (link.n1.position.distanceTo(new Point3((int)x, (int)y, (int)z)) < 0.01 || link.n2.position.distanceTo(new Point3((int)x, (int)y, (int)z)) < 0.01) {
                    nodeConnections.remove(i);
                    shouldReloop = true;
                }
            }
            if(!shouldReloop) {
                break;
            }
        }
    }
    public static void addNodeConnection(Vec3 cVec, Vec3 oVec) {
        MCNode node1 = null;
        MCNode node2 = null;
        for(int r = 0; r < BlockNode.nodes.size(); r++) {
            if(oVec.distanceTo(BlockNode.nodes.get(r).position.toVec3()) < 0.001) {
                node1 = BlockNode.nodes.get(r);
            }
            if(cVec.distanceTo(BlockNode.nodes.get(r).position.toVec3()) < 0.001) {
                node2 = BlockNode.nodes.get(r);
            }
            if(node1 == null || node2 == null) continue;
            break;
        }
        BlockNode.nodeConnections.add(new BlockNode.MCNodePair(node1, node2));
    }
    public static void removeNodeConnection(Vec3 first, Vec3 second) {
        for(int i = 0; i < nodeConnections.size(); i++) {
            if(nodeConnections.get(i).n1.position.distanceTo(Point3.fromVec3(first)) < 0.01 || nodeConnections.get(i).n1.position.distanceTo(Point3.fromVec3(second)) < 0.01) {
                if(nodeConnections.get(i).n2.position.distanceTo(Point3.fromVec3(first)) < 0.01 || nodeConnections.get(i).n2.position.distanceTo(Point3.fromVec3(second)) < 0.01) {
                    nodeConnections.remove(i);
                }
            }
        }
    }
}
