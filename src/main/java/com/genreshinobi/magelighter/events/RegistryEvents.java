package com.genreshinobi.magelighter.events;

import com.genreshinobi.magelighter.Magelighter;
import com.genreshinobi.magelighter.blocks.ClericsOven;
import com.genreshinobi.magelighter.inventory.container.ClericsOvenContainer;
import com.genreshinobi.magelighter.lists.BlockList;
import com.genreshinobi.magelighter.lists.ItemList;
import com.genreshinobi.magelighter.tileentities.ClericsOvenEntity;
import com.genreshinobi.magelighter.util.MagelighterGroup;
import net.minecraft.block.Block;
import net.minecraft.block.OreBlock;
import net.minecraft.block.material.Material;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.potion.*;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class RegistryEvents {

    public static final Logger LOGGER = Magelighter.LOGGER;
    public static final String MOD_ID = Magelighter.MOD_ID;
    public static final ItemGroup MOD_GROUP = MagelighterGroup.instance;

    @SubscribeEvent
    public static void registerItems(final RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                //BlockItems
                ItemList.CLERICS_OVEN = new BlockItem(BlockList.CLERICS_OVEN, new Item.Properties().group(MOD_GROUP)).setRegistryName(Objects.requireNonNull(BlockList.CLERICS_OVEN.getRegistryName())),
                ItemList.COPPER_ORE = new BlockItem(BlockList.COPPER_ORE, new Item.Properties().group(MOD_GROUP)).setRegistryName(Objects.requireNonNull(BlockList.COPPER_ORE.getRegistryName())),
                ItemList.COPPER_BLOCK = new BlockItem(BlockList.COPPER_BLOCK, new Item.Properties().group(MOD_GROUP)).setRegistryName(Objects.requireNonNull(BlockList.COPPER_BLOCK.getRegistryName())),
                //Items
                ItemList.CLAY_JAR = new Item(new Item.Properties().group(MOD_GROUP)).setRegistryName(location("jar_clay")),
                ItemList.JAR = new Item(new Item.Properties().group(MOD_GROUP)).setRegistryName(location("jar")),
                ItemList.ASHEN_FOND = new Item(new Item.Properties().group(MOD_GROUP)).setRegistryName(location("ashen_fond")),
                ItemList.RUIN_DUST = new Item(new Item.Properties().group(MOD_GROUP)).setRegistryName(location("ruin_dust")),
                ItemList.AUTONOMY_DUST = new Item(new Item.Properties().group(MOD_GROUP)).setRegistryName(location("autonomy_dust")),
                ItemList.COPPER_INGOT = new Item(new Item.Properties().group(MOD_GROUP)).setRegistryName(location("copper_ingot")),
                ItemList.COPPER_NUGGET = new Item(new Item.Properties().group(MOD_GROUP)).setRegistryName(location("copper_nugget"))
        );
    }


    @SubscribeEvent
    public static void registerBlocks(final RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(
                BlockList.CLERICS_OVEN = new ClericsOven(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.5F).lightValue(13)).setRegistryName(location("clerics_oven")),
                BlockList.COPPER_ORE = (OreBlock) new OreBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F)).setRegistryName(location("copper_ore")),
                BlockList.COPPER_BLOCK = new Block(Block.Properties.create(Material.IRON).hardnessAndResistance(5.0F,6.0F)).setRegistryName(location("copper_block"))
        );
    }

    @SubscribeEvent
    public static void registerTileEntity(final RegistryEvent.Register<TileEntityType<?>> event) {
        event.getRegistry().register(TileEntityType.Builder.create(ClericsOvenEntity::new, BlockList.CLERICS_OVEN).build(null).setRegistryName(Objects.requireNonNull(BlockList.CLERICS_OVEN.getRegistryName())));
    }

    @SubscribeEvent
    public static void registerContainerEntity(final RegistryEvent.Register<ContainerType<?>> event) {
        event.getRegistry().register(IForgeContainerType.create((windowId, inv, data) -> new ClericsOvenContainer(windowId, inv)).setRegistryName(Objects.requireNonNull(BlockList.CLERICS_OVEN.getRegistryName())));
    }

    @SubscribeEvent
    public static void registerPotions(final RegistryEvent.Register<Potion> event) {
        event.getRegistry().registerAll(
        );
    }

    @SubscribeEvent
    public static void registerEffect(final RegistryEvent.Register<Effect> event) {
        event.getRegistry().registerAll();
    }

    private static ResourceLocation Location(String name) {
        return new ResourceLocation(MOD_ID, name);
    }

    public static ResourceLocation location(String name) {
        return new ResourceLocation(MOD_ID, name);
    }
}
