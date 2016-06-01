package p455w0rd.p455w0rdsthings.materials;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.MaterialLiquid;
import p455w0rd.p455w0rdsthings.util.ReflectionUtils;

public class MaterialLiquidXp extends MaterialLiquid
{
	public MaterialLiquidXp(MapColor color)
    {
        super(color);
        this.setReplaceable();
        this.setNoPushMobility();
        try {
			ReflectionUtils.findMethod(net.minecraft.block.material.Material.class, new String[]{"setTranslucent", "func_76223_p"}).invoke(true);
		}
		catch (Throwable e) {
			System.out.println(e.getMessage());
		}
    }

    /**
     * Returns if blocks of these materials are liquids.
     */
    @Override
    public boolean isLiquid()
    {
        return true;
    }

    /**
     * Returns if this material is considered solid or not
     */
    @Override
    public boolean blocksMovement()
    {
        return false;
    }

    /**
     * Returns true if the block is a considered solid. This is true by default.
     */
    @Override
    public boolean isSolid()
    {
        return false;
    }
}