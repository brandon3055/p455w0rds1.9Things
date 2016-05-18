package p455w0rd.p455w0rdsthings.container;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import p455w0rd.p455w0rdsthings.items.ItemDankNull;

public class DankNullSlot extends Slot {

	public final int xDisplayPosition;
	public final int yDisplayPosition;
	private boolean isDraggable = true;
	private boolean isPlayerSide = false;
	private Container myContainer = null;
	private int IIcon = -1;
	private hasCalculatedValidness isValid;
	protected String backgroundName = null;
	protected ResourceLocation backgroundLocation = null;
	protected Object backgroundMap;
	private final int slotIndex;
	public int slotNumber;
	public final IInventory inventory;

	public DankNullSlot(final IInventory inv, final int idx, final int x, final int y) {
		super(inv, idx, x, y);
		this.slotIndex = idx;
		this.xDisplayPosition = x;
		this.yDisplayPosition = y;
		this.inventory = inv;
		this.setIsValid(hasCalculatedValidness.NotAvailable);
	}

	public int getDisplayPos(String axis) {
		if (axis.equals("x")) {
			return xDisplayPosition;
		}
		else if (axis.equals("y")) {
			return yDisplayPosition;
		}
		else {
			return 0;
		}
	}

	public Slot setNotDraggable() {
		this.setDraggable(false);
		return this;
	}

	public Slot setPlayerSide() {
		this.isPlayerSide = true;
		return this;
	}

	public String getTooltip() {
		return null;
	}

	public void clearStack() {
		putStack(null);
	}

	public boolean isItemValid(final ItemStack itemStackIn) {
		return !(itemStackIn.getItem() instanceof ItemDankNull);
	}

	public ItemStack getStack() {
		if (this.inventory.getSizeInventory() <= this.getSlotIndex()) {
			return null;
		}
		return this.inventory.getStackInSlot(this.slotIndex);
	}

	public int getSlotIndex() {
		return this.slotIndex;
	}

	public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack) {
		this.onSlotChanged();
	}

	@SideOnly(Side.CLIENT)
	public boolean canBeHovered() {
		return true;
	}

	@SideOnly(Side.CLIENT)
	public ResourceLocation getBackgroundLocation() {
		return (backgroundLocation == null ? TextureMap.locationBlocksTexture : backgroundLocation);
	}

	@SideOnly(Side.CLIENT)
	public void setBackgroundLocation(ResourceLocation texture) {
		this.backgroundLocation = texture;
	}

	public void setBackgroundName(String name) {
		this.backgroundName = name;
	}

	@SideOnly(Side.CLIENT)
	public TextureAtlasSprite getBackgroundSprite() {
		String name = getSlotTexture();
		return name == null ? null : getBackgroundMap().getAtlasSprite(name);
	}

	@SideOnly(Side.CLIENT)
	protected TextureMap getBackgroundMap() {
		if (backgroundMap == null)
			backgroundMap = Minecraft.getMinecraft().getTextureMapBlocks();
		return (TextureMap) backgroundMap;
	}

	public boolean getHasStack() {
		return this.getStack() != null;
	}

	public void putStack(ItemStack stack) {
		this.inventory.setInventorySlotContents(this.slotIndex, stack);
		this.onSlotChanged();
	}

	public void onSlotChanged() {
		if (this.inventory instanceof InventoryDankNull) {
			this.inventory.markDirty();
		}
		//this.setIsValid(hasCalculatedValidness.NotAvailable);
	}

	public int getSlotStackLimit() {
		return this.inventory.getInventoryStackLimit();
	}

	public int getItemStackLimit(ItemStack stack) {
		return this.getSlotStackLimit();
	}

	@SideOnly(Side.CLIENT)
	public String getSlotTexture() {
		return backgroundName;
	}

	public ItemStack decrStackSize(int amount) {
		return this.inventory.decrStackSize(this.slotIndex, amount);
	}

	public boolean isHere(IInventory inv, int slotIn) {
		return inv == this.inventory && slotIn == this.slotIndex;
	}

	public boolean canTakeStack(EntityPlayer playerIn) {
		return true;
	}

	public ItemStack getDisplayStack() {
		return getStack();
	}

	public float getOpacityOfIcon() {
		return 0.4f;
	}

	public boolean renderIconWithItem() {
		return false;
	}

	public int getIcon() {
		return this.getIIcon();
	}

	public boolean isPlayerSide() {
		return this.isPlayerSide;
	}

	public int getX() {
		return this.xDisplayPosition;
	}

	public int getY() {
		return this.yDisplayPosition;
	}

	private int getIIcon() {
		return this.IIcon;
	}

	public void setIIcon(final int iIcon) {
		this.IIcon = iIcon;
	}

	public boolean isDraggable() {
		return this.isDraggable;
	}

	private void setDraggable(final boolean isDraggable) {
		this.isDraggable = isDraggable;
	}

	void setPlayerSide(final boolean isPlayerSide) {
		this.isPlayerSide = isPlayerSide;
	}

	public hasCalculatedValidness getIsValid() {
		//return this.isValid;
		return hasCalculatedValidness.Valid;
	}

	public void setIsValid(final hasCalculatedValidness isValid) {
		this.isValid = isValid;
	}

	public Container getContainer() {
		return this.myContainer;
	}

	public void setContainer(final Container myContainer) {
		this.myContainer = myContainer;
	}

	public enum hasCalculatedValidness {
		NotAvailable, Valid, Invalid
	}
}
