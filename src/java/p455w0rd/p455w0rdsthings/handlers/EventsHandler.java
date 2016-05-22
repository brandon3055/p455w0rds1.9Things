package p455w0rd.p455w0rdsthings.handlers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import p455w0rd.p455w0rdsthings.Globals;
import p455w0rd.p455w0rdsthings.items.ItemDankNull;
import p455w0rd.p455w0rdsthings.util.CapeUtils;

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
		}
		else if (player.getHeldItem(EnumHand.OFF_HAND) != null) {
			if (player.getHeldItem(EnumHand.OFF_HAND).getItem() instanceof ItemDankNull) {
				dankNullItem = player.getHeldItem(EnumHand.OFF_HAND);
			}
		}
		if (dankNullItem == null) {
			return;
		}

		if (event.getDwheel() == 0 && event.isButtonstate()) {
			int currentIndex = ItemDankNull.getSelectedStackIndex(dankNullItem);
			int totalSize = ItemDankNull.getItemCount(dankNullItem);
			if (currentIndex == -1 || totalSize <= 1) {
				return;
			}
			//System.out.println("Button: " + event.getButton() + " DWheel: " + event.getDwheel() + " ButonState:" + event.isButtonstate());
			if (event.getButton() == 4) {
				ItemDankNull.setNextSelectedStack(dankNullItem, player);
				event.setCanceled(true);
			}
			else if (event.getButton() == 3) {
				ItemDankNull.setPreviousSelectedStack(dankNullItem, player);
				event.setCanceled(true);
			}
		}
		else {
			if (player.isSneaking()) {
				int currentIndex = ItemDankNull.getSelectedStackIndex(dankNullItem);
				int totalSize = ItemDankNull.getItemCount(dankNullItem);
				if (currentIndex == -1 || totalSize <= 1) {
					return;
				}
				int scrollForward = event.getDwheel();
				if (scrollForward < 0) {
					ItemDankNull.setNextSelectedStack(dankNullItem, player);
					event.setCanceled(true);
				}
				else if (scrollForward > 0) {
					ItemDankNull.setPreviousSelectedStack(dankNullItem, player);
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

	/*
	 * @SubscribeEvent public void renderHandEvent(RenderHandEvent e) {
	 * RenderGlobal test = e.getContext();
	 * 
	 * if (test != null) { //System.out.println(test.toString()); }
	 * net.minecraftforge.client.event.RenderHandEvent event =
	 * ForgeHooksClient.renderFirstPersonHand(e.getContext(),
	 * e.getPartialTicks(), e.getRenderPass()); }
	 */
}
