package net.gigimoi.zombietc.helpers;

import javafx.scene.text.TextAlignment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

/**
 * Created by gigimoi on 7/19/2014.
 */
public class TextRenderHelper {
    public static void drawString(String str, int posX, int posY, TextAlignment alignment) {
        FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
        fr.drawString(str, posX - (alignment == TextAlignment.RIGHT ? fr.getStringWidth(str) : 0), posY + 1, 0x000000);
        fr.drawString(str, posX - (alignment == TextAlignment.RIGHT ? fr.getStringWidth(str) : 0), posY - 1, 0x000000);
        fr.drawString(str, posX - (alignment == TextAlignment.RIGHT ? fr.getStringWidth(str) : 0) + 1, posY, 0x000000);
        fr.drawString(str, posX - (alignment == TextAlignment.RIGHT ? fr.getStringWidth(str) : 0) - 1, posY, 0x000000);
        fr.drawString(str, posX - (alignment == TextAlignment.RIGHT ? fr.getStringWidth(str) : 0), posY, 0xFFFFFF);
    }
}
