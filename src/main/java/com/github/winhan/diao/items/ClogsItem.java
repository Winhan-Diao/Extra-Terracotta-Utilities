package com.github.winhan.diao.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ClogsItem extends Item {
    private final int maxCount;

    public ClogsItem(Properties pProperties, int maxCount) {
        super(pProperties);
        this.maxCount = maxCount;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> tooltip, TooltipFlag pIsAdvanced) {
        tooltip.add(Component.translatable("item.extra_terracotta_utilities.clogs.tooltip.maxcount").append(String.valueOf(maxCount)));
        tooltip.add(Component.translatable("tooltip.extra_terracotta_utilities.function").withStyle(ChatFormatting.GOLD));
        tooltip.add(Component.translatable("item.extra_terracotta_utilities.clogs.tooltip.function_1").withStyle(ChatFormatting.GOLD));
        tooltip.add(Component.translatable("item.extra_terracotta_utilities.clogs.tooltip.function_2").withStyle(ChatFormatting.GOLD));
        tooltip.add(Component.translatable("item.extra_terracotta_utilities.clogs.tooltip.function_3").withStyle(ChatFormatting.GOLD));
        tooltip.add(Component.translatable("tooltip.extra_terracotta_utilities.usage").withStyle(ChatFormatting.DARK_AQUA));
        tooltip.add(Component.translatable("item.extra_terracotta_utilities.clogs.tooltip.usage_1").withStyle(ChatFormatting.DARK_AQUA));

    }

}
