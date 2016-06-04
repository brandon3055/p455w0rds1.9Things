package p455w0rd.p455w0rdsthings.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import p455w0rd.p455w0rdsthings.inventory.InventoryDankNull;
import p455w0rd.p455w0rdsthings.inventory.slot.DankNullSlot;
import p455w0rd.p455w0rdsthings.items.ItemDankNull;
import p455w0rd.p455w0rdsthings.util.ItemUtils;

public class ContainerDankNull extends Container {

	InventoryPlayer inventoryPlayer;
	InventoryDankNull inventoryDankNull;
	ItemStack dankNullStack;
	int numRows = 0;
	int totalSlots = 0;
	public ContainerDankNull(EntityPlayer playerIn) {
		this.inventoryPlayer = playerIn.inventory;
		this.dankNullStack = ItemUtils.getDankNull(inventoryPlayer);
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

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}

	@Override
	public void onContainerClosed(EntityPlayer playerIn) {
		InventoryPlayer inventoryplayer = playerIn.inventory;
		if (inventoryplayer.getItemStack() != null) {
			playerIn.dropItem(inventoryplayer.getItemStack(), false);
			inventoryplayer.setItemStack((ItemStack) null);
		}
		ItemUtils.reArrangeStacks(dankNullStack);
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
		inventoryDankNull.markDirty();
	}

	public static boolean canAddItemToSlot(Slot slotIn, ItemStack stack, boolean stackSizeMatters) {
		return true;
	}

	@Override
	public boolean canDragIntoSlot(Slot slotIn) {
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
		for (int i = 36; i <= totalSlots; i++) {
			ItemStack dankNullStack = inventorySlots.get(i).getStack();
			if (dankNullStack == null) {
				continue;
			}
			if (ItemUtils.areItemsEqual(dankNullStack, itemStackIn)) {
				return true;
			}
		}
		return false;
	}

	private int getStackSlot(ItemStack itemStackIn) {
		for (int i = 36; i <= totalSlots; i++) {
			Slot s = (Slot) this.inventorySlots.get(i);
			if (s == null || s.getStack() == null) {
				continue;
			}
			if (ItemUtils.areItemsEqual(s.getStack(), itemStackIn)) {
				return i;
			}
		}
		return -1;
	}

	private ItemStack addStack(ItemStack heldStack, boolean isSlotEmpty, EntityPlayer player) {
		InventoryPlayer inventoryplayer = player.inventory;
		long maxStackSize = ItemUtils.getDankNullMaxStackSize(dankNullStack);
		if (isSlotEmpty) {
			ItemStack newStack = heldStack.copy();
			newStack.setItemDamage(heldStack.getItemDamage());
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
					long finalStackSize = currentStackSize + additionalStackSize;
					if (finalStackSize <= maxStackSize) {
						slotItem.getTagCompound().setLong("p455w0rd.StackSize", finalStackSize);
					}
					else {
						slotItem.getTagCompound().setLong("p455w0rd.StackSize", maxStackSize);
					}
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
					long finalStackSize = currentStackSize + additionalStackSize;
					if (finalStackSize <= maxStackSize) {
						slotItem.getTagCompound().setLong("p455w0rd.StackSize", finalStackSize);
					}
					else {
						slotItem.getTagCompound().setLong("p455w0rd.StackSize", maxStackSize);
					}
					inventoryplayer.setItemStack(null);
				}
			}
			return heldStack;
		}
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		final Slot clickSlot = this.inventorySlots.get(index);
		if (clickSlot.getHasStack()) {
			if (!isDankNullSlot(index)) {
				if (getNextAvailableSlot() == -1 && !isStackAlreadyAdded(clickSlot.getStack())) {
					if (!moveStackWithinInventory(clickSlot.getStack(), index)) {
						return null;
					}
					inventoryDankNull.markDirty();
					return clickSlot.getStack();
				}
				else {

					addStack(clickSlot.getStack(), true, playerIn);
					clickSlot.putStack((ItemStack) null);
					playerIn.inventory.markDirty();
					ItemUtils.setSelectedStackIndex(dankNullStack, 0);
					inventoryDankNull.markDirty();
				}
			}
			else {
				ItemStack newStack = clickSlot.getStack().copy();
				int realMaxStackSize = newStack.getMaxStackSize();
				long currentStackSize = ItemUtils.getDankNullStackSize(newStack);
				newStack.getTagCompound().removeTag("p455w0rd.StackSize");
				if (newStack.getTagCompound().hasNoTags()) {
					newStack.setTagCompound(null);
				}
				if (currentStackSize > realMaxStackSize) {
					newStack.stackSize = realMaxStackSize;
					if (moveStackToInventory(newStack)) {
						ItemUtils.decrDankNullStackSize(clickSlot.getStack(), dankNullStack, realMaxStackSize);
					}
				}
				else {
					newStack.stackSize = (int) currentStackSize;
					if (moveStackToInventory(newStack)) {
						ItemUtils.decrDankNullStackSize(clickSlot.getStack(), dankNullStack, currentStackSize);
						clickSlot.putStack(null);
					}
					arrangeSlots();
					ItemUtils.setSelectedStackIndex(dankNullStack, 0);
				}
			}
		}
		return null;
	}

	private boolean isInHotbar(int index) {
		return (index >= 0 && index <= 8);
	}

	private boolean isInInventory(int index) {
		return (index >= 9 && index <= 36);
	}

	private boolean moveStackWithinInventory(ItemStack itemStackIn, int index) {
		if (isInHotbar(index)) {
			for (int i = 9; i <= 36; i++) {
				Slot possiblyOpenSlot = this.inventorySlots.get(i);
				if (!possiblyOpenSlot.getHasStack()) {
					possiblyOpenSlot.putStack(itemStackIn);
					inventorySlots.get(index).putStack(null);
					inventoryPlayer.markDirty();
					return true;
				}
			}
		}
		else if (isInInventory(index)) {
			for (int i = 0; i <= 8; i++) {
				Slot possiblyOpenSlot = this.inventorySlots.get(i);
				if (!possiblyOpenSlot.getHasStack()) {
					possiblyOpenSlot.putStack(itemStackIn);
					inventorySlots.get(index).putStack(null);
					inventoryPlayer.markDirty();
					return true;
				}
			}
		}
		return false;
	}

	protected boolean moveStackToInventory(ItemStack itemStackIn) {
		for (int i = 0; i <= 36; i++) {
			Slot possiblyOpenSlot = this.inventorySlots.get(i);
			if (!possiblyOpenSlot.getHasStack()) {
				possiblyOpenSlot.putStack(itemStackIn);
				return true;
			}
		}
		return false;
	}

	private boolean isDankNullSlot(int index) {
		return index >= 36;
	}

	private int getDankNullIndex(int index) {
		return index - 36;
	}

	@Override
	public ItemStack slotClick(int index, int dragType, ClickType clickTypeIn, EntityPlayer player) {
		System.out.println("\nMOUSE INFO\ndragType: " + dragType + "\nclickType: " + clickTypeIn);

		// we only handle stuff in our custom inventory
		if (isDankNullSlot(index)) {
			InventoryPlayer inventoryplayer = player.inventory;

			// stack held by mouse
			ItemStack heldStack = inventoryplayer.getItemStack();
			if (index == -999) {
				if (inventoryplayer.getItemStack() != null) {
					if (dragType == 0) {
						player.dropItem(inventoryplayer.getItemStack(), true);
						inventoryplayer.setItemStack((ItemStack) null);
					}

					if (dragType == 1) {
						player.dropItem(inventoryplayer.getItemStack().splitStack(1), true);

						if (inventoryplayer.getItemStack().stackSize == 0) {
							inventoryplayer.setItemStack((ItemStack) null);
						}
					}
				}
				return heldStack;
			}

			Slot s = (Slot) this.inventorySlots.get(index);
			//stack in slot mouse is over
			ItemStack thisStack = s.getStack();

			if (thisStack != null && thisStack.getItem() instanceof ItemDankNull) {
				return null;
			}

			if (index == -1) {
				return heldStack;
			}

			if (heldStack != null) {
				if (thisStack == null) {
					addStack(heldStack, true, player);
				}
				else {
					addStack(heldStack, false, player);
				}
				return heldStack;
			}
			// slot isn't empty
			else if (heldStack == null && thisStack != null) {
				if (dragType == 0) {
					if ((clickTypeIn == ClickType.PICKUP || clickTypeIn == ClickType.QUICK_MOVE) && dragType == 0) {
						if (clickTypeIn == ClickType.QUICK_MOVE) {
							if (index < 0) {
								return null;
							}
							return transferStackInSlot(player, index);
						}
					}
					long currentStackSize = thisStack.getTagCompound().getLong("p455w0rd.StackSize");
					int thisStackMaxStackSize = thisStack.getMaxStackSize();
					if (thisStack.stackSize == 0 || currentStackSize == 0) {
						this.inventoryDankNull.setInventorySlotContents(getDankNullIndex(index), null);
						inventoryplayer.setItemStack(null);
						this.inventoryDankNull.markDirty();
						return null;
					}
					if (currentStackSize > thisStackMaxStackSize) {
						ItemUtils.decrDankNullStackSize(thisStack, dankNullStack, thisStackMaxStackSize);
						ItemStack newStack = thisStack.copy();
						newStack.getTagCompound().removeTag("p455w0rd.StackSize");
						if (newStack.getTagCompound().hasNoTags()) {
							newStack.setTagCompound(null);
						}
						newStack.stackSize = thisStackMaxStackSize;
						inventoryplayer.setItemStack(newStack);
						inventoryDankNull.markDirty();
					}
					else {
						this.inventoryDankNull.setInventorySlotContents(getDankNullIndex(index), null);
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
		}
		else {
			return super.slotClick(index, dragType, clickTypeIn, player);
		}
		return null;
	}

}
