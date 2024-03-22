package com.github.winhan.diao;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.BarrelBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BufferingMagentaGlazedTerracotta extends BarrelBlock {
    public static final MapCodec<BarrelBlock> CODEC = createCodec(BarrelBlock::new);
    public static final BooleanProperty IMPULSE = BooleanProperty.of("impulse");
    @Override
    public MapCodec<BarrelBlock> getCodec() {
        return CODEC;
    }

    public BufferingMagentaGlazedTerracotta(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(OPEN, false)
                .with(IMPULSE, true)
                .with(Properties.POWERED, false)
                .with(Properties.TRIGGERED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(IMPULSE, Properties.POWERED, Properties.TRIGGERED);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayer().isSneaking() ? ctx.getPlayerLookDirection() : ctx.getPlayerLookDirection().getOpposite());
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        }
        if (player.isSneaking() && player.getStackInHand(hand).isEmpty()) {
            world.setBlockState(pos, state.with(IMPULSE, !state.get(IMPULSE)), NOTIFY_ALL);
            ((ServerPlayerEntity) player).sendMessageToClient(Text.translatable(state.get(IMPULSE) ? "block.extra_terracotta_utilities.buffering_magenta_glazed_terracotta.continuous" : "block.extra_terracotta_utilities.buffering_magenta_glazed_terracotta.impulse"), true);
            return ActionResult.CONSUME;
        }
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof BufferingMagentaGlazedTerracottaEntity bmgtEntity) {
            player.openHandledScreen(bmgtEntity);
            player.incrementStat(Stats.OPEN_BARREL);
            PiglinBrain.onGuardedBlockInteracted(player, true);
        }
        return ActionResult.CONSUME;
    }

    @Override
    @Nullable
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BufferingMagentaGlazedTerracottaEntity(pos, state);
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {

    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (itemStack.hasCustomName()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof BufferingMagentaGlazedTerracottaEntity bmgtEntity) {
                bmgtEntity.setCustomName(itemStack.getName());
            }
        }
        if (!world.isClient) {
            boolean bl = world.isReceivingRedstonePower(pos);
            if (bl != state.get(Properties.POWERED)) {
                world.setBlockState(pos, state.with(Properties.POWERED, bl), Block.NOTIFY_ALL);
            }
        }

    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (!world.isClient) {
            boolean bl = world.isReceivingRedstonePower(pos);
            if (bl != state.get(Properties.POWERED)) {
                world.setBlockState(pos, state.with(Properties.POWERED, bl), Block.NOTIFY_ALL);
            }
        }
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, Initializer.BUFFERING_MAGENTA_GLAZED_TERRACOTTA_ENTITY, BufferingMagentaGlazedTerracottaEntity::serverTick);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        tooltip.add(Text.translatable("tooltip.extra_terracotta_utilities.info.sneak"));
        tooltip.add(Text.translatable("tooltip.extra_terracotta_utilities.function").formatted(Formatting.GOLD));
        tooltip.add(Text.translatable("block.extra_terracotta_utilities.buffering_magenta_glazed_terracotta.tooltip.function_1").formatted(Formatting.GOLD));
        tooltip.add(Text.translatable("block.extra_terracotta_utilities.buffering_magenta_glazed_terracotta.tooltip.function_2").formatted(Formatting.GOLD));
        tooltip.add(Text.translatable("block.extra_terracotta_utilities.buffering_magenta_glazed_terracotta.tooltip.function_3").formatted(Formatting.GOLD));
        tooltip.add(Text.translatable("tooltip.extra_terracotta_utilities.usage").formatted(Formatting.DARK_AQUA));
        tooltip.add(Text.translatable("block.extra_terracotta_utilities.buffering_magenta_glazed_terracotta.tooltip.usage_1").formatted(Formatting.DARK_AQUA));
        tooltip.add(Text.translatable("block.extra_terracotta_utilities.buffering_magenta_glazed_terracotta.tooltip.usage_2").formatted(Formatting.DARK_AQUA));
        tooltip.add(Text.translatable("block.extra_terracotta_utilities.buffering_magenta_glazed_terracotta.tooltip.usage_3").formatted(Formatting.DARK_AQUA));
    }


}
