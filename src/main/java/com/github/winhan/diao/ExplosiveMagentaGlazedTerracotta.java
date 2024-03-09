package com.github.winhan.diao;

import com.github.winhan.diao.utility.BlockFacingUtility;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FacingBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ExplosiveMagentaGlazedTerracotta extends FacingBlock {
//    public static final MapCodec<ExplosiveMagentaGlazedTerracotta> CODEC = createCodec(ExplosiveMagentaGlazedTerracotta::new);
    public static final BooleanProperty POWERED = Properties.POWERED;

    public float getMaxBlastResistance() {
        return Blocks.OBSIDIAN.getBlastResistance() / 2;
    }

//    @Override
//    protected MapCodec<? extends FacingBlock> getCodec() {
//        return CODEC;
//    }

    public ExplosiveMagentaGlazedTerracotta(Settings settings) {
        super(settings);
        this.setDefaultState(((this.stateManager.getDefaultState()).with(FACING, Direction.SOUTH)).with(POWERED, false));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayer().isSneaking() ? ctx.getPlayerLookDirection().getOpposite() : ctx.getPlayerLookDirection());
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWERED);
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (world.isReceivingRedstonePower(pos)) {
            world.scheduleBlockTick(pos, world.getBlockState(pos).getBlock(), 5);
        }
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (world.isReceivingRedstonePower(pos)) {
            world.scheduleBlockTick(pos, world.getBlockState(pos).getBlock(), 5);
        }
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        BlockFacingUtility blockFacingUtility = BlockFacingUtility.getByDirection(state.get(FACING));
        BlockPos posTowards = pos.add(blockFacingUtility.getVec3i());
        List<BlockPos> list = new ArrayList<>();
        if (!world.getBlockState(posTowards).isAir() && world.getBlockState(posTowards).getBlock().getBlastResistance() <= getMaxBlastResistance() && world.getBlockState(posTowards).getBlock().getHardness() >= 0) {
            list.add(posTowards);
            Explosion explosion = new Explosion(world, null, pos.getX(), pos.getY(), pos.getZ(), 10.0f, false, Explosion.DestructionType.DESTROY, list);
            explosion.affectWorld(false);
            world.playSound(null, posTowards, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 1.0f, 1.0f);
            world.spawnParticles(ParticleTypes.EXPLOSION, posTowards.getX()+.5, posTowards.getY()+.5, posTowards.getZ()+.5, 1, 0f, 0f, 0f, 1.0f);
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        tooltip.add(Text.translatable("tooltip.extra_terracotta_utilities.info.sneak"));
        tooltip.add(Text.translatable("tooltip.extra_terracotta_utilities.function").formatted(Formatting.GOLD));
        tooltip.add(Text.translatable("block.extra_terracotta_utilities.explosive_magenta_glazed_terracotta.tooltip.function_1").formatted(Formatting.GOLD));
        tooltip.add(Text.translatable("block.extra_terracotta_utilities.explosive_magenta_glazed_terracotta.tooltip.function_2").formatted(Formatting.GOLD));
        tooltip.add(Text.translatable("block.extra_terracotta_utilities.explosive_magenta_glazed_terracotta.tooltip.function_3").formatted(Formatting.GOLD));
        tooltip.add(Text.translatable("tooltip.extra_terracotta_utilities.usage").formatted(Formatting.DARK_AQUA));
        tooltip.add(Text.translatable("block.extra_terracotta_utilities.explosive_magenta_glazed_terracotta.tooltip.usage").formatted(Formatting.DARK_AQUA));
    }

}
