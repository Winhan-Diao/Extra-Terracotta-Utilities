package com.github.winhan.diao.blocks;

import com.github.winhan.diao.blockentities.AllayedEnderizedMagentaGlazedTerracottaEntity;
import com.github.winhan.diao.blockentities.AllayedMagentaGlazedTerracottaEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AllayedEnderizedMagentaGlazedTerracotta extends AllayedMagentaGlazedTerracotta {
    public static final BooleanProperty WHITELIST = BooleanProperty.create("whitelist");
    public AllayedEnderizedMagentaGlazedTerracotta(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new AllayedEnderizedMagentaGlazedTerracottaEntity(pPos, pState);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(WHITELIST, FACING);
    }

    @Override
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pNeighborBlock, BlockPos pNeighborPos, boolean pMovedByPiston) {
        pLevel.scheduleTick(pPos, pLevel.getBlockState(pPos).getBlock(), 20);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        pLevel.blockEntityChanged(pPos);
        ItemStack itemStack = pPlayer.getItemInHand(pHand);
        BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
        if (blockEntity instanceof AllayedEnderizedMagentaGlazedTerracottaEntity aemgtEntity) {
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
                if (!(itemStack.getItem() instanceof BlockItem)) {        //block when itemStack isn't blockItem
                    if (!pLevel.isClientSide) pPlayer.sendSystemMessage(Component.translatable("block.extra_terracotta_utilities.allayed_magenta_glazed_terracotta.warn"));
                    return InteractionResult.SUCCESS;
                }
                if (!pLevel.isClientSide) {     //!empty & server
                    CompoundTag nbt = new CompoundTag();
                    nbt.putString("filter", itemStack.getDescriptionId());
                    aemgtEntity.load(nbt);
                    pState.updateNeighbourShapes(pLevel, pPos, UPDATE_ALL);
                    pLevel.playSound(null, pPos, SoundEvents.ENDERMAN_AMBIENT, SoundSource.BLOCKS, 1.0f, 1.0f);
                }
            }
            if (!pLevel.isClientSide) { // !(empty & sneak) & server
                CompoundTag nbt2 = new CompoundTag();
                aemgtEntity.saveAdditional(nbt2);
                nbt2.getCompound("filter");
                if (pState.getValue(WHITELIST)) {
                    pPlayer.sendSystemMessage(Component.translatable("block.extra_terracotta_utilities.allayed_enderized_magenta_glazed_terracotta.whitelist_info").append(Component.translatable(nbt2.getString("filter")).withStyle(style -> style.withColor(0xFF6F8A7A))));
                } else {
                    pPlayer.sendSystemMessage(Component.translatable("block.extra_terracotta_utilities.allayed_enderized_magenta_glazed_terracotta.blacklist_info").append(Component.translatable(nbt2.getString("filter")).withStyle(style -> style.withColor(0xFF6F8A7A))));
                }
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (pLevel.getBlockEntity(pPos) instanceof AllayedEnderizedMagentaGlazedTerracottaEntity aemgtEntity) {
            boolean bl = pState.getValue(WHITELIST);
            String filter = aemgtEntity.getFilter();
            BlockPos startPos = pPos.relative(pState.getValue(FACING));
            BlockPos endPos = pPos.relative(pState.getValue(FACING).getOpposite());
            if (!pLevel.getBlockState(startPos).isAir()
                    && pLevel.getBlockState(endPos).isAir()
                    && (pLevel.getBlockState(startPos).getBlock().getDescriptionId().equals(filter) == bl)) {
                pLevel.getServer().getCommands().performPrefixedCommand(pLevel.getServer().createCommandSourceStack().withSource(CommandSource.NULL),
                        String.format("clone %1$d %2$d %3$d %1$d %2$d %3$d %4$d %5$d %6$d", startPos.getX(), startPos.getY(), startPos.getZ(), endPos.getX(), endPos.getY(), endPos.getZ()));
                pLevel.removeBlockEntity(startPos);
                pLevel.setBlock(startPos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL_IMMEDIATE);
                pLevel.playSound(null, pPos, SoundEvents.ENDERMAN_TELEPORT, SoundSource.BLOCKS, 1.0f, 1.0f);
            }
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
        pTooltip.add(Component.translatable("block.extra_terracotta_utilities.allayed_enderized_magenta_glazed_terracotta.tooltip.function_1").withStyle(ChatFormatting.GOLD));
        pTooltip.add(Component.translatable("block.extra_terracotta_utilities.allayed_enderized_magenta_glazed_terracotta.tooltip.function_2").withStyle(ChatFormatting.GOLD));
        pTooltip.add(Component.translatable("tooltip.extra_terracotta_utilities.usage").withStyle(ChatFormatting.DARK_AQUA));
        pTooltip.add(Component.translatable("block.extra_terracotta_utilities.allayed_enderized_magenta_glazed_terracotta.tooltip.usage").withStyle(ChatFormatting.DARK_AQUA));
    }

}
