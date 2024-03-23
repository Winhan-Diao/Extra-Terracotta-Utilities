package com.github.winhan.diao;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SpinningMagentaGlazedTerracotta extends Block {

    public SpinningMagentaGlazedTerracotta(Settings settings) {
        super(settings);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.POWERED);
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (world.isReceivingRedstonePower(pos)) {
            world.scheduleBlockTick(pos, state.getBlock(), 2);
        }
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        boolean bl = world.isReceivingRedstonePower(pos);
        if (bl != state.get(Properties.POWERED)) {
            if (world.isReceivingRedstonePower(pos)) {
                world.scheduleBlockTick(pos, state.getBlock(), 2);
            }
            world.setBlockState(pos, state.with(Properties.POWERED, bl), Block.NOTIFY_ALL);
        }
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        BlockPos posUp = pos.up();
        BlockState stateUp = world.getBlockState(posUp);
        if (stateUp.contains(Properties.FACING) && stateUp.get(Properties.FACING) != Direction.UP && stateUp.get(Properties.FACING) != Direction.DOWN) {
            world.setBlockState(posUp, stateUp.with(Properties.FACING, stateUp.get(Properties.FACING).rotateYClockwise()), Block.NOTIFY_ALL);
        } else if(stateUp.contains((Properties.HORIZONTAL_FACING))) {
            world.setBlockState(posUp, stateUp.with(Properties.HORIZONTAL_FACING, stateUp.get(Properties.HORIZONTAL_FACING).rotateYClockwise()), NOTIFY_ALL);
        } else if (stateUp.contains(Properties.AXIS) && stateUp.get(Properties.AXIS) != Direction.Axis.Y) {
            if (stateUp.get(Properties.AXIS) == Direction.Axis.X) {
                world.setBlockState(posUp, stateUp.with(Properties.AXIS, Direction.Axis.Z));
            } else if (stateUp.get(Properties.AXIS) == Direction.Axis.Z) {
                world.setBlockState(posUp, stateUp.with(Properties.AXIS, Direction.Axis.X));
            }
        } else if (stateUp.contains(Properties.HOPPER_FACING) && stateUp.get(Properties.HOPPER_FACING) != Direction.DOWN) {
            world.setBlockState(posUp, stateUp.with(Properties.HOPPER_FACING, stateUp.get(Properties.HOPPER_FACING).rotateYClockwise()));
        } else {
            world.playSound(null, posUp, SoundEvents.BLOCK_DISPENSER_FAIL, SoundCategory.BLOCKS, 1.0f, 1.2f);
            return;
        }
        world.playSound(null, posUp, SoundEvents.BLOCK_MOSS_PLACE, SoundCategory.BLOCKS, 1.0f, 1.0f);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        tooltip.add(Text.translatable("tooltip.extra_terracotta_utilities.info.sneak"));
        tooltip.add(Text.translatable("tooltip.extra_terracotta_utilities.function").formatted(Formatting.GOLD));
        tooltip.add(Text.translatable("block.extra_terracotta_utilities.spinning_magenta_glazed_terracotta.tooltip.function_1").formatted(Formatting.GOLD));
        tooltip.add(Text.translatable("block.extra_terracotta_utilities.spinning_magenta_glazed_terracotta.tooltip.function_2").formatted(Formatting.GOLD));
        tooltip.add(Text.translatable("tooltip.extra_terracotta_utilities.usage").formatted(Formatting.DARK_AQUA));
        tooltip.add(Text.translatable("block.extra_terracotta_utilities.spinning_magenta_glazed_terracotta.tooltip.usage").formatted(Formatting.DARK_AQUA));
    }

}
