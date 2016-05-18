package p455w0rd.p455w0rdsthings.container;

import java.util.Iterator;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

public class InventoryDankNull implements IInventory, Iterable<ItemStack> {

	private final int size = 54;
	private final ItemStack[] inv;
	private boolean enableClientEvents = false;
	private ItemStack dankNullStack;

	public InventoryDankNull(final ItemStack itemStackIn) {
		this.dankNullStack = itemStackIn;
		this.inv = new ItemStack[this.size];
		this.readFromNBT();
	}

	public boolean isEmpty() {
		for (int x = 0; x < this.size; x++) {
			if (this.getStackInSlot(x) != null) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int getSizeInventory() {
		return this.size;
	}

	@Override
	public ItemStack getStackInSlot(final int var1) {
		return this.inv[var1];
	}

	@Override
	public ItemStack decrStackSize(final int slot, final int qty) {
		if (this.inv[slot] != null) {
			final ItemStack split = this.getStackInSlot(slot);
			ItemStack ns = null;

			if (qty >= split.stackSize) {
				ns = this.inv[slot];
				this.inv[slot] = null;
			}
			else {
				ns = split.splitStack(qty);
			}

			this.markDirty();
			return ns;
		}

		return null;
	}

	public ItemStack getStack() {
		return this.dankNullStack != null ? this.dankNullStack : null;
	}

	protected boolean eventsEnabled() {
		return FMLCommonHandler.instance().getSide() == Side.SERVER || this.isEnableClientEvents();
	}

	public static boolean isSameItem(@Nullable final ItemStack left, @Nullable final ItemStack right) {
		return left != null && right != null && left.isItemEqual(right);
	}

	@Override
	public void setInventorySlotContents(final int slot, final ItemStack newItemStack) {
		final ItemStack oldStack = this.inv[slot];
		this.inv[slot] = newItemStack;

		if (this.eventsEnabled()) {
			ItemStack removed = oldStack;
			ItemStack added = newItemStack;

			if (oldStack != null && newItemStack != null && isSameItem(oldStack, newItemStack)) {
				if (oldStack.stackSize > newItemStack.stackSize) {
					removed = removed.copy();
					removed.stackSize -= newItemStack.stackSize;
					added = null;
				}
				else if (oldStack.stackSize < newItemStack.stackSize) {
					added = added.copy();
					added.stackSize -= oldStack.stackSize;
					removed = null;
				}
				else {
					removed = added = null;
				}
			}

			this.markDirty();
		}
	}

	@Override
	public int getInventoryStackLimit() {
		return Integer.MAX_VALUE;
	}

	@Override
	public void markDirty() {
		this.writeToNBT();
	}

	@Override
	public boolean isUseableByPlayer(final EntityPlayer var1) {
		return true;
	}

	@Override
	public boolean isItemValidForSlot(final int i, final ItemStack itemstack) {
		return true;
	}

	public void setMaxStackSize(final int s) {
		return;
	}

	@Override
	public Iterator<ItemStack> iterator() {
		return new InvIterator(this);
	}

	private boolean isEnableClientEvents() {
		return this.enableClientEvents;
	}

	public void setEnableClientEvents(final boolean enableClientEvents) {
		this.enableClientEvents = enableClientEvents;
	}

	@Override
	public String getName() {
		return "danknull-inventory";
	}

	@Override
	public boolean hasCustomName() {
		return true;
	}

	@Override
	public ITextComponent getDisplayName() {
		return new TextComponentString(this.getName());
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		ItemStack itemStack = getStackInSlot(index);
		if (itemStack != null)
			setInventorySlotContents(index, null);
		return itemStack;
	}

	@Override
	public void openInventory(EntityPlayer player) {
	}

	@Override
	public void closeInventory(EntityPlayer player) {
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
	}
	
	private void writeToNBT() {
		if (this.getStack() == null) {
			return;
		}
		if (!this.getStack().hasTagCompound()) {
			this.getStack().setTagCompound(new NBTTagCompound());
		}
		NBTTagCompound itemTC = this.getStack().getTagCompound();
		NBTTagList nbtTL = new NBTTagList();
		for (int i = 0; i < this.inv.length; i++) {
			if (inv[i] == null) {
				continue;
			}
			NBTTagCompound nbtTC = new NBTTagCompound();
			nbtTC.setInteger("Slot", i);
			inv[i].writeToNBT(nbtTC);
			nbtTL.appendTag(nbtTC);
		}
		itemTC.setTag(this.getName(), nbtTL);
	}
	
	private void readFromNBT() {
		if (this.getStack() == null || this.getStack().getTagCompound() == null) {
			return;
		}
		NBTTagCompound itemTC = this.getStack().getTagCompound();
		NBTTagList nbtTL = itemTC.getTagList(this.getName(), 10);
		for (int i = 0; i < nbtTL.tagCount(); i++) {
			NBTTagCompound nbtTC = (NBTTagCompound) nbtTL.getCompoundTagAt(i);
			if (nbtTC == null) {
				continue;
			}
			int slot = nbtTC.getInteger("Slot");
			this.inv[slot] = ItemStack.loadItemStackFromNBT(nbtTC);
		}
	}
}
