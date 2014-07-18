package net.gigimoi.zombietc;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.world.WorldEvent;

public class NaturalSpawnStopper {
    @SubscribeEvent
    public void onGetPotentialSpawns(WorldEvent.PotentialSpawns event) {
        event.setCanceled(true);
    }
}
