package net.gigimoi.zombietc.item.weapon;

import net.gigimoi.zombietc.ZombieTC;
import net.gigimoi.zombietc.block.BlockBarricade;
import net.gigimoi.zombietc.client.ClientProxy;
import net.gigimoi.zombietc.entity.EntityZZombie;
import net.gigimoi.zombietc.net.MessageReload;
import net.gigimoi.zombietc.net.MessageShoot;
import net.gigimoi.zombietc.net.MessageTryShoot;
import net.gigimoi.zombietc.util.*;
import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import java.util.Random;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by gigimoi on 7/17/2014.
 */
public class ItemWeapon extends Item implements IItemRenderer {
    private static final Block[] ignoredBlocksList = new Block[]{
            BlockBarricade.wooden
    };
    public static ItemWeapon weaponACP44 = new ItemWeapon("ACP44", FireMechanism.burst3fast, 0.6, 4, 21, 210, 18, 1).barrelLength(1.2f).sightHeight(0.2f).damage(2);
    public static ItemWeapon weaponFDRbine = new ItemWeapon("FDRbine", FireMechanism.burst3slow, 0.37, 6, 9, 72, 20, 4).barrelLength(2.0f).sightHeight(1.0f).damage(2);
    public static ItemWeapon weaponKarbine = new ItemWeapon("Karbine", FireMechanism.semiAutomatic, 0.5, 1, 4, 40, 20, 20).barrelLength(2.5f).sightHeight(0.1f).damage(5);
    public static ItemWeapon weaponRadomVis = new ItemWeapon("Radom Vis", FireMechanism.semiAutomatic, 1, 1, 9, 90, 20, 1).barrelLength(1.0f).sightHeight(0.1f).damage(2);
    public static ItemWeapon weaponStormRifle = new ItemWeapon("Storm Rifle", FireMechanism.automatic, 0.55, 6, 30, 120, 20, 3).barrelLength(2.0f).sightHeight(1.0f).damage(2);
    public static ItemWeapon weaponThompson = new ItemWeapon("Thompson", FireMechanism.automatic, 0.5, 6, 30, 120, 20, 2).barrelLength(1.8f).sightHeight(0.1f).damage(2);
    public static ItemWeapon weaponVBR_B = new ItemWeapon("VBR-B", FireMechanism.automatic, 1, 6, 42, 420, 22, 1).barrelLength(1.0f).sightHeight(0.2f).damage(1);
    public static ItemWeapon weaponVenusSMP = new ItemWeapon("Venus SMP", FireMechanism.automatic, 0.52, 3.9, 25, 200, 17, 2).barrelLength(2.0f).sightHeight(0.3f).damage(3);
    public static IModelCustom modelFlash;
    private static Random _r = new Random();
    public FireMechanism fireMechanism;
    public double inventoryScale;
    public int clipSize;
    public int initialAmmo;
    public int reloadTime;
    public int fireDelay;
    public IModelCustom modelGun;
    double adsLift;
    private int damage;
    private float barrelLength;
    private float sightHeight;

    public ItemWeapon(String name, FireMechanism fireMechanism, double inventoryScale, double adsLift, int clipSize, int initialAmmo, int reloadTime, int fireDelay) {
        this.setUnlocalizedName(name);
        setTextureName(ZombieTC.MODID + ":guns/" + name);
        setMaxStackSize(1);
        this.fireMechanism = fireMechanism;
        this.inventoryScale = inventoryScale;
        this.adsLift = adsLift;
        this.clipSize = clipSize;
        this.initialAmmo = initialAmmo;
        this.reloadTime = reloadTime;
        this.fireDelay = fireDelay;
        ZombieTC.proxy.registerWeaponRender(this);
    }

    public ItemWeapon barrelLength(float length) {
        this.barrelLength = length;
        return this;
    }

    public ItemWeapon sightHeight(float height) {
        this.sightHeight = height;
        return this;
    }

    public ItemWeapon damage(int damage) {
        this.damage = damage;
        return this;
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack stack, Object... data) {
        glPushMatrix();
        if (type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
            //EntityPlayer player = (EntityPlayer) data[1];
            glTranslated(0, 1, 0);
        }
        if (type == ItemRenderType.INVENTORY) {
            glScaled(0.8 * inventoryScale, 0.8 * inventoryScale, 0.8 * inventoryScale);
        }
        if (type == ItemRenderType.EQUIPPED) {
            glRotated(90, 0, 1, 0);
            glTranslated(0, 1, 0);
        }
        ensureTagCompund(stack);
        glScaled(0.2f, 0.2f, 0.2f);
        glRotated(90, 1, 0, 0);
        glRotated(135, 0, 0, 1);
        glRotated(0, 0, 1, 0);
        if (type != ItemRenderType.INVENTORY && stack.getTagCompound().getBoolean("InSights") && stack.getTagCompound().getInteger("Reload Timer") == 0) {
            glTranslated(-1, 3.45, -0.65 + -adsLift / 5f);
        }
        if (type != ItemRenderType.INVENTORY && stack.getTagCompound().getInteger("Reload Timer") > 0) {
            glRotated(10, 0, 1, 0);
            glRotated(50, 0, 0, -1);
        }
        if (type == ItemRenderType.INVENTORY) {
            glRotated(-45, 0, 1, 0);
        }
        boolean shoot = false;
        if (stack.getTagCompound().getBoolean("Shoot")) {
            shoot = true;
            stack.getTagCompound().setBoolean("Shoot", false);
        }
        if (modelGun == null) {
            modelGun = AdvancedModelLoader.loadModel(new ResourceLocation(ZombieTC.MODID, "models/guns/" + getUnlocalizedName().substring(5) + ".obj"));
        }
        if (modelFlash == null) {
            modelFlash = AdvancedModelLoader.loadModel(new ResourceLocation(ZombieTC.MODID, "models/muzzleflash.obj"));
        }
        TextureHelper.bindTexture(new ResourceLocation(ZombieTC.MODID, "textures/items/guns/" + getUnlocalizedName().substring(5) + ".png"));
        if (type == ItemRenderType.ENTITY) {
            glScaled(0.5, 0.5, 0.5);
        }
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        modelGun.renderAll();
        if (shoot) {
            TextureHelper.bindTexture(new ResourceLocation(ZombieTC.MODID, "textures/misc/muzzleflash.png"));
            glTranslated(_r.nextInt(100) / 100f - 0.5f, _r.nextInt(100) / 100f - 0.3f, _r.nextInt(100) / 100f - 0.5f);
            glTranslated(-barrelLength * 4, 0, sightHeight * 1.5f);
            modelFlash.renderAll();
        }
        glDisable(GL_BLEND);
        glPopMatrix();
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        ensureTagCompund(stack);
        stack.getTagCompound().setBoolean("InSights", !stack.getTagCompound().getBoolean("InSights"));
        return stack;
    }

    public void ensureTagCompund(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
            stack.getTagCompound().setBoolean("InSights", false);
            stack.getTagCompound().setBoolean("Shoot", false);
            stack.getTagCompound().setInteger("ShootCooldown", 0);
            stack.getTagCompound().setInteger("Rounds", clipSize);
            stack.getTagCompound().setInteger("Ammo", initialAmmo);
            stack.getTagCompound().setInteger("Reload Time", 0);
        }
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int p_77663_4_, boolean p_77663_5_) {
        ensureTagCompund(stack);
        NBTTagCompound tag = stack.getTagCompound();
        if (tag.getInteger("Reload Timer") > 0) {
            tag.setInteger("Reload Timer", tag.getInteger("Reload Timer") - 1);
            if (tag.getInteger("Reload Timer") == 0) {
                int oldAmmoInClip = tag.getInteger("Rounds");
                tag.setInteger("Rounds", clipSize);
                tag.setInteger("Ammo", tag.getInteger("Ammo") + oldAmmoInClip - clipSize);
                if(tag.getInteger("Ammo") < 0) {
                    tag.setInteger("Rounds", tag.getInteger("Ammo") + tag.getInteger("Rounds"));
                    tag.setInteger("Ammo", 0);
                }
                tag.setInteger("ShootCooldown", 0);
            }
        }
        if (ZombieTC.editorModeManager.enabled) {
            return;
        }
        if (entity != null) {
            if (world.isRemote && entity.getClass() != EntityClientPlayerMP.class) {
                return;
            }
            if (!world.isRemote && entity.getClass() == EntityPlayerMP.class) {
                return;
            }
            EntityPlayer player = (EntityPlayer) entity;
            tag.setInteger("ShootCooldown", tag.getInteger("ShootCooldown") - 1);
            if (player.getHeldItem() == stack) {
                player.swingProgress = 0f;
                player.isSwingInProgress = false;
                player.swingProgressInt = 0;
                if (world.isRemote) {
                    if (ClientProxy.reload.isPressed() && tag.getInteger("Reload Timer") == 0 && tag.getInteger("Rounds") != clipSize && tag.getInteger("Ammo") > 0) {
                        tag.setInteger("Reload Timer", reloadTime);
                        ZombieTC.network.sendToServer(new MessageReload(player));
                    }
                    if (tag.getInteger("Reload Timer") == 0 && fireMechanism.checkFire(this, stack)) {
                        if (tag.getInteger("Rounds") > 0) {
                            tag.setInteger("ShootCooldown", fireDelay);
                            tag.setBoolean("Shoot", true);
                            tag.setInteger("Rounds", tag.getInteger("Rounds") - 1);
                            if(tag.getInteger("Rounds") == 0 && tag.getInteger("Ammo") > 0) {
                                tag.setInteger("Reload Timer", reloadTime);
                                ZombieTC.network.sendToServer(new MessageReload(player));
                            }
                            player.setPositionAndRotation(
                                    player.posX,
                                    player.posY,
                                    player.posZ,
                                    player.rotationYaw + ((float) _r.nextInt(100) - 50f) / 20f,
                                    player.rotationPitch - ((float) _r.nextInt(100)) / 20f
                            );
                            ZombieTC.proxy.playSound("shoot-" + getUnlocalizedName().substring(5), (float) player.posX, (float) player.posY, (float) player.posZ);
                            ZombieTC.network.sendToServer(new MessageTryShoot(player));

                            MovingObjectPosition trace = MouseOverHelper.getMouseOver(5000.0F, ignoredBlocksList);

                            if (trace.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
                                Entity hit = trace.entityHit;
                                if (hit != null && hit.getClass() == EntityZZombie.class) {
                                    ZombieTC.network.sendToServer(new MessageShoot(player, trace.entityHit, this));
                                }
                            }
                        }
                    }
                }
            }
        }
        super.onUpdate(stack, world, entity, p_77663_4_, p_77663_5_);
    }

    public void drawUIFor(ItemStack stack, RenderGameOverlayEvent event) {
        ensureTagCompund(stack);
        NBTTagCompound tag = stack.getTagCompound();
        int rounds = tag.getInteger("Rounds");
        int totalAmmo = tag.getInteger("Ammo");
        String out = Lang.get("ui.overlay.ammo") + ": " + rounds + "/" + clipSize + " - " + totalAmmo;
        TextRenderHelper.drawString(out, event.resolution.getScaledWidth() - 2, 2, TextAlignment.Right);
        if (tag.getInteger("Reload Timer") > 0) {
            TextRenderHelper.drawString(Lang.get("ui.overlay.reloading") + "...", event.resolution.getScaledWidth() - 2, 12, TextAlignment.Right);
        }
    }

    public float getBulletDamage() {
        return damage;
    }
}
