package com.github.winhan.diao.blocks;

import com.github.winhan.diao.initialize.Initializer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WoodenMagentaGlazedTerracotta extends Block {
    public static final int LOG_CHANCE = 25;
    public static final int LEAVE_CHANCE = 25;
    public static final int DUP_CHANCE = 1;
    public WoodenMagentaGlazedTerracotta(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (pLevel.getBlockState(pPos.above()).isAir() && pLevel.getBlockState(pPos.below()).is(BlockTags.DIRT)) {
            int i = pRandom.nextInt(100);
            if (i < LOG_CHANCE) {
                pLevel.setBlock(pPos.above(), Blocks.OAK_LOG.defaultBlockState(), Block.UPDATE_ALL);
            } else if (i < LOG_CHANCE + LEAVE_CHANCE) {
                pLevel.setBlock(pPos.above(), Blocks.OAK_LEAVES.defaultBlockState().setValue(LeavesBlock.DISTANCE, 1), Block.UPDATE_ALL);
            } else if (i < LOG_CHANCE + LEAVE_CHANCE + DUP_CHANCE) {
                pLevel.setBlock(pPos.above(), Initializer.WOODEN_MAGENTA_GLAZED_TERRACOTTA.get().defaultBlockState(), Block.UPDATE_ALL);
            } else return;
            pLevel.playSound(null, pPos.above(), SoundEvents.MOSS_PLACE, SoundSource.BLOCKS, 1.0f, 1.0f);
            for(i = 0; i < 15; ++i) {
                pLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER, pPos.getX() + .5, pPos.above().getY() + .5, pPos.getZ() + .5, 1, 0.5, 0.5, 0.5, 0);
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        pTooltip.add(Component.translatable("tooltip.extra_terracotta_utilities.function").withStyle(ChatFormatting.GOLD));
        pTooltip.add(Component.translatable("block.extra_terracotta_utilities.wooden_magenta_glazed_terracotta.tooltip.function_1").withStyle(ChatFormatting.GOLD));
        pTooltip.add(Component.translatable("block.extra_terracotta_utilities.wooden_magenta_glazed_terracotta.tooltip.function_2").withStyle(ChatFormatting.GOLD));
        pTooltip.add(Component.translatable("tooltip.extra_terracotta_utilities.usage").withStyle(ChatFormatting.DARK_AQUA));
        pTooltip.add(Component.translatable("block.extra_terracotta_utilities.wooden_magenta_glazed_terracotta.tooltip.usage").withStyle(ChatFormatting.DARK_AQUA));
    }

}
