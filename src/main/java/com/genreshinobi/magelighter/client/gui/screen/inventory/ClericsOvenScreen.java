package com.genreshinobi.magelighter.client.gui.screen.inventory;

import com.genreshinobi.magelighter.Magelighter;
import com.genreshinobi.magelighter.inventory.container.AbstractClericsOvenContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class ClericsOvenScreen extends AbstractClericsOvenScreen<AbstractClericsOvenContainer> {
    private static final ResourceLocation GUI = new ResourceLocation(Magelighter.MOD_ID,"textures/gui/clerics_oven.png");

    public ClericsOvenScreen(AbstractClericsOvenContainer container, PlayerInventory inventory, ITextComponent textComponent) {
        super(container, inventory, textComponent, GUI);
    }
}
