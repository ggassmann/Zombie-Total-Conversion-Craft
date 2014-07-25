package net.gigimoi.zombietc.gui;

import cpw.mods.fml.client.FMLClientHandler;
import net.gigimoi.zombietc.helpers.TextAlignment;
import net.gigimoi.zombietc.helpers.TextRenderHelper;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.renderer.Tessellator;
import org.apache.commons.io.FileUtils;
import org.lwjgl.opengl.GL11;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gigimoi on 7/24/2014.
 */
public class GuiStartGame extends GuiScreen {
    public GuiScreen returnTo;

    public URL downloading = null;
    boolean hasRenderedDownloading = false;
    public String mapName;
    List<String> mapUrls = new ArrayList<String>();
    List<String> mapNames = new ArrayList<String>();

    public final int CLOSE_BUTTON_ID = 600;
    public GuiButton closebutton;
    public GuiStartGame(GuiScreen screen) {
        returnTo = screen;
    }

    @Override
    public void drawScreen(int par1, int par2, float par3) {
        this.drawDefaultBackground();
        super.drawScreen(par1, par2, par3);
        TextRenderHelper.drawString("Select a map:", width / 2, 2, TextAlignment.Center);
        if(downloading != null) {
            TextRenderHelper.drawString("Downloading...", 0, 0, TextAlignment.Left);
            hasRenderedDownloading = true;
        }
    }

    @Override
    protected void keyTyped(char keyChar, int keyCode) {

    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if(button == closebutton) {
            Minecraft.getMinecraft().displayGuiScreen(returnTo);
        }
        if(button.id >= 700) {
            URL map = null;
            try { map = new URL(mapUrls.get(button.id - 700)); } catch (MalformedURLException e) {e.printStackTrace(); }
            downloading = map;
            mapName = mapNames.get(button.id - 700);
        }
    }

    @Override
    public void initGui() {
        super.initGui();
        closebutton = new GuiButton(CLOSE_BUTTON_ID, 10, this.height - 28, 40, 20, "Close");
        buttonList.add(closebutton);
        ArrayList<String> mapsList = new ArrayList<String>();
        try {
            URL url = new URL("https://raw.githubusercontent.com/gigimoi/Zombie-Total-Conversion-Craft/master/maps/maps.txt");
            FileUtils.copyURLToFile(url, new File("tmp"));
            mapsList = (ArrayList<String>)FileUtils.readLines(new File("tmp"));
            FileUtils.forceDelete(new File("tmp"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(int i = 0; i < mapsList.size(); i++) {
            String mapName = mapsList.get(i).substring(0, mapsList.get(i).lastIndexOf(":"));
            mapName = mapName.substring(0, mapName.lastIndexOf(":"));
            buttonList.add(new GuiButton(700 + i, width / 2 - 150, 30 + i * 30, 300, 20, mapName));
            mapUrls.add(mapsList.get(i).substring(mapName.length() + 1));
            mapNames.add(mapName);
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        if(downloading != null && hasRenderedDownloading == true) {
            try {
                FileUtils.copyURLToFile(downloading, new File("tmpmap"));
                ZipFile zipFile = new ZipFile("tmpmap");
                zipFile.extractAll("saves/");
                FileUtils.forceDelete(new File("tmpmap"));
                downloading = null;
                hasRenderedDownloading = false;
                FMLClientHandler.instance().tryLoadExistingWorld(null, mapName, mapName);
            } catch (IOException e) {
                e.printStackTrace();
                downloading = null;
                hasRenderedDownloading = false;
            } catch (ZipException e) {
                e.printStackTrace();
                downloading = null;
                hasRenderedDownloading = false;
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
