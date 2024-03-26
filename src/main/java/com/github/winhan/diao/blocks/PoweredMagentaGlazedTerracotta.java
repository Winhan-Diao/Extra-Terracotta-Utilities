package com.github.winhan.diao.blocks;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.GlazedTerracottaBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PoweredMagentaGlazedTerracotta extends GlazedTerracottaBlock {
    public PoweredMagentaGlazedTerracotta(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getPlayer().isShiftKeyDown()? pContext.getHorizontalDirection().getOpposite() : pContext.getHorizontalDirection());
    }

    @Override
    public void stepOn(Level pLevel, BlockPos pPos, BlockState pState, Entity pEntity) {
        if (!pEntity.isShiftKeyDown()) {
            switch (pState.getValue(FACING).getName()) {
                case "north" -> pEntity.setDeltaMovement(0, 0, .1);
                case "south" -> pEntity.setDeltaMovement(0, 0, -.1);
                case "east" -> pEntity.setDeltaMovement(-.1, 0, 0);
                case "west" -> pEntity.setDeltaMovement(.1, 0, 0);
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        pTooltip.add(Component.translatable("tooltip.extra_terracotta_utilities.info.sneak"));
        pTooltip.add(Component.translatable("tooltip.extra_terracotta_utilities.function").withStyle(ChatFormatting.GOLD));
        pTooltip.add(Component.translatable("block.extra_terracotta_utilities.powered_magenta_glazed_terracotta.tooltip.function_1").withStyle(ChatFormatting.GOLD));
        pTooltip.add(Component.translatable("block.extra_terracotta_utilities.powered_magenta_glazed_terracotta.tooltip.function_2").withStyle(ChatFormatting.GOLD));
    }
}
