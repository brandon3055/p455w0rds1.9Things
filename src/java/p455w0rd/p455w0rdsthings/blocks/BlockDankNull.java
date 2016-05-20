package p455w0rd.p455w0rdsthings.blocks;

import java.util.Collections;
import java.util.Map;
import java.util.Random;

import javax.annotation.Nonnull;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.LoaderState;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import p455w0rd.p455w0rdsthings.items.IDankNull;
import p455w0rd.p455w0rdsthings.items.IDankNull.HolderLevel;
import p455w0rd.p455w0rdsthings.state.PStateProps;
import p455w0rd.p455w0rdsthings.tesrs.TESRDankNull;
import p455w0rd.p455w0rdsthings.tileentities.TileEntityDankNull;

public class BlockDankNull extends Block implements ITileEntityProvider {

	private static final Map<IDankNull.HolderLevel, ModelResourceLocation> holderLevelModels = Maps.newHashMap();

	public BlockDankNull() {
		super(Material.glass);
		setUnlocalizedName("dankNullBlock");
		setRegistryName("dankNullBlock");
		setCreativeTab(CreativeTabs.tabMisc);
		GameRegistry.register(this);
		GameRegistry.register(new ItemBlock(this), getRegistryName());
		GameRegistry.registerTileEntity(TileEntityDankNull.class, "pedestalblock");
		//setDefaultState(((IExtendedBlockState) blockState.getBaseState()).withProperty(PStateProps.HOLDER_LEVEL, HolderLevel.ofLevel("1")));
	}
	
	@Nonnull
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Nonnull
	@Override
	public BlockStateContainer createBlockState() {
		return new ExtendedBlockState(this, new IProperty[] { PStateProps.COLOR }, new IUnlistedProperty[] { PStateProps.HOLDER_LEVEL });
	}
	
	@Nonnull
	@Override
	public IBlockState getExtendedState(@Nonnull IBlockState state, IBlockAccess world, BlockPos pos) {
		state = getActualState(state, world, pos);
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof TileEntityDankNull) {
			state = ((IExtendedBlockState) state).withProperty(PStateProps.HOLDER_LEVEL, HolderLevel.I);
		}
		return state;
	}

	@SideOnly(Side.CLIENT)
	public void initModel() {
		// ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
		// Bind our TESR to our tile entity
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDankNull.class, new TESRDankNull());
		//for (int i = 0; i < 6; i++) {
		//	registerHolderLevelModel(IDankNull.HolderLevel.ofLevel((String) "" + i), new ModelResourceLocation("p455w0rdsthings:dankNullBlock", "variant=" + i));
		//}
	}

	public static void registerHolderLevelModel(IDankNull.HolderLevel holderLevel, ModelResourceLocation model) {
		Preconditions.checkState(Loader.instance().isInState(LoaderState.PREINITIALIZATION));
		holderLevelModels.put(holderLevel, model);
	}

	/**
	 * @return An immutable and live view of the registered island type model
	 *         map
	 */
	public static Map<IDankNull.HolderLevel, ModelResourceLocation> getRegisteredHolderLevelModels() {
		return Collections.unmodifiableMap(holderLevelModels);
	}

	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isBlockSolid(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityDankNull();
	}

	@Override
	public boolean isBlockNormalCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState blockState) {
		return false;
	}

	public void dropItemStackInWorld(World worldObj, double x, double y, double z, ItemStack stack) {
		float f = 0.7F;
		float d0 = worldObj.rand.nextFloat() * f + (1.0F - f) * 0.5F;
		float d1 = worldObj.rand.nextFloat() * f + (1.0F - f) * 0.5F;
		float d2 = worldObj.rand.nextFloat() * f + (1.0F - f) * 0.5F;
		EntityItem entityitem = new EntityItem(worldObj, x + d0, y + d1, z + d2, stack);
		entityitem.setPickupDelay(10);
		if (stack.hasTagCompound()) {
			entityitem.getEntityItem().setTagCompound((NBTTagCompound) stack.getTagCompound().copy());
		}
		worldObj.spawnEntityInWorld(entityitem);
	}

	@Override
	public boolean onBlockEventReceived(World worldIn, BlockPos pos, IBlockState state, int eventID, int eventParam) {
		super.onBlockEventReceived(worldIn, pos, state, eventID, eventParam);
		return true;
	}

	@Override
	public int quantityDropped(Random random) {
		return 0;
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		final TileEntity te = getTE(worldIn, pos);
		if (te != null) {
			if (te.getTileData() != null) {
				NBTTagCompound nbtTC = te.getTileData();
				ItemStack is = new ItemStack(Item.getItemFromBlock(this));
				is.setTagCompound(nbtTC);
				this.dropItemStackInWorld(worldIn, pos.getX(), pos.getY(), pos.getZ(), is);
			}
			//removeTE(worldIn, pos);
		}
	}

	private TileEntityDankNull getTE(World worldIn, BlockPos pos) {
		return (TileEntityDankNull) worldIn.getTileEntity(pos);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			TileEntityDankNull te = getTE(world, pos);
			if (te.getStack() == null) {
				if (player.getHeldItem(EnumHand.MAIN_HAND) != null) {
					// There is no item in the pedestal and the player is holding an item. We move that item
					// to the pedestal
					te.setStack(player.getHeldItem(EnumHand.MAIN_HAND));
					player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
					// Make sure the client knows about the changes in the player inventory
					player.openContainer.detectAndSendChanges();
				}
			}
			else {
				// There is a stack in the pedestal. In this case we remove it and try to put it in the
				// players inventory if there is room
				ItemStack stack = te.getStack();
				te.setStack(null);
				if (!player.inventory.addItemStackToInventory(stack)) {
					// Not possible. Throw item in the world
					EntityItem entityItem = new EntityItem(world, pos.getX(), pos.getY() + 1, pos.getZ(), stack);
					world.spawnEntityInWorld(entityItem);
				}
				else {
					player.openContainer.detectAndSendChanges();
				}
			}
		}

		// Return true also on the client to make sure that MC knows we handled this and will not try to place
		// a block on the client
		return true;
	}

}
