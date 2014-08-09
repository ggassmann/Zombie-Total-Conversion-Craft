package net.gigimoi.zombietc.client.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.gigimoi.zombietc.client.gui.GuiStartGame;
import net.gigimoi.zombietc.util.Lang;
import net.gigimoi.zombietc.util.TextAlignment;
import net.gigimoi.zombietc.util.TextRenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraftforge.client.event.GuiScreenEvent;

/**
 * Created by gigimoi on 7/24/2014.
 */
public class MainGuiOverrideManager {
    public static final int BUTTON_ID_NEW_GAME = 500;

    @SubscribeEvent
    public void onDrawGui(GuiScreenEvent.DrawScreenEvent event) {
        if (event.gui.getClass() == GuiMainMenu.class) {
            TextRenderHelper.drawString(Lang.get("itemGroup.Zombie Total Conversion"), 2, 2, TextAlignment.Left);
        }
    }

    @SubscribeEvent
    public void onInitGui(GuiScreenEvent.InitGuiEvent.Post event) {
        if(event.gui.getClass() == GuiGameOver.class && event.buttonList.size() > 0) {
            GuiButton respawnButton = (GuiButton) event.buttonList.get(0);
            event.buttonList.remove(respawnButton);
        }
        else if (event.gui.getClass() == GuiMainMenu.class && event.buttonList.size() > 0) {
            GuiButton button = (GuiButton) event.buttonList.get(0);
            button.displayString = Lang.get("ui.Continue");
            button.width /= 2;
            button.width -= 2;
            GuiButton buttonNewGame = new GuiButton(BUTTON_ID_NEW_GAME, button.xPosition + button.width + 4, button.yPosition, button.width, button.height, "ui.newGame");
            event.buttonList.add(buttonNewGame);
        }
        else if (event.gui.getClass() == GuiSelectWorld.class && event.buttonList.size() > 0) {
            event.buttonList.remove(4);
            GuiButton cancelButton = (GuiButton) event.buttonList.get(4);
            cancelButton.xPosition -= cancelButton.width + 6;
            cancelButton.width *= 2;
            cancelButton.width += 6;
            GuiButton createWorldButton = (GuiButton) event.buttonList.get(1);
            createWorldButton.displayString = Lang.get("ui.New World (Editor mode)");
        }
    }

    @SubscribeEvent
    public void onGuiActivate(GuiScreenEvent.ActionPerformedEvent.Pre event) {
        if (event.gui.getClass() == GuiMainMenu.class) {
            if (event.button.id == BUTTON_ID_NEW_GAME) {
                Minecraft.getMinecraft().displayGuiScreen(new GuiStartGame(event.gui));
            }
        }
    }
}
