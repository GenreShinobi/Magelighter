package com.genreshinobi.magelighter.inventory.container;

import com.genreshinobi.magelighter.lists.ItemList;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IRecipeHelperPopulator;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.FurnaceResultSlot;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class AbstractClericsOvenContainer extends Container {
    private final IInventory clericsOvenInventory;
    private final IIntArray intArray;
    protected final World world;
    private final IRecipeType<? extends AbstractCookingRecipe> recipeType;
    public static final int INPUT = 0;
    public static final int FUEL = 1;
    public static final int OUTPUT = 2;
    public static final int COLLECTOR = 3;
    public static final int BYPRODUCT = 4;

    protected AbstractClericsOvenContainer(ContainerType<?> containerTypeIn, IRecipeType<? extends AbstractCookingRecipe> recipeTypeIn, int id, PlayerInventory playerInventoryIn) {
        this(containerTypeIn, recipeTypeIn, id, playerInventoryIn, new Inventory(5), new IntArray(4));
    }

    protected AbstractClericsOvenContainer(ContainerType<?> containerTypeIn, IRecipeType<? extends AbstractCookingRecipe> recipeTypeIn, int id, PlayerInventory playerInventoryIn, IInventory clericsOvenInventoryIn, IIntArray intArrayIn) {
        super (containerTypeIn, id);
        this.recipeType = recipeTypeIn;
        assertInventorySize(clericsOvenInventoryIn, 5);
        assertIntArraySize(intArrayIn, 4);
        this.clericsOvenInventory = clericsOvenInventoryIn;
        this.intArray = intArrayIn;
        this.world = playerInventoryIn.player.world;

        this.addSlot(new Slot(clericsOvenInventoryIn, INPUT, 51, 18));
        this.addSlot(new FuelSlot(this, clericsOvenInventoryIn, FUEL, 51, 54));
        this.addSlot(new FurnaceResultSlot(playerInventoryIn.player, clericsOvenInventoryIn, OUTPUT, 112, 22));
        this.addSlot(new JarSlot(this, clericsOvenInventoryIn, COLLECTOR, 80, 54));
        this.addSlot(new ByproductSlot(playerInventoryIn.player, clericsOvenInventoryIn, BYPRODUCT, 112, 54));

        for(int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventoryIn, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventoryIn, k, 8 + k * 18, 142));
        }

        this.trackIntArray(intArrayIn);
    }

    public void func_201771_a(RecipeItemHelper recipeItemHelperIn) {
        if (this.clericsOvenInventory instanceof IRecipeHelperPopulator) {
            ((IRecipeHelperPopulator)this.clericsOvenInventory).fillStackedContents(recipeItemHelperIn);
        }
    }

    public void clear() {
        this.clericsOvenInventory.clear();
    }

    public boolean matches(IRecipe<? super IInventory> recipeIn) {
        return recipeIn.matches(this.clericsOvenInventory, this.world);
    }

    public int getOutputSlot() {
        return 5;
    }

    public int getWidth() {
        return 1;
    } // TODO Change if error

    public int getHeight() {
        return 1;
    } // TODO Change if error

    @OnlyIn(Dist.CLIENT)
    public int getSize() {
        return 6;
    }

    // Determine whether supplied player can use this container
    public boolean canInteractWith(PlayerEntity playerIn) {
        return this.clericsOvenInventory.isUsableByPlayer(playerIn);
    }

    /**
     * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player
     * inventory and the other inventory(s).
     */
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (index == 5) {
                if (!this.mergeItemStack(itemstack1, 6, 42, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            } else if (index != 4 && index != 3 && index != 2 && index != 1 && index != 0) {
                if (this.func_217057_a(itemstack1)) {
                    if (!this.mergeItemStack(itemstack1, 0, 4, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.isFuel(itemstack1)) {
                    if (!this.mergeItemStack(itemstack1, 4, 5, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 6 && index < 33) {
                    if (!this.mergeItemStack(itemstack1, 33, 42, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 33 && index < 42 && !this.mergeItemStack(itemstack1, 6, 33, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 6, 42, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }

    public static boolean isFuel(ItemStack item) {
        return net.minecraftforge.common.ForgeHooks.getBurnTime(item) > 0;
    }

    protected boolean isJar(ItemStack itemStack) {
        return itemStack.getItem() == ItemList.JAR;
    }

    protected boolean func_217057_a(ItemStack p_217057_1_) {
        return this.world.getRecipeManager().getRecipe((IRecipeType)this.recipeType, new Inventory(p_217057_1_), this.world).isPresent();
    }

    @OnlyIn(Dist.CLIENT)
    public int getCookProgressionScaled() {
        int i = this.intArray.get(2);
        int j = this.intArray.get(3);
        return j != 0 && i != 0 ? i * 24 / j : 0;
    }

    @OnlyIn(Dist.CLIENT)
    public int getBurnLeftScaled() {
        int i = this.intArray.get(1);
        if (i == 0) {
            i = 200;
        }

        return this.intArray.get(0) * 13 / i;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean func_217061_l() {
        return this.intArray.get(0) > 0;
    }

}
