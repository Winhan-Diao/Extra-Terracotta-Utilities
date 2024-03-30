package com.github.winhan.diao;

import com.github.winhan.diao.utility.BlockFacingUtility;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EntropyReducingMagentaGlazedTerracotta extends BlockWithEntity {
    public static final int BREAK_CHANCE = 10;
    public static final DirectionProperty FACING = FacingBlock.FACING;

    public EntropyReducingMagentaGlazedTerracotta(Settings settings) {
        super(settings);
        this.setDefaultState(((this.stateManager.getDefaultState()).with(FACING, Direction.SOUTH)));
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new EntropyReducingMagentaGlazedTerracottaEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        // 由于继承了BlockWithEntity，这个默认为INVISIBLE，所以我们需要更改它！
        return BlockRenderType.MODEL;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayer().isSneaking() ? ctx.getPlayerLookDirection().getOpposite() : ctx.getPlayerLookDirection());
    }

    /**To do:
     * remove awkward usage of utility (done)
     * add randomDisplayTick for water freezing (done)**/
    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        BlockPos backPos = pos.offset(state.get(FACING));
        BlockPos targetPos = pos.offset(state.get(FACING).getOpposite());
        if (world.getBlockState(targetPos).isOf(Blocks.WATER) && world.getBlockState(backPos).isIn(BlockTags.ICE)) {
            world.setBlockState(targetPos, Blocks.ICE.getDefaultState(), NOTIFY_ALL);
            EntropyReducingMagentaGlazedTerracottaEntity.chanceToBreakIce(pos.offset(state.get(FACING)), world, BREAK_CHANCE);
        }

    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        BlockPos backPos = pos.offset(state.get(FACING));
        BlockPos targetPos = pos.offset(state.get(FACING).getOpposite());
        if ((random.nextInt(10) == 0) && world.getBlockState(targetPos).isOf(Blocks.WATER) && world.getBlockState(backPos).isIn(BlockTags.ICE)) {
            world.addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.ICE.getDefaultState()),
                    targetPos.getX() + .5 + MathHelper.nextBetween(random, -.4f, .4f),
                    targetPos.getY() + .5 + MathHelper.nextBetween(random, -.4f, .4f),
                    targetPos.getZ() + .5 + MathHelper.nextBetween(random, -.4f, .4f),
                    0, 0, 0);
        }
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, Initializer.ENTROPY_REDUCING_MAGENTA_GLAZED_TERRACOTTA_ENTITY, EntropyReducingMagentaGlazedTerracottaEntity::constantTick);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        tooltip.add(Text.translatable("tooltip.extra_terracotta_utilities.info.sneak"));
        tooltip.add(Text.translatable("tooltip.extra_terracotta_utilities.function").formatted(Formatting.GOLD));
        tooltip.add(Text.translatable("block.extra_terracotta_utilities.entropy_reducing_magenta_glazed_terracotta.tooltip.function_1", Text.translatable(Initializer.EXPLOSIVE_MAGENTA_GLAZED_TERRACOTTA.getTranslationKey())).formatted(Formatting.GOLD));
        tooltip.add(Text.translatable("block.extra_terracotta_utilities.entropy_reducing_magenta_glazed_terracotta.tooltip.function_2").formatted(Formatting.GOLD));
        tooltip.add(Text.translatable("block.extra_terracotta_utilities.entropy_reducing_magenta_glazed_terracotta.tooltip.function_3").formatted(Formatting.GOLD));
        tooltip.add(Text.translatable("tooltip.extra_terracotta_utilities.usage").formatted(Formatting.DARK_AQUA));
        tooltip.add(Text.translatable("block.extra_terracotta_utilities.entropy_reducing_magenta_glazed_terracotta.tooltip.usage_1").formatted(Formatting.DARK_AQUA));
        tooltip.add(Text.translatable("block.extra_terracotta_utilities.entropy_reducing_magenta_glazed_terracotta.tooltip.usage_2", BREAK_CHANCE).formatted(Formatting.DARK_AQUA));

    }
}
