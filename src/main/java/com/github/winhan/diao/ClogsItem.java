package com.github.winhan.diao;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ClogsItem extends Item {
    private final int maxCount;
    public ClogsItem(Settings settings, int maxCount) {
        super(settings);
        this.maxCount = maxCount;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("item.extra_terracotta_utilities.clogs.tooltip.maxcount").append(String.valueOf(maxCount)));
        tooltip.add(Text.translatable("tooltip.extra_terracotta_utilities.function").formatted(Formatting.GOLD));
        tooltip.add(Text.translatable("item.extra_terracotta_utilities.clogs.tooltip.function_1").formatted(Formatting.GOLD));
        tooltip.add(Text.translatable("item.extra_terracotta_utilities.clogs.tooltip.function_2").formatted(Formatting.GOLD));
        tooltip.add(Text.translatable("item.extra_terracotta_utilities.clogs.tooltip.function_3").formatted(Formatting.GOLD));
        tooltip.add(Text.translatable("tooltip.extra_terracotta_utilities.usage").formatted(Formatting.DARK_AQUA));
        tooltip.add(Text.translatable("item.extra_terracotta_utilities.clogs.tooltip.usage_1").formatted(Formatting.DARK_AQUA));

    }
}
