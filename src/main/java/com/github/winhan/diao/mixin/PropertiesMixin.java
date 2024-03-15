package com.github.winhan.diao.mixin;

import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Properties.class)
public class PropertiesMixin {
    @Unique
    static BooleanProperty WHITELIST;

    static {
        WHITELIST = BooleanProperty.of("whitelist");
    }
}