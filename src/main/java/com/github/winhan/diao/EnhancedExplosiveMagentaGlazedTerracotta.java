package com.github.winhan.diao;

import net.minecraft.block.Blocks;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EnhancedExplosiveMagentaGlazedTerracotta extends ExplosiveMagentaGlazedTerracotta {
    public EnhancedExplosiveMagentaGlazedTerracotta(Settings settings) {
        super(settings);
    }

    @Override
    public float getMaxBlastResistance() {
        return Blocks.OBSIDIAN.getBlastResistance();
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        tooltip.add(Text.translatable("block.extra_terracotta_utilities.enhanced_explosive_magenta_glazed_terracotta.tooltip.add_1", Text.translatable(Initializer.EXPLOSIVE_MAGENTA_GLAZED_TERRACOTTA.getTranslationKey())).formatted(Formatting.DARK_GRAY));
        tooltip.add(Text.translatable("block.extra_terracotta_utilities.enhanced_explosive_magenta_glazed_terracotta.tooltip.add_2", Blocks.OBSIDIAN.getBlastResistance()).formatted(Formatting.DARK_GRAY));
        super.appendTooltip(stack, world, tooltip, options);
    }
}
