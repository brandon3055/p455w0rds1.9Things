package p455w0rd.p455w0rdsthings.handlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import p455w0rd.p455w0rdsthings.Globals;
import p455w0rd.p455w0rdsthings.P455w0rdsThings;
import p455w0rd.p455w0rdsthings.client.gui.GuiDankNull;
import p455w0rd.p455w0rdsthings.container.ContainerDankNull;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer playerIn, World worldIn, int x, int y, int z) {
		if (ID == Globals.GUINUM_DANKNULL) {
			return new ContainerDankNull(playerIn);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer playerIn, World worldIn, int x, int y, int z) {
		if (ID == Globals.GUINUM_DANKNULL) {
			return new GuiDankNull(new ContainerDankNull(playerIn), playerIn.inventory);
		}
		return null;
	}
	
	public static void launchGui(final int ID, final EntityPlayer playerIn, final World worldIn, final int x, final int y, final int z) {
		playerIn.openGui(P455w0rdsThings.INSTANCE, ID, worldIn, x, y, z);
	}

}
