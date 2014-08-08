package net.gigimoi.zombietc;

import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import net.gigimoi.zombietc.client.gui.GuiNode;
import net.gigimoi.zombietc.client.gui.GuiPurchaseEventLever;
import net.gigimoi.zombietc.client.gui.GuiPurchaseItemStack;
import net.gigimoi.zombietc.item.weapon.ItemWeapon;
import net.gigimoi.zombietc.net.*;
import net.gigimoi.zombietc.net.activates.MessageActivateRepairBarricade;
import net.gigimoi.zombietc.net.map.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

/**
 * Created by gigimoi on 7/16/2014.
 */
public class CommonProxy implements IGuiHandler {
    public void renderers() {
    }

    public void keyBinds() {
    }

    public void playSound(String soundName, float x, float y, float z) {
    }

    public void registerWeaponRender(ItemWeapon weapon) {
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == GuiPurchaseItemStack.GUI_ID)
            return new GuiPurchaseItemStack(x, y, z);
        if (ID == GuiNode.GUI_ID)
            return new GuiNode(x, y, z);
        if (ID == GuiPurchaseEventLever.GUI_ID)
            return new GuiPurchaseEventLever(x, y, z);
        return null;
    }

    public World getWorld(Side sidePrefered) {
        return MinecraftServer.getServer().getEntityWorld();
    }

    public void network() {
        SimpleNetworkWrapper network = NetworkRegistry.INSTANCE.newSimpleChannel(ZombieTC.NETWORK_CHANNEL);
        network.registerMessage(MessageSetWave.MessageSetWaveHandler.class, MessageSetWave.class, 0, Side.CLIENT);
        network.registerMessage(MessageChangeEditorMode.MessageChangeEditorModeHandler.class, MessageChangeEditorMode.class, 1, Side.SERVER);
        network.registerMessage(MessageChangeEditorMode.MessageChangeEditorModeHandler.class, MessageChangeEditorMode.class, 2, Side.CLIENT);
        network.registerMessage(MessageAddNode.MessageAddNodeHandler.class, MessageAddNode.class, 3, Side.CLIENT);
        network.registerMessage(MessageRemoveNode.MessageRemoveNodeHandler.class, MessageRemoveNode.class, 4, Side.CLIENT);
        network.registerMessage(MessageAddNodeConnection.MessageAddNodeConnectionHandler.class, MessageAddNodeConnection.class, 5, Side.CLIENT);
        network.registerMessage(MessageAddNodeConnection.MessageAddNodeConnectionHandler.class, MessageAddNodeConnection.class, 6, Side.SERVER);
        network.registerMessage(MessageRemoveNodeConnection.MessageRemoveNodeConnectionHandler.class, MessageRemoveNodeConnection.class, 7, Side.CLIENT);
        network.registerMessage(MessageRemoveNodeConnection.MessageRemoveNodeConnectionHandler.class, MessageRemoveNodeConnection.class, 8, Side.SERVER);
        network.registerMessage(MessageShoot.MessageShootHandler.class, MessageShoot.class, 9, Side.CLIENT);
        network.registerMessage(MessageShoot.MessageShootHandler.class, MessageShoot.class, 10, Side.SERVER);
        network.registerMessage(MessageTryShoot.MessagePlayShootSoundHandler.class, MessageTryShoot.class, 11, Side.SERVER);
        network.registerMessage(MessageReload.MessageReloadHandler.class, MessageReload.class, 12, Side.CLIENT);
        network.registerMessage(MessageReload.MessageReloadHandler.class, MessageReload.class, 13, Side.SERVER);
        network.registerMessage(MessageActivateRepairBarricade.MessageActivateRepairBarricadeHandler.class, MessageActivateRepairBarricade.class, 14, Side.CLIENT);
        network.registerMessage(MessageActivateRepairBarricade.MessageActivateRepairBarricadeHandler.class, MessageActivateRepairBarricade.class, 15, Side.SERVER);
        network.registerMessage(MessageSetPurchaseItemStackPrice.MessageSetPurchaseItemStackPriceHandler.class, MessageSetPurchaseItemStackPrice.class, 16, Side.CLIENT);
        network.registerMessage(MessageSetPurchaseItemStackPrice.MessageSetPurchaseItemStackPriceHandler.class, MessageSetPurchaseItemStackPrice.class, 17, Side.SERVER);
        network.registerMessage(MessageAddBarricade.MessageAddBarricadeHandler.class, MessageAddBarricade.class, 18, Side.CLIENT);
        network.registerMessage(MessageRemoveBarricade.MessageRemoveBarricadeHandler.class, MessageRemoveBarricade.class, 19, Side.CLIENT);
        network.registerMessage(MessagePrepareStaticVariables.MessagePrepareStaticVariablesHandler.class, MessagePrepareStaticVariables.class, 20, Side.CLIENT);
        network.registerMessage(MessageRegeneratePathMap.MessageRegeneratePathMapHandler.class, MessageRegeneratePathMap.class, 21, Side.CLIENT);
        network.registerMessage(MessageRegeneratePathMap.MessageRegeneratePathMapHandler.class, MessageRegeneratePathMap.class, 22, Side.SERVER);
        network.registerMessage(MessageChangeNodeDisabledUntilEvent.MessageChangeNodeDisabledUntilEventHandler.class, MessageChangeNodeDisabledUntilEvent.class, 23, Side.CLIENT);
        network.registerMessage(MessageChangeNodeDisabledUntilEvent.MessageChangeNodeDisabledUntilEventHandler.class, MessageChangeNodeDisabledUntilEvent.class, 24, Side.SERVER);
        network.registerMessage(MessageChangeNodeEventWaitFor.MessageChangeNodeEventWaitForHandler.class, MessageChangeNodeEventWaitFor.class, 25, Side.CLIENT);
        network.registerMessage(MessageChangeNodeEventWaitFor.MessageChangeNodeEventWaitForHandler.class, MessageChangeNodeEventWaitFor.class, 26, Side.SERVER);
        network.registerMessage(MessagePurchaseTile.MessagePurchaseTileHandler.class, MessagePurchaseTile.class, 27, Side.CLIENT);
        network.registerMessage(MessagePurchaseTile.MessagePurchaseTileHandler.class, MessagePurchaseTile.class, 28, Side.SERVER);
        network.registerMessage(MessageSetPurchaseEventLeverInfo.MessageSetPurchaseEventLeverInfoHandler.class, MessageSetPurchaseEventLeverInfo.class, 29, Side.CLIENT);
        network.registerMessage(MessageSetPurchaseEventLeverInfo.MessageSetPurchaseEventLeverInfoHandler.class, MessageSetPurchaseEventLeverInfo.class, 30, Side.SERVER);
        ZombieTC.network = network;
    }

    public EntityPlayer getPlayer() {
        return null;
    }

    public void registerGui() {
    }
}
