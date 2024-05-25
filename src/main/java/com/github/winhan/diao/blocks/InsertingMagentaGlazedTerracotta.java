package com.github.winhan.diao.blocks;

import com.github.winhan.diao.blockentities.InsertingMagentaGlazedTerracottaEntity;
import com.github.winhan.diao.initialize.Initializer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class InsertingMagentaGlazedTerracotta extends BufferingMagentaGlazedTerracotta {

    public InsertingMagentaGlazedTerracotta(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new InsertingMagentaGlazedTerracottaEntity(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, Initializer.INSERTING_MAGENTA_GLAZED_TERRACOTTA_ENTITY.get(), InsertingMagentaGlazedTerracottaEntity::constantTick);
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
        if (blockEntity instanceof InsertingMagentaGlazedTerracottaEntity imgtEntity) {
            pPlayer.openMenu(imgtEntity);
            pPlayer.awardStat(Stats.OPEN_BARREL);
            PiglinAi.angerNearbyPiglins(pPlayer, true);
        }
        return InteractionResult.CONSUME;
    }


}
