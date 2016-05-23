package p455w0rd.p455w0rdsthings.items;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import p455w0rd.p455w0rdsthings.Globals;
import p455w0rd.p455w0rdsthings.client.render.DankNullRenderer;
import p455w0rd.p455w0rdsthings.client.render.PModelRegistryHelper;
import p455w0rd.p455w0rdsthings.handlers.GuiHandler;
import p455w0rd.p455w0rdsthings.proxy.CommonProxy;
import p455w0rd.p455w0rdsthings.util.ItemUtils;

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
	public boolean hasEffect(ItemStack stack) {
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
		return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
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
		ItemStack selectedStack = ItemUtils.getSelectedStack(stack);

		if (playerIn.isSneaking()) {
			if (selectedStack == null) {
				if (ItemUtils.getItemCount(stack) > 0) {
					ItemUtils.setSelectedStackIndex(stack, 0);
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

			if (ItemUtils.getSelectedStackSize(stack) > 0 && playerIn.canPlayerEdit(pos, facing, stack)) {
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

				EnumActionResult result = selectedStack.onItemUse(playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
				if (!playerIn.capabilities.isCreativeMode) {
					ItemUtils.decrSelectedStackSize(stack, 1);
				}
				return result;
			}
		}
		return EnumActionResult.FAIL;
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {

	}

}
