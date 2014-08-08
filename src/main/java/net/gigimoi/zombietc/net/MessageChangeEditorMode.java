package net.gigimoi.zombietc.net;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import net.gigimoi.zombietc.ZombieTC;
import net.gigimoi.zombietc.event.PlayerManager;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

/**
 * Created by gigimoi on 7/15/2014.
 */
public class MessageChangeEditorMode implements IMessage {
    boolean enabled;

    public MessageChangeEditorMode(boolean enabled) {
        this.enabled = enabled;
    }

    public MessageChangeEditorMode() {

    }

    @Override
    public void fromBytes(ByteBuf buf) {
        enabled = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(enabled);
    }

    public static class MessageChangeEditorModeHandler implements IMessageHandler<MessageChangeEditorMode, MessageChangeEditorMode> {
        @Override
        public MessageChangeEditorMode onMessage(MessageChangeEditorMode message, MessageContext ctx) {
            ZombieTC.editorModeManager.enabled = message.enabled;
            List<EntityPlayer> players = ZombieTC.proxy.getWorld(ctx.side).playerEntities;
            for (int i = 0; i < players.size(); i++) {
                PlayerManager.ZombieTCPlayerProperties.get(players.get(i)).vim = 100;
            }
            if (!ZombieTC.editorModeManager.enabled) {
                ZombieTC.gameManager.regeneratePathMap();
            }
            if (ctx.side == Side.SERVER) {
                ZombieTC.network.sendToAll(message);
            }
            return null;
        }
    }
}
