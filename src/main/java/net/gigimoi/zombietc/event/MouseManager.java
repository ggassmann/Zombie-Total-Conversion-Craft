package net.gigimoi.zombietc.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Mouse;

/**
 * Created by gigimoi on 7/18/2014.
 */
public class MouseManager {
    boolean leftPressed = false;
    boolean leftPressedLast = false;
    public boolean isLeftPressed() {
        return leftPressed && !leftPressedLast;
    }
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if(event.phase == TickEvent.Phase.START) {
            leftPressedLast = leftPressed;
            leftPressed = Mouse.isButtonDown(0);
        }
    }
}
