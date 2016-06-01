package p455w0rd.p455w0rdsthings.blocks.tileentities;

import cofh.api.energy.IEnergyConnection;
import cofh.api.energy.IEnergyHandler;
import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public class TileEntityBattery extends TileEntity implements ITickable, IEnergyReceiver, IEnergyProvider {
	
	protected int energy;
	protected int capacity;
	protected int maxReceive;
	protected int maxExtract;

	public TileEntityBattery() {
		capacity = 6400000;
		maxReceive = 2000;
		maxExtract = 2000;
	}

	@Override
	public boolean canConnectEnergy(EnumFacing from) {
		return true;
	}

	@Override
	public int getEnergyStored(EnumFacing from) {
		return 1600000;
	}

	@Override
	public int getMaxEnergyStored(EnumFacing from) {
		return capacity;
	}

	@Override
	public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {
		int energyReceived = Math.min(capacity - energy, Math.min(this.maxReceive, maxReceive));

		if (!simulate) {
			energy += energyReceived;
		}
		return energyReceived;
	}

	@Override
	public int extractEnergy(EnumFacing from, int maxExtract, boolean simulate) {
		int energyExtracted = Math.min(energy, Math.min(this.maxExtract, maxExtract));

		if (!simulate) {
			energy -= energyExtracted;
		}
		return energyExtracted;
	}

	@Override
	public void update() {
		handleSendingEnergy();
	}
	
	public void setEnergyStored(int energy) {

		this.energy = energy;

		if (this.energy > capacity) {
			this.energy = capacity;
		} else if (this.energy < 0) {
			this.energy = 0;
		}
	}

	private void handleSendingEnergy() {
		if (!worldObj.isRemote) {
			//int energyStored = getEnergyStored(EnumFacing.DOWN);
			
			int energyStored = capacity;
			setEnergyStored(energyStored);

			for (EnumFacing facing : EnumFacing.values()) {
				BlockPos pos = getPos().offset(facing);
				TileEntity te = worldObj.getTileEntity(pos);
				if (te instanceof IEnergyHandler) {
					IEnergyConnection connection = (IEnergyConnection) te;
					EnumFacing opposite = facing.getOpposite();
					if (connection.canConnectEnergy(opposite)) {
						int rfToGive = 2000 <= energyStored ? 2000 : energyStored;

						//int received = EnergyTools.receiveEnergy(te, opposite, rfToGive);
						int received = ((IEnergyReceiver) te).receiveEnergy(facing, rfToGive, false);
						energyStored -= extractEnergy(EnumFacing.DOWN, received, false);
						if (energyStored <= 0) {
							break;
						}
					}
				}
			}
		}
	}

}
