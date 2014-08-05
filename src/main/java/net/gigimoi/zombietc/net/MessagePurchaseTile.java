package net.gigimoi.zombietc.net;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import net.gigimoi.zombietc.ZombieTC;
import net.gigimoi.zombietc.block.ITileEntityActivatable;
import net.gigimoi.zombietc.block.ITileEntityPurchasable;
import net.gigimoi.zombietc.event.PlayerManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by gigimoi on 7/26/2014.
 */
public class MessagePurchaseTile implements IMessage {
    int x;
    int y;
    int z;
    Entity activator;

    public MessagePurchaseTile() {
    }

    public MessagePurchaseTile(int xCoord, int yCoord, int zCoord, Entity activator) {
        x = xCoord;
        y = yCoord;
        z = zCoord;
        this.activator = activator;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
        activator = ZombieTC.proxy.getWorld(Side.SERVER).getEntityByID(buf.readInt());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        buf.writeInt(activator.getEntityId());
    }

    public static class MessagePurchaseTileHandler implements IMessageHandler<MessagePurchaseTile, MessagePurchaseTile> {
        @Override
        public MessagePurchaseTile onMessage(MessagePurchaseTile message, MessageContext ctx) {
            TileEntity tile = ZombieTC.proxy.getWorld(ctx.side).getTileEntity(message.x, message.y, message.z);
            ITileEntityActivatable activatable = (ITileEntityActivatable) tile;
            activatable.activate(message.activator, ctx.side);
            if(ITileEntityPurchasable.class.isAssignableFrom(tile.getClass())) {
                EntityPlayer player = ZombieTC.proxy.getWorld(ctx.side).getPlayerEntityByName(message.activator.getCommandSenderName());
                ITileEntityPurchasable purchasable = (ITileEntityPurchasable)tile;
                PlayerManager.ZombieTCPlayerProperties.get(player).vim -= purchasable.getPrice();
            }
            if (ctx.side.isServer()) {
                ZombieTC.network.sendToAll(message);
            }
            return null;
        }
    }
}
