package p455w0rd.p455w0rdsthings.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandle;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import p455w0rd.p455w0rdsthings.Globals;

@SideOnly(Side.CLIENT)
public class CapeUtils {
	private static final ResourceLocation CAPE_LOCATION = new ResourceLocation(Globals.MODID, "textures/capes/p455cape4.png");
	//private static final UUID UUID_P455W0RD = UUID.fromString("7b794048-f144-40bb-83dd-4789a5cf7cc8");

	// Copied from SkinManager
	private static final ExecutorService THREAD_POOL = new ThreadPoolExecutor(0, 2, 1L, TimeUnit.MINUTES, new LinkedBlockingQueue<>());

	private static final MethodHandle GET_PLAYER_INFO = ReflectionUtils.findMethod(AbstractClientPlayer.class, new String[]{"getPlayerInfo", "func_175155_b"});
	private static final MethodHandle GET_PLAYER_TEXTURES = ReflectionUtils.findFieldGetter(NetworkPlayerInfo.class, "playerTextures", "field_187107_a");

	/**
	 * Queue the replacement of a player's cape with the custom cape.
	 * <p>
	 * In at least 100 milliseconds, the player's cape will be replaced on the next iteration of the client's main loop.
	 *
	 * @param player The player
	 */
	public static void queuePlayerCapeReplacement(AbstractClientPlayer player) {
		THREAD_POOL.submit(() -> {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				return;
			}

			Minecraft.getMinecraft().addScheduledTask(() -> replacePlayerCape(player));
		});
	}

	/**
	 * Replace a player's cape with the custom cape.
	 *
	 * @param player The player
	 */
	private static void replacePlayerCape(AbstractClientPlayer player) {
		final NetworkPlayerInfo playerInfo;

		try {
			playerInfo = (NetworkPlayerInfo) GET_PLAYER_INFO.invoke(player);
		} catch (Throwable throwable) {
			return;
		}

		if (playerInfo == null) {
			return;
		}


		final Map<MinecraftProfileTexture.Type, ResourceLocation> playerTextures;
		try {
			playerTextures = (Map<MinecraftProfileTexture.Type, ResourceLocation>) GET_PLAYER_TEXTURES.invoke(playerInfo);
		} catch (Throwable throwable) {
			return;
		}

		playerTextures.put(MinecraftProfileTexture.Type.CAPE, CAPE_LOCATION);
	}
	
	static List<String> getPatronList() {
		try {
			InputStream in = new URL("http://p455w0rd.net/mc/patrons.txt").openStream();
			return IOUtils.readLines(in);
		}
		catch (MalformedURLException ignored) {
		}
		catch (IOException ignored) {
		}
		return null;
	}

	/**
	 * Is player a contribuer/patron?
	 * <p>
	 * Currently only returns true for me
	 *
	 * @param player The player
	 * @return True if the player has a TestMod3 cape
	 */
	public static boolean doesPlayerHaveCape(AbstractClientPlayer player) {
		List<String> patronList = getPatronList();
		if (patronList == null) {
			System.out.println("Could not fetch list!");
			return false;
		}
		for (int i = 0; i < patronList.size(); i++) {
			String uuid = player.getUniqueID().toString();
			String match = patronList.get(i);
			if (uuid.equals(match)) {
				return true;
			}
		}
		return false;
	}
}
