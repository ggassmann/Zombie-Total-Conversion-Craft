package net.gigimoi.zombietc.entity;

import com.stackframe.pathfinder.Dijkstra;
import net.gigimoi.zombietc.ZombieTC;
import net.gigimoi.zombietc.block.BlockNode;
import net.gigimoi.zombietc.event.PlayerManager;
import net.gigimoi.zombietc.util.pathfinding.MCNode;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import scala.Int;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by gigimoi on 7/14/2014.
 */
public class EntityZZombie extends EntityZombie {
    public static class Properties implements IExtendedEntityProperties {
        float speed;
        public static String PropertiesIdentifier = "Entity ZZombie Properties";
        private static Properties getProp(Entity entity) {
            return (Properties) entity.getExtendedProperties(PropertiesIdentifier);
        }
        public static float getSpeed(Entity entity) {
            return getProp(entity).speed;
        }
        public static void setSpeed(Entity entity, float speed) {
            getProp(entity).speed = speed;
        }
        @Override
        public void saveNBTData(NBTTagCompound tag) {
            tag.setFloat("Speed", speed);
        }

        @Override
        public void loadNBTData(NBTTagCompound tag) {
            speed = tag.getFloat("Speed");
        }

        @Override
        public void init(Entity entity, World world) {
            speed = 1f;
        }
    }
    static Random _r = new Random();
    double targetX;
    double targetY;
    double targetZ;
    boolean hasSetDefaultTarget = false;
    boolean yieldingToOtherZombie = false;
    MCNode lastPassed;
    float speed = 1f;

    public EntityZZombie(World world) {
        super(world);
        this.tasks.taskEntries = new ArrayList();
        this.tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, false));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, Float.MAX_VALUE));
        this.targetTasks.taskEntries = new ArrayList();
        this.setSize(0.6F, 1.8F);
        this.registerExtendedProperties(Properties.PropertiesIdentifier, new Properties());
        Properties.setSpeed(this, 1f);
        if(ZombieTC.gameManager.wave > 2) {
            if(_r.nextBoolean() || _r.nextBoolean()) {
                this.setCurrentItemOrArmor(1, new ItemStack(Items.leather_boots));
                Properties.setSpeed(this, Properties.getSpeed(this) + 0.3f);
            }
        }
    }

    @Override
    public void setFire(int duration) {
        if (duration == 8) {
            return;
        }
        super.setFire(duration);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue((int)(ZombieTC.gameManager.wave * 1.3) + 4);
    }

    @Override
    protected int getExperiencePoints(EntityPlayer player) {
        return 0;
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (ZombieTC.editorModeManager.enabled) {
            isDead = true;
        } else {
            if (!yieldingToOtherZombie) {
                move();
            } else {
                yieldingToOtherZombie = false;
            }
            if (!isDead && this.getHealth() > 0) {
                EntityPlayer nearest = worldObj.getClosestPlayerToEntity(this, Int.MaxValue());
                if (nearest != null && Vec3.createVectorHelper(posX, posY, posZ).distanceTo(Vec3.createVectorHelper(nearest.posX, nearest.posY, nearest.posZ)) < 1.5) {
                    nearest.attackEntityFrom(
                            new DamageSource("Zombie"), 2
                    );
                    ((PlayerManager.ZombieTCPlayerProperties) nearest.getExtendedProperties(ZombieTC.MODID)).timeSinceHit = 0;
                }
            } else {
                this.setSize(0.0F, 0.0F);
            }
        }
    }

    @Override
    protected void dropEquipment(boolean p_82160_1_, int p_82160_2_) {//Don't drop equipment
    }

    private void move() {
        if (!hasSetDefaultTarget) {
            targetX = posX;
            targetY = posY;
            targetZ = posZ;
            hasSetDefaultTarget = true;
        }/* This does not work because rounding 1 block wide corners has the wrong zombie yield while the other attempts to push it out of the way.
        List nearbyEntities = worldObj.getEntitiesWithinAABBExcludingEntity(this, AxisAlignedBB.getBoundingBox(posX - 1, posY - 1, posZ - 1, posX + 1, posY + 1, posZ + 1));
        for(int i = 0; i < nearbyEntities.size(); i++) {
            Entity entityRaw = (Entity)nearbyEntities.get(i);
            if(entityRaw.getClass() == EntityZZombie.class) {
                if(entityRaw != this) {
                    ((EntityZZombie)entityRaw).yieldingToOtherZombie = true;
                }
            }
        }
        */
        getMoveHelper().setMoveTo(targetX, targetY, targetZ, Properties.getSpeed(this) * (ZombieTC.gameManager.wave > 4 ? 1.2f : 0.85f));
        if (targetY > posY) {
            getJumpHelper().setJumping();
        }
        if (_r.nextInt(15) == 5 || Vec3.createVectorHelper(posX, posY, posZ).distanceTo(Vec3.createVectorHelper(targetX, targetY, targetZ)) < 0.5) {
            resetTarget();
        }
    }

    private void resetTarget() {
        EntityPlayer player = worldObj.getClosestPlayerToEntity(this, Double.MAX_VALUE);
        if (player != null) {
            Vec3 playerPos = Vec3.createVectorHelper(player.posX, player.posY, player.posZ);
            Vec3 pos = Vec3.createVectorHelper(posX, posY, posZ);
            if (playerPos.distanceTo(pos) < 1.5 && getEntitySenses().canSee(player)) {
                targetX = player.posX;
                targetY = player.posY;
                targetZ = player.posZ;
            } else {
                ArrayList<MCNode> goal = new ArrayList();
                goal.add(BlockNode.getClosestToPosition(worldObj, playerPos, false));
                MCNode start = BlockNode.getClosestToPosition(worldObj, pos, false);
                if (start != null && goal.get(0) != null && BlockNode.nodes != null) {
                    List<MCNode> path = new Dijkstra<MCNode>().findPath(BlockNode.nodes, start, goal);
                    if (path != null) {
                        if (path.get(0).position.toVec3().addVector(0.5, 0, 0.5).distanceTo(Vec3.createVectorHelper(posX, posY, posZ)) < 0.5) {
                            lastPassed = path.get(0);
                        }
                        if (lastPassed == path.get(0)) {
                            if (path.size() > 1) {
                                targetX = path.get(1).position.xCoord + 0.5;
                                targetY = path.get(1).position.yCoord;
                                targetZ = path.get(1).position.zCoord + 0.5;
                            }
                        } else {
                            if (path.size() > 0) {
                                targetX = path.get(0).position.xCoord + 0.5;
                                targetY = path.get(0).position.yCoord;
                                targetZ = path.get(0).position.zCoord + 0.5;
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    protected Item getDropItem() {
        return null;
    }

    @Override
    protected void dropRareDrop(int p_70600_1_) {
    }

    @Override
    protected void addRandomArmor() {
        System.out.println("Tried to add armor");
    }

    @Override
    protected void damageEntity(DamageSource source, float amount) {
        super.damageEntity(source, amount);
        this.hurtResistantTime = 0;
    }
}