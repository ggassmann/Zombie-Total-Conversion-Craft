package net.gigimoi.zombietc;

import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import net.gigimoi.zombietc.client.gui.GuiNode;
import net.gigimoi.zombietc.client.gui.GuiNodeDoor;
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
        if (ID == GuiPurchaseItemStack.GUI_ID) //Don't even think about putting else ifs here
            return new GuiPurchaseItemStack(x, y, z);
        if (ID == GuiNode.GUI_ID)
            return new GuiNode(x, y, z);
        if (ID == GuiPurchaseEventLever.GUI_ID)
            return new GuiPurchaseEventLever(x, y, z);
        if (ID == GuiNodeDoor.GUI_ID)
            return new GuiNodeDoor(x, y, z);
        return null;
    }

    public World getWorld(Side sidePrefered) {
        return MinecraftServer.getServer().getEntityWorld();
    }

    public void network() {
        SimpleNetworkWrapper network = NetworkRegistry.INSTANCE.newSimpleChannel(ZombieTC.NETWORK_CHANNEL);
        ZombieTC.network = network;
        regMessage(MessageSetWave.class);
        regMessage(MessageChangeEditorMode.class);
        regMessage(MessageAddNode.class);
        regMessage(MessageRemoveNode.class);
        regMessage(MessageAddNodeConnection.class);
        regMessage(MessageRemoveNodeConnection.class);
        regMessage(MessageShoot.class);
        regMessage(MessageTryShoot.class);
        regMessage(MessageReload.class);
        regMessage(MessageActivateRepairBarricade.class);
        regMessage(MessageSetPurchaseItemStackPrice.class);
        regMessage(MessageAddBarricade.class);
        regMessage(MessageRemoveBarricade.class);
        regMessage(MessagePrepareStaticVariables.class);
        regMessage(MessageRegeneratePathMap.class);
        regMessage(MessageChangeNodeDisabledUntilEvent.class);
        regMessage(MessageChangeNodeEventWaitFor.class);
        regMessage(MessagePurchaseTile.class);
        regMessage(MessageSetPurchaseEventLeverInfo.class);
        regMessage(MessageChangeNodeDoorTexture.class);
    }
    private int messageID;
    private void regMessage(Class<? extends IMessage> message) {
        regMessage(message, Side.CLIENT, Side.SERVER);
    }
    private void regMessage(Class<? extends IMessage> message, Side side) {
        regMessage(message, side, null);
    }
    private void regMessage(Class<? extends IMessage> message, Side side, Side otherSide) {
        if(otherSide != null) {
            regMessage(message, otherSide);
        }
        if(messageID > 255) {
            System.out.println("Warning: Over 255 messages registered, need another channel");
        } else {
            Class<? extends IMessageHandler<IMessage, IMessage>> messageHandlerClass = (Class<? extends IMessageHandler<IMessage, IMessage>>) message.getDeclaredClasses()[0];
            ZombieTC.network.registerMessage(messageHandlerClass, (Class<IMessage>)message, messageID, side);
            messageID++;
        }
    }

    public EntityPlayer getPlayer() {
        return null;
    }

    public void registerGui() {
    }
}
