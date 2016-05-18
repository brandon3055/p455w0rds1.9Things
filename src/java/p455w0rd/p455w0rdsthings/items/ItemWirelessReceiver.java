package p455w0rd.p455w0rdsthings.items;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import p455w0rd.p455w0rdsthings.proxy.CommonProxy;

public class ItemWirelessReceiver extends Item {

	private final String name = "wireless_receiver";

	public ItemWirelessReceiver() {
		super();
		setRegistryName(this.name);
		setUnlocalizedName(this.name);
		GameRegistry.register(this);
		this.setMaxStackSize(1);
		this.setCreativeTab(CreativeTabs.tabCombat);
		this.setCreativeTab(CommonProxy.creativeTab);
	}

	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(this.getRegistryName(), "inventory"));
	}

}
