package p455w0rd.p455w0rdsthings.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import p455w0rd.p455w0rdsthings.blocks.tileentities.TileEntityBattery;
import p455w0rd.p455w0rdsthings.inventory.slot.BatterySlot;

public class ContainerBattery extends Container {
	
	protected TileEntityBattery tileEntity;

	public ContainerBattery(InventoryPlayer inventoryPlayer, TileEntityBattery te) {
		tileEntity = te;

		// Input Slot
		addSlotToContainer(new BatterySlot((IInventory) tileEntity, 0, 49, 18));

		// Output Slot
		addSlotToContainer(new BatterySlot((IInventory) tileEntity, 1, 106, 18));

		int xbase = 8;
		int ybase = 70;

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, xbase + j * 18, ybase + i * 18));
			}
		}

		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(inventoryPlayer, i, xbase + i * 18, ybase + 58));
		}

	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
		ItemStack stack = null;
		try {
			Slot slotObject = (Slot) inventorySlots.get(slot);

			// null checks and checks if the item can be stacked (maxStackSize >
			// 1)
			if (slotObject != null && slotObject.getHasStack()) {
				ItemStack stackInSlot = slotObject.getStack();
				stack = stackInSlot.copy();

				// Block->Player Inventory
				if (slot < 2) {
					if (!this.mergeItemStack(stackInSlot, 2, 35, true)) {
						return null;
					}
					// Player->Block Inventory
				}
				else if (!this.mergeItemStack(stackInSlot, 0, 1, false)) {
					return null;
				}

				if (stackInSlot.stackSize <= 0) {
					slotObject.putStack(null);
				}
				else {
					slotObject.onSlotChanged();
				}

				if (stackInSlot.stackSize == stack.stackSize) {
					return null;
				}
				slotObject.onPickupFromSlot(player, stackInSlot);
			}
		}
		catch (Exception e) {
		}
		return stack;
	}
}
