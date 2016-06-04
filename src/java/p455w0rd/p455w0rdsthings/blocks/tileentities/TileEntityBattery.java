package p455w0rd.p455w0rdsthings.blocks.tileentities;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import cofh.api.energy.IEnergyStorage;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import p455w0rd.p455w0rdsthings.util.BlockUtils;
import p455w0rd.p455w0rdsthings.util.EnergyUtils;

public class TileEntityBattery extends TileEntity implements ITickable, IEnergyHandler, IEnergyReceiver, IEnergyProvider {

	protected ItemStack[] batteryInv;
	IEnergyReceiver[] adjacentHandlers = new IEnergyReceiver[6];
	protected EnergyStorage energyStorage;
	boolean cached = false;

	protected int capacity;
	protected int maxReceive;
	protected int maxExtract;

	public TileEntityBattery() {
		capacity = 6400000;
		maxReceive = 2000;
		maxExtract = 2000;
		batteryInv = new ItemStack[5];
		energyStorage = new EnergyStorage(capacity, maxReceive);
		this.energyStorage.setEnergyStored(10000);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		NBTTagCompound energyTag = tagCompound.getCompoundTag("Energy");
		this.energyStorage.readFromNBT(energyTag);
/*
		NBTTagList nbtTL = tagCompound.getTagList(this.getName(), 10);
		for (int i = 0; i < nbtTL.tagCount(); i++) {
			NBTTagCompound nbtTC = (NBTTagCompound) nbtTL.getCompoundTagAt(i);
			if (nbtTC == null) {
				continue;
			}
			int slot = nbtTC.getInteger("Slot");
			this.batteryInv[slot] = ItemStack.loadItemStackFromNBT(nbtTC);
		}
		*/
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
		tagCompound = super.writeToNBT(tagCompound);
		NBTTagCompound energyTag = new NBTTagCompound();
		this.energyStorage.writeToNBT(energyTag);
		tagCompound.setTag("Energy", energyTag);
/*
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
		*/	
		return tagCompound;
	}
/*
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
*/
	public IEnergyStorage getEnergyStorage() {
		return this.energyStorage;
	}

	@Override
	public boolean canConnectEnergy(EnumFacing from) {
		return true;
	}

	@Override
	public int receiveEnergy(EnumFacing from, int maxExtract, boolean simulate) {
		return energyStorage.receiveEnergy(maxExtract, simulate);
	}
	
	@Override
	public int extractEnergy(EnumFacing from, int maxExtract, boolean simulate) {
		return energyStorage.extractEnergy(maxExtract, simulate);
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
		if (!this.cached) {
			updateAdjacentHandlers();
		}
		//markDirty();
		//final IBlockState state = getWorld().getBlockState(getPos());
		//getWorld().notifyBlockUpdate(getPos(), state, state, 3);

	}

	public void onNeighborTileChange(int x, int y, int z) {
		updateAdjacentHandler(x, y, z);
	}

	protected void updateAdjacentHandlers() {
		if (worldObj.isRemote) {
			return;
		}
		for (int i = 0; i < 6; i++) {
			TileEntity localTileEntity = BlockUtils.getAdjacentTileEntity(this, i);

			if (EnergyUtils.isEnergyReceiverFromSide(localTileEntity, EnumFacing.values()[(i ^ 0x1)])) {
				this.adjacentHandlers[i] = ((IEnergyReceiver) localTileEntity);
			}
			else {
				this.adjacentHandlers[i] = null;
			}
		}
		this.cached = true;
	}

	protected void updateAdjacentHandler(int x, int y, int z) {
		if (worldObj.isRemote) {
			return;
		}
		int i = BlockUtils.determineAdjacentSide(this, x, y, z);

		TileEntity localTileEntity = this.worldObj.getTileEntity(new BlockPos(x, y, z));

		if (EnergyUtils.isEnergyReceiverFromSide(localTileEntity, EnumFacing.values()[(i ^ 0x1)])) {
			this.adjacentHandlers[i] = ((IEnergyReceiver) localTileEntity);
		}
		else {
			this.adjacentHandlers[i] = null;
		}
	}

	public void setEnergyStored(int energy) {
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
}
