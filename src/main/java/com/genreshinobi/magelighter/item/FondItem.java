package com.genreshinobi.magelighter.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.villager.IVillagerType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

public class FondItem extends Item {
    public FondItem(Item.Properties builder) {
        super(builder);
        setRegistryName("ashen_fond");
    }
}
