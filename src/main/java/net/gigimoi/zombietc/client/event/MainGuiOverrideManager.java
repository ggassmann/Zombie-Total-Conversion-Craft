package net.gigimoi.zombietc.client.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.gigimoi.zombietc.ZombieTC;
import net.gigimoi.zombietc.client.gui.GuiStartGame;
import net.gigimoi.zombietc.util.Lang;
import net.gigimoi.zombietc.util.TextAlignment;
import net.gigimoi.zombietc.util.TextRenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.client.event.GuiScreenEvent;
import org.apache.commons.io.FileUtils;
import org.lwjgl.input.Keyboard;

import java.io.File;
import java.io.IOException;

/**
 * Created by gigimoi on 7/24/2014.
 */
public class MainGuiOverrideManager {
    public static final int BUTTON_ID_NEW_GAME = 500;
    public static final int BUTTON_ID_EDITOR_MODE = 501;

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onDrawGui(GuiScreenEvent.DrawScreenEvent event) {
        if (event.gui.getClass() == GuiMainMenu.class) {
            TextRenderHelper.drawString(Lang.get("itemGroup.Zombie Total Conversion"), 2, 2, TextAlignment.Left);
        }
        else if(event.gui.getClass() == GuiGameOver.class) {
            if(Keyboard.isKeyDown(Keyboard.KEY_G) && Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                Minecraft.getMinecraft().thePlayer.respawnPlayer();
                Minecraft.getMinecraft().displayGuiScreen(null);
            }
            TextRenderHelper.drawString(Lang.get("ui.debug.ongameover"), 2, 2, TextAlignment.Left);
        }
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onInitGui(GuiScreenEvent.InitGuiEvent.Post event) {
        if(event.gui.getClass() == GuiGameOver.class) {
            GuiButton respawnButton = (GuiButton) event.buttonList.get(0);
            event.buttonList.remove(respawnButton);
        }
        else if (event.gui.getClass() == GuiMainMenu.class) {
            GuiButton button = (GuiButton) event.buttonList.get(0);
            button.displayString = Lang.get("ui.Editor Mode");
            button.id = BUTTON_ID_EDITOR_MODE;
            button.width /= 2;
            button.width -= 2;
            GuiButton buttonNewGame = new GuiButton(BUTTON_ID_NEW_GAME, button.xPosition + button.width + 4, button.yPosition, button.width, button.height, Lang.get("ui.newGame"));
            event.buttonList.add(buttonNewGame);
        }
        else if (event.gui.getClass() == GuiSelectWorld.class) {
            event.buttonList.remove(4);
            GuiButton cancelButton = (GuiButton) event.buttonList.get(4);
            cancelButton.xPosition -= cancelButton.width + 6;
            cancelButton.width *= 2;
            cancelButton.width += 6;
            GuiButton createWorldButton = (GuiButton) event.buttonList.get(1);
            createWorldButton.displayString = Lang.get("ui.New World (Editor mode)");
        }
        else if (event.gui.getClass() == GuiIngameMenu.class) {
            ((GuiButton)event.buttonList.get(1)).yPosition += 24;
            ((GuiButton)event.buttonList.get(2)).width *= 2;
            ((GuiButton)event.buttonList.get(2)).width += 4;
            event.buttonList.remove(3);
            event.buttonList.remove(4);
            event.buttonList.remove(4);
            GuiButton quitButton = (GuiButton) event.buttonList.get(0);
            quitButton.displayString = Lang.get("ui.quit");
        }
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onGuiActivate(GuiScreenEvent.ActionPerformedEvent.Pre event) {
        if (event.gui.getClass() == GuiMainMenu.class) {
            if (event.button.id == BUTTON_ID_NEW_GAME) {
                Minecraft.getMinecraft().displayGuiScreen(new GuiStartGame(event.gui));
                ZombieTC.editorModeManager.isEditor = false;
            }
            if(event.button.id == BUTTON_ID_EDITOR_MODE) {
                Minecraft.getMinecraft().displayGuiScreen(new GuiSelectWorld(event.gui));
                ZombieTC.editorModeManager.isEditor = true;
            }
        } else if(event.gui.getClass() == GuiGameOver.class) {
            if(event.button.id == 1) {
                //<copypasta from GuiGameOver>
                Minecraft.getMinecraft().theWorld.sendQuittingDisconnectingPacket();
                Minecraft.getMinecraft().loadWorld(null);
                Minecraft.getMinecraft().displayGuiScreen(new GuiMainMenu());
                //</copypasta>
                event.setCanceled(true);
            };
        } else if(event.gui.getClass() == GuiIngameMenu.class) {
            if(event.button.id == 1) {
                //<copypasta from GuiGameOver>
                String worldDirectory = "saves/" + MinecraftServer.getServer().getFolderName();
                Minecraft.getMinecraft().theWorld.sendQuittingDisconnectingPacket();
                Minecraft.getMinecraft().loadWorld(null);
                Minecraft.getMinecraft().displayGuiScreen(new GuiMainMenu());
                //</copypasta>
                event.setCanceled(true);
                if(!ZombieTC.editorModeManager.isEditor) {
                    try {
                        FileUtils.deleteDirectory(new File(worldDirectory));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
