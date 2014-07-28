package net.gigimoi.zombietc.gui;

import cpw.mods.fml.client.FMLClientHandler;
import de.matthiasmann.twl.utils.PNGDecoder;
import net.gigimoi.zombietc.helpers.TextAlignment;
import net.gigimoi.zombietc.helpers.TextRenderHelper;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.apache.commons.io.FileUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;

/**
 * Created by gigimoi on 7/24/2014.
 */
public class GuiStartGame extends GuiScreen {
    public final int CLOSE_BUTTON_ID = 600;
    public GuiScreen returnTo;
    public URL downloading = null;
    public String mapName;
    public GuiButton closebutton;
    boolean hasRenderedDownloading = false;
    List<String> mapUrls = new ArrayList<String>();
    List<String> mapNames = new ArrayList<String>();
    List<Integer> mapThumbnailTextureNames = new ArrayList<Integer>();

    public GuiStartGame(GuiScreen screen) {
        returnTo = screen;
    }

    @Override
    public void drawScreen(int par1, int par2, float par3) {
        this.drawDefaultBackground();
        TextRenderHelper.drawString("Select a map:", width / 2, 2, TextAlignment.Center);
        for (int i = 0; i < mapNames.size(); i++) {
            TextRenderHelper.drawString(mapNames.get(i), width / 2 - 5, 64 + i * 30, TextAlignment.Left);
            glBindTexture(GL_TEXTURE_2D, mapThumbnailTextureNames.get(i));
            GL11.glPushMatrix();
            GL11.glTranslated(width / 2 - 110, 20 + i * 30, 0);
            GL11.glScaled(0.3, 0.3, 0.3);
            drawTexturedModalRect(0, 0, 0, 0, 250, 250);
            GL11.glPopMatrix();
        }
        if (downloading != null) {
            TextRenderHelper.drawString("Downloading...", 0, 0, TextAlignment.Left);
            hasRenderedDownloading = true;
        }
        super.drawScreen(par1, par2, par3);
    }

    @Override
    protected void keyTyped(char keyChar, int keyCode) {

    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button == closebutton) {
            Minecraft.getMinecraft().displayGuiScreen(returnTo);
        }
        if (button.id >= 700) {
            URL map = null;
            try {
                map = new URL(mapUrls.get(button.id - 700));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            downloading = map;
            mapName = mapNames.get(button.id - 700);
        }
    }

    @Override
    public void initGui() {
        super.initGui();
        mapUrls = new ArrayList<String>();
        mapNames = new ArrayList<String>();
        mapThumbnailTextureNames = new ArrayList<Integer>();
        closebutton = new GuiButton(CLOSE_BUTTON_ID, 10, this.height - 28, 40, 20, "Close");
        buttonList.add(closebutton);
        ArrayList<String> mapsList = new ArrayList<String>();
        try {
            URL url = new URL("https://raw.githubusercontent.com/gigimoi/Zombie-Total-Conversion-Craft/master/maps/maps.txt");
            FileUtils.copyURLToFile(url, new File("tmp"));
            mapsList = (ArrayList<String>) FileUtils.readLines(new File("tmp"));
            FileUtils.forceDelete(new File("tmp"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < mapsList.size(); i++) {
            String mapName = mapsList.get(i).substring(0, mapsList.get(i).lastIndexOf(":"));
            mapName = mapName.substring(0, mapName.lastIndexOf(":"));
            buttonList.add(new GuiButton(700 + i, width / 2 - 10, 72 + i * 30, 150, 20, "Play"));
            mapUrls.add(mapsList.get(i).substring(mapName.length() + 1));
            mapNames.add(mapName);
            File thumbnail = new File(".imagecache/" + mapName + ".png");
            if (!thumbnail.exists()) {
                try {
                    FileUtils.copyURLToFile(new URL("https://raw.githubusercontent.com/gigimoi/Zombie-Total-Conversion-Craft/master/maps/" + mapName.replace(" ", "%20") + ".png"), thumbnail);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (thumbnail.exists()) {
                mapThumbnailTextureNames.add(loadPNGTexture(thumbnail.getAbsolutePath(), GL13.GL_TEXTURE0));
            }
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        if (downloading != null && hasRenderedDownloading == true) {
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

    private int loadPNGTexture(String filename, int textureUnit) {
        ByteBuffer buf = null;
        int tWidth = 0;
        int tHeight = 0;

        try {
            // Open the PNG file as an InputStream
            InputStream in = new FileInputStream(filename);
            // Link the PNG decoder to this stream
            PNGDecoder decoder = new PNGDecoder(in);

            // Get the width and height of the texture
            tWidth = decoder.getWidth();
            tHeight = decoder.getHeight();


            // Decode the PNG file in a ByteBuffer
            buf = ByteBuffer.allocateDirect(
                    4 * decoder.getWidth() * decoder.getHeight());
            decoder.decode(buf, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
            buf.flip();

            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        // Create a new texture object in memory and bind it
        int texId = GL11.glGenTextures();
        GL13.glActiveTexture(textureUnit);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);

        // All RGB bytes are aligned to each other and each component is 1 byte
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);

        // Upload the texture data and generate mip maps (for scaling)
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, tWidth, tHeight, 0,
                GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buf);
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);

        // Setup the ST coordinate system
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

        // Setup what to do when the texture has to be scaled
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER,
                GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
                GL11.GL_LINEAR_MIPMAP_LINEAR);
        return texId;
    }
}
