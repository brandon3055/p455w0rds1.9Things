package p455w0rd.p455w0rdsthings.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import p455w0rd.p455w0rdsthings.blocks.tileentities.TileEntityDankNull;
import p455w0rd.p455w0rdsthings.client.render.TESRDankNull;
import p455w0rd.p455w0rdsthings.proxy.CommonProxy;
import p455w0rd.p455w0rdsthings.util.BlockUtils;
import p455w0rd.p455w0rdsthings.util.ItemUtils;

public class BlockDankNull extends Block implements ITileEntityProvider {

	private String name = "dankNullBlock";
	
	public BlockDankNull() {
		super(Material.glass);
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(CommonProxy.creativeTab);
		GameRegistry.register(this);
		GameRegistry.register(new ItemBlock(this), getRegistryName());
	}

	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
		ClientRegistry.registerTileEntity(TileEntityDankNull.class, name, new TESRDankNull());
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return false;
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
		if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
			if (Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode) {
				return;
			}
		}
		final TileEntity te = getTE(worldIn, pos);
		if (te != null) {
			if (te.getTileData() != null) {
				NBTTagCompound nbtTC = te.getTileData();
				ItemStack is = new ItemStack(Item.getItemFromBlock(this));
				is.setTagCompound(nbtTC);
				ItemUtils.dropItemStackInWorld(worldIn, pos.getX(), pos.getY(), pos.getZ(), is);
			}
		}
	}

	private TileEntityDankNull getTE(World worldIn, BlockPos pos) {
		return (TileEntityDankNull) BlockUtils.getTE(worldIn, pos);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			TileEntityDankNull te = getTE(world, pos);
			if (te.getStack() == null) {
				if (player.getHeldItem(EnumHand.MAIN_HAND) != null) {
					te.setStack(player.getHeldItem(EnumHand.MAIN_HAND));
					player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
					player.openContainer.detectAndSendChanges();
				}
			}
			else {
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
		return true;
	}

}
