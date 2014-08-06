package net.gigimoi.zombietc.tile;

import com.stackframe.pathfinder.AStar;
import net.gigimoi.zombietc.block.BlockNode;
import net.gigimoi.zombietc.event.GameManager;
import net.gigimoi.zombietc.util.pathfinding.MCNode;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;

import javax.vecmath.Vector3f;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gigimoi on 7/14/2014.
 */
public class TileSpawner extends TileEntity {
    public String entityToSpawn = "";
    public boolean enabled = false;

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        entityToSpawn = tag.getString("entity");
        enabled = tag.getBoolean("Enabled");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setString("entity", entityToSpawn);
        tag.setBoolean("Enabled", enabled);
    }

    int timeToRecheckPathing = 1;
    @Override
    public void updateEntity() {
        if (!worldObj.isRemote) {
            timeToRecheckPathing--;
            if(timeToRecheckPathing <= 0) {
                timeToRecheckPathing = 500;
                MCNode closest = BlockNode.getClosestToPosition(worldObj, Vec3.createVectorHelper(xCoord, yCoord + 1, zCoord), true);
                if(closest == null) {
                    enabled = false;
                } else {
                    EntityPlayer closestPlayer = worldObj.getClosestPlayer(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, 50000);
                    if(closestPlayer == null) {
                        enabled = false;
                    } else {
                        MCNode goal = BlockNode.getClosestToPosition(worldObj, Vec3.createVectorHelper(closestPlayer.posX, closestPlayer.posY, closestPlayer.posZ), false);
                        if (goal == null) {
                            enabled = false;
                        } else {
                            ArrayList<MCNode> goals = new ArrayList<MCNode>(1);
                            goals.add(goal);
                            List<MCNode> path = new AStar<MCNode>().findPath(BlockNode.nodes, closest, goals);
                            if(path == null || path.size() == 0) {
                                enabled = false;
                            } else {
                                enabled = true;
                            }
                        }
                    }
                }
            }
            if(enabled) {
                GameManager.spawnPositions.add(new Vector3f(xCoord + 0.5f, yCoord + 1, zCoord + 0.5f));
                GameManager.worldsSpawnedTo.add(worldObj);
            }
        }
    }
}
