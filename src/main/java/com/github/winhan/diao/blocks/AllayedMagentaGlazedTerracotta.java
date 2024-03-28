package com.github.winhan.diao.blocks;

import com.github.winhan.diao.blockentities.AllayedMagentaGlazedTerracottaEntity;
import com.github.winhan.diao.initialize.Initializer;
import com.github.winhan.diao.mixin.BlockEntityTypeDebugger;
import com.mojang.logging.LogUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
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
import org.slf4j.Logger;

import java.util.List;

public class AllayedMagentaGlazedTerracotta extends BaseEntityBlock {
    public static final BooleanProperty WHITELIST = BooleanProperty.create("whitelist");
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public AllayedMagentaGlazedTerracotta(Properties pProperties) {
        super(pProperties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new AllayedMagentaGlazedTerracottaEntity(pPos, pState);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getPlayer().isShiftKeyDown()? pContext.getNearestLookingDirection().getOpposite() : pContext.getNearestLookingDirection());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, WHITELIST);
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pMovedByPiston) {
        this.defaultBlockState().setValue(WHITELIST, true).setValue(FACING, Direction.SOUTH);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide ? null : createTickerHelper(pBlockEntityType, Initializer.ALLAYED_MAGENTA_GLAZED_TERRACOTTA_ENTITY.get(), AllayedMagentaGlazedTerracottaEntity::serverTick);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        pLevel.blockEntityChanged(pPos);
        ItemStack itemStack = pPlayer.getItemInHand(pHand);
        BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
        if (blockEntity instanceof AllayedMagentaGlazedTerracottaEntity amgtEntity) {
            if (pPlayer.getItemInHand(pHand).isEmpty()) {
                if (pPlayer.isShiftKeyDown()) {     //empty & sneak
                    if (!pLevel.isClientSide) {     //empty & sneak & server
                        pLevel.setBlock(pPos, pState.setValue(WHITELIST, !pState.getValue(WHITELIST)), UPDATE_ALL);
                        if (!pState.getValue(WHITELIST)) {
                            pPlayer.sendSystemMessage(Component.translatable("block.extra_terracotta_utilities.allayed_enderized_magenta_glazed_terracotta.whitelist"));
                        } else {
                            pPlayer.sendSystemMessage(Component.translatable("block.extra_terracotta_utilities.allayed_enderized_magenta_glazed_terracotta.blacklist"));
                        }
                        pLevel.playSound(null, pPos, SoundEvents.METAL_PRESSURE_PLATE_CLICK_OFF, SoundSource.BLOCKS, 1.0f, 1.0f);
                    }
                    return InteractionResult.SUCCESS;
                    } else {        //empty & !sneak

                }
            } else {        //!empty
                if (!pLevel.isClientSide) {     //!empty & server
                    CompoundTag nbt = new CompoundTag();
                    nbt.putString("filter", itemStack.getDescriptionId());
                    amgtEntity.load(nbt);
                    pState.updateNeighbourShapes(pLevel, pPos, UPDATE_ALL);
                    pLevel.playSound(null, pPos, SoundEvents.ALLAY_ITEM_TAKEN, SoundSource.BLOCKS, 1.0f, 1.0f);
                }
            }
            if (!pLevel.isClientSide) { // !(empty & sneak) & server
                CompoundTag nbt2 = new CompoundTag();
                amgtEntity.saveAdditional(nbt2);
                nbt2.getCompound("filter");
                if (pState.getValue(WHITELIST)) {
                    pPlayer.sendSystemMessage(Component.translatable("block.extra_terracotta_utilities.allayed_enderized_magenta_glazed_terracotta.whitelist_info").append(Component.translatable(nbt2.getString("filter")).withStyle(ChatFormatting.AQUA)));
                } else {
                    pPlayer.sendSystemMessage(Component.translatable("block.extra_terracotta_utilities.allayed_enderized_magenta_glazed_terracotta.blacklist_info").append(Component.translatable(nbt2.getString("filter")).withStyle(ChatFormatting.AQUA)));
                }
            }
            return InteractionResult.SUCCESS;
       }
        return InteractionResult.PASS;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        pTooltip.add(Component.translatable("tooltip.extra_terracotta_utilities.info.sneak"));
        pTooltip.add(Component.translatable("tooltip.extra_terracotta_utilities.function").withStyle(ChatFormatting.GOLD));
        pTooltip.add(Component.translatable("block.extra_terracotta_utilities.allayed_magenta_glazed_terracotta.tooltip.function_1").withStyle(ChatFormatting.GOLD));
        pTooltip.add(Component.translatable("block.extra_terracotta_utilities.allayed_magenta_glazed_terracotta.tooltip.function_2").withStyle(ChatFormatting.GOLD));
        pTooltip.add(Component.translatable("tooltip.extra_terracotta_utilities.usage").withStyle(ChatFormatting.DARK_AQUA));
        pTooltip.add(Component.translatable("block.extra_terracotta_utilities.allayed_magenta_glazed_terracotta.tooltip.usage_1").withStyle(ChatFormatting.DARK_AQUA));
        pTooltip.add(Component.translatable("block.extra_terracotta_utilities.allayed_magenta_glazed_terracotta.tooltip.usage_2").withStyle(ChatFormatting.DARK_AQUA));
    }


}
