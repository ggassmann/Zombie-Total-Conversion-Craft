package net.gigimoi.zombietc.net.activates;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.gigimoi.zombietc.block.TileBarricade;
import net.gigimoi.zombietc.ZombieTC;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by gigimoi on 7/20/2014.
 */
public class MessageActivateRepairBarricade implements IMessage {
    int x;
    int y;
    int z;
    public MessageActivateRepairBarricade() { }
    public MessageActivateRepairBarricade(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    @Override
    public void fromBytes(ByteBuf buf) {
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
    }
    public static class MessageActivateRepairBarricadeHandler implements IMessageHandler<MessageActivateRepairBarricade, MessageActivateRepairBarricade> {
        @Override
        public MessageActivateRepairBarricade onMessage(MessageActivateRepairBarricade message, MessageContext ctx) {
            if(ctx.side.isServer()) {
                TileEntity tileRaw = MinecraftServer.getServer().getEntityWorld().getTileEntity(message.x, message.y, message.z);
                if(tileRaw != null && tileRaw.getClass() == TileBarricade.class) {
                    TileBarricade tile = (TileBarricade)tileRaw;
                    tile.damage = Math.max(tile.damage - 1, 0);
                    ZombieTC.network.sendToAll(message);
                }
            } else {
                TileEntity tileRaw = Minecraft.getMinecraft().theWorld.getTileEntity(message.x, message.y, message.z);
                if(tileRaw != null && tileRaw.getClass() == TileBarricade.class) {
                    TileBarricade tile = (TileBarricade)tileRaw;
                    tile.damage = Math.max(tile.damage - 1, 0);
                }
            }
            return null;
        }
    }
}
