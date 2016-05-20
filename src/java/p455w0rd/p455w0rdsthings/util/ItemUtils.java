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
		if (player.getHeldItemMainhand() != null) {
			if (player.getHeldItemMainhand().getItem() instanceof ItemDankNull) {
				dankNullItem = player.getHeldItemMainhand();
			}
			else {
				if (player.getHeldItemOffhand() != null) {
					if (player.getHeldItemOffhand().getItem() instanceof ItemDankNull) {
						dankNullItem = player.getHeldItemOffhand();
					}
				}
			}
		}
		else if (player.getHeldItemOffhand() != null) {
			if (player.getHeldItemOffhand().getItem() instanceof ItemDankNull) {
				dankNullItem = player.getHeldItem(EnumHand.OFF_HAND);
			}
			else {
				if (player.getHeldItemMainhand() != null) {
					if (player.getHeldItemMainhand().getItem() instanceof ItemDankNull) {
						dankNullItem = player.getHeldItemMainhand();
					}
				}
			}
		}
		// search player's inventory if not held
		if (dankNullItem == null) {
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
					dankNullItem = itemStack;
					break;
				}
			}
		}
		return dankNullItem;
	}

}
