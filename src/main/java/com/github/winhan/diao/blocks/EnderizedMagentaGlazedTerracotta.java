package com.github.winhan.diao.blocks;

import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSource;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.ticks.TickPriority;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EnderizedMagentaGlazedTerracotta extends DirectionalBlock {
    public EnderizedMagentaGlazedTerracotta(Properties p_52591_) {
        super(p_52591_);
        this.registerDefaultState(((this.stateDefinition.any()).setValue(FACING, Direction.SOUTH)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getPlayer().isShiftKeyDown()? pContext.getNearestLookingDirection().getOpposite() : pContext.getNearestLookingDirection());
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pMovedByPiston) {
            pLevel.scheduleTick(pPos, pLevel.getBlockState(pPos).getBlock(), 20, TickPriority.LOW);
    }

    @Override
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pNeighborBlock, BlockPos pNeighborPos, boolean pMovedByPiston) {
        if (!pLevel.isClientSide) {
            pLevel.scheduleTick(pPos, pLevel.getBlockState(pPos).getBlock(), 20, TickPriority.LOW);
        }
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        BlockPos startPos = pPos.relative(pState.getValue(FACING));
        BlockPos endPos = pPos.relative(pState.getValue(FACING).getOpposite());
        if (!pLevel.getBlockState(startPos).isAir() && pLevel.getBlockState(endPos).isAir()) {
            pLevel.getServer().getCommands().performPrefixedCommand(pLevel.getServer().createCommandSourceStack().withSource(CommandSource.NULL),
                    String.format("clone %1$d %2$d %3$d %1$d %2$d %3$d %4$d %5$d %6$d", startPos.getX(), startPos.getY(), startPos.getZ(), endPos.getX(), endPos.getY(), endPos.getZ()));
            pLevel.removeBlockEntity(startPos);
            pLevel.setBlock(startPos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL_IMMEDIATE);
            pLevel.playSound(null, pPos, SoundEvents.ENDERMAN_TELEPORT, SoundSource.BLOCKS, 1.0f, 1.0f);
        }
    }

    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        for(int i = 0; i < 3; ++i) {
            int j = pRandom.nextInt(2) * 2 - 1;
            int k = pRandom.nextInt(2) * 2 - 1;
            double d0 = (double)pPos.getX() + 0.5D + 0.25D * (double)j;
            double d1 = (double)((float)pPos.getY() + pRandom.nextFloat());
            double d2 = (double)pPos.getZ() + 0.5D + 0.25D * (double)k;
            double d3 = (double)(pRandom.nextFloat() * (float)j);
            double d4 = ((double)pRandom.nextFloat() - 0.5D) * 0.125D;
            double d5 = (double)(pRandom.nextFloat() * (float)k);
            pLevel.addParticle(ParticleTypes.PORTAL, d0, d1, d2, d3, d4, d5);
        }
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        pTooltip.add(Component.translatable("tooltip.extra_terracotta_utilities.info.sneak"));
        pTooltip.add(Component.translatable("tooltip.extra_terracotta_utilities.function").withStyle(ChatFormatting.GOLD));
        pTooltip.add(Component.translatable("block.extra_terracotta_utilities.enderized_magenta_glazed_terracotta.tooltip.function_1").withStyle(ChatFormatting.GOLD));
        pTooltip.add(Component.translatable("block.extra_terracotta_utilities.enderized_magenta_glazed_terracotta.tooltip.function_2").withStyle(ChatFormatting.GOLD));
    }
}
