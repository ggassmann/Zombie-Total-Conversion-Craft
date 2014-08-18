package net.gigimoi.zombietc.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.world.WorldEvent;

public class NaturalSpawnManager {
    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onPotentialSpawns(WorldEvent.PotentialSpawns event) {
        event.setCanceled(true);
    }
}
