package com.github.winhan.diao;

import net.minecraft.block.BlockState;
import net.minecraft.block.GlazedTerracottaBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PoweredMagentaGlazedTerracotta extends GlazedTerracottaBlock {
    public PoweredMagentaGlazedTerracotta(Settings settings) {
		super(settings.slipperiness(1f));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayer().isSneaking() ? ctx.getHorizontalPlayerFacing().getOpposite() : ctx.getHorizontalPlayerFacing());
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        if (state.get(FACING) == Direction.NORTH && !entity.isSneaking()) {
            entity.setVelocity(0, 0, .1);
/*
        for(Entity player :world.getPlayers()) {
            player.sendMessage(Text.literal("Item moving..."));
        }
*/
        }
        if (state.get(FACING) == Direction.SOUTH && !entity.isSneaking()) {
            entity.setVelocity(0, 0, -.1);
        }
        if (state.get(FACING) == Direction.EAST && !entity.isSneaking()) {
            entity.setVelocity(-.1, 0, 0);
        }
        if (state.get(FACING) == Direction.WEST && !entity.isSneaking()) {
            entity.setVelocity(.1, 0, 0);
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        tooltip.add(Text.translatable("tooltip.extra_terracotta_utilities.info.sneak"));
        tooltip.add(Text.translatable("tooltip.extra_terracotta_utilities.function").formatted(Formatting.GOLD));
        tooltip.add(Text.translatable("block.extra_terracotta_utilities.powered_magenta_glazed_terracotta.tooltip.function_1").formatted(Formatting.GOLD));
        tooltip.add(Text.translatable("block.extra_terracotta_utilities.powered_magenta_glazed_terracotta.tooltip.function_2").formatted(Formatting.GOLD));
        }
    }
