package p455w0rd.p455w0rdsthings.client.render;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.GL11;

import codechicken.lib.render.IItemRenderer;
import codechicken.lib.render.TransformUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import p455w0rd.p455w0rdsthings.Globals;
import p455w0rd.p455w0rdsthings.items.ItemDankNull;
import p455w0rd.p455w0rdsthings.util.ItemUtils;

@SideOnly(Side.CLIENT)
public class DankNullRenderer implements IItemRenderer {
	private float timer = 0.0F;

	@Override
	public void renderItem(ItemStack item) {
		if (!(item.getItem() instanceof ItemDankNull)) {
			return;
		}
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		ItemStack stack = ItemUtils.getDankNullStack(player.inventory);
		//ItemModelMesher itemModelMesher = new net.minecraftforge.client.ItemModelMesherForge(Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager());
		int index = ItemDankNull.getSelectedStackIndex(item);
		ItemStack containedStack = ItemDankNull.getItemByIndex(item, index);
		IBakedModel holderModel = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager().getModel(new ModelResourceLocation(item.getItem().getRegistryName() + "" + item.getItemDamage(), "inventory"));
		IBakedModel holderModel2 = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(item);
		TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
		textureManager.bindTexture(TextureMap.locationBlocksTexture);
		textureManager.getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);
		
		if (containedStack != null) {
			IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(containedStack);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.enableRescaleNormal();
			GlStateManager.alphaFunc(516, 0.1F);
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			GlStateManager.pushMatrix();
			// TODO: check if negative scale is a thing
			GlStateManager.scale(0.5D, 0.5D, 0.5D);
			GlStateManager.translate(0.5D, 1.0D, 0.5D);
			if (timer >= 360.1F) {
				timer = 0.0F;
			}
			timer += 0.5F;
			GlStateManager.rotate(timer, 1.0F, 1.0F, 1.0F);
			model = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(model, ItemCameraTransforms.TransformType.NONE, false);

			renderModel(model, -1, item);
			GlStateManager.translate(1.0D, 1.0D, 1.0D);
			GlStateManager.scale(1.0D, 1.0D, 1.0D);

			GlStateManager.cullFace(GlStateManager.CullFace.BACK);
			GlStateManager.popMatrix();
			GlStateManager.disableRescaleNormal();
			GlStateManager.disableBlend();
		}
		GlStateManager.enableRescaleNormal();
		//GlStateManager.alphaFunc(516, 0.1F);
		//GlStateManager.enableBlend();
		//GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

		GlStateManager.pushMatrix();
		renderModel(holderModel, -1, item);
		GlStateManager.popMatrix();

		GlStateManager.cullFace(GlStateManager.CullFace.BACK);
		GlStateManager.disableRescaleNormal();
		//GlStateManager.disableBlend();

		textureManager.bindTexture(TextureMap.locationBlocksTexture);
		textureManager.getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
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

	private void renderQuads(VertexBuffer renderer, List<BakedQuad> quads, int color, ItemStack stack) {
		boolean flag = color == -1 && stack != null;
		int i = 0;

		for (int j = quads.size(); i < j; ++i) {
			BakedQuad bakedquad = (BakedQuad) quads.get(i);
			int k = color;

			if (flag && bakedquad.hasTintIndex()) {
				ItemColors itemColors = Minecraft.getMinecraft().getItemColors();
				k = itemColors.getColorFromItemstack(stack, bakedquad.getTintIndex());

				if (EntityRenderer.anaglyphEnable) {
					k = TextureUtil.anaglyphColor(k);
				}

				k = k | -16777216;
			}

			net.minecraftforge.client.model.pipeline.LightUtil.renderQuadColor(renderer, bakedquad, k);
		}
	}

	@Override
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType) {
		return IPerspectiveAwareModel.MapWrapper.handlePerspective(this, TransformUtils.DEFAULT_BLOCK.getTransforms(), cameraTransformType);
	}

	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
		return new ArrayList<BakedQuad>(); //empty array
	}

	@Override
	public boolean isAmbientOcclusion() {
		return false;
	}

	@Override
	public boolean isGui3d() {
		return false;
	}

	@Override
	public boolean isBuiltInRenderer() {
		return true;
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return null;
	}

	@Override
	public ItemCameraTransforms getItemCameraTransforms() {
		return ItemCameraTransforms.DEFAULT;
	}

	@Override
	public ItemOverrideList getOverrides() {
		return ItemOverrideList.NONE;
	}

}
