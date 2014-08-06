package net.gigimoi.zombietc.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

/**
 * Created by gigimoi on 7/19/2014.
 */
public class TextRenderHelper {
    public static void drawString(String str, int posX, int posY, TextAlignment alignment) {
        FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
        posX = alignment == TextAlignment.Center ? posX - fr.getStringWidth(str) / 2 : posX;
        fr.drawString(str, posX - (alignment == TextAlignment.Right ? fr.getStringWidth(str) : 0), posY + 1, 0x000000);
        fr.drawString(str, posX - (alignment == TextAlignment.Right ? fr.getStringWidth(str) : 0), posY - 1, 0x000000);
        fr.drawString(str, posX - (alignment == TextAlignment.Right ? fr.getStringWidth(str) : 0) + 1, posY, 0x000000);
        fr.drawString(str, posX - (alignment == TextAlignment.Right ? fr.getStringWidth(str) : 0) - 1, posY, 0x000000);
        fr.drawString(str, posX - (alignment == TextAlignment.Right ? fr.getStringWidth(str) : 0), posY, 0xFFFFFF);
    }
}
