package com.github.winhan.diao;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public class WoodenMagentaGlazedTerracotta extends Block {
    public static final int LOG_CHANCE = 25;
    public static final int LEAVE_CHANCE = 25;
    public static final int DUP_CHANCE = 1;
    public WoodenMagentaGlazedTerracotta(Settings settings) {
        super(settings);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (world.getBlockState(pos.down()).isIn(BlockTags.DIRT) && world.getBlockState(pos.up()).isAir()) {
            int i = random.nextInt(100);
            if (i < LOG_CHANCE) {
                world.setBlockState(pos.up(), Blocks.OAK_LOG.getDefaultState(), NOTIFY_ALL);
            } else if (i < LOG_CHANCE + LEAVE_CHANCE) {
                world.setBlockState(pos.up(), Blocks.OAK_LEAVES.getDefaultState().with(LeavesBlock.DISTANCE, 1), NOTIFY_ALL);
            } else if (i < LOG_CHANCE + LEAVE_CHANCE + DUP_CHANCE) {
                world.setBlockState(pos.up(), Initializer.WOODEN_MAGENTA_GLAZED_TERRACOTTA.getDefaultState(), NOTIFY_ALL);
            } else {return;}
            world.playSound(null, pos.up(), SoundEvents.BLOCK_MOSS_PLACE, SoundCategory.BLOCKS, 1.0f, 1.0f);
            for(i = 0; i < 15; ++i) {
                world.spawnParticles(ParticleTypes.HAPPY_VILLAGER, pos.getX() + .5, pos.up().getY() + .5, pos.getZ() + .5, 1, 0.5, 0.5, 0.5, 0);
            }
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        tooltip.add(Text.translatable("tooltip.extra_terracotta_utilities.info.sneak"));
        tooltip.add(Text.translatable("tooltip.extra_terracotta_utilities.function").formatted(Formatting.GOLD));
        tooltip.add(Text.translatable("block.extra_terracotta_utilities.wooden_magenta_glazed_terracotta.tooltip.function_1").formatted(Formatting.GOLD));
        tooltip.add(Text.translatable("block.extra_terracotta_utilities.wooden_magenta_glazed_terracotta.tooltip.function_2").formatted(Formatting.GOLD));
        tooltip.add(Text.translatable("tooltip.extra_terracotta_utilities.usage").formatted(Formatting.DARK_AQUA));
        tooltip.add(Text.translatable("block.extra_terracotta_utilities.wooden_magenta_glazed_terracotta.tooltip.usage").formatted(Formatting.DARK_AQUA));
    }

}
