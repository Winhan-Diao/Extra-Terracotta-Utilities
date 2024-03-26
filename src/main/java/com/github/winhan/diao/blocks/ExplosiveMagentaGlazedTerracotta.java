package com.github.winhan.diao.blocks;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

public class ExplosiveMagentaGlazedTerracotta extends DirectionalBlock {
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public float getMaxBlastResistance() {
        return Blocks.OBSIDIAN.getExplosionResistance() / 2;
    }

    public ExplosiveMagentaGlazedTerracotta(Properties p_52591_) {
        super(p_52591_);
        this.registerDefaultState(((this.stateDefinition.any()).setValue(FACING, Direction.SOUTH)).setValue(POWERED, false));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getPlayer().isShiftKeyDown()? pContext.getHorizontalDirection().getOpposite() : pContext.getHorizontalDirection());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(POWERED, FACING);
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pMovedByPiston) {
        if (pLevel.hasNeighborSignal(pPos)) {
            pLevel.scheduleTick(pPos, pLevel.getBlockState(pPos).getBlock(), 5);
        }
    }

    @Override
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pNeighborBlock, BlockPos pNeighborPos, boolean pMovedByPiston) {
        if (pLevel.hasNeighborSignal(pPos)) {
            pLevel.scheduleTick(pPos, pLevel.getBlockState(pPos).getBlock(), 5);
        }
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        BlockPos posTowards = pPos.relative(pState.getValue(FACING).getOpposite());
        double x = posTowards.getX();
        double y = posTowards.getY();
        double z = posTowards.getZ();
        List<BlockPos> list = new ArrayList<>();
        if (!pLevel.getBlockState(posTowards).isAir() && pLevel.getBlockState(posTowards).getBlock().getExplosionResistance() <= getMaxBlastResistance() && pLevel.getBlockState(posTowards).getBlock().defaultDestroyTime() >= 0) {
            list.add(posTowards);
            Explosion explosion = new Explosion(pLevel, null, x, y, z, 10.0f,false, Explosion.BlockInteraction.DESTROY, list);
            explosion.finalizeExplosion(false);
            pLevel.playSound(null, posTowards, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 1.0f, 1.0f);
            pLevel.sendParticles(ParticleTypes.EXPLOSION, posTowards.getX()+.5, posTowards.getY()+.5, posTowards.getZ()+.5, 1, 0f, 0f, 0f, 1.0f);
        }
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        pTooltip.add(Component.translatable("tooltip.extra_terracotta_utilities.info.sneak"));
        pTooltip.add(Component.translatable("tooltip.extra_terracotta_utilities.function").withStyle(ChatFormatting.GOLD));
        pTooltip.add(Component.translatable("block.extra_terracotta_utilities.explosive_magenta_glazed_terracotta.tooltip.function_1").withStyle(ChatFormatting.GOLD));
        pTooltip.add(Component.translatable("block.extra_terracotta_utilities.explosive_magenta_glazed_terracotta.tooltip.function_2").withStyle(ChatFormatting.GOLD));
        pTooltip.add(Component.translatable("block.extra_terracotta_utilities.explosive_magenta_glazed_terracotta.tooltip.function_3").withStyle(ChatFormatting.GOLD));
        pTooltip.add(Component.translatable("tooltip.extra_terracotta_utilities.usage").withStyle(ChatFormatting.DARK_AQUA));
        pTooltip.add(Component.translatable("block.extra_terracotta_utilities.explosive_magenta_glazed_terracotta.tooltip.usage").withStyle(ChatFormatting.DARK_AQUA));
    }

}
