package net.gigimoi.zombietc.weapon;

import net.gigimoi.zombietc.EntityZZombie;
import net.gigimoi.zombietc.helpers.MouseOverHelper;
import net.gigimoi.zombietc.helpers.SoundHelper;
import net.gigimoi.zombietc.net.MessagePlayShootSound;
import net.gigimoi.zombietc.net.MessageShoot;
import net.gigimoi.zombietc.ZombieTC;
import net.gigimoi.zombietc.helpers.TextureHelper;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.util.Random;

/**
 * Created by gigimoi on 7/17/2014.
 */
public class ItemWeapon extends Item implements IItemRenderer {
    public static ItemWeapon radomVis = new ItemWeapon("Radom Vis", FireMechanism.semiAutomatic);

    public FireMechanism fireMechanism;

    public ItemWeapon(String name, FireMechanism fireMechanism) {
        this.setUnlocalizedName(name);
        setMaxStackSize(1);
        this.fireMechanism = fireMechanism;
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    public IModelCustom modelGun;
    public static IModelCustom modelFlash;
    private static Random _r = new Random();
    @Override
    public void renderItem(ItemRenderType type, ItemStack stack, Object... data) {
        GL11.glPushMatrix();
        if(type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
            //EntityPlayer player = (EntityPlayer) data[1];
            GL11.glTranslated(0, 1, 0);
        }
        ensureTagCompund(stack);
        GL11.glScaled(0.2f, 0.2f, 0.2f);
        GL11.glRotated(90, 1, 0, 0);
        GL11.glRotated(135, 0, 0, 1);
        GL11.glRotated(0, 0, 1, 0);
        if(type != ItemRenderType.INVENTORY && stack.getTagCompound().getBoolean("InSights")) {
            GL11.glTranslated(-1, 3.45, -0.65);
        }
        boolean shoot = false;
        if(stack.getTagCompound().getBoolean("Shoot")) {
            shoot = true;
            stack.getTagCompound().setBoolean("Shoot", false);
        }
        if(modelGun == null) {
            modelGun = AdvancedModelLoader.loadModel(new ResourceLocation(ZombieTC.MODID, "models/guns/" + getUnlocalizedName().substring(5) + ".obj"));
        }
        if(modelFlash == null) {
            modelFlash = AdvancedModelLoader.loadModel(new ResourceLocation(ZombieTC.MODID, "models/muzzleflash.obj"));
        }
        TextureHelper.bindTexture(new ResourceLocation(ZombieTC.MODID, "textures/models/guns/" + getUnlocalizedName().substring(5) + ".png"));
        modelGun.renderAll();
        if(shoot) {
            TextureHelper.bindTexture(new ResourceLocation(ZombieTC.MODID, "textures/models/muzzleflash.png"));
            GL11.glTranslated(_r.nextInt(100) / 100f - 0.5f, _r.nextInt(100) / 100f - 0.3f, _r.nextInt(100) / 100f - 0.5f);
            modelFlash.renderAll();
        }
        GL11.glPopMatrix();
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        ensureTagCompund(stack);
        stack.getTagCompound().setBoolean("InSights",!stack.getTagCompound().getBoolean("InSights"));
        return stack;
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase attacker, EntityLivingBase attacked) {
        return false; //TODO: Deal no damage
    }

    public static void ensureTagCompund(ItemStack stack) {
        if(!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
            stack.getTagCompound().setBoolean("InSights", false);
            stack.getTagCompound().setBoolean("Shoot", false);
            stack.getTagCompound().setInteger("ShootCooldown", 0);
        }
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int p_77663_4_, boolean p_77663_5_) {
        if(entity != null) {
            if(world.isRemote && entity.getClass() != EntityClientPlayerMP.class) {
                return;
            }
            if(!world.isRemote && entity.getClass() == EntityPlayerMP.class) {
                return;
            }
            EntityPlayer player = (EntityPlayer)entity;
            if(player.getHeldItem() == stack) {
                player.swingProgress = 0f;
                player.isSwingInProgress = false;
                player.swingProgressInt = 0;
                ensureTagCompund(stack);
                stack.getTagCompound().setInteger("ShootCooldown", stack.getTagCompound().getInteger("ShootCooldown") - 1);
                if(world.isRemote) {
                    if(fireMechanism.checkFire(this, stack)) {
                        stack.getTagCompound().setBoolean("Shoot", true);
                        ZombieTC.network.sendToServer(new MessagePlayShootSound(player));
                        MovingObjectPosition trace = MouseOverHelper.getMouseOver(5000.0F);
                        if(trace.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
                            Entity hit = trace.entityHit;
                            if(hit != null && hit.getClass() == EntityZZombie.class) {
                                ZombieTC.network.sendToServer(new MessageShoot(player, trace.entityHit, this));
                            }
                        }
                    }
                }
            }
        }
        super.onUpdate(stack, world, entity, p_77663_4_, p_77663_5_);
    }
}
