package com.github.winhan.diao;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FacingBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EnderizedMagentaGlazedTerracotta extends FacingBlock {
//    public static final MapCodec<EnderizedMagentaGlazedTerracotta> CODEC = createCodec(EnderizedMagentaGlazedTerracotta::new);

    public EnderizedMagentaGlazedTerracotta(Settings settings) {
        super(settings);
        this.setDefaultState(((this.stateManager.getDefaultState()).with(FACING, Direction.SOUTH))/*.with(POWERED, false)*/);
    }

//    @Override
//    protected MapCodec<? extends FacingBlock> getCodec() {
//        return CODEC;
//    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayer().isSneaking() ? ctx.getPlayerLookDirection().getOpposite() : ctx.getPlayerLookDirection());
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        world.scheduleBlockTick(pos, world.getBlockState(pos).getBlock(), 20);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (!world.isClient()) {
            world.scheduleBlockTick(pos, world.getBlockState(pos).getBlock(), 20);

        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        switch (state.get(FACING)) {
            case NORTH -> {
                if (!world.getBlockState(pos.north()).isAir() && world.getBlockState(pos.south()).isAir()) {
                    world.getServer().getCommandManager().executeWithPrefix(world.getServer().getCommandSource().withOutput(CommandOutput.DUMMY),
                            String.format("clone %d %d %d %d %d %d %d %d %d", pos.getX(), pos.getY(), pos.getZ() - 1, pos.getX(), pos.getY(), pos.getZ() - 1, pos.getX(), pos.getY(), pos.getZ() + 1));
                    world.removeBlockEntity(pos.north());
                    world.setBlockState(pos.north(), Blocks.AIR.getDefaultState(), Block.field_31022);
                    world.playSound(null, pos, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.BLOCKS, 1.0F, 1.0F);
                }
            }
            case SOUTH -> {
                if (!world.getBlockState(pos.south()).isAir() && world.getBlockState(pos.north()).isAir()) {
                    world.getServer().getCommandManager().executeWithPrefix(world.getServer().getCommandSource().withOutput(CommandOutput.DUMMY),
                            String.format("clone %d %d %d %d %d %d %d %d %d", pos.getX(), pos.getY(), pos.getZ() + 1, pos.getX(), pos.getY(), pos.getZ() + 1, pos.getX(), pos.getY(), pos.getZ() - 1));
                    world.removeBlockEntity(pos.south());
                    world.setBlockState(pos.south(), Blocks.AIR.getDefaultState(), Block.field_31022);
                    world.playSound(null, pos, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.BLOCKS, 1.0F, 1.0F);
                }
            }
            case EAST -> {
                if (!world.getBlockState(pos.east()).isAir() && world.getBlockState(pos.west()).isAir()) {
                    world.getServer().getCommandManager().executeWithPrefix(world.getServer().getCommandSource().withOutput(CommandOutput.DUMMY),
                            String.format("clone %d %d %d %d %d %d %d %d %d", pos.getX() + 1, pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY(), pos.getZ(), pos.getX() - 1, pos.getY(), pos.getZ()));
                    world.removeBlockEntity(pos.east());
                    world.setBlockState(pos.east(), Blocks.AIR.getDefaultState(), Block.field_31022);
                    world.playSound(null, pos, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.BLOCKS, 1.0F, 1.0F);
                }
            }
            case WEST -> {
                if (!world.getBlockState(pos.west()).isAir() && world.getBlockState(pos.east()).isAir()) {
                    world.getServer().getCommandManager().executeWithPrefix(world.getServer().getCommandSource().withOutput(CommandOutput.DUMMY),
                            String.format("clone %d %d %d %d %d %d %d %d %d", pos.getX() - 1, pos.getY(), pos.getZ(), pos.getX() - 1, pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY(), pos.getZ()));
                    world.removeBlockEntity(pos.west());
                    world.setBlockState(pos.west(), Blocks.AIR.getDefaultState(), Block.field_31022);
                    world.playSound(null, pos, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.BLOCKS, 1.0F, 1.0F);
                }
            }
            case UP -> {
                if (!world.getBlockState(pos.up()).isAir() && world.getBlockState(pos.down()).isAir()) {
                    world.getServer().getCommandManager().executeWithPrefix(world.getServer().getCommandSource().withOutput(CommandOutput.DUMMY),
                            String.format("clone %d %d %d %d %d %d %d %d %d", pos.getX(), pos.getY() + 1, pos.getZ(), pos.getX(), pos.getY() + 1, pos.getZ(), pos.getX(), pos.getY() - 1, pos.getZ()));
                    world.removeBlockEntity(pos.up());
                    world.setBlockState(pos.up(), Blocks.AIR.getDefaultState(), Block.field_31022);
                    world.playSound(null, pos, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.BLOCKS, 1.0F, 1.0F);
                }
            }
            case DOWN -> {
                if (!world.getBlockState(pos.down()).isAir() && world.getBlockState(pos.up()).isAir()) {
                    world.getServer().getCommandManager().executeWithPrefix(world.getServer().getCommandSource().withOutput(CommandOutput.DUMMY),
                            String.format("clone %d %d %d %d %d %d %d %d %d", pos.getX(), pos.getY() - 1, pos.getZ(), pos.getX(), pos.getY() - 1, pos.getZ(), pos.getX(), pos.getY() + 1, pos.getZ()));
                    world.removeBlockEntity(pos.down());
                    world.setBlockState(pos.down(), Blocks.AIR.getDefaultState(), Block.field_31022);
                    world.playSound(null, pos, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.BLOCKS, 1.0F, 1.0F);
                }
            }
        }
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        for(int i = 0; i < 3; ++i) {
            int j = random.nextInt(2) * 2 - 1;
            int k = random.nextInt(2) * 2 - 1;
            double d = (double)pos.getX() + 0.5 + 0.25 * (double)j;
            double e = (double)((float)pos.getY() + random.nextFloat());
            double f = (double)pos.getZ() + 0.5 + 0.25 * (double)k;
            double g = (double)(random.nextFloat() * (float)j);
            double h = ((double)random.nextFloat() - 0.5) * 0.125;
            double l = (double)(random.nextFloat() * (float)k);
            world.addParticle(ParticleTypes.PORTAL, d, e, f, g, h, l);
        }

    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        tooltip.add(Text.translatable("tooltip.extra_terracotta_utilities.info.sneak"));
        tooltip.add(Text.translatable("tooltip.extra_terracotta_utilities.function").formatted(Formatting.GOLD));
        tooltip.add(Text.translatable("block.extra_terracotta_utilities.enderized_magenta_glazed_terracotta.tooltip.function_1").formatted(Formatting.GOLD));
        tooltip.add(Text.translatable("block.extra_terracotta_utilities.enderized_magenta_glazed_terracotta.tooltip.function_2").formatted(Formatting.GOLD));
    }

}
