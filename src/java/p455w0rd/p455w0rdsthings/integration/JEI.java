package p455w0rd.p455w0rdsthings.integration;

import javax.annotation.Nonnull;

import mezz.jei.api.*;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.oredict.OreDictionary;
import p455w0rd.p455w0rdsthings.ModItems;

@JEIPlugin
@Optional.Interface(iface = "mezz.jei.api.IItemBlacklist", modid = "JEI", striprefs = true)
public class JEI implements IModPlugin {
	
	@Optional.Method(modid = "JEI")
	@Override
	public void register(@Nonnull IModRegistry registry) {
		IJeiHelpers helpers = registry.getJeiHelpers();
		IItemBlacklist blacklist = helpers.getItemBlacklist();
		blacklist.addItemToBlacklist(new ItemStack(ModItems.dankNullHolder, 1, OreDictionary.WILDCARD_VALUE));
	}

	@Optional.Method(modid = "JEI")
	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {

	}
}
