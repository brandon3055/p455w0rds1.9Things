package p455w0rd.p455w0rdsthings.util;

import cofh.api.energy.IEnergyReceiver;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public class EnergyUtils {
	
	public static boolean isEnergyReceiverFromSide(TileEntity te, EnumFacing facing)
	   {
	     return (te instanceof IEnergyReceiver) ? ((IEnergyReceiver)te).canConnectEnergy(facing) : false;
	   }

}
