package p455w0rd.p455w0rdsthings.client.render;

import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.RenderUtils;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Vector3;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import p455w0rd.p455w0rdsthings.ModFluids;
import p455w0rd.p455w0rdsthings.blocks.tileentities.TileEntityBattery;

@SuppressWarnings("deprecation")
public class TESRBattery extends TileEntitySpecialRenderer<TileEntityBattery> {

	@Override
	public void renderTileEntityAt(TileEntityBattery te, double x, double y, double z, float partialTicks, int destroyStage) {
		CCRenderState.reset();
        CCRenderState.setBrightness(getWorld(), te.getPos());
		renderLiquid(new FluidStack(ModFluids.xpJuice, 15), x, y, z);

	}
	
	public static void renderLiquid(FluidStack liquid, double x, double y, double z) {
        RenderUtils.renderFluidCuboidGL(liquid, new Cuboid6(0.22, 0.12, 0.22, 0.78, 0.121 + 0.63, 0.78).add(new Vector3(x, y, z)), liquid.amount / (16D * FluidContainerRegistry.BUCKET_VOLUME), 0.75);
    }

}
