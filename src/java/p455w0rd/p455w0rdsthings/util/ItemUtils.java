package p455w0rd.p455w0rdsthings.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import p455w0rd.p455w0rdsthings.items.ItemDankNull;

public class ItemUtils {
	
	public static ItemStack getDankNullStack(InventoryPlayer playerInv) {
		EntityPlayer player = playerInv.player;
		ItemStack dankNullItem = null;
		// If held item (either hand) is a /dank/null, take precedence
		if (player.getHeldItem(EnumHand.MAIN_HAND) != null) {
			if (player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemDankNull) {
				dankNullItem = player.getHeldItem(EnumHand.MAIN_HAND);
			}
			else {
				if (player.getHeldItem(EnumHand.OFF_HAND).getItem() instanceof ItemDankNull) {
					dankNullItem = player.getHeldItem(EnumHand.OFF_HAND);
				}
			}
		}
		else if (player.getHeldItem(EnumHand.OFF_HAND) != null) {
			if (player.getHeldItem(EnumHand.OFF_HAND).getItem() instanceof ItemDankNull) {
				dankNullItem = player.getHeldItem(EnumHand.OFF_HAND);
			}
			else {
				if (player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemDankNull) {
					dankNullItem = player.getHeldItem(EnumHand.MAIN_HAND);
				}
			}
		}
		if (dankNullItem == null) {
			return null;
		}
		// search player's inventory if not held
		ItemStack dankNullStack = null;
		int invSize = playerInv.getSizeInventory();
		if (invSize <= 0) {
			return null;
		}
		for (int i = 0; i < invSize; ++i) {
			ItemStack itemStack = playerInv.getStackInSlot(i);
			if (itemStack == null) {
				continue;
			}
			if (itemStack.getItem() instanceof ItemDankNull) {
				dankNullStack = itemStack;
				break;
			}
		}
		return dankNullStack;
	}

}
