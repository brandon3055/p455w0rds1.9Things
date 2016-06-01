package p455w0rd.p455w0rdsthings.inventory.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import p455w0rd.p455w0rdsthings.inventory.InventoryDankNull;

public class FurnaceSlot extends Slot {

	public final int xDisplayPosition;
	public final int yDisplayPosition;
	private Container myContainer = null;
	protected String backgroundName = null;
	protected ResourceLocation backgroundLocation = null;
	protected Object backgroundMap;
	private final int slotIndex;
	public int slotNumber;
	public final IInventory inventory;
	
	public FurnaceSlot(final IInventory inv, final int idx, final int x, final int y) {
		super(inv, idx, x, y);
		this.slotIndex = idx;
		this.xDisplayPosition = x;
		this.yDisplayPosition = y;
		this.inventory = inv;
	}

	public String getTooltip() {
		return null;
	}

	public void clearStack() {
		putStack(null);
	}

	@Override
	public boolean isItemValid(final ItemStack itemStackIn) {
		return inventory.isItemValidForSlot(getSlotIndex(), itemStackIn);
	}

	@Override
	public ItemStack getStack() {
		if (this.inventory.getSizeInventory() <= this.getSlotIndex()) {
			return null;
		}
		return this.inventory.getStackInSlot(this.slotIndex);
	}

	@Override
	public int getSlotIndex() {
		return this.slotIndex;
	}

	@Override
	public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack) {
		this.onSlotChanged();
	}

	@SideOnly(Side.CLIENT)
	public boolean canBeHovered() {
		return true;
	}

	@Override
	public boolean getHasStack() {
		return this.getStack() != null;
	}

	@Override
	public void putStack(ItemStack stack) {
		this.inventory.setInventorySlotContents(this.slotIndex, stack);
		this.onSlotChanged();
	}

	@Override
	public void onSlotChanged() {
		if (this.inventory instanceof InventoryDankNull) {
			this.inventory.markDirty();
		}
	}

	@Override
	public int getSlotStackLimit() {
		return this.inventory.getInventoryStackLimit();
	}

	@Override
	public int getItemStackLimit(ItemStack stack) {
		return this.getSlotStackLimit();
	}

	@Override
	public ItemStack decrStackSize(int amount) {
		return this.inventory.decrStackSize(this.slotIndex, amount);
	}

	@Override
	public boolean canTakeStack(EntityPlayer playerIn) {
		return true;
	}

	public int getX() {
		return this.xDisplayPosition;
	}

	public int getY() {
		return this.yDisplayPosition;
	}

	public Container getContainer() {
		return this.myContainer;
	}

	public void setContainer(final Container myContainer) {
		this.myContainer = myContainer;
	}

}
