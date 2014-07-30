package net.gigimoi.zombietc.event;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.gigimoi.zombietc.ZombieTC;
import net.gigimoi.zombietc.helpers.ByteBufHelper;
import net.minecraft.server.MinecraftServer;

/**
 * Created by gigimoi on 7/28/2014.
 */
public class MessageScoreboardAddRemoveEntry implements IMessage {
    public boolean add;
    public String entry;
    public int initalValueAdd;
    public MessageScoreboardAddRemoveEntry() { }
    public MessageScoreboardAddRemoveEntry(String entry){
        this.add = false;
        this.entry = entry;
    }
    public MessageScoreboardAddRemoveEntry(String entry, int value){
        this.add = true;
        this.entry = entry;
        this.initalValueAdd = value;
    }
    @Override
    public void fromBytes(ByteBuf buf) {
        add = buf.readBoolean();
        entry = ByteBufHelper.readString(buf);
        if(add) {
            initalValueAdd = buf.readInt();
        }
    }
    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(add);
        ByteBufHelper.writeString(entry, buf);
        if(add) {
            buf.writeInt(initalValueAdd);
        }
    }

    public static class MessageScoreboardAddRemoveEntryHandler implements IMessageHandler<MessageScoreboardAddRemoveEntry, MessageScoreboardAddRemoveEntry> {
        @Override
        public MessageScoreboardAddRemoveEntry onMessage(MessageScoreboardAddRemoveEntry message, MessageContext ctx) {
            if(ctx.side.isClient() && MinecraftServer.getServer() != null && MinecraftServer.getServer().isServerRunning()) {
                return null;
            }
            if(message.add) {
                System.out.println("Recieved" + message.entry);
                ZombieTC.scoreboardManager.scoreboardNames.add(message.entry);
                ZombieTC.scoreboardManager.scoreboardScores.add(message.initalValueAdd);
            } else {
                int index = -1;
                for(int i = 0; i < ZombieTC.scoreboardManager.scoreboardNames.size(); i++) {
                    if(ZombieTC.scoreboardManager.scoreboardNames.get(i).equals(message.entry)) {
                        index = i;
                        break;
                    }
                }
                if(index != -1) {
                    ZombieTC.scoreboardManager.scoreboardNames.remove(index);
                    ZombieTC.scoreboardManager.scoreboardScores.remove(index);
                }
            }
            return null;
        }
    }
}
