package net.gigimoi.zombietc.item.weapon;

import net.gigimoi.zombietc.WeaponLoader;
import net.gigimoi.zombietc.ZombieTC;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by gigimoi on 7/18/2014.
 */
public class FireMechanism {
    public static void init() {
        new FireMechanism("semiAutomatic") {
            @Override
            public boolean checkFire(ItemWeapon weaponType, ItemStack stack) {
                if (ZombieTC.mouseManager.isLeftPressed() && Minecraft.getMinecraft().currentScreen == null) {
                    if (stack.getTagCompound().getInteger("ShootCooldown") <= 0) {
                        return true;
                    }
                }
                return false;
            }
        };
        new FireMechanism("automatic") {
            @Override
            public boolean checkFire(ItemWeapon weaponType, ItemStack stack) {
                if (ZombieTC.mouseManager.isLeftDown() && Minecraft.getMinecraft().currentScreen == null) {
                    if (stack.getTagCompound().getInteger("ShootCooldown") <= 0) {
                        return true;
                    }
                }
                return false;
            }
        };
        new FireMechanismBurst("burst3Fast", 3, 0);
        new FireMechanismBurst("burst3Slow", 3, 2);
    }

    public boolean checkFire(ItemWeapon weaponType, ItemStack stack) {
        return false;
    }

    public FireMechanism(String id) {
        WeaponLoader.fireMechanisms.put(id, this);
    }

    public boolean canReload(ItemWeapon weaponType, ItemStack stack) {
        return true;
    }

    private static class FireMechanismBurst extends FireMechanism {
        private int burstShotsLeft;
        private ItemWeapon burstingWeapon;
        private int burstShotDelay;
        private int shots;
        private int delay;

        public FireMechanismBurst(String id, int shots, int delay) {
            super(id);
            this.shots = shots;
            this.delay = delay;
        }

        @Override
        public boolean checkFire(ItemWeapon weaponType, ItemStack stack) {
            NBTTagCompound tag = stack.getTagCompound();
            burstShotDelay = Math.max(0, burstShotDelay - 1);
            if (burstingWeapon == weaponType && burstShotsLeft > 0 && burstShotDelay == 0) {
                burstShotsLeft--;
                burstShotDelay = delay;
                return true;
            }
            if (tag.getInteger("ShootCooldown") <= 0) {
                if (ZombieTC.mouseManager.isLeftPressed() && Minecraft.getMinecraft().currentScreen == null) {
                    burstingWeapon = weaponType;
                    burstShotsLeft = shots - 1;
                    burstShotDelay = delay;
                    stack.setTagCompound(tag);
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean canReload(ItemWeapon weaponType, ItemStack stack) {
            if(burstShotsLeft == 0) {
                return super.canReload(weaponType, stack);
            }
            return false;
        }
    }
}
