package p455w0rd.p455w0rdsthings.handlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import p455w0rd.p455w0rdsthings.Globals;
import p455w0rd.p455w0rdsthings.P455w0rdsThings;
import p455w0rd.p455w0rdsthings.blocks.tileentities.TileEntityBattery;
import p455w0rd.p455w0rdsthings.blocks.tileentities.TileEntityCompressor;
import p455w0rd.p455w0rdsthings.blocks.tileentities.TileEntityFurnace;
import p455w0rd.p455w0rdsthings.client.gui.GuiBattery;
import p455w0rd.p455w0rdsthings.client.gui.GuiCompressor;
import p455w0rd.p455w0rdsthings.client.gui.GuiDankNull;
import p455w0rd.p455w0rdsthings.client.gui.GuiFurnace;
import p455w0rd.p455w0rdsthings.container.ContainerBattery;
import p455w0rd.p455w0rdsthings.container.ContainerCompressor;
import p455w0rd.p455w0rdsthings.container.ContainerDankNull;
import p455w0rd.p455w0rdsthings.container.ContainerFurnace;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer playerIn, World worldIn, int x, int y, int z) {
		if (ID == Globals.GUINUM_DANKNULL) {
			return new ContainerDankNull(playerIn);
		}
		else {
			TileEntity te = worldIn.getTileEntity(new BlockPos(x, y, z));
			if (ID == Globals.GUINUM_COMPRESSOR) {
				if (te == null) {
					return null;
				}
				return new ContainerCompressor(playerIn.inventory, (TileEntityCompressor) te);
			}
			else if (ID == Globals.GUINUM_FURNACE) {
				if (te == null) {
					return null;
				}
				return new ContainerFurnace(playerIn.inventory, (TileEntityFurnace) te);
			}
			else if (ID == Globals.GUINUM_BATTERY) {
				if (te == null) {
					return null;
				}
				return new ContainerBattery(playerIn.inventory, (TileEntityBattery) te);
			}
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer playerIn, World worldIn, int x, int y, int z) {
		if (ID == Globals.GUINUM_DANKNULL) {
			return new GuiDankNull(new ContainerDankNull(playerIn), playerIn.inventory);
		}
		else {
			TileEntity te = worldIn.getTileEntity(new BlockPos(x, y, z));
			if (ID == Globals.GUINUM_COMPRESSOR) {
				if (te == null) {
					return null;
				}
				return new GuiCompressor(playerIn.inventory, (TileEntityCompressor) te);
			}
			else if (ID == Globals.GUINUM_FURNACE) {
				if (te == null) {
					return null;
				}
				return new GuiFurnace(playerIn.inventory, (TileEntityFurnace) te);
			}
			else if (ID == Globals.GUINUM_BATTERY) {
				if (te == null) {
					return null;
				}
				return new GuiBattery(playerIn.inventory, (TileEntityBattery) te);
			}
		}
		return null;
	}

	public static void launchGui(final int ID, final EntityPlayer playerIn, final World worldIn, final int x, final int y, final int z) {
		playerIn.openGui(P455w0rdsThings.INSTANCE, ID, worldIn, x, y, z);
	}

}
