package net.gigimoi.zombietc.item;

import net.gigimoi.zombietc.ZombieTC;
import net.gigimoi.zombietc.block.BlockNode;
import net.gigimoi.zombietc.net.map.MessageAddNodeConnection;
import net.gigimoi.zombietc.net.map.MessageRemoveNodeConnection;
import net.gigimoi.zombietc.util.Point3;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

/**
 * Created by gigimoi on 7/16/2014.
 */
public class ItemNodeLinker extends Item {
    private static ItemNodeLinker _instance = new ItemNodeLinker();

    public ItemNodeLinker() {
        setUnlocalizedName("Node Linker");
        setMaxStackSize(1);
    }

    public static ItemNodeLinker instance() {
        return _instance;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float i, float j, float k) {
        if (world.isRemote) {
            Block target = world.getBlock(x, y, z);
            if (target.getClass() == BlockNode.class) {
                if (!stack.hasTagCompound()) {
                    stack.setTagCompound(new NBTTagCompound());
                    stack.getTagCompound().setInteger("xCoord", x);
                    stack.getTagCompound().setInteger("yCoord", y);
                    stack.getTagCompound().setInteger("zCoord", z);
                    player.addChatMessage(new ChatComponentTranslation("Selected node"));
                } else {
                    int oX = stack.getTagCompound().getInteger("xCoord");
                    int oY = stack.getTagCompound().getInteger("yCoord");
                    int oZ = stack.getTagCompound().getInteger("zCoord");
                    if (oX == x && oY == y && oZ == z) {
                        stack.setTagCompound(null);
                        player.addChatMessage(new ChatComponentTranslation("Cancelling node link"));
                    } else {
                        Vec3 cVec = Vec3.createVectorHelper(x, y, z);
                        Vec3 oVec = Vec3.createVectorHelper(oX, oY, oZ);
                        boolean unlinked = false;
                        for (int index = 0; index < BlockNode.nodeConnections.size(); index++) {
                            BlockNode.MCNodePair pair = BlockNode.nodeConnections.get(index);
                            if ((pair.n1.position.distanceTo(Point3.fromVec3(cVec)) < 0.01f || pair.n2.position.distanceTo(Point3.fromVec3(cVec)) < 0.01f) &&
                                    (pair.n1.position.distanceTo(Point3.fromVec3(oVec)) < 0.01f || pair.n2.position.distanceTo(Point3.fromVec3(oVec)) < 0.01f)) {
                                stack.setTagCompound(null);
                                ZombieTC.network.sendToServer(new MessageRemoveNodeConnection(cVec, oVec));
                                unlinked = true;
                                player.addChatMessage(new ChatComponentTranslation("Unlinked nodes"));
                                break;
                            }
                        }
                        if (!unlinked) {
                            boolean node1good = false;
                            boolean node2good = false;
                            for (int bi = 0; bi < BlockNode.nodes.size(); bi++) {
                                if (BlockNode.nodes.get(bi).position.distanceTo(Point3.fromVec3(cVec)) < 0.01) {
                                    node1good = true;
                                }
                                if (BlockNode.nodes.get(bi).position.distanceTo(Point3.fromVec3(oVec)) < 0.01) {
                                    node2good = true;
                                }
                            }
                            if (node1good && node2good) {
                                ZombieTC.network.sendToServer(new MessageAddNodeConnection(cVec, oVec));
                                player.addChatMessage(new ChatComponentTranslation("Linked nodes"));
                            }
                            stack.setTagCompound(null);
                        }
                    }
                }
                return true;
            }
        }
        return super.onItemUse(stack, player, world, x, y, z, side, i, j, k);
    }
}
