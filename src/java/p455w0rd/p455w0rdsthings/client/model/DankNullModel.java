package p455w0rd.p455w0rdsthings.client.model;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Table;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.pipeline.IVertexConsumer;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.client.model.pipeline.VertexTransformer;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.common.FMLLog;
import p455w0rd.p455w0rdsthings.ModBlocks;
import p455w0rd.p455w0rdsthings.ModItems;
import p455w0rd.p455w0rdsthings.blocks.BlockDankNull;
import p455w0rd.p455w0rdsthings.items.IDankNull;
import p455w0rd.p455w0rdsthings.state.PStateProps;



public class DankNullModel implements IBakedModel, IResourceManagerReloadListener {

	private final Table<IDankNull.HolderLevel, String, CompositeBakedModel> CACHE = HashBasedTable.create();

	protected static BakedQuad transform(BakedQuad quad, final TRSRTransformation transform) {
		UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(DefaultVertexFormats.ITEM);
		final IVertexConsumer consumer = new VertexTransformer(builder) {
			@Override
			public void put(int element, float... data) {
				VertexFormatElement formatElement = DefaultVertexFormats.ITEM.getElement(element);
				switch(formatElement.getUsage()) {
					case POSITION: {
						float[] newData = new float[4];
						Vector4f vec = new Vector4f(data);
						transform.getMatrix().transform(vec);
						vec.get(newData);
						parent.put(element, newData);
						break;
					}
					default: {
						parent.put(element, data);
						break;
					}
				}
			}
		};
		quad.pipe(consumer);
		return builder.build();
	}

	@Override
	public void onResourceManagerReload(@Nonnull IResourceManager resourceManager) {
		CACHE.clear();
	}

	@Nonnull
	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing face, long rand) {
		if(state.getBlock() != ModBlocks.dankNullBlock)
			return Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelManager().getMissingModel().getQuads(state, face, rand);
		IExtendedBlockState realState = ((IExtendedBlockState) state);
		IDankNull.HolderLevel holderLevel = realState.getValue(PStateProps.HOLDER_LEVEL);
		String identifier = "1";

		return getModel(holderLevel, identifier).getQuads(state, face, rand);
	}

	// Get the model for this holder level + item type combination. If it's not cached already, generate it.
	private CompositeBakedModel getModel(IDankNull.HolderLevel holderLevel, String identifier) {
		ModelManager modelManager = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager();

		if(CACHE.contains(holderLevel, identifier)) {
			return CACHE.get(holderLevel, identifier);
		} else {
			IBakedModel holderModel;
			try {
				holderModel = ModelLoaderRegistry.getModel(BlockDankNull.getRegisteredHolderLevelModels().get(holderLevel)).bake(TRSRTransformation.identity(), DefaultVertexFormats.ITEM, ModelLoader.defaultTextureGetter());
			} catch (Exception e) {
				holderModel = modelManager.getMissingModel();
			}

			IBakedModel flowerModel;		
				ItemStack stack = new ItemStack((Item) ModItems.dankNullItem, 1, Integer.parseInt(identifier));
				IBakedModel specialFlowerModel = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(stack);
				flowerModel = specialFlowerModel.getOverrides().handleItemState(specialFlowerModel, stack, null, null);

			// Enhance!
			CompositeBakedModel model = new CompositeBakedModel(flowerModel, holderModel);
			FMLLog.info("[Botania]: Cached floating flower model for islandtype %s and flowertype %s", holderLevel, identifier);
			CACHE.put(holderLevel, (String) "" + identifier, model);
			return model;
		}
	}

	@Nonnull
	@Override
	public ItemOverrideList getOverrides() {
		return itemHandler;
	}

	@Override public boolean isAmbientOcclusion() { return false; }
	@Override public boolean isGui3d() { return true; }
	@Override public boolean isBuiltInRenderer() { return false; }
	@Nonnull
	@Override public TextureAtlasSprite getParticleTexture() { return null; }
	@Nonnull
	@Override public ItemCameraTransforms getItemCameraTransforms() { return ItemCameraTransforms.DEFAULT; }

	private static class CompositeBakedModel implements IBakedModel {

		private final IBakedModel base;
		private final List<BakedQuad> genQuads;

		public CompositeBakedModel(IBakedModel flower, IBakedModel holder) {
			this.base = flower;

			ImmutableList.Builder<BakedQuad> genBuilder = ImmutableList.builder();
			final TRSRTransformation transform = TRSRTransformation.blockCenterToCorner(new TRSRTransformation(new Vector3f(0F, 0.2F, 0F), null, new Vector3f(0.5F, 0.5F, 0.5F), null));


			// Add flower quads, scaled and translated
			for(BakedQuad quad : flower.getQuads(null, null, 0)) {
				genBuilder.add(transform(quad, transform));
			}

			for(EnumFacing e : EnumFacing.VALUES) {
				genBuilder.addAll(flower.getQuads(null, e, 0).stream().map(input -> transform(input, transform)).collect(Collectors.toList()));
			}

			// Add island quads
			genBuilder.addAll(holder.getQuads(null, null, 0));
			for(EnumFacing e : EnumFacing.VALUES) {
				genBuilder.addAll(holder.getQuads(null, e, 0));
			}

			genQuads = genBuilder.build();
		}

		// Forward all to flower model
		@Nonnull
		@Override
		public List<BakedQuad> getQuads(IBlockState state, EnumFacing face, long rand) {
			return genQuads;
		}

		@Override
		public boolean isAmbientOcclusion() {
			return base.isAmbientOcclusion();
		}

		@Override
		public boolean isGui3d() {
			return true;
		}

		@Override
		public boolean isBuiltInRenderer() {
			return base.isBuiltInRenderer();
		}

		@Nonnull
		@Override
		public TextureAtlasSprite getParticleTexture() {
			return base.getParticleTexture();
		}

		@SuppressWarnings("deprecation")
		@Nonnull
		@Override
		public ItemCameraTransforms getItemCameraTransforms() {
			return base.getItemCameraTransforms();
		}

		@Nonnull
		@Override public ItemOverrideList getOverrides() { return ItemOverrideList.NONE; }
	}

	private final ItemOverrideList itemHandler = new ItemOverrideList(ImmutableList.of()) {
		@Nonnull
		@Override
		public IBakedModel handleItemState(@Nonnull IBakedModel model, ItemStack stack, @Nonnull World world, @Nonnull EntityLivingBase entity) {
			// Items always have lvl 1 dank null
			IDankNull.HolderLevel holderLevel = IDankNull.HolderLevel.I;

			return DankNullModel.this.getModel(holderLevel, (String) "" + stack.getItemDamage());
		}
	};
}