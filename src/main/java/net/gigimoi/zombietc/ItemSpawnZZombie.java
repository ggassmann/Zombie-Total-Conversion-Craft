package net.gigimoi.zombietc;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Created by gigimoi on 7/14/2014.
 */
public class ItemSpawnZZombie extends Item {
    static ItemSpawnZZombie _instance;

    ItemSpawnZZombie() {
        setUnlocalizedName("[DEBUG]Spawn Z Zombie");
    }

    public static ItemSpawnZZombie instance() {
        if (_instance == null) {
            _instance = new ItemSpawnZZombie();
        }
        return _instance;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (world.isRemote) {
            return stack;
        }
        EntityZZombie zombie = new EntityZZombie(world);
        zombie.setPosition(player.posX, player.posY, player.posZ);
        world.spawnEntityInWorld(zombie);
        return super.onItemRightClick(stack, world, player);
    }
}
