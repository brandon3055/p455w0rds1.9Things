package p455w0rd.p455w0rdsthings.items;

import java.util.HashMap;

import net.minecraft.item.ItemStack;

public interface IDankNull {

	public ItemStack getDisplayStack();

	public HolderLevel getIslandType();

	public void setHolderLevel(HolderLevel type);

	public class HolderLevel {
		private static HashMap<String, HolderLevel> registry = new HashMap<>();

		public static final HolderLevel I = new HolderLevel("0");
		public static final HolderLevel II = new HolderLevel("1");
		public static final HolderLevel III = new HolderLevel("2");
		public static final HolderLevel IV = new HolderLevel("3");
		public static final HolderLevel V = new HolderLevel("4");
		public static final HolderLevel VI = new HolderLevel("5");

		private final String typeName;

		/**
		 * Instantiates and registers a new floating flower island type
		 * Note that you need to register the model for this island type, see BotaniaAPIClient
		 * @param name The name of this floating flower island type
         */
		public HolderLevel(String name) {
			if (registry.containsKey(name)) throw new IllegalArgumentException(name+" already registered!");
			this.typeName = name;
			registry.put(name, this);
		}

		public static HolderLevel ofLevel(String typeStr) {
			HolderLevel type = registry.get(typeStr);
			return type == null ? I : type;
		}

		public String toString() {
			return this.typeName;
		}

	}
	
}
