package p455w0rd.p455w0rdsthings;

public class Globals {
	public static final String MODID = "p455w0rdsthings";
	public static final String VERSION = "@version@";
	public static final String NAME = "p455w0rd's Things";
	public static final String SERVER_PROXY_CLASS = "p455w0rd.p455w0rdsthings.proxy.CommonProxy";
	public static final String CLIENT_PROXY_CLASS = "p455w0rd.p455w0rdsthings.proxy.ClientProxy";
	public static final String GUI_FACTORY = "p455w0rd.client.gui.GuiFactory";
	
	private static int guiIndex = 0;
	public static int GUINUM_DANKNULL = ++guiIndex;
	
	public static boolean GUI_DANKNULL_ISOPEN = false;
}
