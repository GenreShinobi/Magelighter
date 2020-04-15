package com.genreshinobi.magelighter.inventory.container;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.IIntArray;

import static com.genreshinobi.magelighter.lists.BlockList.CLERICSOVEN_CONTAINER;

public class ClericsOvenContainer extends AbstractClericsOvenContainer {

    public ClericsOvenContainer( int id, PlayerInventory player) {
        super(CLERICSOVEN_CONTAINER, IRecipeType.SMELTING, id, player);
    }

    public ClericsOvenContainer(int id, PlayerInventory player, IInventory entity, IIntArray furnace) {
        super(CLERICSOVEN_CONTAINER, IRecipeType.SMELTING, id, player, entity, furnace);
    }
}
