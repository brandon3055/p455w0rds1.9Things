package p455w0rd.p455w0rdsthings.handlers;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import p455w0rd.p455w0rdsthings.ModItems;
import p455w0rd.p455w0rdsthings.recipes.CompressorRecipe;
import p455w0rd.p455w0rdsthings.recipes.CompressorRecipeRegistry;

public class CompressorRecipeHandler {
	
	private final ItemStack coalBlockStack = new ItemStack(Item.getItemFromBlock(Blocks.COAL_BLOCK), 1);
	private final ItemStack carbonStack = new ItemStack(ModItems.carbonItem, 1);
	
	public void addRecipes() {
		CompressorRecipeRegistry.INSTANCE.registerRecipe(new CompressorRecipe(coalBlockStack, carbonStack, 30000));
	}
	
}
