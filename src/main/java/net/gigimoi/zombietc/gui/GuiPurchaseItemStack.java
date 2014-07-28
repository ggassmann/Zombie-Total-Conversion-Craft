package net.gigimoi.zombietc.gui;

import net.gigimoi.zombietc.ZombieTC;
import net.gigimoi.zombietc.block.TilePurchaseItemStack;
import net.gigimoi.zombietc.net.MessageSetPurchaseItemStackPrice;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;

/**
 * Created by gigimoi on 7/21/2014.
 */
public class GuiPurchaseItemStack extends GuiScreen {
    public static final int GUI_ID = 10401;
    public GuiTextField priceTextField;
    public GuiButton done;

    int xCoord;
    int yCoord;
    int zCoord;

    public GuiPurchaseItemStack(int x, int y, int z) {
        this.xCoord = x;
        this.yCoord = y;
        this.zCoord = z;
    }

    @Override
    public void updateScreen()
    {
        this.priceTextField.updateCursorCounter();
        String text = this.priceTextField.getText();
        text = text.replaceAll("[^\\d]", "");
        this.priceTextField.setText(text);
    }

    @Override
    public void drawScreen(int par1, int par2, float par3) {
        this.drawDefaultBackground();
        priceTextField.drawTextBox();
        super.drawScreen(par1, par2, par3);
    }

    @Override
    public void initGui() {
        super.initGui();
        priceTextField = new GuiTextField(this.fontRendererObj, this.width / 2 - 150, 50, 300, 20);
        priceTextField.setMaxStringLength(Integer.MAX_VALUE);
        priceTextField.setFocused(true);
        priceTextField.setText(((TilePurchaseItemStack)mc.theWorld.getTileEntity(xCoord, yCoord, zCoord)).getPrice() + "");
        buttonList.clear();
        this.buttonList.add(done = new GuiButton(0, this.width / 2 - 4 - 150 / 2, this.height / 4 + 120 + 12, 150, 20, I18n.format("gui.done", new Object[0])));
    }
    @Override
    protected void keyTyped(char par1, int par2)
    {
        this.priceTextField.textboxKeyTyped(par1, par2);
    }
    @Override
    protected void mouseClicked(int par1, int par2, int par3)
    {
        super.mouseClicked(par1, par2, par3);
        this.priceTextField.mouseClicked(par1, par2, par3);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if(button == done) {
            ZombieTC.network.sendToServer(new MessageSetPurchaseItemStackPrice(xCoord, yCoord, zCoord, Integer.parseInt(priceTextField.getText())));
            mc.displayGuiScreen(null);
        }
    }
}
