package com.github.winhan.diao.blocks;

import com.github.winhan.diao.blockentities.BufferingMagentaGlazedTerracottaEntity;
import com.github.winhan.diao.initialize.Initializer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

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

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(IMPULSE, POWERED, TRIGGERED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getPlayer().isShiftKeyDown()? pContext.getNearestLookingDirection().getOpposite() : pContext.getNearestLookingDirection());
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        if (pPlayer.isShiftKeyDown() && pPlayer.getItemInHand(pHand).isEmpty()) {
            pLevel.setBlock(pPos, pState.setValue(IMPULSE, !pState.getValue(IMPULSE)), UPDATE_ALL);
            ((ServerPlayer)pPlayer).sendSystemMessage(Component.translatable(pState.getValue(IMPULSE) ? "block.extra_terracotta_utilities.buffering_magenta_glazed_terracotta.continuous" : "block.extra_terracotta_utilities.buffering_magenta_glazed_terracotta.impulse"), false);
            return InteractionResult.CONSUME;
        }
        BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
        if (blockEntity instanceof BufferingMagentaGlazedTerracottaEntity bmgtEntity) {
            pPlayer.openMenu(bmgtEntity);
            pPlayer.awardStat(Stats.OPEN_BARREL);
            PiglinAi.angerNearbyPiglins(pPlayer, true);
        }
        return InteractionResult.CONSUME;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new BufferingMagentaGlazedTerracottaEntity(pPos, pState);
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @javax.annotation.Nullable LivingEntity pPlacer, ItemStack pStack) {
        if (pStack.hasCustomHoverName()) {
            BlockEntity blockentity = pLevel.getBlockEntity(pPos);
            if (blockentity instanceof BufferingMagentaGlazedTerracottaEntity) {
                ((BufferingMagentaGlazedTerracottaEntity)blockentity).setCustomName(pStack.getHoverName());
            }
        }
        if (!pLevel.isClientSide) {
            boolean bl = pLevel.hasNeighborSignal(pPos);
            if (bl != pState.getValue(POWERED)) {
                pLevel.setBlock(pPos, pState.setValue(POWERED, bl), UPDATE_ALL);
            }
        }

    }

    @Override
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pNeighborBlock, BlockPos pNeighborPos, boolean pMovedByPiston) {
        if (!pLevel.isClientSide) {
            boolean bl = pLevel.hasNeighborSignal(pPos);
            if (bl != pState.getValue(POWERED)) {
                pLevel.setBlock(pPos, pState.setValue(POWERED, bl), UPDATE_ALL);
            }
        }
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, Initializer.BUFFERING_MAGENTA_GLAZED_TERRACOTTA_ENTITY.get(), BufferingMagentaGlazedTerracottaEntity::constantTick);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        pTooltip.add(Component.translatable("tooltip.extra_terracotta_utilities.info.sneak"));
        pTooltip.add(Component.translatable("tooltip.extra_terracotta_utilities.function").withStyle(ChatFormatting.GOLD));
        pTooltip.add(Component.translatable("block.extra_terracotta_utilities.buffering_magenta_glazed_terracotta.tooltip.function_1").withStyle(ChatFormatting.GOLD));
        pTooltip.add(Component.translatable("block.extra_terracotta_utilities.buffering_magenta_glazed_terracotta.tooltip.function_2").withStyle(ChatFormatting.GOLD));
        pTooltip.add(Component.translatable("block.extra_terracotta_utilities.buffering_magenta_glazed_terracotta.tooltip.function_3").withStyle(ChatFormatting.GOLD));
        pTooltip.add(Component.translatable("tooltip.extra_terracotta_utilities.usage").withStyle(ChatFormatting.DARK_AQUA));
        pTooltip.add(Component.translatable("block.extra_terracotta_utilities.buffering_magenta_glazed_terracotta.tooltip.usage_1").withStyle(ChatFormatting.DARK_AQUA));
        pTooltip.add(Component.translatable("block.extra_terracotta_utilities.buffering_magenta_glazed_terracotta.tooltip.usage_2").withStyle(ChatFormatting.DARK_AQUA));
        pTooltip.add(Component.translatable("block.extra_terracotta_utilities.buffering_magenta_glazed_terracotta.tooltip.usage_3").withStyle(ChatFormatting.DARK_AQUA));
    }

}
