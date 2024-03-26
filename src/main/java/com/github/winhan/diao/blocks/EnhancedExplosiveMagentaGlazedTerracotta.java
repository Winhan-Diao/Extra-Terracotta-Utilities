package com.github.winhan.diao.blocks;

import com.github.winhan.diao.initialize.Initializer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EnhancedExplosiveMagentaGlazedTerracotta extends ExplosiveMagentaGlazedTerracotta {

    public float getMaxBlastResistance() {
        return Blocks.OBSIDIAN.getExplosionResistance();
    }

    public EnhancedExplosiveMagentaGlazedTerracotta(Properties p_52591_) {
        super(p_52591_);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        pTooltip.add(Component.translatable("block.extra_terracotta_utilities.enhanced_explosive_magenta_glazed_terracotta.tooltip.add_1", Component.translatable(Initializer.EXPLOSIVE_MAGENTA_GLAZED_TERRACOTTA.getId().toLanguageKey())).withStyle(ChatFormatting.DARK_GRAY));
        pTooltip.add(Component.translatable("block.extra_terracotta_utilities.enhanced_explosive_magenta_glazed_terracotta.tooltip.add_2", Blocks.OBSIDIAN.getExplosionResistance()).withStyle(ChatFormatting.DARK_GRAY));
        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
    }

}