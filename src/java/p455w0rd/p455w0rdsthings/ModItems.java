package p455w0rd.p455w0rdsthings;

import net.minecraft.block.Block;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import p455w0rd.p455w0rdsthings.items.ItemBucketXpJuice;
import p455w0rd.p455w0rdsthings.items.ItemCarbon;
import p455w0rd.p455w0rdsthings.items.ItemDankNull;
import p455w0rd.p455w0rdsthings.items.ItemDankNullHolder;
import p455w0rd.p455w0rdsthings.items.ItemPShield;

public class ModItems {
	
	public static ItemDankNull dankNullItem;
	public static ItemDankNullHolder dankNullHolder;
	public static ItemPShield pShieldItem;
	//public static ItemWirelessReceiver wirelessReceiver;
	public static ItemCarbon carbonItem;
	public static ItemBucketXpJuice xpJuiceBucket;
	
	public static void init() {
		dankNullHolder = new ItemDankNullHolder();
		dankNullItem = new ItemDankNull();
		pShieldItem = new ItemPShield();
		//wirelessReceiver = new ItemWirelessReceiver();
		carbonItem = new ItemCarbon();
		xpJuiceBucket = new ItemBucketXpJuice((Block) ModBlocks.xpJuiceBlock);
	}
	
	@SideOnly(Side.CLIENT)
	public static void preInitModels() {
		dankNullHolder.initModel();
		dankNullItem.initModel();
		pShieldItem.initModel();
		//wirelessReceiver.initModel();
		carbonItem.initModel();
		xpJuiceBucket.initModel();
	}
	
}
