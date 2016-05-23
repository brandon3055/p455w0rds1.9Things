package p455w0rd.p455w0rdsthings.util;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import p455w0rd.p455w0rdsthings.handlers.PacketHandler;
import p455w0rd.p455w0rdsthings.items.ItemDankNull;
import p455w0rd.p455w0rdsthings.network.PacketSetSelectedItem;

public class ItemUtils {

	public static ItemStack getDankNull(InventoryPlayer playerInv) {
		EntityPlayer player = playerInv.player;
		ItemStack dankNullItem = null;
		// If held item (either hand) is a /dank/null, take precedence
		if (player.getHeldItemMainhand() != null) {
			if (player.getHeldItemMainhand().getItem() instanceof ItemDankNull) {
				dankNullItem = player.getHeldItemMainhand();
			}
			else {
				if (player.getHeldItemOffhand() != null) {
					if (player.getHeldItemOffhand().getItem() instanceof ItemDankNull) {
						dankNullItem = player.getHeldItemOffhand();
					}
				}
			}
		}
		else if (player.getHeldItemOffhand() != null) {
			if (player.getHeldItemOffhand().getItem() instanceof ItemDankNull) {
				dankNullItem = player.getHeldItem(EnumHand.OFF_HAND);
			}
			else {
				if (player.getHeldItemMainhand() != null) {
					if (player.getHeldItemMainhand().getItem() instanceof ItemDankNull) {
						dankNullItem = player.getHeldItemMainhand();
					}
				}
			}
		}
		// search player's inventory if not held
		if (dankNullItem == null) {
			int invSize = playerInv.getSizeInventory();
			if (invSize <= 0) {
				return null;
			}
			for (int i = 0; i < invSize; ++i) {
				ItemStack itemStack = playerInv.getStackInSlot(i);
				if (itemStack == null) {
					continue;
				}
				if (itemStack.getItem() instanceof ItemDankNull) {
					dankNullItem = itemStack;
					break;
				}
			}
		}
		return dankNullItem;
	}

	// Utilities referencing the /dank/null inventory by proxy via /dank/null ItemStack
	
	public static void reArrangeStacks(ItemStack itemStackIn) {
		NBTTagList itemList = getInventoryTagList(itemStackIn);
		if (itemList != null) {
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
				PacketHandler.INSTANCE.sendToServer(new PacketSetSelectedItem(newIndex));
			}
			else {
				newIndex = currentIndex + 1;
				PacketHandler.INSTANCE.sendToServer(new PacketSetSelectedItem(newIndex));
			}
			if (player != null) {
				displaySelectedMessage(itemStackIn, player, newIndex);
			}
		}
		return;
	}

	public static void displaySelectedMessage(ItemStack itemStackIn, EntityPlayer player, int index) {
		player.addChatComponentMessage(new TextComponentString(TextFormatting.BLUE + "" + TextFormatting.ITALIC + "" + getItemByIndex(itemStackIn, index).getDisplayName() + " Selected"));
	}

	public static void setPreviousSelectedStack(ItemStack itemStackIn, EntityPlayer player) {
		int currentIndex = getSelectedStackIndex(itemStackIn);
		int totalSize = getItemCount(itemStackIn);
		int maxIndex = totalSize - 1;
		int newIndex = 0;
		if (totalSize > 1) {
			if (currentIndex == 0) {
				newIndex = maxIndex;
				PacketHandler.INSTANCE.sendToServer(new PacketSetSelectedItem(newIndex));
			}
			else {
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

	public static NBTTagList getInventoryTagList(ItemStack itemStackIn) {
		if (itemStackIn.hasTagCompound() && itemStackIn.getTagCompound().hasKey("danknull-inventory")) {
			return itemStackIn.getTagCompound().getTagList("danknull-inventory", 10);
		}
		return null;
	}

	public static void decrSelectedStackSize(ItemStack itemStackIn, long amount) {
		long newStackSize = getSelectedStackSize(itemStackIn) - amount;
		NBTTagCompound nbtTC = getSelectedStack(itemStackIn).getTagCompound();
		if (newStackSize >= 1) {
			nbtTC.setLong("p455w0rd.StackSize", newStackSize);
		}
		else {
			NBTTagList tagList = itemStackIn.getTagCompound().getTagList("danknull-inventory", 10);
			int index = getSelectedStackIndex(itemStackIn);
			tagList.removeTag(index);
			reArrangeStacks(itemStackIn);
		}
	}
	
	public static long getSelectedStackSize(ItemStack itemStackIn) {
		ItemStack selectedStack = getSelectedStack(itemStackIn);
		if (selectedStack != null) {
			long selectedStackSize = selectedStack.getTagCompound().getLong("p455w0rd.StackSize");
			return selectedStackSize;
		}
		return 0;
	}

	public static ItemStack getSelectedStack(ItemStack itemStackIn) {
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
	
	// Utilities for manipulating /dank/null inventory item stacks directly
	
	public static void decrDankNullStackSize(ItemStack itemStackIn, long amount) {
		long newStackSize = getDankNullStackSize(itemStackIn) - amount;
		NBTTagCompound nbtTC = itemStackIn.getTagCompound();
		if (newStackSize >= 1) {
			nbtTC.setLong("p455w0rd.StackSize", newStackSize);
		}
		else {
			NBTTagList tagList = itemStackIn.getTagCompound().getTagList("danknull-inventory", 10);
			int index = getSelectedStackIndex(itemStackIn);
			tagList.removeTag(index);
			reArrangeStacks(itemStackIn);
		}
	}
	
	public static long getDankNullStackSize(ItemStack itemStackIn) {
		if (isDankNullStack(itemStackIn)) {
			return itemStackIn.getTagCompound().getLong("p455w0rd.StackSize");
		}
		return 0L;
	}
	
	public static boolean isDankNullStack(ItemStack itemStackIn) {
		return itemStackIn.hasTagCompound() && itemStackIn.getTagCompound().hasKey("p455w0rd.StackSize");
	}

	public static long getDankNullMaxStackSize(ItemStack itemStackIn) {
		int level = itemStackIn.getItemDamage() + 1;
		if (level == 6) {
			return Long.MAX_VALUE;
		}
		return level * 128;
	}
	
	// other misc item utilities

	public static boolean areItemTagsEqual(ItemStack is1, ItemStack itemStackIn) {
		ItemStack newStack = is1.copy();
		if (newStack.hasTagCompound()) {
			if (newStack.getTagCompound().hasKey("p455w0rd.StackSize")) {
				newStack.getTagCompound().removeTag("p455w0rd.StackSize");
			}
			if (newStack.getTagCompound().hasNoTags()) {
				newStack.setTagCompound(null);
			}
		}
		return ItemStack.areItemStackTagsEqual(newStack, itemStackIn);
	}
	
	public static boolean areItemsEqual(ItemStack is1, ItemStack itemStackIn) {
		return (is1.getItem() == itemStackIn.getItem() && is1.getItemDamage() == itemStackIn.getItemDamage() && areItemTagsEqual(is1, itemStackIn));
	}

}
