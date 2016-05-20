package p455w0rd.p455w0rdsthings.container;

import java.util.Set;

import com.google.common.collect.Sets;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import p455w0rd.p455w0rdsthings.items.ItemDankNull;
import p455w0rd.p455w0rdsthings.util.ItemUtils;

public class ContainerDankNull extends Container {

	InventoryPlayer inventoryPlayer;
	InventoryDankNull inventoryDankNull;
	ItemStack dankNullStack;
	int numRows = 0;
	int totalSlots = 0;
	/**
	 * The current drag mode (0 : evenly split, 1 : one item by slot, 2 : not
	 * used ?)
	 */
	private int dragMode = -1;
	/** The current drag event (0 : start, 1 : add slot : 2 : end) */
	private int dragEvent;
	private final Set<Slot> dragSlots = Sets.<Slot> newHashSet();

	public ContainerDankNull(EntityPlayer playerIn) {
		this.inventoryPlayer = playerIn.inventory;
		this.dankNullStack = ItemUtils.getDankNullStack(inventoryPlayer);
		this.inventoryDankNull = new InventoryDankNull(dankNullStack);
		this.numRows = dankNullStack.getItemDamage() + 1;
		// Add hotbar slots
		for (int i = 0; i < 9; ++i) {
			this.addSlotToContainer(new Slot(this.inventoryPlayer, i, i * 18 + 8, (198 - ((6 - this.numRows) * 18 + (this.numRows == 1 ? 1 : 0)))));
			totalSlots++;
		}

		// Add player inventory slots
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlotToContainer(new Slot(this.inventoryPlayer, j + i * 9 + 9, j * 18 + 8, (140 - ((6 - this.numRows) * 18)) + i * 18 - (this.numRows == 1 ? 1 : 0)));
				totalSlots++;
			}
		}

		// Add dank null slots
		for (int i = 0; i < this.numRows; i++) {
			for (int j = 0; j < 9; j++) {
				this.addSlotToContainer(new DankNullSlot(this.inventoryDankNull, j + i * 9, j * 18 + 8, 18 + i * 18));
				totalSlots++;
			}
		}
		totalSlots--;
	}

	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		return null;
		/*
		Slot slot = (Slot) this.inventorySlots.get(index);
		return slot != null ? slot.getStack() : null;
		*/
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}
	
	@Override
	public void onContainerClosed(EntityPlayer playerIn)
    {
        InventoryPlayer inventoryplayer = playerIn.inventory;
        if (inventoryplayer.getItemStack() != null)
        {
            playerIn.dropPlayerItemWithRandomChoice(inventoryplayer.getItemStack(), false);
            inventoryplayer.setItemStack((ItemStack)null);
        }
        ItemDankNull.reArrangeStacks(dankNullStack);
    }
	
	private void arrangeSlots() {
		int invSize = inventoryDankNull.getSizeInventory();
		if (invSize <= 0) {
			return;
		}
		for (int i = 0; i < invSize; i++) {
			if (inventoryDankNull.getStackInSlot(i) == null) {
				for (int j = 0; j < invSize; j++) {
					if (j <= i) {
						continue;
					}
					if (inventoryDankNull.getStackInSlot(j) != null) {
						inventoryDankNull.setInventorySlotContents(i, inventoryDankNull.getStackInSlot(j));
						inventoryDankNull.decrStackSize(j, 1);
						break;
					}
				}
			}
		}
	}
	
	public static boolean canAddItemToSlot(Slot slotIn, ItemStack stack, boolean stackSizeMatters)
    {
		return true;
    }
	
	@Override
	public boolean canDragIntoSlot(Slot slotIn)
    {
		if (slotIn.inventory instanceof InventoryDankNull) {
			return false;
		}
        return true;
    }

	public Slot getSlotFromInventory(IInventory inv, int slotIn) {
		for (int i = 0; i < this.inventorySlots.size(); ++i) {
			Slot slot = (Slot) this.inventorySlots.get(i);

			if (slot.isHere(inv, slotIn)) {
				return slot;
			}
		}

		return null;
	}

	private int getNextAvailableSlot() {
		for (int i = 36; i <= totalSlots; i++) {
			Slot s = (Slot) this.inventorySlots.get(i);
			if (s == null) {
				continue;
			}
			if (s.getStack() == null) {
				return i;
			}
		}
		return -1;
	}

	private boolean isStackAlreadyAdded(ItemStack itemStackIn) {
		for (int i = 36; i < totalSlots; i++) {
			Slot s = (Slot) this.inventorySlots.get(i);
			if (s == null || s.getStack() == null) {
				continue;
			}
			if (s.getStack().getItem() == itemStackIn.getItem()) {
				return true;
			}
		}
		return false;
	}

	private int getStackSlot(ItemStack itemStackIn) {
		for (int i = 36; i < totalSlots; i++) {
			Slot s = (Slot) this.inventorySlots.get(i);
			if (s == null || s.getStack() == null) {
				continue;
			}
			if (s.getStack().getItem() == itemStackIn.getItem()) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public ItemStack func_184996_a(int index, int dragType, ClickType clickTypeIn, EntityPlayer player) {

		ItemStack itemstack = null;
		InventoryPlayer inventoryplayer = player.inventory;

		ItemStack heldStack = inventoryplayer.getItemStack();
		
		if (index == -999) {
			if (inventoryplayer.getItemStack() != null) {
				if (dragType == 0) {
					player.dropPlayerItemWithRandomChoice(inventoryplayer.getItemStack(), true);
					inventoryplayer.setItemStack((ItemStack) null);
				}

				if (dragType == 1) {
					player.dropPlayerItemWithRandomChoice(inventoryplayer.getItemStack().splitStack(1), true);

					if (inventoryplayer.getItemStack().stackSize == 0) {
						inventoryplayer.setItemStack((ItemStack) null);
					}
				}
			}
			return heldStack;
		}
		
		if (index == -1) {
			return heldStack;
		}
		Slot s = (Slot) this.inventorySlots.get(index);
		ItemStack thisStack = s.getStack();
		// custom inventory
		if (index >= 36 && heldStack != null) {
			if (thisStack == null) {
				ItemStack newStack = heldStack.copy();
				if (!isStackAlreadyAdded(heldStack)) {
					if (!newStack.hasTagCompound()) {
						newStack.setTagCompound(new NBTTagCompound());
					}
					NBTTagCompound nbtTC = newStack.getTagCompound();
					nbtTC.setLong("p455w0rd.StackSize", (long) heldStack.stackSize);
					newStack.setTagCompound(nbtTC);
					if (getNextAvailableSlot() != -1) {
						newStack.stackSize = 1;
						((Slot) this.inventorySlots.get(getNextAvailableSlot())).putStack(newStack);
						inventoryplayer.setItemStack(null);
					}
				}
				else {
					if (getStackSlot(heldStack) != -1) {
						ItemStack slotItem = ((Slot) this.inventorySlots.get(getStackSlot(heldStack))).getStack();
						long currentStackSize = slotItem.getTagCompound().getLong("p455w0rd.StackSize");
						long additionalStackSize = heldStack.stackSize;
						slotItem.getTagCompound().setLong("p455w0rd.StackSize", currentStackSize + additionalStackSize);
						inventoryplayer.setItemStack(null);
					}
				}
				return heldStack;
			}
			else {
				ItemStack newStack = heldStack.copy();

				if (!isStackAlreadyAdded(heldStack)) {
					if (!newStack.hasTagCompound()) {
						newStack.setTagCompound(new NBTTagCompound());
					}
					NBTTagCompound nbtTC = newStack.getTagCompound();
					nbtTC.setLong("p455w0rd.StackSize", (long) heldStack.stackSize);
					newStack.setTagCompound(nbtTC);
					if (getNextAvailableSlot() != -1) {
						newStack.stackSize = 1;
						((Slot) this.inventorySlots.get(getNextAvailableSlot())).putStack(newStack);
						inventoryplayer.setItemStack(null);
					}
					else {
						return heldStack;
					}
				}
				else {
					if (getStackSlot(heldStack) != -1) {
						ItemStack slotItem = ((Slot) this.inventorySlots.get(getStackSlot(heldStack))).getStack();
						long currentStackSize = slotItem.getTagCompound().getLong("p455w0rd.StackSize");
						long additionalStackSize = heldStack.stackSize;
						slotItem.getTagCompound().setLong("p455w0rd.StackSize", currentStackSize + additionalStackSize);
						inventoryplayer.setItemStack(null);
					}
				}
			}
			return heldStack;
		}
		else if (index >= 36 && heldStack == null && thisStack != null && dragType == 0) {
			long currentStackSize = thisStack.getTagCompound().getLong("p455w0rd.StackSize");
			if (clickTypeIn == ClickType.CLONE) {
				ItemStack newStack = thisStack.copy();
				newStack.getTagCompound().removeTag("p455w0rd.StackSize");
				newStack.stackSize = 64;
				inventoryplayer.setItemStack(newStack);
			}
			else {
				if (thisStack.stackSize == 0 || currentStackSize == 0) {
					this.inventoryDankNull.setInventorySlotContents(index - 36, null);
					inventoryplayer.setItemStack(null);
					this.inventoryDankNull.markDirty();
					return null;
				}
				if (currentStackSize > 64) {
					thisStack.getTagCompound().setLong("p455w0rd.StackSize", currentStackSize - 64);
					ItemStack newStack = thisStack.copy();
					newStack.getTagCompound().removeTag("p455w0rd.StackSize");
					if (newStack.getTagCompound().hasNoTags()) {
						newStack.setTagCompound(null);
					}
					newStack.stackSize = 64;
					inventoryplayer.setItemStack(newStack);
				}
				else {
					this.inventoryDankNull.setInventorySlotContents(index - 36, null);
					this.inventoryDankNull.markDirty();
					ItemStack newStack = thisStack.copy();
					newStack.getTagCompound().removeTag("p455w0rd.StackSize");
					if (newStack.getTagCompound().hasNoTags()) {
						newStack.setTagCompound(null);
					}
					newStack.stackSize = (int) currentStackSize;
					inventoryplayer.setItemStack(newStack);
				}
			}
			arrangeSlots();
			return thisStack;
		}

		if (clickTypeIn == ClickType.QUICK_CRAFT) {
			int i = this.dragEvent;
			this.dragEvent = getDragEvent(dragType);

			if ((i != 1 || this.dragEvent != 2) && i != this.dragEvent) {
				this.resetDrag();
			}
			else if (inventoryplayer.getItemStack() == null) {
				this.resetDrag();
			}
			else if (this.dragEvent == 0) {
				this.dragMode = extractDragMode(dragType);

				if (isValidDragMode(this.dragMode, player)) {
					this.dragEvent = 1;
					this.dragSlots.clear();
				}
				else {
					this.resetDrag();
				}
			}
			else if (this.dragEvent == 1) {
				Slot slot = (Slot) this.inventorySlots.get(index);

				if (slot != null && canAddItemToSlot(slot, inventoryplayer.getItemStack(), true) && slot.isItemValid(inventoryplayer.getItemStack()) && inventoryplayer.getItemStack().stackSize > this.dragSlots.size() && this.canDragIntoSlot(slot)) {
					this.dragSlots.add(slot);
				}
			}
			else if (this.dragEvent == 2) {
				if (!this.dragSlots.isEmpty()) {
					ItemStack itemstack3 = inventoryplayer.getItemStack().copy();
					int j = inventoryplayer.getItemStack().stackSize;

					for (Slot slot1 : this.dragSlots) {
						if (slot1 != null && canAddItemToSlot(slot1, inventoryplayer.getItemStack(), true) && slot1.isItemValid(inventoryplayer.getItemStack()) && inventoryplayer.getItemStack().stackSize >= this.dragSlots.size() && this.canDragIntoSlot(slot1)) {
							ItemStack itemstack1 = itemstack3.copy();
							int k = slot1.getHasStack() ? slot1.getStack().stackSize : 0;
							computeStackSize(this.dragSlots, this.dragMode, itemstack1, k);

							if (itemstack1.stackSize > itemstack1.getMaxStackSize()) {
								itemstack1.stackSize = itemstack1.getMaxStackSize();
							}

							if (itemstack1.stackSize > slot1.getItemStackLimit(itemstack1)) {
								itemstack1.stackSize = slot1.getItemStackLimit(itemstack1);
							}

							j -= itemstack1.stackSize - k;
							slot1.putStack(itemstack1);
						}
					}

					itemstack3.stackSize = j;

					if (itemstack3.stackSize <= 0) {
						itemstack3 = null;
					}

					inventoryplayer.setItemStack(itemstack3);
				}

				this.resetDrag();
			}
			else {
				this.resetDrag();
			}
		}
		else if (this.dragEvent != 0) {
			this.resetDrag();
		}
		else if ((clickTypeIn == ClickType.PICKUP || clickTypeIn == ClickType.QUICK_MOVE) && (dragType == 0 || dragType == 1)) {
			if (clickTypeIn == ClickType.QUICK_MOVE) {
				if (index < 0) {
					return null;
				}

				Slot slot6 = (Slot) this.inventorySlots.get(index);

				if (slot6 != null && slot6.canTakeStack(player)) {
					ItemStack itemstack8 = slot6.getStack();

					if (itemstack8 != null && itemstack8.stackSize <= 0) {
						itemstack = itemstack8.copy();
						slot6.putStack((ItemStack) null);
					}

					ItemStack itemstack11 = this.transferStackInSlot(player, index);

					if (itemstack11 != null) {
						Item item = itemstack11.getItem();
						itemstack = itemstack11.copy();

						if (slot6.getStack() != null && slot6.getStack().getItem() == item) {
							this.retrySlotClick(index, dragType, true, player);
						}
					}
				}
			}
			else {
				if (index < 0) {
					return null;
				}

				Slot slot7 = (Slot) this.inventorySlots.get(index);

				if (slot7 != null) {
					ItemStack itemstack9 = slot7.getStack();
					ItemStack itemstack12 = inventoryplayer.getItemStack();

					if (itemstack9 != null) {
						itemstack = itemstack9.copy();
					}

					if (itemstack9 == null) {
						if (itemstack12 != null && slot7.isItemValid(itemstack12)) {
							int l2 = dragType == 0 ? itemstack12.stackSize : 1;

							if (l2 > slot7.getItemStackLimit(itemstack12)) {
								l2 = slot7.getItemStackLimit(itemstack12);
							}

							slot7.putStack(itemstack12.splitStack(l2));

							if (itemstack12.stackSize == 0) {
								inventoryplayer.setItemStack((ItemStack) null);
							}
						}
					}
					else if (slot7.canTakeStack(player)) {
						if (itemstack12 == null) {
							if (itemstack9.stackSize > 0) {
								int k2 = dragType == 0 ? itemstack9.stackSize : (itemstack9.stackSize + 1) / 2;
								inventoryplayer.setItemStack(slot7.decrStackSize(k2));

								if (itemstack9.stackSize <= 0) {
									slot7.putStack((ItemStack) null);
								}

								slot7.onPickupFromSlot(player, inventoryplayer.getItemStack());
							}
							else {
								slot7.putStack((ItemStack) null);
								inventoryplayer.setItemStack((ItemStack) null);
							}
						}
						else if (slot7.isItemValid(itemstack12)) {
							if (itemstack9.getItem() == itemstack12.getItem() && itemstack9.getMetadata() == itemstack12.getMetadata() && ItemStack.areItemStackTagsEqual(itemstack9, itemstack12)) {
								int j2 = dragType == 0 ? itemstack12.stackSize : 1;

								if (j2 > slot7.getItemStackLimit(itemstack12) - itemstack9.stackSize) {
									j2 = slot7.getItemStackLimit(itemstack12) - itemstack9.stackSize;
								}

								if (j2 > itemstack12.getMaxStackSize() - itemstack9.stackSize) {
									j2 = itemstack12.getMaxStackSize() - itemstack9.stackSize;
								}

								itemstack12.splitStack(j2);

								if (itemstack12.stackSize == 0) {
									inventoryplayer.setItemStack((ItemStack) null);
								}

								itemstack9.stackSize += j2;
							}
							else if (itemstack12.stackSize <= slot7.getItemStackLimit(itemstack12)) {
								slot7.putStack(itemstack12);
								inventoryplayer.setItemStack(itemstack9);
							}
						}
						else if (itemstack9.getItem() == itemstack12.getItem() && itemstack12.getMaxStackSize() > 1 && (!itemstack9.getHasSubtypes() || itemstack9.getMetadata() == itemstack12.getMetadata()) && ItemStack.areItemStackTagsEqual(itemstack9, itemstack12)) {
							int i2 = itemstack9.stackSize;

							if (i2 > 0 && i2 + itemstack12.stackSize <= itemstack12.getMaxStackSize()) {
								itemstack12.stackSize += i2;
								itemstack9 = slot7.decrStackSize(i2);

								if (itemstack9.stackSize == 0) {
									slot7.putStack((ItemStack) null);
								}

								slot7.onPickupFromSlot(player, inventoryplayer.getItemStack());
							}
						}
					}

					slot7.onSlotChanged();
				}
			}
		}
		else if (clickTypeIn == ClickType.SWAP && dragType >= 0 && dragType < 9) {
			Slot slot5 = (Slot) this.inventorySlots.get(index);
			ItemStack itemstack7 = inventoryplayer.getStackInSlot(dragType);

			if (itemstack7 != null && itemstack7.stackSize <= 0) {
				itemstack7 = null;
				inventoryplayer.setInventorySlotContents(dragType, (ItemStack) null);
			}

			ItemStack itemstack10 = slot5.getStack();

			if (itemstack7 != null || itemstack10 != null) {
				if (itemstack7 == null) {
					if (slot5.canTakeStack(player)) {
						inventoryplayer.setInventorySlotContents(dragType, itemstack10);
						slot5.putStack((ItemStack) null);
						slot5.onPickupFromSlot(player, itemstack10);
					}
				}
				else if (itemstack10 == null) {
					if (slot5.isItemValid(itemstack7)) {
						int k1 = slot5.getItemStackLimit(itemstack7);

						if (itemstack7.stackSize > k1) {
							slot5.putStack(itemstack7.splitStack(k1));
						}
						else {
							slot5.putStack(itemstack7);
							inventoryplayer.setInventorySlotContents(dragType, (ItemStack) null);
						}
					}
				}
				else if (slot5.canTakeStack(player) && slot5.isItemValid(itemstack7)) {
					int l1 = slot5.getItemStackLimit(itemstack7);

					if (itemstack7.stackSize > l1) {
						slot5.putStack(itemstack7.splitStack(l1));
						slot5.onPickupFromSlot(player, itemstack10);

						if (!inventoryplayer.addItemStackToInventory(itemstack10)) {
							player.dropPlayerItemWithRandomChoice(itemstack10, true);
						}
					}
					else {
						slot5.putStack(itemstack7);
						inventoryplayer.setInventorySlotContents(dragType, itemstack10);
						slot5.onPickupFromSlot(player, itemstack10);
					}
				}
			}
		}
		else if (clickTypeIn == ClickType.CLONE && player.capabilities.isCreativeMode && inventoryplayer.getItemStack() == null && index >= 0) {
			Slot slot4 = (Slot) this.inventorySlots.get(index);

			if (slot4 != null && slot4.getHasStack()) {
				if (slot4.getStack().stackSize > 0) {
					ItemStack itemstack6 = slot4.getStack().copy();
					itemstack6.stackSize = itemstack6.getMaxStackSize();
					inventoryplayer.setItemStack(itemstack6);
				}
				else {
					slot4.putStack((ItemStack) null);
				}
			}
		}
		else if (clickTypeIn == ClickType.THROW && inventoryplayer.getItemStack() == null && index >= 0) {
			Slot slot3 = (Slot) this.inventorySlots.get(index);

			if (slot3 != null && slot3.getHasStack() && slot3.canTakeStack(player)) {
				ItemStack itemstack5 = slot3.decrStackSize(dragType == 0 ? 1 : slot3.getStack().stackSize);
				slot3.onPickupFromSlot(player, itemstack5);
				player.dropPlayerItemWithRandomChoice(itemstack5, true);
			}
		}
		else if (clickTypeIn == ClickType.PICKUP_ALL && index >= 0) {
			Slot slot2 = (Slot) this.inventorySlots.get(index);
			ItemStack itemstack4 = inventoryplayer.getItemStack();

			if (itemstack4 != null && (slot2 == null || !slot2.getHasStack() || !slot2.canTakeStack(player))) {
				int i1 = dragType == 0 ? 0 : this.inventorySlots.size() - 1;
				int j1 = dragType == 0 ? 1 : -1;

				for (int i3 = 0; i3 < 2; ++i3) {
					for (int j3 = i1; j3 >= 0 && j3 < this.inventorySlots.size() && itemstack4.stackSize < itemstack4.getMaxStackSize(); j3 += j1) {
						Slot slot8 = (Slot) this.inventorySlots.get(j3);

						if (slot8.getHasStack() && canAddItemToSlot(slot8, itemstack4, true) && slot8.canTakeStack(player) && this.canMergeSlot(itemstack4, slot8) && (i3 != 0 || slot8.getStack().stackSize != slot8.getStack().getMaxStackSize())) {
							int l = Math.min(itemstack4.getMaxStackSize() - itemstack4.stackSize, slot8.getStack().stackSize);
							ItemStack itemstack2 = slot8.decrStackSize(l);
							itemstack4.stackSize += l;

							if (itemstack2.stackSize <= 0) {
								slot8.putStack((ItemStack) null);
							}

							slot8.onPickupFromSlot(player, itemstack2);
						}
					}
				}
			}

			this.detectAndSendChanges();
		}

		return itemstack;
	}

}
