package p455w0rd.p455w0rdsthings.state;

import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.item.EnumDyeColor;
import p455w0rd.p455w0rdsthings.items.IDankNull;

public final class PStateProps {

	public static final PropertyObject<IDankNull.HolderLevel> HOLDER_LEVEL = new PropertyObject<>("holderlevel", IDankNull.HolderLevel.class);
	public static final PropertyEnum<EnumDyeColor> COLOR = PropertyEnum.create("color", EnumDyeColor.class);
	
	private PStateProps() {
		
	}
}
