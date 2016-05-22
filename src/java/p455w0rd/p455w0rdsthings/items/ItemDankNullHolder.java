package p455w0rd.p455w0rdsthings.items;

import java.util.List;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import p455w0rd.p455w0rdsthings.Globals;

public class ItemDankNullHolder extends Item {
	private final String name = "dankNullHolder";
	private final String modelBaseName = "dankNull";

	public ItemDankNullHolder() {
		setRegistryName(this.name);
		setUnlocalizedName(this.name);
		GameRegistry.register(this);
		setMaxStackSize(1);
	}
	
	@SideOnly(Side.CLIENT)
	public void initModel() {
		for (int i = 0; i < 6; i++) {
			ModelLoader.setCustomModelResourceLocation(this, i, new ModelResourceLocation(new ResourceLocation(Globals.MODID, modelBaseName) + "" + i, "inventory"));
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
		for (int i = 0; i < 6; i++) {
			subItems.add(new ItemStack(itemIn, 1, i));
		}
	}
}
