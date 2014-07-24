package net.gigimoi.zombietc.weapon;

import net.gigimoi.zombietc.EntityZZombie;
import net.gigimoi.zombietc.TileBarricade;
import net.gigimoi.zombietc.event.GameManager;
import net.gigimoi.zombietc.helpers.MouseOverHelper;
import net.gigimoi.zombietc.helpers.TextAlignment;
import net.gigimoi.zombietc.helpers.TextRenderHelper;
import net.gigimoi.zombietc.net.MessageTryShoot;
import net.gigimoi.zombietc.net.MessageReload;
import net.gigimoi.zombietc.net.MessageShoot;
import net.gigimoi.zombietc.ZombieTC;
import net.gigimoi.zombietc.helpers.TextureHelper;
import net.gigimoi.zombietc.pathfinding.Point3;
import net.gigimoi.zombietc.proxy.ClientProxy;
import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;
import scala.util.Right;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by gigimoi on 7/17/2014.
 */
public class ItemWeapon extends Item implements IItemRenderer {
    public static ItemWeapon radomVis = new ItemWeapon("Radom Vis", FireMechanism.semiAutomatic, 1, 1, 9, 90, 20, 1).barrelLength(1f).sightHeight(0.1f);
    public static ItemWeapon stormRifle = new ItemWeapon("Storm Rifle", FireMechanism.automatic, 0.55, 6, 30, 120, 20, 3).barrelLength(2f).sightHeight(1f);
    public static ItemWeapon thompson = new ItemWeapon("Thompson", FireMechanism.automatic, 0.55, 6, 30, 120, 20, 2).barrelLength(1.8f).sightHeight(0.1f);
    public static ItemWeapon karbine = new ItemWeapon("Karbine", FireMechanism.semiAutomatic, 0.5, 1, 4, 90, 20, 20).barrelLength(2.5f).sightHeight(0.1f);

    public FireMechanism fireMechanism;
    public double inventoryScale;
    double adsLift;
    public int clipSize;
    public int initialAmmo;
    public int reloadTime;
    public int fireDelay;
    private float barrelLength;
    private float sightHeight;
    public ItemWeapon barrelLength(float length) { this.barrelLength = length; return this; }
    public ItemWeapon sightHeight(float height) { this.sightHeight = height; return this; }

    public ItemWeapon(String name, FireMechanism fireMechanism, double inventoryScale, double adsLift, int clipSize, int initialAmmo, int reloadTime, int fireDelay) {
        this.setUnlocalizedName(name);
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
        if(type == ItemRenderType.INVENTORY) {
            GL11.glScaled(0.8 * inventoryScale, 0.8 * inventoryScale, 0.8 * inventoryScale);
        }
        if(type == ItemRenderType.EQUIPPED) {
            GL11.glRotated(90, 0, 1, 0);
            GL11.glTranslated(0, 1, 0);
        }
        ensureTagCompund(stack);
        GL11.glScaled(0.2f, 0.2f, 0.2f);
        GL11.glRotated(90, 1, 0, 0);
        GL11.glRotated(135, 0, 0, 1);
        GL11.glRotated(0, 0, 1, 0);
        if(type != ItemRenderType.INVENTORY && stack.getTagCompound().getBoolean("InSights") && stack.getTagCompound().getInteger("Reload Timer") == 0) {
            GL11.glTranslated(-1, 3.45, -0.65 + -adsLift / 5f);
        }
        if(type != ItemRenderType.INVENTORY && stack.getTagCompound().getInteger("Reload Timer") > 0) {
            GL11.glRotated(10, 0, 1, 0);
            GL11.glRotated(50, 0, 0, -1);
        }
        if(type == ItemRenderType.INVENTORY) {
            GL11.glRotated(-45, 0, 1, 0);
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
        if(type == ItemRenderType.ENTITY) {
            GL11.glScaled(0.5, 0.5, 0.5);
        }
        modelGun.renderAll();
        if(shoot) {
            TextureHelper.bindTexture(new ResourceLocation(ZombieTC.MODID, "textures/models/muzzleflash.png"));
            GL11.glTranslated(_r.nextInt(100) / 100f - 0.5f, _r.nextInt(100) / 100f - 0.3f, _r.nextInt(100) / 100f - 0.5f);
            GL11.glTranslated(-barrelLength * 4, 0, sightHeight * 1.5f);
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

    public void ensureTagCompund(ItemStack stack) {
        if(!stack.hasTagCompound()) {
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
        if(tag.getInteger("Reload Timer") > 0) {
            tag.setInteger("Reload Timer", tag.getInteger("Reload Timer") - 1);
            if(tag.getInteger("Reload Timer") == 0) {
                tag.setInteger("Rounds", clipSize);
                tag.setInteger("ShootCooldown", 0);
            }
        }
        if(ZombieTC.editorModeManager.enabled) {
            return;
        }
        if(entity != null) {
            if(world.isRemote && entity.getClass() != EntityClientPlayerMP.class) {
                return;
            }
            if(!world.isRemote && entity.getClass() == EntityPlayerMP.class) {
                return;
            }
            EntityPlayer player = (EntityPlayer)entity;
            tag.setInteger("ShootCooldown", tag.getInteger("ShootCooldown") - 1);
            if(player.getHeldItem() == stack) {
                player.swingProgress = 0f;
                player.isSwingInProgress = false;
                player.swingProgressInt = 0;
                if(world.isRemote) {
                    if(ClientProxy.reload.isPressed() && tag.getInteger("Reload Timer") == 0 && tag.getInteger("Rounds") != clipSize) {
                        tag.setInteger("Reload Timer", reloadTime);
                        ZombieTC.network.sendToServer(new MessageReload(player));
                    }
                    if(tag.getInteger("Reload Timer") == 0 && fireMechanism.checkFire(this, stack)) {
                        if(tag.getInteger("Rounds") > 0) {
                            tag.setInteger("ShootCooldown", fireDelay);
                            tag.setBoolean("Shoot", true);
                            tag.setInteger("Rounds", tag.getInteger("Rounds") - 1);
                            //player.cameraYaw += 1;
                            ZombieTC.proxy.playSound("pistolShoot", (float)player.posX, (float)player.posY, (float)player.posZ);
                            ZombieTC.network.sendToServer(new MessageTryShoot(player));

                            //<fuckmefuckmefuckme>
                            ArrayList<Integer> damages = new ArrayList<Integer>();
                            ArrayList<Integer> tickers = new ArrayList<Integer>();
                            List blocks = new ArrayList<Block>();
                            List<Point3> blockBarricades = (List<Point3>)GameManager.blockBarricades.clone();
                            for(int i = 0; i < blockBarricades.size(); i++) {
                                Point3 vec = GameManager.blockBarricades.get(i);
                                blocks.add(world.getBlock((int)vec.xCoord, (int)vec.yCoord, (int)vec.zCoord));
                                TileEntity te = world.getTileEntity((int) vec.xCoord, (int) vec.yCoord, (int) vec.zCoord);
                                damages.add(((TileBarricade)te).damage);
                                tickers.add(((TileBarricade)te).ticker);
                                world.setBlock((int) vec.xCoord, (int) vec.yCoord, (int) vec.zCoord, Blocks.air);
                            }
                            MovingObjectPosition trace = MouseOverHelper.getMouseOver(5000.0F);
                            for(int i = 0; i < blockBarricades.size(); i++) {
                                Point3 vec = blockBarricades.get(i);
                                world.setBlock((int) vec.xCoord, (int) vec.yCoord, (int) vec.zCoord, (Block) blocks.get(i));
                                TileBarricade te = (TileBarricade)world.getTileEntity((int) vec.xCoord, (int) vec.yCoord, (int) vec.zCoord);
                                te.damage = damages.get(i);
                                te.ticker = tickers.get(i);
                            }
                            //</fuckmefuckmefuckme>
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
        }
        super.onUpdate(stack, world, entity, p_77663_4_, p_77663_5_);
    }

    public void drawUIFor(ItemStack stack, RenderGameOverlayEvent event) {
        ensureTagCompund(stack);
        NBTTagCompound tag = stack.getTagCompound();
        int rounds = tag.getInteger("Rounds");
        int totalAmmo = tag.getInteger("Ammo");
        String out = "Ammo: " + rounds + "/" + clipSize + " - " + totalAmmo;
        TextRenderHelper.drawString(out, event.resolution.getScaledWidth() - 2, 2, TextAlignment.Right);
        if(tag.getInteger("Reload Timer") > 0) {
            TextRenderHelper.drawString("Reloading...", event.resolution.getScaledWidth() - 2, 12, TextAlignment.Right);
        }
    }
}
