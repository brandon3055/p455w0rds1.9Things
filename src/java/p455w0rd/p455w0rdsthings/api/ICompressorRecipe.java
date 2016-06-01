package p455w0rd.p455w0rdsthings.api;

import net.minecraft.item.ItemStack;

public interface ICompressorRecipe {
	
	boolean hasSecondOutput();
	
	int getEnergyRequired();
	
	float getSecondOutputChance();
	
	ItemStack getInput();
	
	ItemStack getOutput();

	ItemStack getSecondOutput();
	
}
