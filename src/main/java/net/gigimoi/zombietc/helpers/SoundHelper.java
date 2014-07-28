package net.gigimoi.zombietc.helpers;

import net.gigimoi.zombietc.ZombieTC;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

/**
 * Created by gigimoi on 7/17/2014.
 */
public class SoundHelper {
    public static void onEntityPlay(String name, World world, Entity entityName, float volume, float pitch) {
        world.playSoundAtEntity(entityName, (ZombieTC.MODID + ":" + name), (float) volume, (float) pitch);
    }
}
