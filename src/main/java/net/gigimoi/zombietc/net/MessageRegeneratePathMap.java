package net.gigimoi.zombietc.net;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.gigimoi.zombietc.ZombieTC;
import net.gigimoi.zombietc.event.GameManager;
import net.gigimoi.zombietc.pathfinding.BlockNode;
import net.gigimoi.zombietc.pathfinding.MCNode;

import java.util.ArrayList;

/**
 * Created by gigimoi on 7/22/2014.
 */
public class MessageRegeneratePathMap implements IMessage {
    @Override
    public void fromBytes(ByteBuf buf) {
    }
    @Override
    public void toBytes(ByteBuf buf) {
    }
    public static class MessageRegeneratePathMapHandler implements IMessageHandler<MessageRegeneratePathMap, MessageRegeneratePathMap> {
        @Override
        public MessageRegeneratePathMap onMessage(MessageRegeneratePathMap message, MessageContext ctx) {
            ZombieTC.gameManager.regeneratePathMap();
            return null;
        }
    }
}
