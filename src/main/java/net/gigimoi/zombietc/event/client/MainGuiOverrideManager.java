package net.gigimoi.zombietc.event.client;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.gigimoi.zombietc.ZombieTC;
import net.gigimoi.zombietc.gui.GuiStartGame;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
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
