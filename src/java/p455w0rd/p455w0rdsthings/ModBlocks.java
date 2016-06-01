package p455w0rd.p455w0rdsthings;

import net.minecraft.block.material.Material;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import p455w0rd.p455w0rdsthings.blocks.BlockBattery;
import p455w0rd.p455w0rdsthings.blocks.BlockCompressor;
import p455w0rd.p455w0rdsthings.blocks.BlockDankNull;
import p455w0rd.p455w0rdsthings.blocks.BlockFurnace;
import p455w0rd.p455w0rdsthings.blocks.fluid.BlockXpJuice;

public class ModBlocks {
	
	public static BlockDankNull dankNullBlock;
	public static BlockCompressor compressorBlock;
	public static BlockBattery batteryBlock;
	public static BlockFurnace furnaceBlock;
	
	public static BlockXpJuice xpJuiceBlock;
	
	public static void init() {
		dankNullBlock = new BlockDankNull();
		compressorBlock = new BlockCompressor();
		batteryBlock = new BlockBattery();
		furnaceBlock = new BlockFurnace();
		xpJuiceBlock = new BlockXpJuice(ModFluids.xpJuice, Material.water);
	}
	
	@SideOnly(Side.CLIENT)
	public static void initModels() {
		dankNullBlock.initModel();
		compressorBlock.initModel();
		batteryBlock.initModel();
		furnaceBlock.initModel();
		xpJuiceBlock.initModel();
	}

}
