package net.gigimoi.zombietc.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.world.WorldEvent;

public class NaturalSpawnStopper {
    @SubscribeEvent
    public void onGetPotentialSpawns(WorldEvent.PotentialSpawns event) {
        event.setCanceled(true);
    }
}
