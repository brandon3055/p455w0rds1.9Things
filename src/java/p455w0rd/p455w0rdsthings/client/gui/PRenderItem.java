package p455w0rd.p455w0rdsthings.client.gui;

import javax.annotation.Nonnull;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import p455w0rd.p455w0rdsthings.util.ItemUtils;
import p455w0rd.p455w0rdsthings.util.ReadableNumberConverter;

/**
 * @author AlgorithmX2
 * @author thatsIch
 * @version rv2
 * @since rv0
 */
public class PRenderItem extends RenderItem {

	private boolean useLg = false;
	ItemStack itemStack;

	public PRenderItem(TextureManager p_i46552_1_, ModelManager p_i46552_2_, ItemColors p_i46552_3_, boolean useLg) {
		super(p_i46552_1_, p_i46552_2_, p_i46552_3_);
		this.useLg = useLg;
	}

	@Override
	public void renderItemOverlayIntoGUI(final FontRenderer fontRenderer, final ItemStack is, final int par4, final int par5, final String par6Str) {

		if (is != null) {
			
			float scaleFactor = useLg ? 1.0f : 0.5f;
			float inverseScaleFactor = 1.0f / scaleFactor;
			int offset = useLg ? 0 : -1;
			String stackSize = "";

			final boolean unicodeFlag = fontRenderer.getUnicodeFlag();
			fontRenderer.setUnicodeFlag(false);

			if (is.getItem().showDurabilityBar(is)) {
					
	                double health = is.getItem().getDurabilityForDisplay(is);
	                int j = (int)Math.round(13.0D - health * 13.0D);
	                int i = (int)Math.round(255.0D - health * 255.0D);
	                //GlStateManager.enableLighting();
	                GlStateManager.disableDepth();
	                GlStateManager.disableTexture2D();
	                //GlStateManager.disableAlpha();
	                //GlStateManager.disableBlend();
	                Tessellator tessellator = Tessellator.getInstance();
	                VertexBuffer vertexbuffer = tessellator.getBuffer();
	                this.draw(vertexbuffer, par4 + 2, par5 + 13, 13, 2, 0, 0, 0, 255);
	                this.draw(vertexbuffer, par4 + 2, par5 + 13, 12, 1, (255 - i) / 4, 64, 0, 255);
	                this.draw(vertexbuffer, par4 + 2, par5 + 13, j, 1, 255 - i, i, 0, 255);
	                //GlStateManager.enableBlend();
	                //GlStateManager.enableAlpha();
	                GlStateManager.enableTexture2D();
	                //GlStateManager.enableLighting();
	                GlStateManager.enableDepth();
			}

			long amount = 0;
			if (ItemUtils.isDankNullStack(is)) {
				amount = ItemUtils.getDankNullStackSize(is);
				if (amount != 0) {
					scaleFactor = 0.5f;
					inverseScaleFactor = 1.0f / scaleFactor;
					offset = -1;
					stackSize = this.getToBeRenderedStackSize(amount);
				}
			}
			else {
				amount = is.stackSize;
				if (amount != 0) {
					scaleFactor = 1.0f;
					inverseScaleFactor = 1.0f / scaleFactor;
					offset = 0;
					stackSize = this.getToBeRenderedStackSize(amount);
				}
			}
			GlStateManager.disableLighting();
			GlStateManager.disableAlpha();
			GlStateManager.disableBlend();
			GlStateManager.disableDepth();
			GlStateManager.pushMatrix();
			GlStateManager.scale(scaleFactor, scaleFactor, scaleFactor);

			final int X = (int) (((float) par4 + offset + 16.0f - fontRenderer.getStringWidth(stackSize) * scaleFactor) * inverseScaleFactor);
			final int Y = (int) (((float) par5 + offset + 16.0f - 7.0f * scaleFactor) * inverseScaleFactor);
			if (amount > 1) {
				fontRenderer.drawStringWithShadow(stackSize, X, Y, 16777215);
			}

			GlStateManager.popMatrix();
			GlStateManager.enableDepth();
			GlStateManager.enableBlend();
			GlStateManager.enableAlpha();
			GlStateManager.enableLighting();
			

			fontRenderer.setUnicodeFlag(unicodeFlag);
		}
	}
	
	private void draw(VertexBuffer renderer, int x, int y, int width, int height, int red, int green, int blue, int alpha)
    {
        renderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        renderer.pos((double)(x + 0), (double)(y + 0), 0.0D).color(red, green, blue, alpha).endVertex();
        renderer.pos((double)(x + 0), (double)(y + height), 0.0D).color(red, green, blue, alpha).endVertex();
        renderer.pos((double)(x + width), (double)(y + height), 0.0D).color(red, green, blue, alpha).endVertex();
        renderer.pos((double)(x + width), (double)(y + 0), 0.0D).color(red, green, blue, alpha).endVertex();
        Tessellator.getInstance().draw();
    }

	private String getToBeRenderedStackSize(final long originalSize) {
		if (this.useLg) {
			return ReadableNumberConverter.INSTANCE.toSlimReadableForm(originalSize);
		}
		else {
			return ReadableNumberConverter.INSTANCE.toWideReadableForm(originalSize);
		}
	}
	
	public ItemStack getStack()
	{
		return this.itemStack;
	}

	public void setStack( @Nonnull final ItemStack stack, boolean regularSlotStack )
	{
		this.itemStack = stack;
		this.useLg = regularSlotStack;
	}
}
