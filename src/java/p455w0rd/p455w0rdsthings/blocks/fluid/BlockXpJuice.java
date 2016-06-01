package p455w0rd.p455w0rdsthings.blocks.fluid;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import p455w0rd.p455w0rdsthings.Globals;
import p455w0rd.p455w0rdsthings.ModBlocks;
import p455w0rd.p455w0rdsthings.ModFluids;
import p455w0rd.p455w0rdsthings.client.model.MeshDefinitionFix;
import p455w0rd.p455w0rdsthings.materials.PMaterial;
import p455w0rd.p455w0rdsthings.proxy.CommonProxy;

public class BlockXpJuice extends BlockFluidClassic implements IFluidBlock {

	protected FluidStack stack;
	protected int quantaPerBlock = 8;
	protected float quantaPerBlockFloat = 8F;

	@SuppressWarnings({ "deprecation" })
	public BlockXpJuice(Fluid fluid, Material material) {
		super(fluid, material);
		setLightLevel(1.0F);
		stack = new FluidStack(fluid, fluid.getDensity());
		setRegistryName(this.getFluid().getName());
		setUnlocalizedName(this.getFluid().getName());
		setCreativeTab(CommonProxy.creativeTab);
		GameRegistry.registerBlock(this);
		fluid.setBlock(this);
	}

	public void initModel() {
		Block block = ModBlocks.xpJuiceBlock;
		Item item = Item.getItemFromBlock(block);

		ModelBakery.registerItemVariants(item);

		ModelResourceLocation modelResourceLocation = new ModelResourceLocation(Globals.MODID + ":fluid", ((BlockXpJuice) block).getFluid().getName());

		ModelLoader.setCustomMeshDefinition(item, MeshDefinitionFix.create(stack -> modelResourceLocation));

		ModelLoader.setCustomStateMapper((Block) block, new StateMapperBase() {
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState p_178132_1_) {
				return modelResourceLocation;
			}
		});
	}

	@Override
	public Fluid getFluid() {
		return ModFluids.xpJuice;
	}

	@Override
	public int tickRate(World worldIn) {
		return 5;
	}

	@SuppressWarnings("deprecation")
	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState worldIn, World pos, BlockPos state, Random rand) {
		double d0 = (double) state.getX();
		double d1 = (double) state.getY();
		double d2 = (double) state.getZ();

		int i = ((Integer) worldIn.getValue(LEVEL)).intValue();

		if (i > 0 && i < 8) {
			if (rand.nextInt(64) == 0) {
				pos.playSound(d0 + 0.5D, d1 + 0.5D, d2 + 0.5D, SoundEvents.block_water_ambient, SoundCategory.BLOCKS, rand.nextFloat() * 0.25F + 0.75F, rand.nextFloat() + 0.5F, false);
			}
		}
		else if (rand.nextInt(10) == 0) {
			pos.spawnParticle(EnumParticleTypes.SUSPENDED, d0 + (double) rand.nextFloat(), d1 + (double) rand.nextFloat(), d2 + (double) rand.nextFloat(), 0.0D, 0.0D, 0.0D, new int[0]);
		}

		if (rand.nextInt(10) == 0 && pos.getBlockState(state.down()).isFullyOpaque()) {
			Material material = pos.getBlockState(state.down(2)).getMaterial();

			if (!material.blocksMovement() && !material.isLiquid()) {
				double d3 = d0 + (double) rand.nextFloat();
				double d5 = d1 - 1.05D;
				double d7 = d2 + (double) rand.nextFloat();

				if (this.blockMaterial == PMaterial.xpjuice) {
					pos.spawnParticle(EnumParticleTypes.PORTAL, d3, d5, d7, 0.0D, 0.0D, 0.0D, new int[0]);
				}
			}
		}
	}

	@Override
	public FluidStack drain(World world, BlockPos pos, boolean doDrain) {
		if (!isSourceBlock(world, pos)) {
			return null;
		}

		if (doDrain) {
			world.setBlockToAir(pos);
		}

		return stack.copy();
	}

	public boolean isSourceBlock(IBlockAccess world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		return state.getBlock() == this && state.getValue(LEVEL) == 0;
	}

	@Override
	public boolean canDrain(World world, BlockPos pos) {
		return isSourceBlock(world, pos);
	}

	@Override
	public float getFilledPercentage(World world, BlockPos pos) {
		int quantaRemaining = getQuantaValue(world, pos) + 1;
		float remaining = quantaRemaining / quantaPerBlockFloat;
		if (remaining > 1)
			remaining = 1.0f;
		return remaining * (stack.getFluid().getDensity() > 0 ? 1 : -1);
	}

	public int getQuantaValue(IBlockAccess world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		if (state.getBlock() == Blocks.air) {
			return 0;
		}

		if (state.getBlock() != this) {
			return -1;
		}

		int quantaRemaining = quantaPerBlock - state.getValue(LEVEL);
		return quantaRemaining;
	}
}
