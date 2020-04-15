package com.genreshinobi.magelighter.setup;

import com.genreshinobi.magelighter.client.gui.screen.inventory.ClericsOvenScreen;
import com.genreshinobi.magelighter.lists.BlockList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class ClientProxy {
    public World getClientWorld() {
        return Minecraft.getInstance().world;
    }

    public PlayerEntity getClientPlayer() {
        return Minecraft.getInstance().player;
    }

    public static void init() {
        ScreenManager.registerFactory(BlockList.CLERICSOVEN_CONTAINER, ClericsOvenScreen::new);
    }
}
