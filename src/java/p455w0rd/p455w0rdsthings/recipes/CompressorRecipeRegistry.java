package p455w0rd.p455w0rdsthings.recipes;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import p455w0rd.p455w0rdsthings.api.ICompressorRecipe;

public class CompressorRecipeRegistry {

	public static final CompressorRecipeRegistry INSTANCE = new CompressorRecipeRegistry();
	
	private final List<ICompressorRecipe> compressorRecipes = new ArrayList<ICompressorRecipe>();
	
	public void registerRecipe(ICompressorRecipe recipe) {
		boolean isRegistered = compressorRecipes.contains(recipe) && !getInputList().contains(recipe.getInput());
		if (!isRegistered) {
			compressorRecipes.add(recipe);
		}
	}
	
	public List<ICompressorRecipe> getRecipeList() {
		return compressorRecipes;
	}
	
	public ICompressorRecipe getRecipeByIndex(int index) {
		return compressorRecipes.get(index);
	}
	
	public List<ItemStack> getInputList() {
		if (getRecipeList() != null && getRecipeList().size() > 0) {
			List<ItemStack> inputList = new ArrayList<ItemStack>();
			for (int i = 0; i < getRecipeList().size();  i++) {
				inputList.add(getRecipeList().get(i).getInput());
			}
			return inputList;
		}
		return null;
	}
	
	public ItemStack getOutputFromInput(ItemStack inputStack) {
		if (getInputList().contains(inputStack)) {
			int index = getInputList().indexOf(inputStack);
			return getRecipeByIndex(index).getOutput();
		}
		return null;
	}
	
}
