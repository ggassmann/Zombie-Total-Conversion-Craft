package net.gigimoi.zombietc.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.renderer.Tessellator;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.lwjgl.opengl.GL11;

/**
 * Created by gigimoi on 7/24/2014.
 */
public class GuiStartGame extends GuiScreen {
    public GuiScreen returnTo;

    public final int CLOSE_BUTTON_ID = 600;
    public GuiButton closebutton;
    public GuiStartGame(GuiScreen screen) {
        returnTo = screen;
    }

    @Override
    public void drawScreen(int par1, int par2, float par3) {
        this.drawDefaultBackground();
        super.drawScreen(par1, par2, par3);
        GL11.glPushMatrix();
        GL11.glTranslated(50, 20, 0);
        GL11.glPopMatrix();
        //this.drawCenteredString(this.fontRendererObj, I18n.format("selectWorld.create", new Object[0]), this.width / 2, 20, -1);
    }

    @Override
    protected void keyTyped(char keyChar, int keyCode) {

    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if(button == closebutton) {
            Minecraft.getMinecraft().displayGuiScreen(returnTo);
        }
    }

    @Override
    public void initGui() {
        super.initGui();
        closebutton = new GuiButton(CLOSE_BUTTON_ID, 10, this.height - 28, 40, 20, "Close");
        buttonList.add(closebutton);
        GitHubClient client = new GitHubClient();
        
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }
}
