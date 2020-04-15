package com.genreshinobi.magelighter.tileentities;


import com.genreshinobi.magelighter.inventory.container.ClericsOvenContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import static com.genreshinobi.magelighter.lists.BlockList.CLERICSOVEN_TILE;

public class ClericsOvenEntity extends AbstractClericsOvenTileEntity {

    public ClericsOvenEntity() { super(CLERICSOVEN_TILE, IRecipeType.SMELTING); }

    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("block.magelighter.clerics_oven");
    }

    protected Container createMenu(int id, PlayerInventory player) {
        return new ClericsOvenContainer(id, player, this, this.furnaceData);
    }
}