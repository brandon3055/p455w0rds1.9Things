package p455w0rd.p455w0rdsthings.client.render;

import codechicken.lib.render.IItemRenderer;
import codechicken.lib.render.ModelRegistryHelper;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;

public class PModelRegistryHelper extends ModelRegistryHelper {
	
	public static void registerDankNullRenderer(Item item, IItemRenderer renderer, int damage) {
        final ModelResourceLocation modelLoc = new ModelResourceLocation(Item.itemRegistry.getNameForObject(item) + "" + damage, "inventory");
        register(modelLoc, renderer);
        ModelLoader.setCustomMeshDefinition(item, new ItemMeshDefinition() {
            @Override
            public ModelResourceLocation getModelLocation(ItemStack stack) {
                return modelLoc;
            }
        });
    }
	
}
