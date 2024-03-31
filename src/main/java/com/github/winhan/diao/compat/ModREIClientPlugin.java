package com.github.winhan.diao.compat;

import com.github.winhan.diao.Initializer;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;

public class ModREIClientPlugin implements REIClientPlugin {

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new HauntingCategory());
        registry.addWorkstations(HauntingCategory.HAUNTING, EntryStacks.of(Initializer.HAUNTED_MAGENTA_GLAZED_TERRACOTTA));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {

        registry.add(new HauntingDisplay(Blocks.NETHERRACK.asItem(), Items.ZOMBIE_SPAWN_EGG));
        registry.add(new HauntingDisplay(Blocks.NETHER_GOLD_ORE.asItem(), Items.ZOMBIFIED_PIGLIN_SPAWN_EGG));
        registry.add(new HauntingDisplay(Blocks.MAGMA_BLOCK.asItem(), Items.MAGMA_CUBE_SPAWN_EGG));
        registry.add(new HauntingDisplay(Blocks.GLOWSTONE.asItem(), Items.BLAZE_SPAWN_EGG));
        registry.add(new HauntingDisplay(Blocks.COAL_BLOCK.asItem(), Items.WITHER_SKELETON_SPAWN_EGG));
        registry.add(new HauntingDisplay(Blocks.PURPUR_BLOCK.asItem(), Items.SHULKER_SPAWN_EGG));
        registry.add(new HauntingDisplay(Blocks.CRYING_OBSIDIAN.asItem(), Items.GHAST_SPAWN_EGG));
        registry.add(new HauntingDisplay(Blocks.AMETHYST_CLUSTER.asItem(), Items.ALLAY_SPAWN_EGG));
        registry.add(new HauntingDisplay(Blocks.DRIED_KELP_BLOCK.asItem(), Items.DROWNED_SPAWN_EGG));

    }


}
