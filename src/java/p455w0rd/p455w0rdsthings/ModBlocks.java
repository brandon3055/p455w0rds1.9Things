package p455w0rd.p455w0rdsthings;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import p455w0rd.p455w0rdsthings.blocks.BlockDankNull;
import p455w0rd.p455w0rdsthings.blocks.PartTerminal;

public class ModBlocks {
	
	public static BlockDankNull dankNullBlock;
	public static PartTerminal partTerminal;
	
	public static void init() {
		dankNullBlock = new BlockDankNull();
		partTerminal = new PartTerminal();
	}
	
	@SideOnly(Side.CLIENT)
	public static void initModels() {
		dankNullBlock.initModel();
		partTerminal.initModel();
	}

}
