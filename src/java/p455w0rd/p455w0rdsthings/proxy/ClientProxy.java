package p455w0rd.p455w0rdsthings.proxy;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import p455w0rd.p455w0rdsthings.ModItems;

public class ClientProxy extends CommonProxy {
	
	@Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);
        //OBJLoader.INSTANCE.addDomain(MODID);
        //ModelLoaderRegistry.registerLoader(new BakedModelLoader());

        // Typically initialization of models and such goes here:
        //ModBlocks.initModels();
        ModItems.preInitModels();
    }

    @Override
    public void init(FMLInitializationEvent e) {
        super.init(e);
        ModItems.initModels();

        // Initialize our input handler so we can listen to keys
        // MinecraftForge.EVENT_BUS.register(new InputHandler());
        //KeyBindings.init();

        //ModBlocks.initItemModels();
    }
	
}
