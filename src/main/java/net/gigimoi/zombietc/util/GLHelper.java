package net.gigimoi.zombietc.util;

import org.lwjgl.opengl.GL11;

/**
 * Created by gigimoi on 8/31/2014.
 */
public class GLHelper {
    private GLHelper() {}
    public static void glRotateDirection(int direction) {
        if (direction == 0) {
            GL11.glTranslated(0.5, 0, 0.5);
            GL11.glRotated(180, 0, 1, 0);
        } else if (direction == 2) {
            GL11.glTranslated(0.5, 0, 0.5);
        } else if (direction == 3) {
            GL11.glRotated(90, 0, 1, 0);
            GL11.glTranslated(-0.5, 0, 0.5);
            GL11.glRotated(180, 0, 1, 0);
        } else {
            GL11.glRotated(90, 0, 1, 0);
            GL11.glTranslated(-0.5, 0, 0.5);
        }
    }
}
