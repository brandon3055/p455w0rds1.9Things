package p455w0rd.p455w0rdsthings.recipes;

import net.minecraft.item.ItemStack;
import p455w0rd.p455w0rdsthings.api.ICompressorRecipe;

public class CompressorRecipe implements ICompressorRecipe {
	
	ItemStack inputStack;
	ItemStack outputStack;
	ItemStack secondOutputStack;
	int requiredEnergy;
	float secondOutputChance;
	
	public CompressorRecipe(ItemStack inputStack, ItemStack outputStack, int requiredEnergy) {
		this(inputStack, outputStack, null, requiredEnergy, 0);
	}
	
	public CompressorRecipe(ItemStack inputStack, ItemStack outputStack, ItemStack secondOutputStack, int requiredEnergy, float secondOutputChance) {
		this.inputStack = inputStack;
		this.outputStack = outputStack;
		this.requiredEnergy = requiredEnergy;
		if (secondOutputStack != null && secondOutputChance > 0) {
			this.secondOutputStack = secondOutputStack;
			this.secondOutputChance = secondOutputChance;
		}
	}
	
	public boolean isValid() {
		return (inputStack != null && outputStack != null && requiredEnergy > 0);
	}

	@Override
	public int getEnergyRequired() {
		return requiredEnergy;
	}

	@Override
	public float getSecondOutputChance() {
		return secondOutputChance;
	}

	@Override
	public ItemStack getInput() {
		return inputStack;
	}

	@Override
	public ItemStack getOutput() {
		return outputStack;
	}

	@Override
	public ItemStack getSecondOutput() {
		return secondOutputStack;
	}

	@Override
	public boolean hasSecondOutput() {
		return getSecondOutput() != null && getSecondOutputChance() > 0;
	}

}
