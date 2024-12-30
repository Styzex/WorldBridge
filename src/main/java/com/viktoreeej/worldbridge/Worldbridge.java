package com.viktoreeej.worldbridge;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


@Mod(modid = Worldbridge.MODID, version = Worldbridge.VERSION)
public class Worldbridge
{
    public static final String MODID = "worldbridge";
    public static final String VERSION = "0.1";
    private static final Logger LOGGER = LogManager.getLogger();

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        LOGGER.info("Initializing WorldBridge");
        MinecraftForge.EVENT_BUS.register(this);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(new ShareIPCommand());
    }
}
