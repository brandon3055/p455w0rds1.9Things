package p455w0rd.p455w0rdsthings.util;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockUtils {

	public static TileEntity getTE(World worldIn, BlockPos pos) {
		return worldIn.getTileEntity(pos);
	}

	public static void wrenchBlock(World worldIn, BlockPos pos, IBlockState state) {
		if (!worldIn.isRemote) {
			TileEntity te = getTE(worldIn, pos);
			ItemStack blockStack = new ItemStack(Item.getItemFromBlock(worldIn.getBlockState(pos).getBlock()), 1);

			if (te.getBlockMetadata() > 0) {
				blockStack.setItemDamage(te.getBlockMetadata());
			}
			if (te.getTileData() != null) {
				blockStack.setTagCompound(te.getTileData());
			}
			EntityItem entityItem = new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), blockStack);
			if (te != null) {
				worldIn.spawnEntityInWorld(entityItem);
				dropTEItems(worldIn, pos);
				worldIn.setBlockToAir(pos);
			}
		}
	}

	public static void dropTEItems(World world, BlockPos pos) {
		Random rand = new Random();

		TileEntity tileEntity = world.getTileEntity(pos);
		if (!(tileEntity instanceof IInventory)) {
			return;
		}
		IInventory inventory = (IInventory) tileEntity;

		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			ItemStack item = inventory.getStackInSlot(i);

			if (item != null && item.stackSize > 0) {
				float rx = rand.nextFloat() * 0.8F + 0.1F;
				float ry = rand.nextFloat() * 0.8F + 0.1F;
				float rz = rand.nextFloat() * 0.8F + 0.1F;

				EntityItem entityItem = new EntityItem(world, pos.getX() + rx, pos.getY() + ry, pos.getZ() + rz, new ItemStack(item.getItem(), item.stackSize, item.getItemDamage()));

				if (item.hasTagCompound()) {
					entityItem.getEntityItem().setTagCompound((NBTTagCompound) item.getTagCompound().copy());
				}

				float factor = 0.05F;
				entityItem.motionX = rand.nextGaussian() * factor;
				entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
				entityItem.motionZ = rand.nextGaussian() * factor;
				world.spawnEntityInWorld(entityItem);
				item.stackSize = 0;
			}
		}
	}

	public static TileEntity getAdjacentTileEntity(TileEntity te, int side) {
		World w = te.getWorld();
		EnumFacing s = EnumFacing.values()[side];
		BlockPos pos = new BlockPos(te.getPos().offset(s));
		return w.getTileEntity(pos);
	}

	public static int determineAdjacentSide(TileEntity te, int x, int y, int z) {
		return x > te.getPos().getX() ? 5 : z < te.getPos().getZ() ? 2 : z > te.getPos().getZ() ? 3 : y < te.getPos().getY() ? 0 : y > te.getPos().getY() ? 1 : 4;
	}

}
