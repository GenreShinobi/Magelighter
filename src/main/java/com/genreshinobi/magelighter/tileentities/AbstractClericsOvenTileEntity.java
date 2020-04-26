package com.genreshinobi.magelighter.tileentities;

import com.genreshinobi.magelighter.Magelighter;
import com.genreshinobi.magelighter.item.crafting.ByProductRecipes;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.Blocks;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IRecipeHelperPopulator;
import net.minecraft.inventory.IRecipeHolder;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.Tag;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public abstract class AbstractClericsOvenTileEntity extends LockableTileEntity implements ISidedInventory, IRecipeHolder, IRecipeHelperPopulator, ITickableTileEntity {
    private static final Logger LOGGER = Magelighter.LOGGER;
    private static final int[] SLOTS_UP = new int[]{0};
    private static final int[] SLOTS_DOWN = new int[]{2, 1};
    private static final int[] SLOTS_HORIZONTAL = new int[]{1};
    protected NonNullList<ItemStack> items = NonNullList.withSize(6, ItemStack.EMPTY);
    private int burnTime, recipesUsed, cookTime, cookTimeTotal;
    protected final IIntArray furnaceData = new IIntArray() {
        public int get(int index) {
            switch(index) {
                case 0:
                    return AbstractClericsOvenTileEntity.this.burnTime;
                case 1:
                    return AbstractClericsOvenTileEntity.this.recipesUsed;
                case 2:
                    return AbstractClericsOvenTileEntity.this.cookTime;
                case 3:
                    return AbstractClericsOvenTileEntity.this.cookTimeTotal;
                default:
                    return 0;
            }
        }

        public void set(int index, int value) {
            switch(index) {
                case 0:
                    AbstractClericsOvenTileEntity.this.burnTime = value;
                    break;
                case 1:
                    AbstractClericsOvenTileEntity.this.recipesUsed = value;
                    break;
                case 2:
                    AbstractClericsOvenTileEntity.this.cookTime = value;
                    break;
                case 3:
                    AbstractClericsOvenTileEntity.this.cookTimeTotal = value;
            }
        }

        public int size() {
            return 4;
        }
    };
    private final Map<ResourceLocation, Integer> mapHash = Maps.newHashMap();
    protected final IRecipeType<? extends AbstractCookingRecipe> recipeType;

    protected AbstractClericsOvenTileEntity(TileEntityType<?> tileTypeIn, IRecipeType<? extends AbstractCookingRecipe> recipeTypeIn) {
        super(tileTypeIn);
        this.recipeType = recipeTypeIn;
    }

    @Deprecated // Forge - get burn times by calling ForgeHooks#getBurnTime(ItemStack)
    public static Map<Item, Integer> getBurnTimes() {
        return Maps.newLinkedHashMap();
    }

    private static void addItemTagBurnTime(Map<Item, Integer> map, Tag<Item> itemTag, int burnTimeIn) {
        for(Item item : itemTag.getAllElements()) {
            map.put(item, burnTimeIn);
        }
    }

    private static void addItemBurnTime(Map<Item, Integer> map, IItemProvider itemProvider, int burnTimeIn) {
        map.put(itemProvider.asItem(), burnTimeIn);
    }

    private boolean isBurning() {
        return this.burnTime > 0;
    }

    public void read(CompoundNBT compound) {
        super.read(compound);
        this.items = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, this.items);
        this.burnTime = compound.getInt("BurnTime");
        this.cookTime = compound.getInt("CookTime");
        this.cookTimeTotal = compound.getInt("CookTimeTotal");
        this.recipesUsed = net.minecraftforge.common.ForgeHooks.getBurnTime(this.items.get(1));
        int i = compound.getShort("RecipesUsedSize");

        for(int j = 0; j < i; ++j) {
            ResourceLocation resourcelocation = new ResourceLocation(compound.getString("RecipeLocation" + j));
            int k = compound.getInt("RecipeAmount" + j);
            this.mapHash.put(resourcelocation, k);
        }

    }

    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.putInt("BurnTime", this.burnTime);
        compound.putInt("CookTime", this.cookTime);
        compound.putInt("CookTimeTotal", this.cookTimeTotal);
        ItemStackHelper.saveAllItems(compound, this.items);
        compound.putShort("RecipesUsedSize", (short)this.mapHash.size());
        int i = 0;

        for(Map.Entry<ResourceLocation, Integer> entry : this.mapHash.entrySet()) {
            compound.putString("RecipeLocation" + i, entry.getKey().toString());
            compound.putInt("RecipeAmount" + i, entry.getValue());
            ++i;
        }

        return compound;
    }

    public void tick() {

        boolean flag = this.isBurning();
        boolean flag1 = false;
        if (this.isBurning()) {
            --this.burnTime;
        }

        assert this.world != null;
        if (!this.world.isRemote) {
            ItemStack itemstack = this.items.get(1);
            if (this.isBurning() || !itemstack.isEmpty() && !this.items.get(0).isEmpty()) {
                IRecipe<?> irecipe = this.world.getRecipeManager().getRecipe((IRecipeType<AbstractCookingRecipe>)this.recipeType, this, this.world).orElse(null);
                if (!this.isBurning() && this.canSmelt(irecipe)) {
                    this.burnTime = net.minecraftforge.common.ForgeHooks.getBurnTime(itemstack);
                    this.recipesUsed = this.burnTime;
                    if (this.isBurning()) {
                        flag1 = true;
                        if (itemstack.hasContainerItem())
                            this.items.set(1, itemstack.getContainerItem());
                        else
                        if (!itemstack.isEmpty()) {
                            Item item = itemstack.getItem();
                            itemstack.shrink(1);
                            if (itemstack.isEmpty()) {
                                this.items.set(1, itemstack.getContainerItem());
                            }
                        }
                    }
                }

                if (this.isBurning() && this.canSmelt(irecipe)) {
                    ++this.cookTime;
                    if (this.cookTime == this.cookTimeTotal) {
                        this.cookTime = 0;
                        this.cookTimeTotal = this.func_214005_h();
                        this.smelt(irecipe);
                        flag1 = true;
                    }
                } else {
                    this.cookTime = 0;
                }
            } else if (!this.isBurning() && this.cookTime > 0) {
                this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.cookTimeTotal);
            }

            if (flag != this.isBurning()) {
                flag1 = true;
                this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).with(AbstractFurnaceBlock.LIT, this.isBurning()), 3);
            }
        }

        if (flag1) {
            this.markDirty();
        }

    }

    protected boolean canSmelt(@Nullable IRecipe<?> recipeIn) {
        if (!this.items.get(0).isEmpty() && recipeIn != null) {
            ItemStack itemstack = recipeIn.getRecipeOutput();
            if (itemstack.isEmpty()) {
                return false;
            } else {
                ItemStack itemstack1 = this.items.get(2);
                if (itemstack1.isEmpty()) {
                    return true;
                } else if (!itemstack1.isItemEqual(itemstack)) {
                    return false;
                } else if (itemstack1.getCount() + itemstack.getCount() <= this.getInventoryStackLimit() && itemstack1.getCount() + itemstack.getCount() <= itemstack1.getMaxStackSize()) { // Forge fix: make furnace respect stack sizes in furnace recipes
                    return true;
                } else {
                    return itemstack1.getCount() + itemstack.getCount() <= itemstack.getMaxStackSize(); // Forge fix: make furnace respect stack sizes in furnace recipes
                }
            }
        } else {
            return false;
        }
    }

    private void func_214007_c(@Nullable IRecipe<?> p_214007_1_) {
        if (p_214007_1_ != null && this.canSmelt(p_214007_1_)) {
            ItemStack itemstack = this.items.get(0);
            ItemStack itemstack1 = p_214007_1_.getRecipeOutput();
            ItemStack itemstack2 = this.items.get(2);
            if (itemstack2.isEmpty()) {
                this.items.set(2, itemstack1.copy());
            } else if (itemstack2.getItem() == itemstack1.getItem()) {
                itemstack2.grow(itemstack1.getCount());
            }

            if (!this.world.isRemote) {
                this.setRecipeUsed(p_214007_1_);
            }

            if (itemstack.getItem() == Blocks.WET_SPONGE.asItem() && !this.items.get(1).isEmpty() && this.items.get(1).getItem() == Items.BUCKET) {
                this.items.set(1, new ItemStack(Items.WATER_BUCKET));
            }

            itemstack.shrink(1);
        }
    }

    protected int getBurnTime(ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return 0;
        } else {
            Item item = itemStack.getItem();
            return net.minecraftforge.common.ForgeHooks.getBurnTime(itemStack);
        }
    }

    protected int func_214005_h() {
        return this.world.getRecipeManager().getRecipe((IRecipeType<AbstractCookingRecipe>)this.recipeType, this, this.world).map(AbstractCookingRecipe::getCookTime).orElse(200);
    }

    public static boolean isFuel(ItemStack p_213991_0_) {
        return p_213991_0_.getItem() == Items.BLAZE_POWDER;
    }

    public int[] getSlotsForFace(Direction side) {
        if (side == Direction.DOWN) {
            return SLOTS_DOWN;
        } else {
            return side == Direction.UP ? SLOTS_UP : SLOTS_HORIZONTAL;
        }
    }

    /**
     * Returns true if automation can insert the given item in the given slot from the given side.
     */
    public boolean canInsertItem(int index, ItemStack itemStackIn, @Nullable Direction direction) {
        return this.isItemValidForSlot(index, itemStackIn);
    }

    /**
     * Returns true if automation can extract the given item in the given slot from the given side.
     */
    public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
        if (direction == Direction.DOWN && index == 1) {
            Item item = stack.getItem();
            if (item != Items.WATER_BUCKET && item != Items.BUCKET) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory() {
        return this.items.size();
    }

    public boolean isEmpty() {
        for(ItemStack itemstack : this.items) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns the stack in the given slot.
     */
    public ItemStack getStackInSlot(int index) {
        return this.items.get(index);
    }

    /**
     * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
     */
    public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(this.items, index, count);
    }

    /**
     * Removes a stack from the given slot and returns it.
     */
    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(this.items, index);
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int index, ItemStack stack) {
        ItemStack itemstack = this.items.get(index);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
        this.items.set(index, stack);
        if (stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }

        if (index == 0 && !flag) {
            this.cookTimeTotal = this.func_214005_h();
            this.cookTime = 0;
            this.markDirty();
        }

    }

    /**
     * Don't rename this method to canInteractWith due to conflicts with Container
     */
    public boolean isUsableByPlayer(PlayerEntity player) {
        if (this.world.getTileEntity(this.pos) != this) {
            return false;
        } else {
            return player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
        }
    }

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot. For
     * guis use Slot.isItemValid
     */
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        if (index == 2) {
            return false;
        } else if (index != 1) {
            return true;
        } else {
            ItemStack itemstack = this.items.get(1);
            return isFuel(stack) || stack.getItem() == Items.BUCKET && itemstack.getItem() != Items.BUCKET;
        }
    }

    public void clear() {
        this.items.clear();
    }

    public void setRecipeUsed(@Nullable IRecipe<?> recipe) {
        if (recipe != null) {
            this.mapHash.compute(recipe.getId(), (p_214004_0_, p_214004_1_) -> {
                return 1 + (p_214004_1_ == null ? 0 : p_214004_1_);
            });
        }

    }

    @Nullable
    public IRecipe<?> getRecipeUsed() {
        return null;
    }

    public void onCrafting(PlayerEntity player) {
    }

    public void func_213995_d(PlayerEntity p_213995_1_) {
        List<IRecipe<?>> list = Lists.newArrayList();

        for(Map.Entry<ResourceLocation, Integer> entry : this.mapHash.entrySet()) {
            p_213995_1_.world.getRecipeManager().getRecipe(entry.getKey()).ifPresent((p_213993_3_) -> {
                list.add(p_213993_3_);
                func_214003_a(p_213995_1_, entry.getValue(), ((AbstractCookingRecipe)p_213993_3_).getExperience());
            });
        }

        p_213995_1_.unlockRecipes(list);
        this.mapHash.clear();
    }

    private static void func_214003_a(PlayerEntity p_214003_0_, int p_214003_1_, float p_214003_2_) {
        if (p_214003_2_ == 0.0F) {
            p_214003_1_ = 0;
        } else if (p_214003_2_ < 1.0F) {
            int i = MathHelper.floor((float)p_214003_1_ * p_214003_2_);
            if (i < MathHelper.ceil((float)p_214003_1_ * p_214003_2_) && Math.random() < (double)((float)p_214003_1_ * p_214003_2_ - (float)i)) {
                ++i;
            }

            p_214003_1_ = i;
        }

        while(p_214003_1_ > 0) {
            int j = ExperienceOrbEntity.getXPSplit(p_214003_1_);
            p_214003_1_ -= j;
            p_214003_0_.world.addEntity(new ExperienceOrbEntity(p_214003_0_.world, p_214003_0_.getPosX(), p_214003_0_.getPosY() + 0.5D, p_214003_0_.getPosZ() + 0.5D, j));
        }

    }

    public void fillStackedContents(RecipeItemHelper helper) {
        for(ItemStack itemstack : this.items) {
            helper.accountStack(itemstack);
        }

    }

    net.minecraftforge.common.util.LazyOptional<? extends net.minecraftforge.items.IItemHandler>[] handlers =
            net.minecraftforge.items.wrapper.SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);

    @Override
    public <T> net.minecraftforge.common.util.LazyOptional<T> getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable Direction facing) {
        if (!this.removed && facing != null && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (facing == Direction.UP)
                return handlers[0].cast();
            else if (facing == Direction.DOWN)
                return handlers[1].cast();
            else
                return handlers[2].cast();
        }
        return super.getCapability(capability, facing);
    }

    /**
     * invalidates a tile entity
     */
    @Override
    public void remove() {
        super.remove();
        for (int x = 0; x < handlers.length; x++)
            handlers[x].invalidate();
    }

    private boolean canProduceByProduct() {
        if(((ItemStack)this.items.get(0)).isEmpty() || ((ItemStack)this.items.get(3)).isEmpty()) return false;
        else {
            ItemStack result = ByProductRecipes.getInstance().getByProduct((ItemStack)this.items.get(0), (ItemStack)this.items.get(3));
            if(result.isEmpty()) return false;
            else {
                ItemStack byProduct = (ItemStack)this.items.get(4);
                if(byProduct.isEmpty()) return true;
                if(!byProduct.isItemEqual(result)) return false;
                int res = byProduct.getCount() + result.getCount();
                return res <= getInventoryStackLimit() && res <= byProduct.getMaxStackSize();
            }
        }
    }

    private void getByProduct() {
        try {
            // gets the chance for the current by product. Set when the recipe is created in ByProductRecipes
            double baseChance = ByProductRecipes.getInstance().getByProductChance(this.items.get(0));
            LOGGER.debug("" + this.items.get(0) + ": " + baseChance);

            // gets a random double, checks against the current chance of the by product, and confirms the jar slot is not empty
            assert world != null;
            if (world.rand.nextDouble() <= Math.min(baseChance, 1.0D) && !this.items.get(3).isEmpty()) {
                createByProduct(ByProductRecipes.getInstance().getByProduct(this.items.get(0),this.items.get(3)));
            }

        } catch (Throwable var1) {
            LOGGER.warn("Exception occurred while generating a by product from a clerics oven", var1);
        }
    }

    private void createByProduct(ItemStack byProduct) {
        // if there's nothing in byproduct slot, create the byproduct.
        if (this.items.get(4).isEmpty()) {
            this.items.set(4, byProduct.copy());

        // if the byproduct already exists, grow it.
        } else if ( this.items.get(4).isItemEqual(byProduct) && this.items.get(4).getCount() < this.items.get(4).getMaxStackSize()) {
            this.items.get(4).grow(1);
        }

        // consumes jar
        this.items.get(3).shrink(1);
    }

    private void smelt(@Nullable IRecipe<?> recipe) {
        if (recipe != null && this.canSmelt(recipe)) {
            ItemStack itemstack = this.items.get(0); // The input slot
            ItemStack itemstack1 = recipe.getRecipeOutput(); // The potential output
            ItemStack itemstack2 = this.items.get(2); // The output slot

            if (itemstack2.isEmpty()) {
                this.items.set(2, itemstack1.copy());
            } else if (itemstack2.isItemEqual(itemstack1)) {
                itemstack2.grow(itemstack1.getCount());
            }

            this.getByProduct();
            itemstack.shrink(1);

            if (!this.world.isRemote) {
                this.setRecipeUsed(recipe);
            }

            if (itemstack.getItem() == Blocks.WET_SPONGE.asItem() && !this.items.get(1).isEmpty() && this.items.get(1).getItem() == Items.BUCKET) {
                this.items.set(1, new ItemStack(Items.WATER_BUCKET));
            }
        }
    }
}
