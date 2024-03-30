package com.github.winhan.diao;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.impl.client.indigo.Indigo;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**To do:
 * add config ()**/
public class ConfigsClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger(Indigo.class);

    @Override
    public void onInitializeClient() {
        File configDir =  FabricLoader.getInstance().getConfigDir().resolve("extra_terracotta_utilities").toFile();

        if (!configDir.exists()) {
            if (!configDir.mkdir()) {
                LOGGER.warn("Could not create configuration directory: " + configDir.getAbsolutePath());
            }
        }

        File configFile = new File(configDir, "extra_terracotta_utilities_client.properties");

        if (!configFile.exists()) {
            try {
                if (!configFile.createNewFile()) {
                    LOGGER.warn("[Indigo] Could not create configuration file: " + configFile.getAbsolutePath());
                }
            } catch (IOException ignored) {}
        }

    }

    public static void configInitialize (File configFile) {

    }
}
