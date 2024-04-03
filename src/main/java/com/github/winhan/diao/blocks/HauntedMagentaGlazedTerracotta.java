package com.github.winhan.diao.blocks;

import com.github.winhan.diao.utilities.Utilities;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class HauntedMagentaGlazedTerracotta extends HorizontalDirectionalBlock {
    public static final int TRANSFORMATION_TIME = 200;


    public HauntedMagentaGlazedTerracotta(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getPlayer().isShiftKeyDown()? pContext.getHorizontalDirection().getOpposite() : pContext.getHorizontalDirection());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pMovedByPiston) {
        BlockPos pPosAbove = pPos.above();
        Block pBlockAbove = pLevel.getBlockState(pPosAbove).getBlock();
        if (Arrays.stream(BlocksForTransform.values()).map(BlocksForTransform::getBlock).anyMatch(pBlockAbove::equals)) {
            pLevel.scheduleTick(pPos, pLevel.getBlockState(pPos).getBlock(), TRANSFORMATION_TIME);
        }
    }

    @Override
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pNeighborBlock, BlockPos pNeighborPos, boolean pMovedByPiston) {
        BlockPos pPosAbove = pPos.above();
        Block pBlockAbove = pLevel.getBlockState(pPosAbove).getBlock();
        if (pNeighborPos.equals(pPosAbove) && Arrays.stream(BlocksForTransform.values()).map(BlocksForTransform::getBlock).anyMatch(pBlockAbove::equals)) {
            pLevel.scheduleTick(pPos, pLevel.getBlockState(pPos).getBlock(), TRANSFORMATION_TIME);
        }
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        Block blockAbove = pLevel.getBlockState(pPos.above()).getBlock();
        Arrays.stream(BlocksForTransform.values()).filter(blockFT -> blockFT.getBlock().equals(blockAbove)).map(BlocksForTransform::getEntityType).forEach(entityType -> Utilities.mobCreateAndSpawn(entityType, pLevel, pPos.above()));
    }

    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        if (!pLevel.getBlockState(pPos.above()).isAir()) {
            for (int i = 0; i < 6; ++i) {
                int j = pRandom.nextInt(2) * 2 - 1;
                int k = pRandom.nextInt(2) * 2 - 1;
                double d0 = (double) pPos.getX() + 0.5D + 0.25D * (double) j;
                double d1 = (double) ((float) pPos.getY() + pRandom.nextFloat());
                double d2 = (double) pPos.getZ() + 0.5D + 0.25D * (double) k;
                pLevel.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0, .1, 0);
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        pTooltip.add(Component.translatable("tooltip.extra_terracotta_utilities.info.sneak"));
        pTooltip.add(Component.translatable("tooltip.extra_terracotta_utilities.function").withStyle(ChatFormatting.GOLD));
        pTooltip.add(Component.translatable("block.extra_terracotta_utilities.haunted_magenta_glazed_terracotta.tooltip.function_1").withStyle(ChatFormatting.GOLD));
        pTooltip.add(Component.translatable("tooltip.extra_terracotta_utilities.usage").withStyle(ChatFormatting.DARK_AQUA));
        pTooltip.add(Component.translatable("block.extra_terracotta_utilities.haunted_magenta_glazed_terracotta.tooltip.usage").withStyle(ChatFormatting.DARK_AQUA));
        pTooltip.add(Component.translatable("tooltip.extra_terracotta_utilities.details").withStyle(ChatFormatting.DARK_GREEN));
        if(Screen.hasShiftDown()) {
            pTooltip.add((Component.literal("  ").append(Component.translatable(Blocks.NETHERRACK.getDescriptionId())).append(" -> ").append(Component.translatable(EntityType.ZOMBIE.getDescriptionId()))).withStyle(ChatFormatting.DARK_GREEN));
            pTooltip.add((Component.literal("  ").append(Component.translatable(Blocks.NETHER_GOLD_ORE.getDescriptionId())).append(" -> ").append(Component.translatable(EntityType.ZOMBIFIED_PIGLIN.getDescriptionId()))).withStyle(ChatFormatting.DARK_GREEN));
            pTooltip.add((Component.literal("  ").append(Component.translatable(Blocks.MAGMA_BLOCK.getDescriptionId())).append(" -> ").append(Component.translatable(EntityType.MAGMA_CUBE.getDescriptionId()))).withStyle(ChatFormatting.DARK_GREEN));
            pTooltip.add((Component.literal("  ").append(Component.translatable(Blocks.GLOWSTONE.getDescriptionId())).append(" -> ").append(Component.translatable(EntityType.WITHER_SKELETON.getDescriptionId()))).withStyle(ChatFormatting.DARK_GREEN));
            pTooltip.add((Component.literal("  ").append(Component.translatable(Blocks.COAL_BLOCK.getDescriptionId())).append(" -> ").append(Component.translatable(EntityType.ALLAY.getDescriptionId()))).withStyle(ChatFormatting.DARK_GREEN));
            pTooltip.add((Component.literal("  ").append(Component.translatable(Blocks.AMETHYST_CLUSTER.getDescriptionId())).append(" -> ").append(Component.translatable(EntityType.GHAST.getDescriptionId()))).withStyle(ChatFormatting.DARK_GREEN));
            pTooltip.add((Component.literal("  ").append(Component.translatable(Blocks.CRYING_OBSIDIAN.getDescriptionId())).append(" -> ").append(Component.translatable(EntityType.ZOMBIE.getDescriptionId()))).withStyle(ChatFormatting.DARK_GREEN));
            pTooltip.add((Component.literal("  ").append(Component.translatable(Blocks.PURPUR_BLOCK.getDescriptionId())).append(" -> ").append(Component.translatable(EntityType.SHULKER.getDescriptionId()))).withStyle(ChatFormatting.DARK_GREEN));
            pTooltip.add((Component.literal("  ").append(Component.translatable(Blocks.DRIED_KELP_BLOCK.getDescriptionId())).append(" -> ").append(Component.translatable(EntityType.DROWNED.getDescriptionId()))).withStyle(ChatFormatting.DARK_GREEN));

        }

    }

    public enum BlocksForTransform {
        NETHERRACK(Blocks.NETHERRACK, EntityType.ZOMBIE),
        NETHER_GOLD_ORE(Blocks.NETHER_GOLD_ORE, EntityType.ZOMBIFIED_PIGLIN),
        MAGMA_BLOCK(Blocks.MAGMA_BLOCK, EntityType.MAGMA_CUBE),
        KELP_BLOCK(Blocks.DRIED_KELP_BLOCK, EntityType.DROWNED),
        CRYING_OBSIDIAN(Blocks.CRYING_OBSIDIAN, EntityType.GHAST),
        AMETHYST_BLOCK(Blocks.AMETHYST_CLUSTER, EntityType.ALLAY),
        COAL_BLOCK(Blocks.COAL_BLOCK, EntityType.WITHER_SKELETON),
        PURPUR_BLOCK(Blocks.PURPUR_BLOCK, EntityType.SHULKER),
        GLOWSTONE(Blocks.GLOWSTONE, EntityType.BLAZE);

        private final Block block;
        private final EntityType<? extends Mob> entityType;

        BlocksForTransform(Block block, EntityType<? extends Mob> entityType) {
            this.block = block;
            this.entityType = entityType;
        }

        public Block getBlock() {
            return block;
        }

        public EntityType<? extends Mob> getEntityType() {
            return entityType;
        }

    }

}
//pLevel.getPlayers(Entity::isAlive).forEach(player -> player.sendSystemMessage(Component.literal("YES")));
