package net.gigimoi.zombietc.gui;

import cpw.mods.fml.client.config.GuiButtonExt;
import cpw.mods.fml.relauncher.Side;
import net.gigimoi.zombietc.ZombieTC;
import net.gigimoi.zombietc.helpers.TextAlignment;
import net.gigimoi.zombietc.helpers.TextRenderHelper;
import net.gigimoi.zombietc.net.map.MessageChangeNodeDisabledUntilEvent;
import net.gigimoi.zombietc.net.map.MessageChangeNodeEventWaitFor;
import net.gigimoi.zombietc.pathfinding.TileNode;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;

/**
 * Created by gigimoi on 7/26/2014.
 */
public class GuiNode extends GuiScreen {
    public static final int GUI_ID = 10402;

    GuiButtonExt buttonDone;
    GuiButtonExt buttonYesOnlyAfterEvent;
    GuiButtonExt buttonNoOnlyAfterEvent;
    GuiTextField textFieldEvent;

    int x;
    int y;
    int z;
    public GuiNode(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public TileNode getTile() {
        return (TileNode) ZombieTC.proxy.getWorld(Side.CLIENT).getTileEntity(x, y, z);
    }

    @Override
    public void drawScreen(int par1, int par2, float par3) {
        TextRenderHelper.drawString("Disable until purchase?", width / 2 - 100, height / 2 - 100 + 32, TextAlignment.Right);
        super.drawScreen(par1, par2, par3);
        textFieldEvent.drawTextBox();
    }

    @Override
    protected void keyTyped(char par1, int par2) {
        super.keyTyped(par1, par2);
        textFieldEvent.textboxKeyTyped(par1, par2);
        if(par2 != Keyboard.KEY_ESCAPE) {
            ZombieTC.network.sendToServer(new MessageChangeNodeEventWaitFor(x, y, z, textFieldEvent.getText()));
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if(button == buttonDone) {
            mc.displayGuiScreen(null);
        }
        if(button == buttonNoOnlyAfterEvent) {
            buttonYesOnlyAfterEvent.enabled = true;
            buttonNoOnlyAfterEvent.enabled = false;
            ZombieTC.network.sendToServer(new MessageChangeNodeDisabledUntilEvent(x, y, z, false));
            textFieldEvent.setEnabled(false);
            textFieldEvent.setText("");
        }
        if(button == buttonYesOnlyAfterEvent) {
            buttonYesOnlyAfterEvent.enabled = false;
            buttonNoOnlyAfterEvent.enabled = true;
            ZombieTC.network.sendToServer(new MessageChangeNodeDisabledUntilEvent(x, y, z, true));
            textFieldEvent.setEnabled(true);
        }
    }

    @Override
    public void initGui() {
        buttonDone = new GuiButtonExt(0, width / 2 - 100, height / 2 - 100, 200, 20, I18n.format("gui.done", new Object[0]));
        buttonYesOnlyAfterEvent = new GuiButtonExt(1, buttonDone.xPosition, buttonDone.yPosition + 22, 30, 20, "Yes");
        buttonNoOnlyAfterEvent = new GuiButtonExt(1, buttonDone.xPosition + 32, buttonDone.yPosition + 22, 30, 20, "No");

        textFieldEvent = new GuiTextField(mc.fontRenderer, width / 2 - 100 + 64, height / 2 - 100 + 22, 200 - 64, 20);
        textFieldEvent.setText(getTile().eventWaitFor);

        if(getTile().deactivatedUntilEvent) {
            buttonNoOnlyAfterEvent.enabled = true;
            buttonYesOnlyAfterEvent.enabled = false;
        }
        else {
            buttonNoOnlyAfterEvent.enabled = false;
            buttonYesOnlyAfterEvent.enabled = true;
            textFieldEvent.setEnabled(false);
        }

        this.buttonList.add(buttonDone);
        this.buttonList.add(buttonYesOnlyAfterEvent);
        this.buttonList.add(buttonNoOnlyAfterEvent);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        textFieldEvent.updateCursorCounter();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    protected void mouseClicked(int par1, int par2, int par3) {
        super.mouseClicked(par1, par2, par3);
        this.textFieldEvent.mouseClicked(par1, par2, par3);
    }
}
