package p455w0rd.p455w0rdsthings.tesrs;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import p455w0rd.p455w0rdsthings.tileentities.TileEntityDankNull;

public class TESRDankNull extends TileEntitySpecialRenderer<TileEntityDankNull> {

    public TESRDankNull() {
    	try {
            // Manually load our rotating model here
            //model = ModelLoaderRegistry.getModel(new ResourceLocation(Globals.MODID, "models/block/dankNullBlock.json"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void renderTileEntityAt(TileEntityDankNull te, double x, double y, double z, float partialTicks, int destroyStage) {
        GlStateManager.pushAttrib();
        GlStateManager.pushMatrix();

        // Translate to the location of our tile entity
        GlStateManager.translate(x, y, z);
        GlStateManager.disableRescaleNormal();

        // Render the rotating handles
        //renderHandles(te);

        // Render our item
        renderItem(te);

        GlStateManager.popMatrix();
        GlStateManager.popAttrib();

    }

    private void renderItem(TileEntityDankNull te) {
        ItemStack stack = te.getStack();
        if (stack != null) {
            RenderHelper.enableStandardItemLighting();
            GlStateManager.enableLighting();
            GlStateManager.pushMatrix();
            // Translate to the center of the block and .9 points higher
            GlStateManager.translate(.5, .5, .5);
            GlStateManager.scale(.7f, .7f, .7f);

            Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.NONE);

            GlStateManager.popMatrix();
        }
    }

}
