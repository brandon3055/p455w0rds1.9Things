package p455w0rd.p455w0rdsthings;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import p455w0rd.p455w0rdsthings.handlers.GuiHandler;
import p455w0rd.p455w0rdsthings.handlers.PacketHandler;
import p455w0rd.p455w0rdsthings.proxy.CommonProxy;

@Mod(modid = Globals.MODID, name=Globals.NAME, dependencies =
"required-after:Forge@["
		+ net.minecraftforge.common.ForgeVersion.majorVersion + '.' // majorVersion
		+ net.minecraftforge.common.ForgeVersion.minorVersion + '.' // minorVersion
		+ net.minecraftforge.common.ForgeVersion.revisionVersion + '.' // revisionVersion
		+ net.minecraftforge.common.ForgeVersion.buildVersion + ",)",
version = Globals.VERSION)
public class P455w0rdsThings {

	@SidedProxy(clientSide=Globals.CLIENT_PROXY_CLASS, serverSide=Globals.SERVER_PROXY_CLASS)
    public static CommonProxy proxy;

    @Mod.Instance(Globals.MODID)
    public static P455w0rdsThings INSTANCE;
	
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
    	P455w0rdsThings.INSTANCE = this;
    	PacketHandler.registerMessages(Globals.MODID);
    	proxy.preInit(e);
    }

	@Mod.EventHandler
    public void init(FMLInitializationEvent e) {
		proxy.init(e);
    }
    
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
    	NetworkRegistry.INSTANCE.registerGuiHandler(Globals.MODID, new GuiHandler());
    }
}
