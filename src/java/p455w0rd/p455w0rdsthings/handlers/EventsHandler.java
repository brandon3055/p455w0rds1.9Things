package p455w0rd.p455w0rdsthings.handlers;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import p455w0rd.p455w0rdsthings.Globals;
import p455w0rd.p455w0rdsthings.ModBlocks;
import p455w0rd.p455w0rdsthings.ModItems;
import p455w0rd.p455w0rdsthings.items.ItemDankNull;
import p455w0rd.p455w0rdsthings.util.CapeUtils;
import p455w0rd.p455w0rdsthings.util.ItemUtils;

public class EventsHandler {

	@SubscribeEvent
	public void attackEvent(LivingAttackEvent e) {
		float damage = e.getAmount();
		ItemStack activeItemStack;
		EntityPlayer player;
		if (!(e.getEntityLiving() instanceof EntityPlayer)) {
			return;
		}
		player = (EntityPlayer) e.getEntityLiving();
		if (player.getActiveItemStack() == null) {
			return;
		}
		activeItemStack = player.getActiveItemStack();

		if (damage > 0.0F && activeItemStack != null && activeItemStack.getItem() instanceof ItemShield) {
			int i = 1 + MathHelper.floor_float(damage);
			activeItemStack.damageItem(i, player);

			if (activeItemStack.stackSize <= 0) {
				EnumHand enumhand = player.getActiveHand();
				net.minecraftforge.event.ForgeEventFactory.onPlayerDestroyItem(player, activeItemStack, enumhand);

				if (enumhand == EnumHand.MAIN_HAND) {
					player.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, (ItemStack) null);
				}
				else {
					player.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, (ItemStack) null);
				}

				activeItemStack = null;
				if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
					player.playSound(SoundEvents.item_shield_break, 0.8F, 0.8F + player.worldObj.rand.nextFloat() * 0.4F);
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onBucketFill(FillBucketEvent event) {
		IBlockState state = event.getWorld().getBlockState(event.getTarget().getBlockPos());
	    if (state.getBlock() == ModBlocks.xpJuiceBlock) {
	    	if (((IFluidBlock) state.getBlock()).getFilledPercentage(event.getWorld(), event.getTarget().getBlockPos()) == 1) {
	    		event.setFilledBucket(new ItemStack(ModItems.xpJuiceBucket));
	    		event.getWorld().setBlockState(event.getTarget().getBlockPos(), Blocks.air.getDefaultState(), 11);
	    		event.setResult(Result.ALLOW);
	    		return;
	    	}
	    	event.setResult(Result.DENY);
	    }
	    
	}
	
	@SubscribeEvent
	public void onFogRender(EntityViewRenderEvent.FogColors e) {
		IBlockState iblockstate = ActiveRenderInfo.getBlockStateAtEntityViewpoint(e.getEntity().getEntityWorld(), e.getEntity(), (float) e.getRenderPartialTicks());
		if (iblockstate.getBlock() == ModBlocks.xpJuiceBlock) {
			float f12 = 0.0F;
			EntityLivingBase entity = (EntityLivingBase) e.getEntity();

            if (entity instanceof EntityLivingBase)
            {
                f12 = (float)EnchantmentHelper.getRespirationModifier((EntityLivingBase)entity) * 0.2F;

                if (((EntityLivingBase)entity).isPotionActive(MobEffects.waterBreathing))
                {
                    f12 = f12 * 0.3F + 0.6F;
                }
            }
            
            e.setRed(0.02F + 0);
            e.setBlue(0.02F + f12);
            e.setGreen(0.02F + 0);
            e.setResult(Result.DENY);
            return;
		}
	}
	
	@SubscribeEvent
	public void onItemPickUp(EntityItemPickupEvent e) {
		final EntityPlayer player = e.getEntityPlayer();
		final ItemStack entityStack = e.getItem().getEntityItem();
		if (entityStack == null || player == null) {
			return;
		}

		for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
			final ItemStack stack = player.inventory.getStackInSlot(i);

			if (stack != null && stack.getItem() instanceof ItemDankNull) {
				if (ItemUtils.isFiltered(stack, entityStack) != null) {
					if (ItemUtils.addFilteredStackToDankNull(stack, entityStack)) {
						entityStack.stackSize = 0;
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void entityJoinWorld(EntityJoinWorldEvent event) {
		if (event.getEntity() instanceof AbstractClientPlayer && CapeUtils.doesPlayerHaveCape((AbstractClientPlayer) event.getEntity())) {
			CapeUtils.queuePlayerCapeReplacement((AbstractClientPlayer) event.getEntity());
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onMouseEvent(MouseEvent event) {
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		ItemStack dankNullItem = null;
		// in case of holding 2 /dank/nulls, main hand takes precedence
		if (player.getHeldItem(EnumHand.MAIN_HAND) != null) {
			if (player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemDankNull) {
				dankNullItem = player.getHeldItem(EnumHand.MAIN_HAND);
			}
			else if (player.getHeldItem(EnumHand.OFF_HAND) != null && player.getHeldItem(EnumHand.OFF_HAND).getItem() instanceof ItemDankNull) {
				dankNullItem = player.getHeldItem(EnumHand.OFF_HAND);
			}
		}
		else if (player.getHeldItem(EnumHand.OFF_HAND) != null) {
			if (player.getHeldItem(EnumHand.OFF_HAND).getItem() instanceof ItemDankNull) {
				dankNullItem = player.getHeldItem(EnumHand.OFF_HAND);
			}
			else if (player.getHeldItem(EnumHand.MAIN_HAND) != null && player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemDankNull) {
				dankNullItem = player.getHeldItem(EnumHand.MAIN_HAND);
			}
		}
		if (dankNullItem == null) {
			return;
		}

		if (event.getDwheel() == 0 && event.isButtonstate()) {
			int currentIndex = ItemUtils.getSelectedStackIndex(dankNullItem);
			int totalSize = ItemUtils.getItemCount(dankNullItem);
			if (currentIndex == -1 || totalSize <= 1) {
				return;
			}
			//System.out.println("Button: " + event.getButton() + " DWheel: " + event.getDwheel() + " ButonState:" + event.isButtonstate());
			if (event.getButton() == 3) {
				ItemUtils.setNextSelectedStack(dankNullItem, player);
				event.setCanceled(true);
			}
			else if (event.getButton() == 4) {
				ItemUtils.setPreviousSelectedStack(dankNullItem, player);
				event.setCanceled(true);
			}
		}
		else {
			if (player.isSneaking()) {
				int currentIndex = ItemUtils.getSelectedStackIndex(dankNullItem);
				int totalSize = ItemUtils.getItemCount(dankNullItem);
				if (currentIndex == -1 || totalSize <= 1) {
					return;
				}
				int scrollForward = event.getDwheel();
				if (scrollForward < 0) {
					ItemUtils.setNextSelectedStack(dankNullItem, player);
					event.setCanceled(true);
				}
				else if (scrollForward > 0) {
					ItemUtils.setPreviousSelectedStack(dankNullItem, player);
					event.setCanceled(true);
				}
			}
		}
		return;
	}

	@SubscribeEvent
	public void renderOverlayEvent(RenderGameOverlayEvent e) {
		if (Globals.GUI_DANKNULL_ISOPEN) {
			if (e.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
				e.setCanceled(true);
			}
		}
	}
}
