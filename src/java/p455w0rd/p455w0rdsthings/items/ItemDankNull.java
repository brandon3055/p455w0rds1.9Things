package p455w0rd.p455w0rdsthings.items;

import java.util.List;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import p455w0rd.p455w0rdsthings.Globals;
import p455w0rd.p455w0rdsthings.client.render.DankNullRenderer;
import p455w0rd.p455w0rdsthings.client.render.PModelRegistryHelper;
import p455w0rd.p455w0rdsthings.handlers.GuiHandler;
import p455w0rd.p455w0rdsthings.handlers.PacketHandler;
import p455w0rd.p455w0rdsthings.network.PacketSetSelectedItem;
import p455w0rd.p455w0rdsthings.proxy.CommonProxy;

public class ItemDankNull extends Item {

	private final String name = "dankNull";

	public ItemDankNull() {
		setRegistryName(this.name);
		setUnlocalizedName(this.name);
		GameRegistry.register(this);
		setMaxStackSize(1);
		setMaxDamage(0);
		setCreativeTab(CreativeTabs.tabMisc);
		setCreativeTab(CommonProxy.creativeTab);
	}
	
	@SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack)
    {
        return stack.getItemDamage() >= 5;
    }

	@SideOnly(Side.CLIENT)
	public void initModel() {
		for (int i = 0; i < 6; i++) {
			PModelRegistryHelper.registerDankNullRenderer(this, new DankNullRenderer(), i);
		}
	}

	public String getItemStackDisplayName(ItemStack stack) {
		return (I18n.translateToLocal(this.getUnlocalizedNameInefficiently(stack) + "v" + this.getDamage(stack) + ".name")).trim();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
		if (playerIn.isSneaking()) {
			GuiHandler.launchGui(Globals.GUINUM_DANKNULL, playerIn, worldIn, (int) playerIn.posX, (int) playerIn.posY, (int) playerIn.posZ);
		}
		return new ActionResult(EnumActionResult.FAIL, itemStackIn);
	}

	@SideOnly(Side.CLIENT)
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
		for (int i = 0; i < 6; i++) {
			subItems.add(new ItemStack(itemIn, 1, i));
		}
	}

	@Override
	public boolean isDamaged(final ItemStack stack) {
		return false;
	}

	@Override
	public boolean isRepairable() {
		return false;
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return false;
	}

	@Override
	public boolean isDamageable() {
		return false;
	}

	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (worldIn.isRemote) {
			return EnumActionResult.FAIL;
		}
		ItemStack selectedStack = getSelectedStack(stack);

		if (playerIn.isSneaking()) {
			if (selectedStack == null) {
				if (getItemCount(stack) > 0) {
					setSelectedStackIndex(stack, 0);
				}
			}
			GuiHandler.launchGui(Globals.GUINUM_DANKNULL, playerIn, worldIn, (int) playerIn.posX, (int) playerIn.posY, (int) playerIn.posZ);
		}
		else {
			if (selectedStack == null) {
				return EnumActionResult.FAIL;
			}
			if (!(selectedStack.getItem() instanceof ItemBlock)) {
				return EnumActionResult.FAIL;
			}
			((ItemBlock) selectedStack.getItem()).getBlock();
			IBlockState iblockstate = worldIn.getBlockState(pos);
			Block block = iblockstate.getBlock();

			if (block.isReplaceable(worldIn, pos) && block == Blocks.snow_layer) {
				facing = EnumFacing.UP;
			}
			else if (!block.isReplaceable(worldIn, pos)) {
				pos = pos.offset(facing);
			}

			if (getSelectedStackSize(stack) > 0 && playerIn.canPlayerEdit(pos, facing, stack)) {
				if (facing == EnumFacing.UP) {
					pos = pos.offset(EnumFacing.DOWN);
				}
				else if (facing == EnumFacing.DOWN) {
					pos = pos.offset(EnumFacing.UP);
				}
				else if (facing == EnumFacing.EAST) {
					pos = pos.offset(EnumFacing.WEST);
				}
				else if (facing == EnumFacing.WEST) {
					pos = pos.offset(EnumFacing.EAST);
				}
				else if (facing == EnumFacing.NORTH) {
					pos = pos.offset(EnumFacing.SOUTH);
				}
				else if (facing == EnumFacing.SOUTH) {
					pos = pos.offset(EnumFacing.NORTH);
				}

				selectedStack.onItemUse(playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
				if (!playerIn.capabilities.isCreativeMode) {
					decrSelectedStackSize(stack);
				}
			}
		}
		return EnumActionResult.FAIL;
	}

	public static void reArrangeStacks(ItemStack itemStackIn) {
		NBTTagList itemList = getInventoryTagList(itemStackIn);
		if (itemList != null) {
			/*
			 * int listSize = itemList.tagCount(); for (int i = 0; i < listSize;
			 * i++) { NBTTagCompound currentTag = itemList.getCompoundTagAt(i);
			 * if (currentTag == null) { return; } ItemStack currentStack =
			 * ItemStack.loadItemStackFromNBT(currentTag); if (currentStack ==
			 * null) { for (int j = 0; j < listSize; j++) { if (j <= i) {
			 * continue; } if (itemList.getCompoundTagAt(j) == null) {
			 * currentStack.getTagCompound().setInteger("Slot", i); } } } }
			 */
			for (int i = 0; i < itemList.tagCount(); i++) {
				itemList.getCompoundTagAt(i).setInteger("Slot", i);
			}
		}
	}

	@SuppressWarnings("unused")
	private List<ItemStack> getInventoryList(ItemStack itemStackIn) {
		if (itemStackIn.hasTagCompound() && itemStackIn.getTagCompound().hasKey("danknull-inventory")) {
			List<ItemStack> inventoryItemStacks = Lists.<ItemStack> newArrayList();
			NBTTagCompound nbtTC = itemStackIn.getTagCompound();
			NBTTagList itemList = nbtTC.getTagList("danknull-inventory", 10);
			if (itemList != null) {
				for (int i = 0; i < itemList.tagCount(); i++) {
					inventoryItemStacks.add(ItemStack.loadItemStackFromNBT(itemList.getCompoundTagAt(i)));
				}
				return inventoryItemStacks;
			}
		}
		return null;
	}

	public static int getSelectedStackIndex(ItemStack itemStackIn) {
		if (!itemStackIn.hasTagCompound()) {
			itemStackIn.setTagCompound(new NBTTagCompound());
		}
		if (!itemStackIn.getTagCompound().hasKey("selectedIndex")) {
			itemStackIn.getTagCompound().setInteger("selectedIndex", 0);
		}
		return itemStackIn.getTagCompound().getInteger("selectedIndex");
	}

	public static ItemStack getItemByIndex(ItemStack itemStackIn, int index) {
		if (itemStackIn.hasTagCompound() && itemStackIn.getTagCompound().hasKey("danknull-inventory")) {
			NBTTagCompound nbtTC = itemStackIn.getTagCompound();
			NBTTagList itemList = nbtTC.getTagList("danknull-inventory", 10);
			if (itemList.getCompoundTagAt(index) != null) {
				return ItemStack.loadItemStackFromNBT(itemList.getCompoundTagAt(index));
			}
		}
		return null;
	}

	public static void setSelectedStackIndex(ItemStack itemStackIn, int index) {
		if (!itemStackIn.hasTagCompound()) {
			itemStackIn.setTagCompound(new NBTTagCompound());
		}
		itemStackIn.getTagCompound().setInteger("selectedIndex", index);
	}

	public static void setNextSelectedStack(ItemStack itemStackIn) {
		setNextSelectedStack(itemStackIn, null);
	}

	public static void setNextSelectedStack(ItemStack itemStackIn, EntityPlayer player) {
		int currentIndex = getSelectedStackIndex(itemStackIn);
		int totalSize = getItemCount(itemStackIn);
		int maxIndex = totalSize - 1;
		int newIndex = 0;
		if (totalSize > 1) {
			if (currentIndex == maxIndex) {
				//setSelectedStackIndex(itemStackIn, 0);
				PacketHandler.INSTANCE.sendToServer(new PacketSetSelectedItem(newIndex));
			}
			else {
				//setSelectedStackIndex(itemStackIn, currentIndex + 1);
				newIndex = currentIndex + 1;
				PacketHandler.INSTANCE.sendToServer(new PacketSetSelectedItem(newIndex));
			}
			if (player != null) {
				displaySelectedMessage(itemStackIn, player, newIndex);
			}
		}
		return;
	}

	private static void displaySelectedMessage(ItemStack itemStackIn, EntityPlayer player, int index) {
		player.addChatComponentMessage(new TextComponentString(TextFormatting.BLUE + "" + TextFormatting.ITALIC + "" + getItemByIndex(itemStackIn, index).getDisplayName() + " Selected"));
	}

	public static void setPreviousSelectedStack(ItemStack itemStackIn, EntityPlayer player) {
		int currentIndex = getSelectedStackIndex(itemStackIn);
		int totalSize = getItemCount(itemStackIn);
		int maxIndex = totalSize - 1;
		int newIndex = 0;
		if (totalSize > 1) {
			if (currentIndex == 0) {
				//setSelectedStackIndex(itemStackIn, maxIndex);
				newIndex = maxIndex;
				PacketHandler.INSTANCE.sendToServer(new PacketSetSelectedItem(newIndex));
			}
			else {
				//setSelectedStackIndex(itemStackIn, currentIndex - 1);
				newIndex = currentIndex - 1;
				PacketHandler.INSTANCE.sendToServer(new PacketSetSelectedItem(newIndex));
			}
			if (player != null) {
				displaySelectedMessage(itemStackIn, player, newIndex);
			}
		}
		return;
	}

	public static int getItemCount(ItemStack itemStackIn) {
		if (getInventoryTagList(itemStackIn) == null) {
			return 0;
		}
		return getInventoryTagList(itemStackIn).tagCount();
	}

	private static NBTTagList getInventoryTagList(ItemStack itemStackIn) {
		if (itemStackIn.hasTagCompound() && itemStackIn.getTagCompound().hasKey("danknull-inventory")) {
			return itemStackIn.getTagCompound().getTagList("danknull-inventory", 10);
		}
		return null;
	}

	private static void decrSelectedStackSize(ItemStack itemStackIn) {
		long newStackSize = getSelectedStackSize(itemStackIn) - 1;
		NBTTagCompound nbtTC = getSelectedStack(itemStackIn).getTagCompound();
		if (newStackSize >= 1) {
			nbtTC.setLong("p455w0rd.StackSize", newStackSize);
		}
		else {
			NBTTagList tagList = itemStackIn.getTagCompound().getTagList("danknull-inventory", 10);
			int index = getSelectedStackIndex(itemStackIn);
			tagList.removeTag(index);
			//tagList.set(index, new NBTTagCompound());
			reArrangeStacks(itemStackIn);
		}
	}

	private static long getSelectedStackSize(ItemStack itemStackIn) {
		ItemStack selectedStack = getSelectedStack(itemStackIn);
		if (selectedStack != null) {
			long selectedStackSize = selectedStack.getTagCompound().getLong("p455w0rd.StackSize");
			return selectedStackSize;
		}
		return 0;
	}

	private static ItemStack getSelectedStack(ItemStack itemStackIn) {
		if (itemStackIn.hasTagCompound() && itemStackIn.getTagCompound().hasKey("danknull-inventory")) {
			NBTTagCompound nbtTC = itemStackIn.getTagCompound();
			if (!nbtTC.hasKey("selectedIndex")) {
				nbtTC.setInteger("selectedIndex", 0);
			}
			int selectedIndex = nbtTC.getInteger("selectedIndex");
			NBTTagList itemList = nbtTC.getTagList("danknull-inventory", 10);
			if (itemList != null) {
				ItemStack selectedStack = ItemStack.loadItemStackFromNBT(itemList.getCompoundTagAt(selectedIndex));
				if (selectedStack != null) {
					return selectedStack;
				}
			}
		}
		return null;
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {

	}

}
