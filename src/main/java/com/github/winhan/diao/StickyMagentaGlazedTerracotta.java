package com.github.winhan.diao;

import net.minecraft.block.BlockState;
import net.minecraft.block.SlimeBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class StickyMagentaGlazedTerracotta extends SlimeBlock {
    public StickyMagentaGlazedTerracotta(Settings settings) {
        super(settings);
    }

    /**To do: 
     * need formalize ( )**/
    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        if (!entity.isSneaking() && !(entity instanceof ExperienceOrbEntity)) {
            entity.setVelocity(entity.getVelocity().x, 1, entity.getVelocity().z);
            world.playSound(null, pos, SoundEvents.BLOCK_SLIME_BLOCK_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
        }
    }

    @Override
    public void onEntityLand(BlockView world, Entity entity) {
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        tooltip.add(Text.translatable("tooltip.extra_terracotta_utilities.function").formatted(Formatting.GOLD));
        tooltip.add(Text.translatable("block.extra_terracotta_utilities.sticky_magenta_glazed_terracotta.tooltip.function_1").formatted(Formatting.GOLD));
        tooltip.add(Text.translatable("block.extra_terracotta_utilities.sticky_magenta_glazed_terracotta.tooltip.function_2").formatted(Formatting.GOLD));

    }
}
