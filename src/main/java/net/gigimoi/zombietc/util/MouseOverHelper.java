package net.gigimoi.zombietc.util;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gigimoi on 7/17/2014.
 */
public class MouseOverHelper {
    private static Entity pointedEntity;

    public static MovingObjectPosition getMouseOver(float maxDistance, Block[] ignoredBlocks) {
        MovingObjectPosition trace = null;
        Minecraft mc = Minecraft.getMinecraft();

        List<Block> blocksRemoved = new ArrayList();
        List<Point3> blockPositions = new ArrayList();
        List<NBTTagCompound> blockDatas = new ArrayList();
        List<Block> blocksIgnored = new ArrayList(ignoredBlocks.length);
        for(int i = 0; i < ignoredBlocks.length; i++) {
            blocksIgnored.add(ignoredBlocks[i]);
        }

        if (mc.renderViewEntity != null) {
            if (mc.theWorld != null) {
                pointedEntity = null;
                double d0 = (double) maxDistance;
                while(trace == null ||(
                         trace.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK &&
                         blocksIgnored.contains(mc.theWorld.getBlock(trace.blockX, trace.blockY, trace.blockZ))
                    )){
                    if(trace != null && trace.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                        NBTTagCompound nbt = new NBTTagCompound();
                        TileEntity tile = mc.theWorld.getTileEntity(trace.blockX, trace.blockY, trace.blockZ);
                        if(tile != null) {tile.writeToNBT(nbt); blockDatas.add(nbt); }
                        else { blockDatas.add(null); }
                        blocksRemoved.add(mc.theWorld.getBlock(trace.blockX, trace.blockY, trace.blockZ));
                        blockPositions.add(new Point3(trace.blockX, trace.blockY, trace.blockZ));
                        mc.theWorld.setBlock(trace.blockX, trace.blockY, trace.blockZ, Blocks.air);
                    }
                    trace = mc.renderViewEntity.rayTrace(d0, 1);
                }
                double d1 = d0;
                Vec3 vec3 = mc.renderViewEntity.getPosition(1);

                if (trace != null) {
                    d1 = trace.hitVec.distanceTo(vec3);
                }

                Vec3 vec31 = mc.renderViewEntity.getLook(1);
                Vec3 vec32 = vec3.addVector(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0);
                pointedEntity = null;
                Vec3 vec33 = null;
                float f1 = 1.0F;
                List list = mc.theWorld.getEntitiesWithinAABBExcludingEntity(mc.renderViewEntity, mc.renderViewEntity.boundingBox.addCoord(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0).expand((double) f1, (double) f1, (double) f1));
                double d2 = d1;

                for (int i = 0; i < list.size(); ++i) {
                    Entity entity = (Entity) list.get(i);

                    if (entity.canBeCollidedWith()) {
                        float f2 = entity.getCollisionBorderSize();
                        AxisAlignedBB axisalignedbb = entity.boundingBox.expand((double) f2, (double) f2, (double) f2);
                        MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec32);

                        if (axisalignedbb.isVecInside(vec3)) {
                            if (0.0D < d2 || d2 == 0.0D) {
                                pointedEntity = entity;
                                vec33 = movingobjectposition == null ? vec3 : movingobjectposition.hitVec;
                                d2 = 0.0D;
                            }
                        } else if (movingobjectposition != null) {
                            double d3 = vec3.distanceTo(movingobjectposition.hitVec);

                            if (d3 < d2 || d2 == 0.0D) {
                                if (entity == mc.renderViewEntity.ridingEntity && !entity.canRiderInteract()) {
                                    if (d2 == 0.0D) {
                                        pointedEntity = entity;
                                        vec33 = movingobjectposition.hitVec;
                                    }
                                } else {
                                    pointedEntity = entity;
                                    vec33 = movingobjectposition.hitVec;
                                    d2 = d3;
                                }
                            }
                        }
                    }
                }

                if (pointedEntity != null && (d2 < d1 || trace == null)) {
                    trace = new MovingObjectPosition(pointedEntity, vec33);
                }
            }
        }
        for(int i = 0; i < blocksRemoved.size(); i++) {
            Block block = blocksRemoved.get(i);
            Point3 position = blockPositions.get(i);
            NBTTagCompound data = blockDatas.get(i);
            mc.theWorld.setBlock(position.xCoord, position.yCoord, position.zCoord, block);
            TileEntity tile = mc.theWorld.getTileEntity(position.xCoord, position.yCoord, position.zCoord);
            if(tile != null && data != null) {
                tile.readFromNBT(data);
            }
        }
        return trace;
    }
}
