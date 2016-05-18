package p455w0rd.p455w0rdsthings.proxy;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import p455w0rd.p455w0rdsthings.CreativeTabP;
import p455w0rd.p455w0rdsthings.ModBlocks;
import p455w0rd.p455w0rdsthings.ModItems;
import p455w0rd.p455w0rdsthings.handlers.EventsHandler;

public class CommonProxy {
	
	public static CreativeTabs creativeTab;

	public void preInit(FMLPreInitializationEvent e) {
		creativeTab = new CreativeTabP().setNoScrollbar();
        ModBlocks.init();
        ModItems.init();
       
    }

    public void init(FMLInitializationEvent e) {
    	MinecraftForge.EVENT_BUS.register(new EventsHandler());
    }

    public void postInit(FMLPostInitializationEvent e) {
    	
    }
	
}
