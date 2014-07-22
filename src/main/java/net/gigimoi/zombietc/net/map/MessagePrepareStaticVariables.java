package net.gigimoi.zombietc.net.map;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.gigimoi.zombietc.ZombieTC;
import net.minecraft.util.Vec3;

import java.util.ArrayList;

/**
 * Created by gigimoi on 7/22/2014.
 */
public class MessagePrepareStaticVariables implements IMessage {
    @Override
    public void fromBytes(ByteBuf buf) {
    }
    @Override
    public void toBytes(ByteBuf buf) {
    }
    public static class MessagePrepareStaticVariablesHandler implements IMessageHandler<MessagePrepareStaticVariables, MessagePrepareStaticVariables> {

        @Override
        public MessagePrepareStaticVariables onMessage(MessagePrepareStaticVariables message, MessageContext ctx) {
            ZombieTC.gameManager.blockBarricades = new ArrayList<Vec3>();
            return null;
        }
    }
}
