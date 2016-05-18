package p455w0rd.p455w0rdsthings;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class CreativeTabP extends CreativeTabs {

	public CreativeTabP() {
		super(Globals.MODID);
	}

	@Override
	public Item getTabIconItem() {
		return ModItems.dankNullItem;
	}

}
