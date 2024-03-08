package com.github.winhan.diao;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AllayedMagentaGlazedTerracotta extends BlockWithEntity {
    public static final DirectionProperty FACING = FacingBlock.FACING;
    public static final MapCodec<AllayedMagentaGlazedTerracotta> CODEC = createCodec(AllayedMagentaGlazedTerracotta::new);

    @Override
    public MapCodec<AllayedMagentaGlazedTerracotta> getCodec() {
        return CODEC;
    }

    public AllayedMagentaGlazedTerracotta(Settings settings) {
        super(settings);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        // 由于继承了BlockWithEntity，这个默认为INVISIBLE，所以我们需要更改它！
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new AllayedMagentaGlazedTerracottaEntity(pos, state);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        this.setDefaultState(((this.stateManager.getDefaultState()).with(FACING, Direction.SOUTH)));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayer().isSneaking() ? ctx.getPlayerLookDirection().getOpposite() : ctx.getPlayerLookDirection());
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? null : validateTicker(type, Initializer.ALLAYED_MAGENTA_GLAZED_TERRACOTTA_ENTITY, AllayedMagentaGlazedTerracottaEntity::serverTick);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        world.markDirty(pos);
        ItemStack itemStack = player.getStackInHand(hand);
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof AllayedMagentaGlazedTerracottaEntity) {
            AllayedMagentaGlazedTerracottaEntity amgtEntity = (AllayedMagentaGlazedTerracottaEntity) blockEntity;
            if (!player.getStackInHand(hand).isEmpty()) {
                world.playSound(null, pos, SoundEvents.ENTITY_ALLAY_ITEM_GIVEN, SoundCategory.BLOCKS, 1, 1);
                NbtCompound nbtToRead = new NbtCompound();
                nbtToRead.putString("filter", itemStack.getTranslationKey());
                amgtEntity.readNbt(nbtToRead);
            }
            NbtCompound nbtToWrite = new NbtCompound();
            amgtEntity.writeNbt(nbtToWrite);
            if (!world.isClient) {
                player.sendMessage(Text.translatable("block.extra_terracotta_utilities.allayed_magenta_glazed_terracotta.message").append(Text.translatable(nbtToWrite.getString("filter")).formatted(Formatting.AQUA)));
            }
            return  ActionResult.SUCCESS;
        }
        return  ActionResult.PASS;
    }


    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        tooltip.add(Text.translatable("tooltip.extra_terracotta_utilities.info.sneak"));
        tooltip.add(Text.translatable("tooltip.extra_terracotta_utilities.function").formatted(Formatting.GOLD));
        tooltip.add(Text.translatable("block.extra_terracotta_utilities.allayed_magenta_glazed_terracotta.tooltip.function_1").formatted(Formatting.GOLD));
        tooltip.add(Text.translatable("block.extra_terracotta_utilities.allayed_magenta_glazed_terracotta.tooltip.function_2").formatted(Formatting.GOLD));
        tooltip.add(Text.translatable("tooltip.extra_terracotta_utilities.usage").formatted(Formatting.DARK_AQUA));
        tooltip.add(Text.translatable("block.extra_terracotta_utilities.allayed_magenta_glazed_terracotta.tooltip.usage").formatted(Formatting.DARK_AQUA));
    }
}
