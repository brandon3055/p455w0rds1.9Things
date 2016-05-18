package p455w0rd.p455w0rdsthings.client.gui;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.google.common.collect.Sets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import p455w0rd.p455w0rdsthings.Globals;
import p455w0rd.p455w0rdsthings.container.DankNullSlot;
import p455w0rd.p455w0rdsthings.util.ItemUtils;

public class GuiDankNull extends GuiContainer {

	private PRenderItem pRenderItem = new PRenderItem(Minecraft.getMinecraft().renderEngine, Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager(), Minecraft.getMinecraft().getItemColors(), false);
	private final List<DankNullSlot> slots = new LinkedList<DankNullSlot>();
	private Slot theSlot;
	private Slot returningStackDestSlot;
	private long returningStackTime;
	private ItemStack returningStack;
	private ItemStack draggedStack;
	protected boolean dragSplitting;
	private int dragSplittingRemnant;
	private boolean isRightMouseClick;
	private int touchUpX;
	private int touchUpY;

	protected int xSize = 176;
	/** The Y size of the inventory window in pixels. */
	protected int ySize = 222;
	/** A list of the players inventory slots */
	public Container inventorySlots;
	/**
	 * Starting X position for the Gui. Inconsistent use for Gui backgrounds.
	 */
	protected int guiLeft;
	/**
	 * Starting Y position for the Gui. Inconsistent use for Gui backgrounds.
	 */
	protected int guiTop;
	/** holds the slot currently hovered */
	private Slot clickedSlot;
	private Slot currentDragTargetSlot;
	private long dragItemDropDelay;
	protected final Set<Slot> dragSplittingSlots = Sets.<Slot> newHashSet();
	private int dragSplittingLimit;
	private int dragSplittingButton;
	private boolean ignoreMouseUp;
	private long lastClickTime;
	private Slot lastClickSlot;
	private int lastClickButton;
	private boolean doubleClick;
	private ItemStack shiftClickedSlot;

	public GuiDankNull(final Container inventorySlotsIn) {
		super(inventorySlotsIn);
		this.inventorySlots = inventorySlotsIn;
		this.ignoreMouseUp = true;
		
	}
	
	@Override
	public void initGui()
    {
        super.initGui();
        this.setItemRender(pRenderItem);
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;
    }
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.85F);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glTranslatef(0.0f, 0.0f, 0.0f);
		this.mc.getTextureManager().bindTexture(new ResourceLocation(Globals.MODID, "textures/gui/danknullscreen" + ItemUtils.getDankNullStack(this.mc.thePlayer.inventory).getItemDamage() + ".png"));
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.fontRendererObj.drawString("/dank/null", 7, 5, 0xFFFFFF);
		this.mc.fontRendererObj.drawString(I18n.format("container.inventory"), 7, this.ySize - 93, 0xFFFFFF);
	}

	protected List<DankNullSlot> getSlots() {
		return this.slots;
	}

	private RenderItem setItemRender(final RenderItem renderer) {
		final RenderItem ri = this.itemRender;
		this.itemRender = renderer;
		return ri;
	}

	private void drawSlot(final Slot slotIn) {
		int i = slotIn.xDisplayPosition;
        int j = slotIn.yDisplayPosition;
        ItemStack itemstack = slotIn.getStack();
        boolean flag = false;
        boolean flag1 = slotIn == this.clickedSlot && this.draggedStack != null && !this.isRightMouseClick;
        ItemStack itemstack1 = this.mc.thePlayer.inventory.getItemStack();
        String s = null;

        if (slotIn == this.clickedSlot && this.draggedStack != null && this.isRightMouseClick && itemstack != null)
        {
            itemstack = itemstack.copy();
            itemstack.stackSize /= 2;
        }
        else if (this.dragSplitting && this.dragSplittingSlots.contains(slotIn) && itemstack1 != null)
        {
            if (this.dragSplittingSlots.size() == 1)
            {
                return;
            }

            if (Container.canAddItemToSlot(slotIn, itemstack1, true) && this.inventorySlots.canDragIntoSlot(slotIn))
            {
                itemstack = itemstack1.copy();
                flag = true;
                Container.computeStackSize(this.dragSplittingSlots, this.dragSplittingLimit, itemstack, slotIn.getStack() == null ? 0 : slotIn.getStack().stackSize);

                if (itemstack.stackSize > itemstack.getMaxStackSize())
                {
                    s = TextFormatting.YELLOW + "" + itemstack.getMaxStackSize();
                    itemstack.stackSize = itemstack.getMaxStackSize();
                }

                if (itemstack.stackSize > slotIn.getItemStackLimit(itemstack))
                {
                    s = TextFormatting.YELLOW + "" + slotIn.getItemStackLimit(itemstack);
                    itemstack.stackSize = slotIn.getItemStackLimit(itemstack);
                }
            }
            else
            {
                this.dragSplittingSlots.remove(slotIn);
                this.func_146980_g();
            }
        }

        this.zLevel = 100.0F;
        this.itemRender.zLevel = 100.0F;

        if (itemstack == null && slotIn.canBeHovered())
        {
            TextureAtlasSprite textureatlassprite = slotIn.getBackgroundSprite();

            if (textureatlassprite != null)
            {
                GlStateManager.disableLighting();
                this.mc.getTextureManager().bindTexture(slotIn.getBackgroundLocation());
                this.drawTexturedModalRect(i, j, textureatlassprite, 16, 16);
                GlStateManager.enableLighting();
                flag1 = true;
            }
        }

        if (!flag1)
        {
            if (flag)
            {
                drawRect(i, j, i + 16, j + 16, -2130706433);
            }

            GlStateManager.enableDepth();
            this.itemRender.renderItemAndEffectIntoGUI(this.mc.thePlayer, itemstack, i, j);
            this.itemRender.renderItemOverlayIntoGUI(this.fontRendererObj, itemstack, i, j, s);
        }

        this.itemRender.zLevel = 0.0F;
        this.zLevel = 0.0F;
	}



	private void drawItemStack(ItemStack stack, int x, int y, String altText) {
		GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GlStateManager.translate(0.0F, 0.0F, 32.0F);
		this.zLevel = 200.0F;
		this.itemRender.zLevel = 200.0F;
		net.minecraft.client.gui.FontRenderer font = null;
		if (stack != null)
			font = stack.getItem().getFontRenderer(stack);
		if (font == null)
			font = fontRendererObj;
		this.itemRender.renderItemAndEffectIntoGUI(stack, x, y);
		this.itemRender.renderItemOverlayIntoGUI(font, stack, x, y - (this.draggedStack == null ? 0 : 8), altText);
		GL11.glPopAttrib();
		this.zLevel = 0.0F;
		this.itemRender.zLevel = 0.0F;
	}
	
	@Override
	public void drawDefaultBackground()
    {
        this.drawWorldBackground(0);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.GuiScreenEvent.BackgroundDrawnEvent(this));
    }
	
	public void drawWorldBackground(int tint)
    {
        if (this.mc.theWorld != null)
        {
            //this.drawGradientRect(0, 0, this.width, this.height, -1072689136, -804253680);
        	//this.drawGradientRect(0, 0, this.width, this.height, -1072689136, 0x000000FF);
        	int topHeight = ((this.height - this.ySize) / 2) + 1;
        	int leftWidth = ((this.width - this.ySize) / 2) + 24;
        	int leftHeight = this.height - (topHeight * 2) + topHeight;
        	int rightOffset = this.width + leftWidth;
        	Gui.drawRect(0, 0, this.width, topHeight, 0xBB000000);
        	Gui.drawRect(0, topHeight, leftWidth, leftHeight, 0xBB000000);
        	Gui.drawRect(rightOffset, topHeight, leftWidth + this.xSize - 2, leftHeight, 0xBB000000);
        	Gui.drawRect(0, this.height - topHeight, this.width, this.height, 0xBB000000);
        }
        else
        {
            this.drawBackground(tint);
        }
    }
	
	@Override
	public void drawBackground(int tint)
    {
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexbuffer = tessellator.getBuffer();
        this.mc.getTextureManager().bindTexture(optionsBackground);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.5F);
        float f = 32.0F;
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        vertexbuffer.pos(0.0D, (double)this.height, 0.0D).tex(0.0D, (double)((float)this.height / 32.0F + (float)tint)).color(64, 64, 64, 255).endVertex();
        vertexbuffer.pos((double)this.width, (double)this.height, 0.0D).tex((double)((float)this.width / 32.0F), (double)((float)this.height / 32.0F + (float)tint)).color(64, 64, 64, 255).endVertex();
        vertexbuffer.pos((double)this.width, 0.0D, 0.0D).tex((double)((float)this.width / 32.0F), (double)tint).color(64, 64, 64, 255).endVertex();
        vertexbuffer.pos(0.0D, 0.0D, 0.0D).tex(0.0D, (double)tint).color(64, 64, 64, 255).endVertex();
        tessellator.draw();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {

		this.drawDefaultBackground();
		int i = this.guiLeft;
		int j = this.guiTop;
		this.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);

		GlStateManager.disableRescaleNormal();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableLighting();
		GlStateManager.disableDepth();
		for (int i2 = 0; i2 < this.buttonList.size(); ++i2) {
			((GuiButton) this.buttonList.get(i)).drawButton(this.mc, mouseX, mouseY);
		}

		for (int j2 = 0; j2 < this.labelList.size(); ++j2) {
			((GuiLabel) this.labelList.get(j)).drawLabel(this.mc, mouseX, mouseY);
		}
		RenderHelper.enableGUIStandardItemLighting();
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) i, (float) j, 0.0F);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableRescaleNormal();

		this.theSlot = null;
		int k = 240;
		int l = 240;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) k, (float) l);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

		for (int i1 = 0; i1 < this.inventorySlots.inventorySlots.size(); ++i1) {
			Slot slot = this.inventorySlots.inventorySlots.get(i1);
			this.drawSlot(slot);

			if (this.isMouseOverSlot(slot, mouseX, mouseY) && slot.canBeHovered()) {
				this.theSlot = slot;
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                int j1 = slot.xDisplayPosition;
                int k1 = slot.yDisplayPosition;
                GlStateManager.colorMask(true, true, true, false);
                this.drawGradientRect(j1, k1, j1 + 16, k1 + 16, -2130706433, 0x999);
                GlStateManager.colorMask(true, true, true, true);
                GlStateManager.enableLighting();
                GlStateManager.enableDepth();
			}

		}
		RenderHelper.disableStandardItemLighting();
		this.drawGuiContainerForegroundLayer(mouseX, mouseY);
		RenderHelper.enableGUIStandardItemLighting();
		InventoryPlayer inventoryplayer = this.mc.thePlayer.inventory;
		ItemStack itemstack = this.draggedStack == null ? inventoryplayer.getItemStack() : this.draggedStack;

		if (itemstack != null) {
			int j2 = 8;
			int k2 = this.draggedStack == null ? 8 : 16;
			String s = null;

			if (this.draggedStack != null && this.isRightMouseClick) {
				itemstack = itemstack.copy();
				itemstack.stackSize = MathHelper.ceiling_float_int((float) itemstack.stackSize / 2.0F);
			}
			else if (this.dragSplitting && this.dragSplittingSlots.size() > 1) {
				itemstack = itemstack.copy();
				itemstack.stackSize = this.dragSplittingRemnant;

				if (itemstack.stackSize == 0) {
					s = "" + TextFormatting.YELLOW + "0";
				}
			}

			this.drawItemStack(itemstack, mouseX - i - j2, mouseY - j - k2, s);
		}

		if (this.returningStack != null) {
			float f = (float) (Minecraft.getSystemTime() - this.returningStackTime) / 100.0F;

			if (f >= 1.0F) {
				f = 1.0F;
				this.returningStack = null;
			}

			int l2 = this.returningStackDestSlot.xDisplayPosition - this.touchUpX;
			int i3 = this.returningStackDestSlot.yDisplayPosition - this.touchUpY;
			int l1 = this.touchUpX + (int) ((float) l2 * f);
			int i2 = this.touchUpY + (int) ((float) i3 * f);
			this.drawItemStack(this.returningStack, l1, i2, (String) null);
		}

		GlStateManager.popMatrix();

		if (inventoryplayer.getItemStack() == null && this.theSlot != null && this.theSlot.getHasStack()) {
			ItemStack itemstack1 = (this.theSlot instanceof DankNullSlot ? ((DankNullSlot) this.theSlot).getStack() : ((Slot) this.theSlot).getStack());
			this.renderToolTip(itemstack1, mouseX, mouseY);
		}

		GlStateManager.enableLighting();
		GlStateManager.enableDepth();
		RenderHelper.enableStandardItemLighting();

	}
	/*
	@Override
	public void setGuiSize(int w, int h)
    {
        this.width = w;
        this.height = h;
    }
*/
	private Slot getSlotAtPosition(int x, int y) {
		for (int i = 0; i < this.inventorySlots.inventorySlots.size(); ++i) {
			Slot slot = (Slot) this.inventorySlots.inventorySlots.get(i);

			if (this.isMouseOverSlot(slot, x, y)) {
				return slot;
			}
		}
		return null;
	}

	@Override
	public void updateScreen()
    {

        if (!this.mc.thePlayer.isEntityAlive() || this.mc.thePlayer.isDead)
        {
            this.mc.thePlayer.closeScreen();
        }
    }

	private void func_146980_g() {
		ItemStack itemstack = this.mc.thePlayer.inventory.getItemStack();

		if (itemstack != null && this.dragSplitting) {
			this.dragSplittingRemnant = itemstack.stackSize;

			for (Slot slot : this.dragSplittingSlots) {
				ItemStack itemstack1 = itemstack.copy();
				int i = slot.getStack() == null ? 0 : slot.getStack().stackSize;
				Container.computeStackSize(this.dragSplittingSlots, this.dragSplittingLimit, itemstack1, i);

				if (itemstack1.stackSize > itemstack1.getMaxStackSize()) {
					itemstack1.stackSize = itemstack1.getMaxStackSize();
				}

				if (itemstack1.stackSize > slot.getItemStackLimit(itemstack1)) {
					itemstack1.stackSize = slot.getItemStackLimit(itemstack1);
				}

				this.dragSplittingRemnant -= itemstack1.stackSize - i;
			}
		}
	}

	private boolean isMouseOverSlot(Slot slotIn, int mouseX, int mouseY) {
		return this.isPointInRegion(slotIn.xDisplayPosition, slotIn.yDisplayPosition, 16, 16, mouseX, mouseY);
	}
	
	@Override
	public Slot getSlotUnderMouse()
    {
        return this.theSlot;
    }
	
	@Override
	protected boolean isPointInRegion(int rectX, int rectY, int rectWidth, int rectHeight, int pointX, int pointY)
    {
        int i = this.guiLeft;
        int j = this.guiTop;
        pointX = pointX - i;
        pointY = pointY - j;
        return pointX >= rectX - 1 && pointX < rectX + rectWidth + 1 && pointY >= rectY - 1 && pointY < rectY + rectHeight + 1;
    }
}
