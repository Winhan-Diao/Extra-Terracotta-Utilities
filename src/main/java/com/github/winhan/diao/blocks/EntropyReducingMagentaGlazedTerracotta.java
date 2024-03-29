package com.github.winhan.diao.blocks;

import com.github.winhan.diao.blockentities.EntropyReducingMagentaGlazedTerracottaEntity;
import com.github.winhan.diao.initialize.Initializer;
import com.github.winhan.diao.utilities.Utilities;
import com.mojang.logging.LogUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.List;

public class EntropyReducingMagentaGlazedTerracotta extends BaseEntityBlock {
    public static final int BREAK_CHANCE = 10;
    public static final DirectionProperty FACING = DirectionalBlock.FACING;
    public static Logger logger = LogUtils.getLogger();

    public EntropyReducingMagentaGlazedTerracotta(Properties pProperties) {
        super(pProperties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new EntropyReducingMagentaGlazedTerracottaEntity(pPos, pState);
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
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, Initializer.ENTROPY_REDUCING_MAGENTA_GLAZED_TERRACOTTA_ENTITY.get(), EntropyReducingMagentaGlazedTerracottaEntity::constantTick);
    }

    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (pLevel.getBlockState(pPos.relative(pState.getValue(FACING).getOpposite())).is(Blocks.WATER) && pLevel.getBlockState(pPos.relative(pState.getValue(FACING))).is(Blocks.ICE)) {
            pLevel.setBlock(pPos.relative(pState.getValue(FACING).getOpposite()), Blocks.ICE.defaultBlockState(), UPDATE_ALL);
            Utilities.chanceToDestroyBlock(pPos.relative(pState.getValue(FACING)), pLevel, BREAK_CHANCE, false, pRandom);
        }
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.getBlockEntity(pPos) instanceof EntropyReducingMagentaGlazedTerracottaEntity ermgtEntity) {
            if (pPlayer.getItemInHand(pHand).is(Items.BARRIER)) {
                ermgtEntity.setTarget("debug");
                return InteractionResult.SUCCESS;
            }
            if (pPlayer.getItemInHand(pHand).is(Items.CHAIN_COMMAND_BLOCK)) {
                logger.debug(ermgtEntity.getTarget());
                return InteractionResult.SUCCESS;
            }
            if (pPlayer.getItemInHand(pHand).is(Items.COMMAND_BLOCK)) {
                logger.debug(String.valueOf(ermgtEntity.getCooldown()));
                return InteractionResult.SUCCESS;
            }
            if (pPlayer.getItemInHand(pHand).is(Items.REPEATING_COMMAND_BLOCK)) {
                ermgtEntity.setTarget("debug");
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        pTooltip.add(Component.translatable("tooltip.extra_terracotta_utilities.info.sneak"));
        pTooltip.add(Component.translatable("tooltip.extra_terracotta_utilities.function").withStyle(ChatFormatting.GOLD));
        pTooltip.add(Component.translatable("block.extra_terracotta_utilities.entropy_reducing_magenta_glazed_terracotta.tooltip.function_1", Component.translatable(Initializer.EXPLOSIVE_MAGENTA_GLAZED_TERRACOTTA.get().getDescriptionId())).withStyle(ChatFormatting.GOLD));
        pTooltip.add(Component.translatable("block.extra_terracotta_utilities.entropy_reducing_magenta_glazed_terracotta.tooltip.function_2").withStyle(ChatFormatting.GOLD));
        pTooltip.add(Component.translatable("block.extra_terracotta_utilities.entropy_reducing_magenta_glazed_terracotta.tooltip.function_3").withStyle(ChatFormatting.GOLD));
        pTooltip.add(Component.translatable("tooltip.extra_terracotta_utilities.usage").withStyle(ChatFormatting.DARK_AQUA));
        pTooltip.add(Component.translatable("block.extra_terracotta_utilities.entropy_reducing_magenta_glazed_terracotta.tooltip.usage_1").withStyle(ChatFormatting.DARK_AQUA));
        pTooltip.add(Component.translatable("block.extra_terracotta_utilities.entropy_reducing_magenta_glazed_terracotta.tooltip.usage_2", BREAK_CHANCE).withStyle(ChatFormatting.DARK_AQUA));

    }


}
