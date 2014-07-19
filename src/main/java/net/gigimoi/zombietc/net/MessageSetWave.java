package net.gigimoi.zombietc.net;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.gigimoi.zombietc.ZombieTC;

/**
 * Created by gigimoi on 7/15/2014.
 */
public class MessageSetWave implements IMessage {
    public int wave;
    public MessageSetWave() {
        this(0);
    }
    public MessageSetWave(int wave) {
        this.wave = wave;
    }
    @Override
    public void fromBytes(ByteBuf buf) {
        wave = buf.getInt(0);
    }
    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(wave);
    }

    public static class MessageSetWaveHandler implements IMessageHandler<MessageSetWave, MessageSetWave> {
        @Override
        public MessageSetWave onMessage(MessageSetWave message, MessageContext ctx) {
            ZombieTC.gameManager.wave = message.wave;
            return null;
        }
    }
}
