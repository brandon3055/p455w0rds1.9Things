package p455w0rd.p455w0rdsthings.blocks.tileentities;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import cofh.api.energy.IEnergyStorage;
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

public class TileEntityBattery extends TileEntity implements ITickable, IEnergyHandler, IEnergyReceiver, IEnergyProvider, ISidedInventory {

	protected ItemStack[] batteryInv;
	IEnergyReceiver[] adjacentHandlers = new IEnergyReceiver[6];
	protected EnergyStorage energyStorage;
	boolean cached = false;
	private final int invSize = 5;

	protected int capacity;
	protected int maxReceive;
	protected int maxExtract;

	public TileEntityBattery() {
		capacity = 6400000;
		maxReceive = 2000;
		maxExtract = 2000;
		batteryInv = new ItemStack[invSize];
		energyStorage = new EnergyStorage(capacity, maxReceive);
		this.energyStorage.setEnergyStored(10000);
		//readFromNBT(this.getTileData());
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
			this.batteryInv[slot] = ItemStack.loadItemStackFromNBT(nbtTC);
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
		tagCompound = super.writeToNBT(tagCompound);
		NBTTagCompound energyTag = new NBTTagCompound();
		this.energyStorage.writeToNBT(energyTag);
		tagCompound.setTag("Energy", energyTag);

		NBTTagList nbtTL = new NBTTagList();
		for (int i = 0; i < this.batteryInv.length; i++) {
			if (batteryInv[i] == null) {
				continue;
			}
			NBTTagCompound nbtTC = new NBTTagCompound();
			nbtTC.setInteger("Slot", i);
			batteryInv[i].writeToNBT(nbtTC);
			nbtTL.appendTag(nbtTC);
		}
		tagCompound.setTag(this.getName(), nbtTL);	

		return tagCompound;
	}
	
	@Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound updateTag = super.getUpdateTag();
        writeToNBT(updateTag);
        return updateTag;
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

	public IEnergyStorage getEnergyStorage() {
		return this.energyStorage;
	}

	@Override
	public boolean canConnectEnergy(EnumFacing from) {
		return true;
	}

	@Override
	public int receiveEnergy(EnumFacing from, int maxExtract, boolean simulate) {
		int tosend = energyStorage.receiveEnergy(maxExtract, simulate);
		if (tosend > 0 && !simulate) {
			this.markDirty();
		}
		return tosend;
	}
	
	@Override
	public int extractEnergy(EnumFacing from, int maxExtract, boolean simulate) {
		int toget = energyStorage.extractEnergy(maxExtract, simulate);
		if (toget > 0 && !simulate) {
			this.markDirty();
		}
		return toget;
	}

	@Override
	public int getEnergyStored(EnumFacing from) {
		return energyStorage.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored(EnumFacing from) {
		return energyStorage.getMaxEnergyStored();
	}

	public String getName() {
		return "batteryBlock";
	}

	@Override
	public void update() {
		if (getWorld().isRemote) {
			return;
		}
		handleSendingEnergy();
		handleReceivingEnergy();
	}


	public void setEnergyStored(int energy) {
		this.markDirty();
		this.energyStorage.setEnergyStored(energy);
	}
	
	public int getEnergy() {
		return getEnergyStored(EnumFacing.DOWN);
	}
	
	public int getMaxCapacity() {
		return getMaxEnergyStored(EnumFacing.DOWN);
	}

	private void handleSendingEnergy() {
		if (!worldObj.isRemote) {
			if (getEnergy() <= 0) {
				return;
			}
			for (EnumFacing dir : EnumFacing.values()) {
				BlockPos targetBlock = getPos().add(dir.getDirectionVec());

				TileEntity tile = worldObj.getTileEntity(targetBlock);
				if (tile instanceof IEnergyReceiver) {
					IEnergyReceiver receiver = (IEnergyReceiver) tile;

					if (receiver.canConnectEnergy(dir.getOpposite())) {
						int tosend = energyStorage.extractEnergy(this.maxExtract, true);
						int used = ((IEnergyReceiver) tile).receiveEnergy(dir.getOpposite(), tosend, false);
						// TODO: need this? It doesn't really *need* state saved
						if (used > 0) {
							this.markDirty();
						}
						energyStorage.extractEnergy(used, false);
					}
				}
			}
		}
	}

	private void handleReceivingEnergy() {
		if (!worldObj.isRemote) {
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
	public int getSizeInventory() {
		// TODO Auto-generated method stub
		return batteryInv.length;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		// TODO Auto-generated method stub
		return batteryInv[index];
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		ItemStack stack = ((ItemStack) batteryInv[index]);
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
		// TODO Auto-generated method stub
		batteryInv[index] = stack;
	}

	@Override
	public int getInventoryStackLimit() {
		// TODO Auto-generated method stub
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		// TODO Auto-generated method stub
		return stack.getItem() instanceof IEnergyHandler;
	}
}
