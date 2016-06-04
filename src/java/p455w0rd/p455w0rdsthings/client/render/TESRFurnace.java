package p455w0rd.p455w0rdsthings.client.render;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityBeaconRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import p455w0rd.p455w0rdsthings.blocks.tileentities.TileEntityFurnace;

public class TESRFurnace extends TileEntitySpecialRenderer<TileEntityFurnace> {

	private float rotation;
	private final List<TileEntityBeacon.BeamSegment> beamSegments = Lists.<TileEntityBeacon.BeamSegment>newArrayList();
	private float ticks;
	private TileEntityFurnace furnaceTE;
	
	public TESRFurnace() {
		this.ticks = 0;
		this.rotation = 90F;
	}

	@Override
	public void renderTileEntityAt(TileEntityFurnace te, double x, double y, double z, float partialTicks, int destroyStage) {
		this.furnaceTE = te;
        this.beamSegments.clear();
        this.beamSegments.add(new TileEntityBeacon.BeamSegment(EntitySheep.getDyeRgb(EnumDyeColor.WHITE)));
        if (getWorld().canBlockSeeSky(getTE().getPos()) && te.getInputSlotStack() != null) {
        	this.renderBeacon(x, y+0.5, z, (double)partialTicks, (double)getTE().getWorld().getTotalWorldTime()+ticks, this.beamSegments, (double)getTE().getWorld().getTotalWorldTime());
        }
        else {
        	this.ticks = 0;
        }
		
		GlStateManager.pushAttrib();
		GlStateManager.pushMatrix();

		// Translate to the location of our tile entity
		GlStateManager.translate(x, y, z);
		GlStateManager.disableRescaleNormal();

		// Render our item
		renderItem(te);

		GlStateManager.popMatrix();
		GlStateManager.popAttrib();

	}

	private void renderItem(TileEntityFurnace te) {
		ItemStack stack = te.getInputSlotStack();
		if (stack != null) {
			RenderHelper.enableStandardItemLighting();
			GlStateManager.enableLighting();

			GlStateManager.color(1.0F, 1.0F, 1.0F, 0.1F);
			GlStateManager.pushMatrix();
			GlStateManager.translate(.5, .5, .5);
			GlStateManager.scale(.35f, .35f, .35f);
			if (rotation >= 360) {
				rotation = 0;
			}
			
			if (getWorld().canBlockSeeSky(te.getPos()) && !Minecraft.getMinecraft().isGamePaused()) {
				if (this.ticks >= 255) {
					int x = te.getPos().getX();
					int y = te.getPos().getY();
					int z = te.getPos().getZ();
					rotation += 0.5;
					if (rotation % 36 == 0) {
						getWorld().spawnParticle(EnumParticleTypes.FLAME, x+0.5, y+0.75, z+0.5, 0, 0, 0, new int[0]);
					}
				}
			}
			
			GlStateManager.rotate(rotation, 0, rotation, 0);

			Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.NONE);
			GlStateManager.popMatrix();
		}
	}

	public void renderBeacon(double p_188206_1_, double p_188206_3_, double p_188206_5_, double p_188206_7_, double p_188206_9_, List<TileEntityBeacon.BeamSegment> p_188206_11_, double p_188206_12_)
    {
        GlStateManager.alphaFunc(516, 0.1F);
        this.bindTexture(TileEntityBeaconRenderer.TEXTURE_BEACON_BEAM);

        if (p_188206_9_ > 0.0D)
        {
            GlStateManager.disableFog();
            int i = 0;
            
            for (int j = 0; j < p_188206_11_.size(); ++j)
            {
                TileEntityBeacon.BeamSegment tileentitybeacon$beamsegment = (TileEntityBeacon.BeamSegment)p_188206_11_.get(j);
        		
                renderBeamSegment(p_188206_1_, p_188206_3_, p_188206_5_, p_188206_7_, p_188206_9_, p_188206_12_, i, tileentitybeacon$beamsegment.getHeight()*300, tileentitybeacon$beamsegment.getColors());
                i += tileentitybeacon$beamsegment.getHeight();
            }

            GlStateManager.enableFog();
        }
    }

    public void renderBeamSegment(double p_188204_0_, double p_188204_2_, double p_188204_4_, double p_188204_6_, double p_188204_8_, double p_188204_10_, int p_188204_12_, int p_188204_13_, float[] p_188204_14_)
    {
        renderBeamSegment(p_188204_0_, p_188204_2_, p_188204_4_, p_188204_6_, p_188204_8_, p_188204_10_, p_188204_12_, p_188204_13_, p_188204_14_, 0.04D, 0.06D);
    }
    
    public TileEntityFurnace getTE() {
    	return this.furnaceTE;
    }

    public void renderBeamSegment(double p_188205_0_, double p_188205_2_, double p_188205_4_, double p_188205_6_, double p_188205_8_, double p_188205_10_, int p_188205_12_, int p_188205_13_, float[] p_188205_14_, double p_188205_15_, double p_188205_17_)
    {
    	if (this.ticks < 255) {
    		if (getTE().getInputSlotStack() != null) {
    			this.ticks++;
    		}
    		else {
    			this.ticks = 0;
    		}
    	}
        int i = p_188205_12_ + p_188205_13_;
        GlStateManager.glTexParameteri(3553, 10242, 10497);
        GlStateManager.glTexParameteri(3553, 10243, 10497);
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.disableBlend();
        GlStateManager.depthMask(true);
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexbuffer = tessellator.getBuffer();
        double d0 = p_188205_10_ + p_188205_6_;
        double d1 = p_188205_13_ < 0 ? d0 : -d0;
        double d2 = MathHelper.frac(d1 * 0.2D - (double)MathHelper.floor_double(d1 * 0.1D));
        float f = p_188205_14_[0] + ticks;
        float f1 = p_188205_14_[1];
        float f2 = p_188205_14_[2] + ticks;
        double d3 = d0 * 0.025D * -10D;
        double d4 = 0.5D + Math.cos(d3 + 2.356194490192345D) * p_188205_15_;
        double d5 = 0.5D + Math.sin(d3 + 2.356194490192345D) * p_188205_15_;
        double d6 = 0.5D + Math.cos(d3 + (Math.PI / 4D)) * p_188205_15_;
        double d7 = 0.5D + Math.sin(d3 + (Math.PI / 4D)) * p_188205_15_;
        double d8 = 0.5D + Math.cos(d3 + 3.9269908169872414D) * p_188205_15_;
        double d9 = 0.5D + Math.sin(d3 + 3.9269908169872414D) * p_188205_15_;
        double d10 = 0.5D + Math.cos(d3 + 5.497787143782138D) * p_188205_15_;
        double d11 = 0.5D + Math.sin(d3 + 5.497787143782138D) * p_188205_15_;
        //double d12 = 0.0D;
        double d13 = 1.0D;
        double d14 = -1.0D + d2;
        double d15 = (double)p_188205_13_ * p_188205_8_ * (0.5D / p_188205_15_) + d14;
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        vertexbuffer.pos(p_188205_0_ + d4, p_188205_2_ + (double)i, p_188205_4_ + d5).tex(1.0D, d15).color(f, f1, f2, 1.0F).endVertex();
        vertexbuffer.pos(p_188205_0_ + d4, p_188205_2_ + (double)p_188205_12_, p_188205_4_ + d5).tex(1.0D, d14).color(f, f1, f2, 1.0F).endVertex();
        vertexbuffer.pos(p_188205_0_ + d6, p_188205_2_ + (double)p_188205_12_, p_188205_4_ + d7).tex(0.0D, d14).color(f, f1, f2, 1.0F).endVertex();
        vertexbuffer.pos(p_188205_0_ + d6, p_188205_2_ + (double)i, p_188205_4_ + d7).tex(0.0D, d15).color(f, f1, f2, 1.0F).endVertex();
        vertexbuffer.pos(p_188205_0_ + d10, p_188205_2_ + (double)i, p_188205_4_ + d11).tex(1.0D, d15).color(f, f1, f2, 1.0F).endVertex();
        vertexbuffer.pos(p_188205_0_ + d10, p_188205_2_ + (double)p_188205_12_, p_188205_4_ + d11).tex(1.0D, d14).color(f, f1, f2, 1.0F).endVertex();
        vertexbuffer.pos(p_188205_0_ + d8, p_188205_2_ + (double)p_188205_12_, p_188205_4_ + d9).tex(0.0D, d14).color(f, f1, f2, 1.0F).endVertex();
        vertexbuffer.pos(p_188205_0_ + d8, p_188205_2_ + (double)i, p_188205_4_ + d9).tex(0.0D, d15).color(f, f1, f2, 1.0F).endVertex();
        vertexbuffer.pos(p_188205_0_ + d6, p_188205_2_ + (double)i, p_188205_4_ + d7).tex(1.0D, d15).color(f, f1, f2, 1.0F).endVertex();
        vertexbuffer.pos(p_188205_0_ + d6, p_188205_2_ + (double)p_188205_12_, p_188205_4_ + d7).tex(1.0D, d14).color(f, f1, f2, 1.0F).endVertex();
        vertexbuffer.pos(p_188205_0_ + d10, p_188205_2_ + (double)p_188205_12_, p_188205_4_ + d11).tex(0.0D, d14).color(f, f1, f2, 1.0F).endVertex();
        vertexbuffer.pos(p_188205_0_ + d10, p_188205_2_ + (double)i, p_188205_4_ + d11).tex(0.0D, d15).color(f, f1, f2, 1.0F).endVertex();
        vertexbuffer.pos(p_188205_0_ + d8, p_188205_2_ + (double)i, p_188205_4_ + d9).tex(1.0D, d15).color(f, f1, f2, 1.0F).endVertex();
        vertexbuffer.pos(p_188205_0_ + d8, p_188205_2_ + (double)p_188205_12_, p_188205_4_ + d9).tex(1.0D, d14).color(f, f1, f2, 1.0F).endVertex();
        vertexbuffer.pos(p_188205_0_ + d4, p_188205_2_ + (double)p_188205_12_, p_188205_4_ + d5).tex(0.0D, d14).color(f, f1, f2, 1.0F).endVertex();
        vertexbuffer.pos(p_188205_0_ + d4, p_188205_2_ + (double)i, p_188205_4_ + d5).tex(0.0D, d15).color(f, f1, f2, 1.0F).endVertex();
        tessellator.draw();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.depthMask(false);
        d3 = 0.5D - p_188205_17_;
        d4 = 0.5D - p_188205_17_;
        d5 = 0.5D + p_188205_17_;
        d6 = 0.5D - p_188205_17_;
        d7 = 0.5D - p_188205_17_;
        d8 = 0.5D + p_188205_17_;
        d9 = 0.5D + p_188205_17_;
        d10 = 0.5D + p_188205_17_;
        d11 = 0.0D;
        //d12 = 1.0D;
        d13 = -1.0D + d2;
        d14 = (double)p_188205_13_ * p_188205_8_ + d13;
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        vertexbuffer.pos(p_188205_0_ + d3, p_188205_2_ + (double)i, p_188205_4_ + d4).tex(1.0D, d14).color(f, f1, f2, 0.125F).endVertex();
        vertexbuffer.pos(p_188205_0_ + d3, p_188205_2_ + (double)p_188205_12_, p_188205_4_ + d4).tex(1.0D, d13).color(f, f1, f2, 0.125F).endVertex();
        vertexbuffer.pos(p_188205_0_ + d5, p_188205_2_ + (double)p_188205_12_, p_188205_4_ + d6).tex(0.0D, d13).color(f, f1, f2, 0.125F).endVertex();
        vertexbuffer.pos(p_188205_0_ + d5, p_188205_2_ + (double)i, p_188205_4_ + d6).tex(0.0D, d14).color(f, f1, f2, 0.125F).endVertex();
        vertexbuffer.pos(p_188205_0_ + d9, p_188205_2_ + (double)i, p_188205_4_ + d10).tex(1.0D, d14).color(f, f1, f2, 0.125F).endVertex();
        vertexbuffer.pos(p_188205_0_ + d9, p_188205_2_ + (double)p_188205_12_, p_188205_4_ + d10).tex(1.0D, d13).color(f, f1, f2, 0.125F).endVertex();
        vertexbuffer.pos(p_188205_0_ + d7, p_188205_2_ + (double)p_188205_12_, p_188205_4_ + d8).tex(0.0D, d13).color(f, f1, f2, 0.125F).endVertex();
        vertexbuffer.pos(p_188205_0_ + d7, p_188205_2_ + (double)i, p_188205_4_ + d8).tex(0.0D, d14).color(f, f1, f2, 0.125F).endVertex();
        vertexbuffer.pos(p_188205_0_ + d5, p_188205_2_ + (double)i, p_188205_4_ + d6).tex(1.0D, d14).color(f, f1, f2, 0.125F).endVertex();
        vertexbuffer.pos(p_188205_0_ + d5, p_188205_2_ + (double)p_188205_12_, p_188205_4_ + d6).tex(1.0D, d13).color(f, f1, f2, 0.125F).endVertex();
        vertexbuffer.pos(p_188205_0_ + d9, p_188205_2_ + (double)p_188205_12_, p_188205_4_ + d10).tex(0.0D, d13).color(f, f1, f2, 0.125F).endVertex();
        vertexbuffer.pos(p_188205_0_ + d9, p_188205_2_ + (double)i, p_188205_4_ + d10).tex(0.0D, d14).color(f, f1, f2, 0.125F).endVertex();
        vertexbuffer.pos(p_188205_0_ + d7, p_188205_2_ + (double)i, p_188205_4_ + d8).tex(1.0D, d14).color(f, f1, f2, 0.125F).endVertex();
        vertexbuffer.pos(p_188205_0_ + d7, p_188205_2_ + (double)p_188205_12_, p_188205_4_ + d8).tex(1.0D, d13).color(f, f1, f2, 0.125F).endVertex();
        vertexbuffer.pos(p_188205_0_ + d3, p_188205_2_ + (double)p_188205_12_, p_188205_4_ + d4).tex(0.0D, d13).color(f, f1, f2, 0.125F).endVertex();
        vertexbuffer.pos(p_188205_0_ + d3, p_188205_2_ + (double)i, p_188205_4_ + d4).tex(0.0D, d14).color(f, f1, f2, 0.125F).endVertex();
        tessellator.draw();
        GlStateManager.enableLighting();
        GlStateManager.enableTexture2D();
        GlStateManager.depthMask(true);
    }
    
    public boolean isGlobalRenderer(TileEntityFurnace p_188185_1_)
    {
        return true;
    }

}
