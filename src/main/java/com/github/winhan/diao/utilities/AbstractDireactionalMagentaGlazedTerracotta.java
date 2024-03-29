package com.github.winhan.diao.utilities;

import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

public abstract class AbstractDireactionalMagentaGlazedTerracotta extends DirectionalBlock {

    protected AbstractDireactionalMagentaGlazedTerracotta(Properties p_52591_) {
        super(p_52591_);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getPlayer().isShiftKeyDown()? pContext.getNearestLookingDirection().getOpposite() : pContext.getNearestLookingDirection());
    }


}
