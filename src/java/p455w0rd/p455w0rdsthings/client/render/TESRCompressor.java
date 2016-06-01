package p455w0rd.p455w0rdsthings.client.render;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import p455w0rd.p455w0rdsthings.blocks.tileentities.TileEntityCompressor;
import p455w0rd.p455w0rdsthings.items.ItemDankNull;

public class TESRCompressor extends TileEntitySpecialRenderer<TileEntityCompressor> {

	private float rotation;
	private static float ticks = 0;
	private boolean rev = false;

	@Override
	public void renderTileEntityAt(TileEntityCompressor te, double x, double y, double z, float partialTicks, int destroyStage) {

		GlStateManager.pushAttrib();
		GlStateManager.pushMatrix();

		// Translate to the location of our tile entity
		GlStateManager.translate(x, y, z);
		GlStateManager.disableRescaleNormal();

		// Render our item
		renderItem(te);

		GlStateManager.popMatrix();
		GlStateManager.popAttrib();
		if (te.getInputSlotStack() != null) {
			renderBeams(partialTicks, te, x, y, z);
		}
		else {
			ticks = 0;
		}

	}

	private void renderBeams(float partialTicks, TileEntityCompressor te, double x, double y, double z) {
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer vertexbuffer = tessellator.getBuffer();
		RenderHelper.disableStandardItemLighting();
		float f = ((float) ticks + partialTicks) / 1000.0F;
		float f1 = 0.0F;

		if (f > 0.8F) {
			f1 = (f - 0.8F) / 0.2F;
		}

		Random random = new Random(432L);
		GlStateManager.disableTexture2D();
		GlStateManager.shadeModel(7425);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
		GlStateManager.disableAlpha();
		GlStateManager.enableCull();
		GlStateManager.depthMask(false);
		GlStateManager.pushMatrix();
		//GlStateManager.translate(0.0F, -1.0F, -2.0F);
		GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);

		for (int i = 6; (float) i < (f + f * f) + 6 / 2.0F * 12.0F; ++i) {
			GlStateManager.rotate(random.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(random.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(random.nextFloat() * 360.0F, 0.0F, 0.0F, 1.0F);
			GlStateManager.rotate(random.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(random.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(random.nextFloat() * 360.0F + f * 90.0F, 0.0F, 0.0F, 1.0F);

			float f2 = random.nextFloat() * 10.0F + 5.0F + (f1 / 2) * 10.0F;
			float f3 = random.nextFloat() * 2.0F + 1.0F + (f1 / 2) * 2.0F;
			f2 /= 20;
			f3 /= 20;

			/*
			float f2 = random.nextFloat() * 20.0F + 5.0F + f1 * 10.0F;
			float f3 = random.nextFloat() * 2.0F + 1.0F + f1 * 2.0F;
			*/
			vertexbuffer.begin(6, DefaultVertexFormats.POSITION_COLOR);

			vertexbuffer.pos(0.0D, 0.0D, 0.0D).color(255, 255, 255, (int) (255.0F)).endVertex();

			//vertexbuffer.pos(0.0D, 0.0D, 0.0D).color(0, 255, 0, (int)(255.0F * (1.0F - f1))).endVertex();

			vertexbuffer.pos(-0.866D * (double) f3, (double) f2, (double) (-0.5F * f3)).color(0, 0, 0, 0).endVertex();
			vertexbuffer.pos(0.866D * (double) f3, (double) f2, (double) (-0.5F * f3)).color(0, 0, 0, 0).endVertex();
			vertexbuffer.pos(0.0D, (double) f2, (double) (1.0F * f3)).color(0, 0, 0, 0).endVertex();
			vertexbuffer.pos(-0.866D * (double) f3, (double) f2, (double) (-0.5F * f3)).color(0, 0, 0, 0).endVertex();

			/*
			vertexbuffer.pos(-0.866D * (double)f3, (double)f2, (double)(-0.5F * f3)).color(255, 0, 255, 0).endVertex();
			vertexbuffer.pos(0.866D * (double)f3, (double)f2, (double)(-0.5F * f3)).color(255, 0, 255, 0).endVertex();
			vertexbuffer.pos(0.0D, (double)f2, (double)(1.0F * f3)).color(255, 0, 255, 0).endVertex();
			vertexbuffer.pos(-0.866D * (double)f3, (double)f2, (double)(-0.5F * f3)).color(255, 0, 255, 0).endVertex();
			*/
			tessellator.draw();
		}

		GlStateManager.popMatrix();
		GlStateManager.depthMask(true);
		GlStateManager.disableCull();
		GlStateManager.disableBlend();
		GlStateManager.shadeModel(7424);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableTexture2D();
		GlStateManager.enableAlpha();
		RenderHelper.enableStandardItemLighting();
	}

	private void renderItem(TileEntityCompressor te) {
		ItemStack stack = te.getInputSlotStack();
		if (stack != null) {
			IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(stack);
			RenderHelper.enableStandardItemLighting();
			GlStateManager.enableLighting();

			GlStateManager.pushMatrix();

			GlStateManager.translate(.5, .5, .5);
			GlStateManager.scale(.35f, .35f, .35f);
			if (rotation >= 360) {
				rotation = 0;
			}

			if (getWorld().canBlockSeeSky(te.getPos()) && !Minecraft.getMinecraft().isGamePaused()) {
				//if (!getWorld().isBlockPowered(te.getPos())) {
					int x = te.getPos().getX();
					int y = te.getPos().getY();
					int z = te.getPos().getZ();
					rotation += 0.5;
					if (rotation % 36 == 0) {
						getWorld().spawnParticle(EnumParticleTypes.PORTAL, x + 0.5, y + 0.75, z + 0.5, 0, 0, 0, new int[0]);
					}
				//}
			}

			GlStateManager.rotate(rotation, 0, rotation, 0);

			Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.NONE);
				GlStateManager.translate(-0.5, -0.5, -0.5);
				GlStateManager.enableBlend();
				renderEffect(model);
				GlStateManager.disableBlend();
			GlStateManager.popMatrix();

		}
	}

	private void renderModel(IBakedModel model, ItemStack stack) {
		this.renderModel(model, -1, stack);
	}

	private void renderModel(IBakedModel model, int color) {
		this.renderModel(model, color, (ItemStack) null);
	}

	private void renderModel(IBakedModel model, int color, ItemStack stack) {
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer vertexbuffer = tessellator.getBuffer();
		vertexbuffer.begin(7, DefaultVertexFormats.ITEM);

		for (EnumFacing enumfacing : EnumFacing.values()) {
			this.renderQuads(vertexbuffer, model.getQuads((IBlockState) null, enumfacing, 0L), color, stack);
		}

		this.renderQuads(vertexbuffer, model.getQuads((IBlockState) null, (EnumFacing) null, 0L), color, stack);
		tessellator.draw();
	}

	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
		return new ArrayList<BakedQuad>();
	}

	private void renderQuads(VertexBuffer renderer, List<BakedQuad> quads, int color, ItemStack stack) {
		boolean flag = color == -1 && stack != null;
		int i = 0;

		for (int j = quads.size(); i < j; ++i) {
			BakedQuad bakedquad = (BakedQuad) quads.get(i);
			int k = color;

			if (flag && bakedquad.hasTintIndex()) {
				ItemColors itemColors = Minecraft.getMinecraft().getItemColors();
				k = itemColors.getColorFromItemstack(stack, bakedquad.getTintIndex());
				//k=0x00FF00;

				if (EntityRenderer.anaglyphEnable) {
					k = TextureUtil.anaglyphColor(k);
				}
				k = k | -16777216;
				//k = k | 0x00FF00;
			}
			net.minecraftforge.client.model.pipeline.LightUtil.renderQuadColor(renderer, bakedquad, k);
		}
	}

	public void renderItem(ItemStack stack, IBakedModel model) {
		if (stack != null) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(-0.5F, -0.5F, -0.5F);

			if (model.isBuiltInRenderer()) {
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				GlStateManager.enableRescaleNormal();
				//TileEntityItemStackRenderer.instance.renderByItem(stack);
				Minecraft.getMinecraft().getItemRenderer().renderItem(Minecraft.getMinecraft().thePlayer, stack, TransformType.NONE);
			}
			else {

				this.renderModel(model, stack);

				if (stack.hasEffect()) {
					if (stack.getItem() instanceof ItemDankNull) {
						this.renderEffect2(model);
					}
					else {
						this.renderEffect(model);
					}
				}
			}

			GlStateManager.popMatrix();
		}
	}

	private void renderEffect(IBakedModel model) {
		GlStateManager.depthMask(false);
		GlStateManager.depthFunc(514);
		GlStateManager.disableLighting();
		//GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ONE);
		Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("minecraft", "textures/misc/enchanted_item_glint.png"));
		GlStateManager.matrixMode(5890);
		GlStateManager.pushMatrix();
		GlStateManager.scale(4.0F, 4.0F, 4.0F);
		float f = (float) (Minecraft.getSystemTime() % 3000L) / 3000.0F / 8.0F;
		GlStateManager.translate(f, 0.0F, 0.0F);
		GlStateManager.rotate(-50.0F, 0.0F, 0.0F, 1.0F);
		this.renderModel(model, 0x33FFFFFF);
		GlStateManager.popMatrix();
		GlStateManager.pushMatrix();
		GlStateManager.scale(12.0F, 12.0F, 12.0F);
		float f1 = (float) (Minecraft.getSystemTime() % 4873L) / 4873.0F / 8.0F;
		GlStateManager.translate(-f1, 0.0F, 0.0F);
		GlStateManager.rotate(10.0F, 0.0F, 0.0F, 1.0F);
		this.renderModel(model, 0x33FFFFFF);
		GlStateManager.popMatrix();
		GlStateManager.matrixMode(5888);
		//GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		GlStateManager.enableLighting();
		GlStateManager.depthFunc(515);
		GlStateManager.depthMask(true);
		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
	}

	private void renderEffect2(IBakedModel model) {
		GlStateManager.depthMask(false);
		GlStateManager.depthFunc(514);
		GlStateManager.disableLighting();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ONE);
		Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("minecraft", "textures/misc/enchanted_item_glint.png"));
		GlStateManager.matrixMode(5890);
		GlStateManager.pushMatrix();
		GlStateManager.scale(8.0F, 8.0F, 8.0F);
		float f = (float) (Minecraft.getSystemTime() % 3000L) / 3000.0F / 8.0F;
		GlStateManager.translate(f, 0.0F, 0.0F);
		GlStateManager.rotate(-50.0F, 0.0F, 0.0F, 1.0F);
		//this.renderModel(model, -8372020);
		this.renderModel(model, 0xFFFFFF00);
		GlStateManager.popMatrix();
		/*
		GlStateManager.pushMatrix();
		GlStateManager.scale(8.0F, 8.0F, 8.0F);
		float f1 = (float) (Minecraft.getSystemTime() % 4873L) / 4873.0F / 8.0F;
		GlStateManager.translate(-f1, 0.0F, 0.0F);
		GlStateManager.rotate(10.0F, 0.0F, 0.0F, 1.0F);
		//this.renderModel(model, -8372020);
		this.renderModel(model, 0xFFFFFF00);
		GlStateManager.popMatrix();
		*/
		GlStateManager.matrixMode(5888);
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		GlStateManager.enableLighting();
		GlStateManager.depthFunc(515);
		GlStateManager.depthMask(true);
		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
	}

	@SubscribeEvent
	public void tickEvent(RenderTickEvent e) {
		if (!rev) {
			if (ticks < 1000) {
				ticks += 0.5;
			}
			else {
				rev = true;
			}
		}
		else {
			if (ticks > 0) {
				ticks -= 0.5;
			}
			else {
				rev = false;
			}
		}
	}

}
