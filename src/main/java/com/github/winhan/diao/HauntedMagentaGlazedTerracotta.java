package com.github.winhan.diao;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.AllayEntity;
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
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HauntedMagentaGlazedTerracotta extends HorizontalFacingBlock {
//    public static final MapCodec<GlazedTerracottaBlock> CODEC = createCodec(GlazedTerracottaBlock::new);

    protected HauntedMagentaGlazedTerracotta(Settings settings) {
        super(settings);
        this.setDefaultState((this.stateManager.getDefaultState()).with(FACING, Direction.NORTH));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }


//    @Override
//    public MapCodec<GlazedTerracottaBlock> getCodec() {
//        return CODEC;
//    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }


    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (sourcePos.equals(pos.up()) && (world.getBlockState(pos.up()).isOf(Blocks.NETHERRACK) ||
                world.getBlockState(pos.up()).isOf(Blocks.NETHER_GOLD_ORE) ||
                world.getBlockState(pos.up()).isOf(Blocks.MAGMA_BLOCK) ||
                world.getBlockState(pos.up()).isOf(Blocks.GLOWSTONE) ||
                world.getBlockState(pos.up()).isOf(Blocks.DRIED_KELP_BLOCK) ||
                world.getBlockState(pos.up()).isOf(Blocks.COAL_BLOCK) ||
                world.getBlockState(pos.up()).isOf(Blocks.PURPUR_BLOCK) ||
                world.getBlockState(pos.up()).isOf(Blocks.CRYING_OBSIDIAN) ||
                world.getBlockState(pos.up()).isOf(Blocks.AMETHYST_CLUSTER))) {
            world.scheduleBlockTick(pos, state.getBlock(), 200);
        }

    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (!world.getBlockState(pos.up()).isAir()) {
            for (int i = 0; i < 6; ++i) {
                int j = random.nextInt(2) * 2 - 1;
                int k = random.nextInt(2) * 2 - 1;
                double d = (double) pos.getX() + 0.5 + 0.25 * (double) j;
                double e = (double) ((float) pos.getY() + random.nextFloat() + 2);
                double f = (double) pos.getZ() + 0.5 + 0.25 * (double) k;
                world.addParticle(ParticleTypes.SMOKE, d, e, f, 0, .1, 0);
            }
        }
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        BlockPos posUp = pos.up();
        BlockState stateUp = world.getBlockState(posUp);
        Entity entity = null;
        if (stateUp.isOf(Blocks.NETHERRACK)) {
            entity = new ZombieEntity(EntityType.ZOMBIE, world);
        } else if (stateUp.isOf(Blocks.NETHER_GOLD_ORE)) {
            entity = new ZombifiedPiglinEntity(EntityType.ZOMBIFIED_PIGLIN, world);
        } else if (stateUp.isOf(Blocks.MAGMA_BLOCK)) {
            world.getServer().getCommandManager().executeWithPrefix(world.getServer().getCommandSource().withOutput(CommandOutput.DUMMY),
                    String.format("summon magma_cube %s %s %s", posUp.getX(), posUp.getY(), posUp.getZ()) );
        } else if (stateUp.isOf(Blocks.GLOWSTONE)) {
            entity = new BlazeEntity(EntityType.BLAZE, world);
        } else if (stateUp.isOf(Blocks.DRIED_KELP_BLOCK)) {
            entity = new DrownedEntity(EntityType.DROWNED, world);
        } else if (stateUp.isOf(Blocks.COAL_BLOCK)) {
            entity = new WitherSkeletonEntity(EntityType.WITHER_SKELETON, world);
        } else if (stateUp.isOf(Blocks.PURPUR_BLOCK)) {
            entity = new ShulkerEntity(EntityType.SHULKER, world);
        } else if (stateUp.isOf(Blocks.CRYING_OBSIDIAN)) {
            entity = new GhastEntity(EntityType.GHAST, world);
        } else if (stateUp.isOf(Blocks.AMETHYST_CLUSTER)) {
            entity = new AllayEntity(EntityType.ALLAY, world);
        } else {
            return;
        }
        world.playSound(null, posUp, SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, SoundCategory.BLOCKS, .8f, 1.0f);
        world.breakBlock(posUp, false);
        if (entity != null) {
            entity.updatePosition(posUp.getX() + .5, posUp.getY(), posUp.getZ() + .5);
            world.spawnEntity(entity);
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        tooltip.add(Text.translatable("tooltip.extra_terracotta_utilities.info.sneak"));
        tooltip.add(Text.translatable("tooltip.extra_terracotta_utilities.function").formatted(Formatting.GOLD));
        tooltip.add(Text.translatable("block.extra_terracotta_utilities.haunted_magenta_glazed_terracotta.tooltip.function_1").formatted(Formatting.GOLD));
        tooltip.add(Text.translatable("tooltip.extra_terracotta_utilities.usage").formatted(Formatting.DARK_AQUA));
        tooltip.add(Text.translatable("block.extra_terracotta_utilities.haunted_magenta_glazed_terracotta.tooltip.usage").formatted(Formatting.DARK_AQUA));
        tooltip.add(Text.translatable("tooltip.extra_terracotta_utilities.details").formatted(Formatting.DARK_GREEN));
        if(Screen.hasShiftDown()) {
            tooltip.add((Text.literal("  ").append(Text.translatable(Blocks.NETHERRACK.getTranslationKey())).append(" -> ").append(Text.translatable(EntityType.ZOMBIE.getTranslationKey()))).formatted(Formatting.DARK_GREEN));
            tooltip.add((Text.literal("  ").append(Text.translatable(Blocks.NETHER_GOLD_ORE.getTranslationKey())).append(" -> ").append(Text.translatable(EntityType.ZOMBIFIED_PIGLIN.getTranslationKey()))).formatted(Formatting.DARK_GREEN));
            tooltip.add((Text.literal("  ").append(Text.translatable(Blocks.MAGMA_BLOCK.getTranslationKey())).append(" -> ").append(Text.translatable(EntityType.MAGMA_CUBE.getTranslationKey()))).formatted(Formatting.DARK_GREEN));
            tooltip.add((Text.literal("  ").append(Text.translatable(Blocks.GLOWSTONE.getTranslationKey())).append(" -> ").append(Text.translatable(EntityType.WITHER_SKELETON.getTranslationKey()))).formatted(Formatting.DARK_GREEN));
            tooltip.add((Text.literal("  ").append(Text.translatable(Blocks.COAL_BLOCK.getTranslationKey())).append(" -> ").append(Text.translatable(EntityType.ALLAY.getTranslationKey()))).formatted(Formatting.DARK_GREEN));
            tooltip.add((Text.literal("  ").append(Text.translatable(Blocks.AMETHYST_CLUSTER.getTranslationKey())).append(" -> ").append(Text.translatable(EntityType.GHAST.getTranslationKey()))).formatted(Formatting.DARK_GREEN));
            tooltip.add((Text.literal("  ").append(Text.translatable(Blocks.CRYING_OBSIDIAN.getTranslationKey())).append(" -> ").append(Text.translatable(EntityType.ZOMBIE.getTranslationKey()))).formatted(Formatting.DARK_GREEN));
            tooltip.add((Text.literal("  ").append(Text.translatable(Blocks.PURPUR_BLOCK.getTranslationKey())).append(" -> ").append(Text.translatable(EntityType.SHULKER.getTranslationKey()))).formatted(Formatting.DARK_GREEN));
            tooltip.add((Text.literal("  ").append(Text.translatable(Blocks.DRIED_KELP_BLOCK.getTranslationKey())).append(" -> ").append(Text.translatable(EntityType.DROWNED.getTranslationKey()))).formatted(Formatting.DARK_GREEN));

        }

    }

}
