package com.genreshinobi.magelighter.util;

import com.genreshinobi.magelighter.lists.ItemList;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class MagelighterGroup extends ItemGroup {

    public static final MagelighterGroup instance = new MagelighterGroup(ItemGroup.GROUPS.length, "magelighter_group");

    private MagelighterGroup(int index, String label) { super(index, label); }

    @Override
    public ItemStack createIcon() { return new ItemStack(ItemList.JAR); }
}
