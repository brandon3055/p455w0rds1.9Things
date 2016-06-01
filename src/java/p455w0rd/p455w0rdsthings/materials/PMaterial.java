package p455w0rd.p455w0rdsthings.materials;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;

public class PMaterial extends Material {
	
	public static final Material xpjuice = (new MaterialLiquid(MapColor.waterColor));

	public PMaterial(MapColor color) {
		super(color);
	}

}
