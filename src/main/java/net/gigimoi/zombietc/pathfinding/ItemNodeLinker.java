package net.gigimoi.zombietc.pathfinding;

import net.gigimoi.zombietc.ZombieTC;
import net.gigimoi.zombietc.net.MessageAddNodeConnection;
import net.gigimoi.zombietc.net.MessageRemoveNodeConnection;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

/**
 * Created by gigimoi on 7/16/2014.
 */
public class ItemNodeLinker extends Item {
    private static ItemNodeLinker _instance = new ItemNodeLinker();
    public static ItemNodeLinker instance() { return _instance; }
    public ItemNodeLinker() {
        setUnlocalizedName("Node Linker");
        setMaxStackSize(1);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float i, float j, float k) {
        if(!world.isRemote) {
            Block target = world.getBlock(x, y, z);
            if (target.getClass() == BlockNode.class) {
                if (!stack.hasTagCompound()) {
                    stack.setTagCompound(new NBTTagCompound());
                    stack.getTagCompound().setInteger("x", x);
                    stack.getTagCompound().setInteger("y", y);
                    stack.getTagCompound().setInteger("z", z);
                    player.addChatMessage(new ChatComponentTranslation("Set n1 node"));
                } else {
                    int oX = stack.getTagCompound().getInteger("x");
                    int oY = stack.getTagCompound().getInteger("y");
                    int oZ = stack.getTagCompound().getInteger("z");
                    if (oX == x && oY == y && oZ == z) {
                        stack.setTagCompound(null);
                        player.addChatMessage(new ChatComponentTranslation("Cancelling node link"));
                    } else {
                        Vec3 cVec = Vec3.createVectorHelper(x, y, z);
                        Vec3 oVec = Vec3.createVectorHelper(oX, oY, oZ);
                        boolean unlinked = false;
                        for (int index = 0; index < BlockNode.nodeConnections.size(); index++) {
                            BlockNode.MCNodePair pair = BlockNode.nodeConnections.get(index);
                            if ((pair.n1.position.distanceTo(cVec) < 0.01f || pair.n2.position.distanceTo(cVec) < 0.01f) &&
                                (pair.n1.position.distanceTo(oVec) < 0.01f || pair.n2.position.distanceTo(oVec) < 0.01f)) {
                                stack.setTagCompound(null);
                                ZombieTC.network.sendToAll(new MessageRemoveNodeConnection(cVec, oVec));
                                unlinked = true;
                                player.addChatMessage(new ChatComponentTranslation("Unlinked nodes"));
                                break;
                            }
                        }
                        if (!unlinked) {
                            ZombieTC.network.sendToAll(new MessageAddNodeConnection(cVec, oVec));
                            stack.setTagCompound(null);
                            player.addChatMessage(new ChatComponentTranslation("Linked nodes"));
                        }
                    }
                }
                return true;
            }
        }
        return super.onItemUse(stack, player, world, x, y, z, side, i, j, k);
    }
}
