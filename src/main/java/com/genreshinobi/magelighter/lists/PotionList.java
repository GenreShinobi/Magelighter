package com.genreshinobi.magelighter.lists;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.potion.*;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PotionList {
    public static Potion more_health_potion = null;
    public static Effect more_health_effect = null;

    private static Method brewing_mixes;

    private static void addMix(Potion start, Item ingredient, Potion result) {
        if(brewing_mixes == null) {
            brewing_mixes = ObfuscationReflectionHelper.findMethod(PotionBrewing.class, "addMix", Potion.class, Item.class, Potion.class);
            brewing_mixes.setAccessible(true);
        }

        try {
            brewing_mixes.invoke(null, start, ingredient, result);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static void addBrewingRecipes() {
        addMix(Potions.AWKWARD, Items.COAL, PotionList.more_health_potion);
    }

    public static class MoreHealthEffect extends Effect {
        public MoreHealthEffect(EffectType typeIn, int liquidColorIn) {
            super(typeIn, liquidColorIn);
        }
    }
}
