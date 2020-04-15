package com.genreshinobi.magelighter.inventory.container;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class JarSlot extends Slot {
    private final AbstractClericsOvenContainer clericsOvenContainer;

    public JarSlot(AbstractClericsOvenContainer containerIn, IInventory inventory, int index, int xPos, int yPos) {
        super(inventory, index, xPos, yPos);
        this.clericsOvenContainer = containerIn;
    }

    public boolean isItemValid(ItemStack stack) {
        return this.clericsOvenContainer.isJar(stack);
    }

    public int getItemStackLimit(ItemStack stack) {
        return isBucket(stack) ? 1: super.getItemStackLimit(stack);
    }

    public static boolean isBucket(ItemStack stack) {
        return stack.getItem() == Items.BUCKET;
    }
}
