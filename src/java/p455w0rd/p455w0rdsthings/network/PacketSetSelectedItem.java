package p455w0rd.p455w0rdsthings.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import p455w0rd.p455w0rdsthings.util.ItemUtils;

public class PacketSetSelectedItem implements IMessage {
	private static int index;

	@Override
	public void fromBytes(ByteBuf buf) {
		index = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(index);
	}
	
	public PacketSetSelectedItem() {
	}

	public PacketSetSelectedItem(int newIndex) {
		index = newIndex;
	}

	public static class Handler implements IMessageHandler<PacketSetSelectedItem, IMessage> {
		@Override
		public IMessage onMessage(PacketSetSelectedItem message, MessageContext ctx) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
			return null;
		}

		private void handle(PacketSetSelectedItem message, MessageContext ctx) {
			EntityPlayerMP player = ctx.getServerHandler().playerEntity;
			ItemStack dankNullItem = ItemUtils.getDankNull(player.inventory);
			if (dankNullItem != null) {
				ItemUtils.setSelectedStackIndex(dankNullItem, PacketSetSelectedItem.index);
			}
		}
	}
}