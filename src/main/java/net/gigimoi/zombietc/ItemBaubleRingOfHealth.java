package net.gigimoi.zombietc;

import baubles.api.BaubleType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

/**
 * Created by gigimoi on 8/1/2014.
 */
public class ItemBaubleRingOfHealth extends ItemZombieTCBauble {
    private ItemBaubleRingOfHealth() {
        setUnlocalizedName("Ring of Health");
    }

    @Override
    public BaubleType getBaubleType(ItemStack stack) {
        return BaubleType.RING;
    }

    @Override
    public void onWornTick(ItemStack stack, EntityLivingBase entityLivingBase) {

    }

    @Override
    public void onEquipped(ItemStack stack, EntityLivingBase entityLivingBase) {

    }

    @Override
    public void onUnequipped(ItemStack stack, EntityLivingBase entityLivingBase) {

    }

    static private ItemBaubleRingOfHealth _instance;
    public static ItemBaubleRingOfHealth instance() {
        if(_instance == null) {
            _instance = new ItemBaubleRingOfHealth();
        }
        return _instance;
    }
}
