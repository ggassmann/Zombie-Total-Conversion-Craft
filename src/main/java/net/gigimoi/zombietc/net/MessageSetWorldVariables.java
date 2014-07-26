package net.gigimoi.zombietc.net;

import com.google.gson.Gson;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.gigimoi.zombietc.event.GameManager;
import net.gigimoi.zombietc.helpers.ByteBufHelper;
import net.minecraft.client.Minecraft;

import java.util.HashMap;

/**
 * Created by gigimoi on 7/26/2014.
 */
public class MessageSetWorldVariables implements IMessage {
    HashMap<String, Object> worldVariables;
    public MessageSetWorldVariables(HashMap<String, Object> worldVariables) {
        this.worldVariables = worldVariables;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        Gson gson = new Gson();
        worldVariables = gson.fromJson(ByteBufHelper.readString(buf), HashMap.class);
    }
    @Override
    public void toBytes(ByteBuf buf) {
        Gson gson = new Gson();
        ByteBufHelper.writeString(gson.toJson(worldVariables), buf);
    }
    public static class MessageSetWorldVariablesHandler implements IMessageHandler<MessageSetWorldVariables, MessageSetWorldVariables> {
        @Override
        public MessageSetWorldVariables onMessage(MessageSetWorldVariables message, MessageContext ctx) {
            GameManager.worldVariables = message.worldVariables;
            return null;
        }
    }
}
