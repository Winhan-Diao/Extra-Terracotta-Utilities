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
        if (!entity.isSneaking()) {
            switch (state.get(FACING).getName()) {
                case "north" -> entity.setVelocity(0, 0, .1);
                case "south" -> entity.setVelocity(0, 0, -.1);
                case "east" -> entity.setVelocity(-.1, 0, 0);
                case "west" -> entity.setVelocity(.1, 0, 0);
            }
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
//world.getPlayers().forEach(player -> player.sendMessage(Text.literal("Item moving...")));
