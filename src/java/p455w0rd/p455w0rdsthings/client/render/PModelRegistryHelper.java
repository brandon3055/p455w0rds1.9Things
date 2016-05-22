package p455w0rd.p455w0rdsthings.client.render;

import codechicken.lib.render.IItemRenderer;
import codechicken.lib.render.ModelRegistryHelper;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class PModelRegistryHelper extends ModelRegistryHelper {
	
	public static void registerDankNullRenderer(Item item, IItemRenderer renderer, int damage) {
        final ModelResourceLocation modelLoc = new ModelResourceLocation(Item.itemRegistry.getNameForObject(item) + "" + damage + damage, "inventory");
        ModelLoader.setCustomModelResourceLocation(item, damage, modelLoc);
        register(modelLoc, renderer);
        
    }
	
}
