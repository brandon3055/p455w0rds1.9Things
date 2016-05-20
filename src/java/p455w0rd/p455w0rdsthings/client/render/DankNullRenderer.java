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
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import p455w0rd.p455w0rdsthings.items.ItemDankNull;
import p455w0rd.p455w0rdsthings.util.ItemUtils;

public class DankNullRenderer implements IItemRenderer {
	
	protected static RenderItem itemRenderer = new RenderItem(Minecraft.getMinecraft().renderEngine, Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager(), Minecraft.getMinecraft().getItemColors());

	public DankNullRenderer() {
		
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

	@Override
	public void renderItem(ItemStack item) {
		if (!(item.getItem() instanceof ItemDankNull)) {
			return;
		}
		GL11.glPushMatrix();
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		ItemStack stack = ItemUtils.getDankNullStack(player.inventory);
		int index = ItemDankNull.getSelectedStackIndex(stack);
		ItemStack containedStack = ItemDankNull.getItemByIndex(stack, index);
		if (containedStack == null) {
			return;
		}
		//IBakedModel holderModel = itemRenderer.getItemModelMesher().getItemModel(stack);
		
		itemRenderer.renderItem(item, ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND);

		if (containedStack != null && (containedStack.getItem() instanceof ItemBlock)) {
			GL11.glTranslated(0.5, 0.5, 0.5);
			GL11.glScalef(0.8f, 0.8f, 0.8f);
			itemRenderer.renderItem(containedStack, ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND);
		}
		GL11.glPopMatrix();
	}

}
