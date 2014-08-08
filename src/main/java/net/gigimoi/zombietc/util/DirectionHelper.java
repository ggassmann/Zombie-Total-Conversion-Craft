package net.gigimoi.zombietc.util;

import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by gigimoi on 8/7/2014.
 */
public class DirectionHelper {
    public static int getPlayerDirection(EntityPlayer player) {
        int yaw = (int) player.rotationYaw;
        if (yaw < 0) {
            yaw += 360;
        }
        yaw += 22;
        yaw %= 360;

        return yaw / 90;
    }
}
