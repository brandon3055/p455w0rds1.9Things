package p455w0rd.p455w0rdsthings.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import p455w0rd.p455w0rdsthings.Globals;
import p455w0rd.p455w0rdsthings.blocks.tileentities.TileEntityBattery;
import p455w0rd.p455w0rdsthings.client.render.TESRBattery;
import p455w0rd.p455w0rdsthings.handlers.GuiHandler;
import p455w0rd.p455w0rdsthings.proxy.CommonProxy;
import p455w0rd.p455w0rdsthings.util.BlockUtils;

@SuppressWarnings("deprecation")
public class BlockBattery extends Block implements ITileEntityProvider {

	private String name = "batteryBlock";

	public BlockBattery() {
		super(Material.ROCK);
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(CommonProxy.creativeTab);
		setResistance(6000000.0F);
		setHardness(5.6F);
		GameRegistry.register(this);
		GameRegistry.register(new ItemBlock(this), getRegistryName());
		GameRegistry.registerTileEntity(TileEntityBattery.class, name);
	}
	
	
	@Override
	public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int eventID, int eventParam) {
		super.eventReceived(state, worldIn, pos, eventID, eventParam);
		return true;
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityBattery();
	}
	
	private TileEntityBattery getTE(World worldIn, BlockPos pos) {
		return (TileEntityBattery) BlockUtils.getTE(worldIn, pos);
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!worldIn.isRemote) {
			TileEntityBattery te = getTE(worldIn, pos);
			if (te != null && !playerIn.isSneaking()) {
				GuiHandler.launchGui(Globals.GUINUM_BATTERY, playerIn, worldIn, pos.getX(), pos.getY(), pos.getZ());
			}
			else if (playerIn.isSneaking()) {
				//BlockUtils.wrenchBlock(worldIn, pos, state);
			}
		}
		else {
			if (playerIn.isSneaking()) {
				worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, (double) pos.getX(), (double) pos.getY() + 1.0D, (double) pos.getZ(), 0, 0, 0, new int[0]);
			}
		}
		return true;
	}
	
	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBattery.class, new TESRBattery());
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
		//dropItems(worldIn, pos);
        worldIn.removeTileEntity(pos);
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		return false;
	}

	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isBlockSolid(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
		return true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean isBlockNormalCube(IBlockState state) {
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isOpaqueCube(IBlockState blockState) {
		return false;
	}
	
}
