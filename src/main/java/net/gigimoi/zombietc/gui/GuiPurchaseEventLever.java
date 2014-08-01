package net.gigimoi.zombietc.gui;

import net.gigimoi.zombietc.ZombieTC;
import net.gigimoi.zombietc.block.purchasable.TilePurchaseEventLever;
import net.gigimoi.zombietc.helpers.TextAlignment;
import net.gigimoi.zombietc.helpers.TextRenderHelper;
import net.gigimoi.zombietc.net.map.MessageSetPurchaseEventLeverInfo;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;

/**
 * Created by gigimoi on 7/27/2014.
 */
public class GuiPurchaseEventLever extends GuiScreen {
    public static final int GUI_ID = 10403;
    public GuiTextField textFieldPrice;
    public GuiTextField textFieldEvent;
    public GuiButton done;

    int xCoord;
    int yCoord;
    int zCoord;

    public GuiPurchaseEventLever(int x, int y, int z) {
        this.xCoord = x;
        this.yCoord = y;
        this.zCoord = z;
    }

    @Override
    public void updateScreen() {
        this.textFieldEvent.updateCursorCounter();
        this.textFieldPrice.updateCursorCounter();
        String text = this.textFieldPrice.getText();
        text = text.replaceAll("[^\\d]", "");
        this.textFieldPrice.setText(text);
    }

    @Override
    public void drawScreen(int par1, int par2, float par3) {
        this.drawDefaultBackground();
        textFieldPrice.drawTextBox();
        textFieldEvent.drawTextBox();
        TextRenderHelper.drawString("Price: ", textFieldPrice.xPosition - 2, textFieldPrice.yPosition + 5, TextAlignment.Right);
        TextRenderHelper.drawString("Event: ", textFieldEvent.xPosition - 2, textFieldEvent.yPosition + 5, TextAlignment.Right);
        super.drawScreen(par1, par2, par3);
    }

    @Override
    public void initGui() {
        super.initGui();
        textFieldEvent = new GuiTextField(this.fontRendererObj, this.width / 2 - 150, 50 + 22, 300, 20);
        textFieldEvent.setMaxStringLength(Integer.MAX_VALUE);
        textFieldEvent.setText(((TilePurchaseEventLever) mc.theWorld.getTileEntity(xCoord, yCoord, zCoord)).event);
        textFieldPrice = new GuiTextField(this.fontRendererObj, this.width / 2 - 150, 50, 300, 20);
        textFieldPrice.setMaxStringLength(Integer.MAX_VALUE);
        textFieldPrice.setFocused(true);
        textFieldPrice.setText(((TilePurchaseEventLever) mc.theWorld.getTileEntity(xCoord, yCoord, zCoord)).getPrice() + "");
        buttonList.clear();
        this.buttonList.add(done = new GuiButton(0, this.width / 2 - 4 - 150 / 2, this.height / 4 + 120 + 12, 150, 20, I18n.format("gui.done", new Object[0])));
    }

    @Override
    protected void keyTyped(char par1, int par2) {
        textFieldPrice.textboxKeyTyped(par1, par2);
        textFieldEvent.textboxKeyTyped(par1, par2);
    }

    @Override
    protected void mouseClicked(int par1, int par2, int par3) {
        super.mouseClicked(par1, par2, par3);
        textFieldPrice.mouseClicked(par1, par2, par3);
        textFieldEvent.mouseClicked(par1, par2, par3);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button == done) {
            ZombieTC.network.sendToServer(new MessageSetPurchaseEventLeverInfo(xCoord, yCoord, zCoord, textFieldEvent.getText(), Integer.parseInt(textFieldPrice.getText())));
            mc.displayGuiScreen(null);
        }
    }
}
