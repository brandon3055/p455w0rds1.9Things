package p455w0rd.p455w0rdsthings.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelPShield extends ModelBase {
	
	public ModelRenderer field_187063_a;
	public ModelRenderer field_187064_b;

	public ModelPShield() {
		this.textureWidth = 64;
		this.textureHeight = 64;
		this.field_187063_a = new ModelRenderer(this, 0, 0);
		this.field_187063_a.addBox(-6.0F, -11.0F, -2.0F, 12, 22, 1, 0.0F);
		this.field_187064_b = new ModelRenderer(this, 26, 0);
		this.field_187064_b.addBox(-1.0F, -3.0F, -1.0F, 2, 6, 6, 0.0F);
	}

	public void render() {
		this.field_187063_a.render(0.0625F);
		this.field_187064_b.render(0.0625F);
	}
}
