package p455w0rd.p455w0rdsthings.blocks.tileentities;

import java.util.List;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import p455w0rd.p455w0rdsthings.recipes.CompressorRecipeRegistry;
import p455w0rd.p455w0rdsthings.util.ItemUtils;

public class TileEntityCompressor extends TileEntity implements ITickable, ISidedInventory, IEnergyReceiver {

	protected ItemStack[] compressorInv;
	protected int numSlots = 3;
	protected int capacity;
	protected int maxReceive;
	protected int maxExtract;
	protected EnergyStorage energyStorage;
	private boolean isProcessing;
	private float pctCompleted;

	public TileEntityCompressor() {
		capacity = 1600000;
		maxReceive = 200;
		maxExtract = 200;
		compressorInv = new ItemStack[numSlots];
		energyStorage = new EnergyStorage(capacity, maxReceive);
		readFromNBT(this.getTileData());
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound nbtTag = new NBTTagCompound();
		this.writeToNBT(nbtTag);
		return new SPacketUpdateTileEntity(getPos(), 0, nbtTag);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
		this.readFromNBT(packet.getNbtCompound());
	}

	@SideOnly(Side.CLIENT)
	@Override
	public net.minecraft.util.math.AxisAlignedBB getRenderBoundingBox() {
		return INFINITE_EXTENT_AABB;
	}

	public String getName() {
		return "compressorBlock";
	}

	@Override
	public int getSizeInventory() {
		return numSlots;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return compressorInv[index];
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		ItemStack stack = ((ItemStack) compressorInv[index]);
		if (stack.stackSize >= count) {
			setInventorySlotContents(index, null);
		}
		else {
			setInventorySlotContents(index, new ItemStack(stack.getItem(), count));
			stack.stackSize -= count;
		}
		return stack;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		ItemStack itemStack = getStackInSlot(index);
		if (itemStack != null) {
			setInventorySlotContents(index, null);
		}
		return itemStack;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		compressorInv[index] = stack;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return true;
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

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public ITextComponent getDisplayName() {
		return new TextComponentString(getName());
	}

	@Override
	public boolean canConnectEnergy(EnumFacing from) {
		return true;
	}

	@Override
	public int getEnergyStored(EnumFacing from) {
		return energyStorage.getEnergyStored();
	}

	public int getEnergy() {
		return getEnergyStored(EnumFacing.DOWN);
	}

	public int getMaxCapacity() {
		return getMaxEnergyStored(EnumFacing.DOWN);
	}

	@Override
	public int getMaxEnergyStored(EnumFacing from) {
		return energyStorage.getMaxEnergyStored();
	}

	public int getMaxExtract() {
		return maxExtract;
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		int[] validSlots = { 0, 1 };
		return validSlots;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		if (index == 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		if (index == 1) {
			return true;
		}
		return false;
	}

	@Override
	public int receiveEnergy(EnumFacing from, int maxExtract, boolean simulate) {
		return energyStorage.receiveEnergy(maxExtract, simulate);
	}

	@Override
	public void update() {
		handleReceivingEnergy();
	}

	private void handleReceivingEnergy() {
		if (worldObj.isRemote) {
			return;
		}
		if (getEnergy() >= getMaxCapacity()) {
			return;
		}
		for (EnumFacing dir : EnumFacing.values()) {
			BlockPos targetBlock = getPos().add(dir.getDirectionVec());

			TileEntity tile = worldObj.getTileEntity(targetBlock);
			if (tile instanceof IEnergyProvider) {
				IEnergyProvider provider = (IEnergyProvider) tile;

				if (provider.canConnectEnergy(dir.getOpposite())) {
					int toget = energyStorage.receiveEnergy(this.maxReceive, true);
					int received = ((IEnergyProvider) tile).extractEnergy(dir.getOpposite(), toget, false);
					// TODO: need this? It doesn't really *need* state saved
					if (received > 0) {
						this.markDirty();
					}
					energyStorage.receiveEnergy(received, false);
				}
			}
		}
	}

	public boolean hasEnergy() {
		return getEnergy() > 0;
	}

	private void handleProcessing() {
		if (hasEnergy()) {
			if (getOutputSlotStack() == null || getOutputSlotStack().stackSize < getOutputSlotStack().getMaxStackSize()) {

			}
		}
	}

	public ItemStack getInputSlotStack() {
		return compressorInv[0];
	}

	public ItemStack getOutputSlotStack() {
		return compressorInv[1];
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		NBTTagCompound energyTag = tagCompound.getCompoundTag("Energy");
		this.energyStorage.readFromNBT(energyTag);

		NBTTagList nbtTL = tagCompound.getTagList(this.getName(), 10);
		for (int i = 0; i < nbtTL.tagCount(); i++) {
			NBTTagCompound nbtTC = (NBTTagCompound) nbtTL.getCompoundTagAt(i);
			if (nbtTC == null) {
				continue;
			}
			int slot = nbtTC.getInteger("Slot");
			this.compressorInv[slot] = ItemStack.loadItemStackFromNBT(nbtTC);
		}

	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
		tagCompound = super.writeToNBT(tagCompound);
		NBTTagCompound energyTag = new NBTTagCompound();
		this.energyStorage.writeToNBT(energyTag);
		tagCompound.setTag("Energy", energyTag);

		NBTTagList nbtTL = new NBTTagList();
		for (int i = 0; i < this.compressorInv.length; i++) {
			if (compressorInv[i] == null) {
				continue;
			}
			NBTTagCompound nbtTC = new NBTTagCompound();
			nbtTC.setInteger("Slot", i);
			compressorInv[i].writeToNBT(nbtTC);
			nbtTL.appendTag(nbtTC);
		}
		tagCompound.setTag(this.getName(), nbtTL);

		return tagCompound;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public double getMaxRenderDistanceSquared() {
		return 65536.0D;
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		if (index == 0 && CompressorRecipeRegistry.INSTANCE.getInputList() != null) {
			List<ItemStack> inputList = CompressorRecipeRegistry.INSTANCE.getInputList();
			for (int i = 0; i < inputList.size(); i++) {
				if (ItemUtils.areItemsEqual(stack, inputList.get(i))) {
					return true;
				}
			}
		}
		return false;
	}
}
