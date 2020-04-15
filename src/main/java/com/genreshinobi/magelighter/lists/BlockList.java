package com.genreshinobi.magelighter.lists;

import com.genreshinobi.magelighter.inventory.container.ClericsOvenContainer;
import com.genreshinobi.magelighter.tileentities.ClericsOvenEntity;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;

public class BlockList {
    @ObjectHolder("magelighter:clerics_oven")
    public static Block CLERICSOVEN;
    @ObjectHolder("magelighter:clerics_oven")
    public static TileEntityType<ClericsOvenEntity> CLERICSOVEN_TILE;
    @ObjectHolder("magelighter:clerics_oven")
    public static ContainerType<ClericsOvenContainer> CLERICSOVEN_CONTAINER;

}