package p455w0rd.p455w0rdsthings.client.render;

import codechicken.lib.render.CCRenderState;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Vector3;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraftforge.fluids.FluidStack;
import p455w0rd.p455w0rdsthings.ModFluids;
import p455w0rd.p455w0rdsthings.blocks.tileentities.TileEntityBattery;
import p455w0rd.p455w0rdsthings.util.PRenderUtils;

public class TESRBattery extends TileEntitySpecialRenderer<TileEntityBattery> {

	@Override
	public void renderTileEntityAt(TileEntityBattery te, double x, double y, double z, float partialTicks, int destroyStage) {
		CCRenderState.reset();
        CCRenderState.setBrightness(getWorld(), te.getPos());
		renderLiquid(new FluidStack(ModFluids.xpJuice, 10000), x, y, z);

	}
	
	public static void renderLiquid(FluidStack liquid, double x, double y, double z) {
        PRenderUtils.renderFluidCuboidGL(liquid, new Cuboid6(0.18, 0.12, 0.18, 0.80, 0.121 + 0.63, 0.80).add(new Vector3(x, y, z)), liquid.amount / (16D * 1000), 0.75);
    }

}
