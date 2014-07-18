package net.gigimoi.zombietc.weapon;

import net.gigimoi.zombietc.ZombieTC;
import net.minecraft.item.ItemStack;

/**
 * Created by gigimoi on 7/18/2014.
 */
public class FireMechanism {
    public static final FireMechanism semiAutomatic = new FireMechanism() {
        @Override
        public boolean checkFire(ItemWeapon weaponType, ItemStack stack) {
            if(ZombieTC.mouseManager.isLeftPressed()) {
                if(stack.getTagCompound().getInteger("ShootCooldown") <= 0) {
                    return true;
                }
            }
            return false;
        }
    };
    public boolean checkFire(ItemWeapon weaponType, ItemStack stack) {
        return false;
    }
}
