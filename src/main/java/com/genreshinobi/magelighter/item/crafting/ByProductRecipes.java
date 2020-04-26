package com.genreshinobi.magelighter.item.crafting;

import com.genreshinobi.magelighter.Magelighter;
import com.genreshinobi.magelighter.lists.ItemList;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Map.Entry;

public class ByProductRecipes {
    public static Logger LOGGER = Magelighter.LOGGER;
    private static final ByProductRecipes INSTANCE = new ByProductRecipes();
    private final Table<ItemStack, ItemStack, ItemStack> byProductList = HashBasedTable.create();
    private final Map<ItemStack, Float> experienceList = Maps.newHashMap();
    private final Map<ItemStack, Double> chanceList = Maps.newHashMap();

    public static ByProductRecipes getInstance() {
        return INSTANCE;
    }

    private ByProductRecipes() {
        // Ashen Fond Recipes
        addByProductRecipe(new ItemStack(Items.POTATO), new ItemStack(ItemList.JAR), new ItemStack(ItemList.ASHEN_FOND), 0F, 0.3D);
        addByProductRecipe(new ItemStack(Items.CHICKEN), new ItemStack(ItemList.JAR), new ItemStack(ItemList.ASHEN_FOND), 0F,0.3D);
        addByProductRecipe(new ItemStack(Items.COD), new ItemStack(ItemList.JAR), new ItemStack(ItemList.ASHEN_FOND), 0F, 0.3D);
        addByProductRecipe(new ItemStack(Items.MUTTON), new ItemStack(ItemList.JAR), new ItemStack(ItemList.ASHEN_FOND), 0F, 0.3D);
        addByProductRecipe(new ItemStack(Items.PORKCHOP), new ItemStack(ItemList.JAR), new ItemStack(ItemList.ASHEN_FOND), 0F, 0.3D);
        addByProductRecipe(new ItemStack(Items.SALMON), new ItemStack(ItemList.JAR), new ItemStack(ItemList.ASHEN_FOND), 0F, 0.3D);
        addByProductRecipe(new ItemStack(Items.RABBIT), new ItemStack(ItemList.JAR), new ItemStack(ItemList.ASHEN_FOND), 0F, 0.3D);
        addByProductRecipe(new ItemStack(Items.BEEF), new ItemStack(ItemList.JAR), new ItemStack(ItemList.ASHEN_FOND), 0F, 0.3D);
        addByProductRecipe(new ItemStack(Items.KELP), new ItemStack(ItemList.JAR), new ItemStack(ItemList.ASHEN_FOND), 0F, 0.3D);

        // Dustment Recipes
        addByProductRecipe(new ItemStack(Items.IRON_ORE), new ItemStack(ItemList.JAR), new ItemStack(ItemList.RUIN_DUST), 0F, 0.3D);
        addByProductRecipe(new ItemStack(Items.GOLD_ORE), new ItemStack(ItemList.JAR), new ItemStack(ItemList.PRESERVATION_DUST), 0F, 0.3D);
    }

    public void addByProductRecipe(ItemStack input1, ItemStack input2, ItemStack result, float experience, double chance) {
        if(getByProduct(input1, input2) != ItemStack.EMPTY) return;
        this.byProductList.put(input1, input2, result);
        this.experienceList.put(result, experience);
        this.chanceList.put(input1, chance);
    }

    public ItemStack getByProduct(ItemStack input1, ItemStack input2) {
        for (Entry<ItemStack, Map<ItemStack, ItemStack>> entry : this.byProductList.columnMap().entrySet()) {
            if (this.compareItemStacks(input2, entry.getKey())) {
                for (Entry<ItemStack, ItemStack> ent : entry.getValue().entrySet()) {
                    if (this.compareItemStacks(input1, ent.getKey())) {
                        return ent.getValue();
                    }
                }
            }
        }
        return ItemStack.EMPTY;
    }

    private boolean compareItemStacks(ItemStack stack1, ItemStack stack2) {
        return stack2.getItem() == stack1.getItem();
    }

    public Table<ItemStack, ItemStack, ItemStack> getByProductList() {
        return this.byProductList;
    }

    public float getByProductExperience(ItemStack stack) {
        for (Entry<ItemStack, Float> entry : this.experienceList.entrySet()) {
            if(this.compareItemStacks(stack, entry.getKey())) {
                return entry.getValue();
            }
        }
        return 0.0F;
    }

    public boolean checkByProductChance(ItemStack stack) {
        int upper = 100;
        int lower = 0;
        int r = (int) (Math.random() * (upper - lower)) + lower;
        double chance = getByProductChance(stack);
        return r < chance;
    }

    public double getByProductChance(ItemStack stack) {
        for (Entry<ItemStack, Double> entry : this.chanceList.entrySet()) {
            if(this.compareItemStacks(stack, entry.getKey())) {
                return entry.getValue();
            }
        }
        return 0.0D;
    }
}
