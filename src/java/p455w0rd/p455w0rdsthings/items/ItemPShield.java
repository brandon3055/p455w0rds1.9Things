package p455w0rd.p455w0rdsthings.items;

import java.util.List;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import p455w0rd.p455w0rdsthings.Globals;
import p455w0rd.p455w0rdsthings.client.model.ModelPShield;
import p455w0rd.p455w0rdsthings.handlers.LocaleHandler;
import p455w0rd.p455w0rdsthings.proxy.CommonProxy;

public class ItemPShield extends Item {
	private final String name = "p455w0rdshield";
	private ModelPShield modelShield = new ModelPShield();

	public ItemPShield() {
		setRegistryName(this.name);
		setUnlocalizedName(this.name);
		GameRegistry.register(this);
		this.setMaxStackSize(1);
		this.setMaxDamage(999);
		this.setCreativeTab(CreativeTabs.CREATIVE_TAB_ARRAY[8]);
		this.setCreativeTab(CommonProxy.creativeTab);
		addPropertyOverride(new ResourceLocation(Globals.MODID, "blocking"), new IItemPropertyGetter() {
			@Override
			@SideOnly(Side.CLIENT)
			public float apply(ItemStack stack, World worldIn, EntityLivingBase entityIn) {
				return entityIn != null && entityIn.isHandActive() && entityIn.getActiveItemStack() == stack ? 1.0F : 0.0F;
			}
		});
	}

	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(this.getRegistryName(), "inventory"));
		//ModelLoader.setCustomModelResourceLocation(this, 1, new ModelResourceLocation(this.getRegistryName() + "_blocking", "inventory"));
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
		playerIn.setActiveHand(hand);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		return LocaleHandler.p455w0rdshield.getLocal();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		tooltip.add("Tooltip");
	}

	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
		return repair.getItem() == net.minecraft.init.Items.DIAMOND ? true : super.getIsRepairable(toRepair, repair);
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		GlStateManager.pushMatrix();
		GlStateManager.scale(1.0F, -1.0F, -1.0F);
		this.modelShield.render();
		GlStateManager.popMatrix();
		return EnumActionResult.SUCCESS;
		//return super.onItemUse(stack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.BLOCK;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 72000;
	}

}