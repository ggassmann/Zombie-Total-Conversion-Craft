package net.gigimoi.zombietc.event.client;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.gigimoi.zombietc.ZombieTC;
import net.gigimoi.zombietc.gui.GuiStartGame;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraftforge.client.event.GuiScreenEvent;

/**
 * Created by gigimoi on 7/24/2014.
 */
public class MainGuiOverrideManager {
    public static final int BUTTON_ID_NEW_GAME = 500;
    @SubscribeEvent
    public void onInitGui(GuiScreenEvent.InitGuiEvent.Post event) {
        if(event.gui.getClass() == GuiMainMenu.class && event.buttonList.size() > 0) {
            GuiButton button = (GuiButton)event.buttonList.get(0);
            button.displayString = "Continue";
            button.width /= 2;
            button.width -= 2;
            GuiButton buttonNewGame = new GuiButton(BUTTON_ID_NEW_GAME, button.xPosition + button.width + 4, button.yPosition, button.width, button.height, "New Game");
            event.buttonList.add(buttonNewGame);
        }
        if(event.gui.getClass() == GuiSelectWorld.class && event.buttonList.size() > 0) {
            event.buttonList.remove(4);
            GuiButton cancelButton = (GuiButton) event.buttonList.get(4);
            cancelButton.xPosition -= cancelButton.width + 6;
            cancelButton.width *= 2;
            cancelButton.width += 6;
        }
    }
    @SubscribeEvent
    public void onGuiActivate(GuiScreenEvent.ActionPerformedEvent.Pre event) {
        if(event.gui.getClass() == GuiMainMenu.class) {
            if(event.button.id == BUTTON_ID_NEW_GAME) {
                Minecraft.getMinecraft().displayGuiScreen(new GuiStartGame(event.gui));
            }
        }
    }
}