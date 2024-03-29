package com.github.winhan.diao.blocks;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

public class BufferingMagentaGlazedTerracotta extends BarrelBlock {
    public static final BooleanProperty IMPULSE = BooleanProperty.create("impulse");
    public static final BooleanProperty OPEN = BarrelBlock.OPEN;
    public static final DirectionProperty FACING = DirectionalBlock.FACING;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;

    public BufferingMagentaGlazedTerracotta(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(IMPULSE, true)
                .setValue(OPEN, false)
                .setValue(FACING, Direction.NORTH)
                .setValue(POWERED, false)
                .setValue(TRIGGERED, false));
    }
}
