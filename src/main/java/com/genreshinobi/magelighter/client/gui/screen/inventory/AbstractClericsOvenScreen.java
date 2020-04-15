package com.genreshinobi.magelighter.client.gui.screen.inventory;

import com.genreshinobi.magelighter.inventory.container.AbstractClericsOvenContainer;
import com.genreshinobi.magelighter.tileentities.AbstractClericsOvenTileEntity;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public abstract class AbstractClericsOvenScreen<T extends AbstractClericsOvenContainer> extends ContainerScreen<T> {
    private boolean field_214090_m;
    private final ResourceLocation GUI;

    public AbstractClericsOvenScreen(T container, PlayerInventory inv, ITextComponent name, ResourceLocation gui) {
        super(container, inv, name);
        this.GUI = gui;
    }

    public void init() {
        super.init();
        this.field_214090_m = this.width < 379;
    }

    public void tick() {
        super.tick();
    }

    public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
        this.renderBackground();
        this.drawGuiContainerBackgroundLayer(p_render_3_, p_render_1_, p_render_2_);
        super.render(p_render_1_, p_render_2_, p_render_3_);
        this.renderHoveredToolTip(p_render_1_, p_render_2_);
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String s = this.title.getFormattedText();
        this.font.drawString(s, (float)(this.xSize / 2 - this.font.getStringWidth(s) / 2), 6.0F, 4210752);
        this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8.0F, (float)(this.ySize - 96 + 2), 4210752);
    }

    /**
     * Draws the background layer of this container (behind the items).
     */
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(this.GUI);
        int i = this.guiLeft;
        int j = this.guiTop;
        this.blit(i, j, 0, 0, this.xSize, this.ySize);
        if (((AbstractClericsOvenContainer)this.container).func_217061_l()) {
            int k = ((AbstractClericsOvenContainer)this.container).getBurnLeftScaled();
            this.blit(i + 56, j + 36 + 12 - k, 176, 12 - k, 14, k + 1);
        }

        int l = ((AbstractClericsOvenContainer)this.container).getCookProgressionScaled();
        this.blit(i + 79, j + 34, 176, 14, l + 1, 16);
    }

    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        return this.field_214090_m || super.mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_);
    }

    /**
     * Called when the mouse is clicked over a slot or outside the gui.
     */
    protected void handleMouseClick(Slot slotIn, int slotId, int mouseButton, ClickType type) {
        super.handleMouseClick(slotIn, slotId, mouseButton, type);
    }

    public void removed() {
        super.removed();
    }

}
