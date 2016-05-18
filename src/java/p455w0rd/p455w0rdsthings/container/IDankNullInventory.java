package p455w0rd.p455w0rdsthings.container;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public interface IDankNullInventory {
	void saveChanges();

	void onChangeInventory( IInventory inv, int slot, InvOperation mc, ItemStack removedStack, ItemStack newStack );
}
