package net.gigimoi.zombietc.item.weapon;

import net.gigimoi.zombietc.ZombieTC;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by gigimoi on 7/18/2014.
 */
public class FireMechanism {
    public static final FireMechanism semiAutomatic = new FireMechanism() {
        @Override
        public boolean checkFire(ItemWeapon weaponType, ItemStack stack) {
            if (ZombieTC.mouseManager.isLeftPressed()) {
                if (stack.getTagCompound().getInteger("ShootCooldown") <= 0) {
                    return true;
                }
            }
            return false;
        }
    };
    public static final FireMechanism automatic = new FireMechanism() {
        @Override
        public boolean checkFire(ItemWeapon weaponType, ItemStack stack) {
            if (ZombieTC.mouseManager.isLeftDown()) {
                if (stack.getTagCompound().getInteger("ShootCooldown") <= 0) {
                    return true;
                }
            }
            return false;
        }
    };
    public static FireMechanism burst3fast = new FireMechanismBurst(3, 0);
    public static FireMechanism burst3slow = new FireMechanismBurst(3, 2);

    public boolean checkFire(ItemWeapon weaponType, ItemStack stack) {
        return false;
    }
    private static class FireMechanismBurst extends FireMechanism {
        private int burstShotsLeft;
        private ItemWeapon burstingWeapon;
        private int burstShotDelay;
        private int shots;
        private int delay;

        public FireMechanismBurst(int shots, int delay) {
            this.shots = shots;
            this.delay = delay;
        }

        @Override
        public boolean checkFire(ItemWeapon weaponType, ItemStack stack) {
            NBTTagCompound tag = stack.getTagCompound();
            burstShotDelay = Math.max(0, burstShotDelay - 1);
            if(burstingWeapon == weaponType && burstShotsLeft > 0 && burstShotDelay == 0) {
                burstShotsLeft--;
                burstShotDelay = delay;
                return true;
            }
            if (tag.getInteger("ShootCooldown") <= 0) {
                if(ZombieTC.mouseManager.isLeftPressed()) {
                    burstingWeapon = weaponType;
                    burstShotsLeft = shots - 1;
                    burstShotDelay = delay;
                    stack.setTagCompound(tag);
                    return true;
                }
            }
            return false;
        }
    }
}
