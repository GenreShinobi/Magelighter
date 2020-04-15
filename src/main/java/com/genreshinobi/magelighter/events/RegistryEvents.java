package com.genreshinobi.magelighter.events;

import com.genreshinobi.magelighter.Magelighter;
import com.genreshinobi.magelighter.blocks.ClericsOven;
import com.genreshinobi.magelighter.inventory.container.ClericsOvenContainer;
import com.genreshinobi.magelighter.lists.BlockList;
import com.genreshinobi.magelighter.lists.ItemList;
import com.genreshinobi.magelighter.lists.PotionList;
import com.genreshinobi.magelighter.tileentities.ClericsOvenEntity;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
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

import java.util.UUID;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class RegistryEvents {

    public static final Logger LOGGER = Magelighter.LOGGER;
    public static final String MOD_ID = Magelighter.MOD_ID;

    @SubscribeEvent
    public static void registerItems(final RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
            ItemList.CLERICSOVEN = new BlockItem(BlockList.CLERICSOVEN, new Item.Properties().group(ItemGroup.MISC)).setRegistryName(BlockList.CLERICSOVEN.getRegistryName()));
    }

    @SubscribeEvent
    public static void registerBlocks(final RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(
                BlockList.CLERICSOVEN = new ClericsOven(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.5F).lightValue(13)).setRegistryName(location("clerics_oven")));
    }

    @SubscribeEvent
    public static void registerTileEntity(final RegistryEvent.Register<TileEntityType<?>> event) {
        event.getRegistry().register(TileEntityType.Builder.create(ClericsOvenEntity::new, BlockList.CLERICSOVEN).build(null).setRegistryName(BlockList.CLERICSOVEN.getRegistryName()));
    }

    @SubscribeEvent
    public static void registerContainerEntity(final RegistryEvent.Register<ContainerType<?>> event) {
        event.getRegistry().register(IForgeContainerType.create((windowId, inv, data) -> {
            return new ClericsOvenContainer(windowId, inv);
        }).setRegistryName(BlockList.CLERICSOVEN.getRegistryName()));
    }

    @SubscribeEvent
    public static void registerPotions(final RegistryEvent.Register<Potion> event) {
        event.getRegistry().registerAll(
            PotionList.more_health_potion = new Potion(new EffectInstance(PotionList.more_health_effect, 3600)).setRegistryName(Location("more_health"))
        );
    }

    @SubscribeEvent
    public static void registerEffect(final RegistryEvent.Register<Effect> event) {
        event.getRegistry().registerAll(
                PotionList.more_health_effect = new PotionList.MoreHealthEffect(EffectType.BENEFICIAL, 0xd4ff00).addAttributesModifier(SharedMonsterAttributes.MAX_HEALTH, UUID.randomUUID().toString(), (double)0.5F, AttributeModifier.Operation.MULTIPLY_TOTAL).setRegistryName("more_health")
        );
    }

    private static ResourceLocation Location(String name) {
        return new ResourceLocation(MOD_ID, name);
    }

    public static ResourceLocation location(String name) {
        return new ResourceLocation(MOD_ID, name);
    }
}
