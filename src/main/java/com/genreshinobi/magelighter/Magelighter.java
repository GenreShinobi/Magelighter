package com.genreshinobi.magelighter;

import com.genreshinobi.magelighter.client.gui.screen.inventory.ClericsOvenScreen;
import com.genreshinobi.magelighter.lists.BlockList;
import com.genreshinobi.magelighter.lists.PotionList;
import com.genreshinobi.magelighter.setup.ClientProxy;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Magelighter.MOD_ID)
public class Magelighter
{
    public static Magelighter instance;
    public static final String MOD_ID = "magelighter";
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();

    public Magelighter() {
        instance = this;

        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        PotionList.addBrewingRecipes();
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        ScreenManager.registerFactory(BlockList.CLERICSOVEN_CONTAINER, ClericsOvenScreen::new);
    }

    public void onServerStarting(final FMLClientSetupEvent event) {

    }

}
