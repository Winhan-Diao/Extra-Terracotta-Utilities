package com.github.winhan.diao;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.util.TriState;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

public class ConfigsCommon implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger(ConfigsCommon.class);
    public static final int HAUNTED_TRANSFORM_TIME;
    private static boolean asBoolean(String property, boolean defValue) {
        return switch (asTriState(property)) {
            case TRUE -> true;
            case FALSE -> false;
            default -> defValue;
        };
    }

    public static int asInt (String propertyStr, int defProperty) {
        System.out.println(propertyStr);
        return Integer.parseInt(propertyStr);
    }

    private static TriState asTriState(String property) {
        if (property == null || property.isEmpty()) {
            return TriState.DEFAULT;
        } else {
            return switch (property.toLowerCase(Locale.ROOT)) {
                case "true" -> TriState.TRUE;
                case "false" -> TriState.FALSE;
                default -> TriState.DEFAULT;
            };
        }
    }

    @Override
    public void onInitialize() {}
    static {
        File configDir =  FabricLoader.getInstance().getConfigDir().resolve("extra_terracotta_utilities").toFile();

        if (!configDir.exists()) {
            if (!configDir.mkdir()) {
                LOGGER.warn("Could not create configuration directory: " + configDir.getAbsolutePath());
            }
        }

        File configFile = new File(configDir, "extra_terracotta_utilities_common.properties");
        Properties properties = new Properties();

        if (configFile.exists()) {
            try (FileInputStream stream = new FileInputStream(configFile)) {
                properties.load(stream);
            } catch (IOException e) {
                LOGGER.warn("Could not read property file '" + configFile.getAbsolutePath() + "'", e);
            }
        }

        HAUNTED_TRANSFORM_TIME = Integer.parseInt((String)properties.compute("haunted-magenta-glazed-terracotta-haunting-time", (k, v) -> checkIntValidity((String) v, 0, null) ? v : "200"));

        try (FileOutputStream stream = new FileOutputStream(configFile)) {
            properties.store(stream, "Common Settings");
        } catch (IOException e) {
            LOGGER.warn("Could not store property file '" + configFile.getAbsolutePath() + "'", e);
        }

    }

    public static boolean checkIntValidity(String target, @Nullable Integer origin, @Nullable Integer bound) {
        if (target == null || target.isEmpty()) return false;
        try {
            int targetInt = Integer.parseInt(target);
            return (origin == null || targetInt >= origin) && (bound == null || targetInt < bound);
        } catch (NumberFormatException e) {
            return false;
        }
    }


}
