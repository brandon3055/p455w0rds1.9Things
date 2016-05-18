package p455w0rd.p455w0rdsthings.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import p455w0rd.p455w0rdsthings.items.ItemDankNull;
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
			// Always use a construct like this to actually handle your message. This ensures that
			// youre 'handle' code is run on the main Minecraft thread. 'onMessage' itself
			// is called on the networking thread so it is not safe to do a lot of things
			// here.
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
			return null;
		}

		private void handle(PacketSetSelectedItem message, MessageContext ctx) {
			// This code is run on the server side. So you can do server-side calculations here
			EntityPlayerMP player = ctx.getServerHandler().playerEntity;
			ItemStack dankNullItem = ItemUtils.getDankNullStack(player.inventory);
			if (dankNullItem != null) {
				ItemDankNull.setSelectedStackIndex(dankNullItem, PacketSetSelectedItem.index);
			}
		}
	}
}