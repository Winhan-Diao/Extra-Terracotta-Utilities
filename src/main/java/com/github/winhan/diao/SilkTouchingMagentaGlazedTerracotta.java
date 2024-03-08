package com.github.winhan.diao;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SilkTouchingMagentaGlazedTerracotta extends ExplosiveMagentaGlazedTerracotta{

    public static final int BREAK_CHANCE = 10;
    public SilkTouchingMagentaGlazedTerracotta(Settings settings) {
        super(settings);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayer().isSneaking() ? ctx.getPlayerLookDirection().getOpposite() : ctx.getPlayerLookDirection());
    }


    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        ItemStack tool = Items.STONE_PICKAXE.getDefaultStack();
        tool.addEnchantment(Enchantments.SILK_TOUCH, 1);
        BlockPos targetPos = pos.offset(state.get(Properties.FACING).getOpposite());
        BlockState targetState = world.getBlockState(targetPos);
        if (world.isReceivingRedstonePower(pos)) {
            if (!world.getBlockState(targetPos).isAir() && world.getBlockState(targetPos).getBlock().getBlastResistance() <= getMaxBlastResistance() && world.getBlockState(targetPos).getBlock().getHardness() >= 0) {
                Block.dropStacks(targetState, world, targetPos, world.getBlockEntity(targetPos), null, tool);
                world.breakBlock(targetPos, false);
                world.playSound(null, targetPos, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 1.0f, 1.0f);
                world.spawnParticles(ParticleTypes.EXPLOSION, targetPos.getX() + .5, targetPos.getY() + .5, targetPos.getZ() + .5, 1, 0f, 0f, 0f, 1.0f);
                if (random.nextInt(100) < BREAK_CHANCE) {
                    world.setBlockState(pos, Initializer.SILK_TOUCHING_MAGENTA_GLAZED_TERRACOTTA_UNCOVERED.getDefaultState().with(Properties.FACING, state.get(FACING)), NOTIFY_ALL);
                    world.playSound(null, targetPos, SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 1.0f, 1.0f);
                }
            }
        }
    }

    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return 15;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        tooltip.add(Text.translatable("tooltip.extra_terracotta_utilities.info.sneak"));
        tooltip.add(Text.translatable("tooltip.extra_terracotta_utilities.function").formatted(Formatting.GOLD));
        tooltip.add(Text.translatable("block.extra_terracotta_utilities.silk_touching_magenta_glazed_terracotta.tooltip.function_1", Text.translatable("block.extra_terracotta_utilities.explosive_magenta_glazed_terracotta")).formatted(Formatting.GOLD));
        tooltip.add(Text.translatable("block.extra_terracotta_utilities.silk_touching_magenta_glazed_terracotta.tooltip.function_2").formatted(Formatting.GOLD));
        tooltip.add(Text.translatable("block.extra_terracotta_utilities.silk_touching_magenta_glazed_terracotta.tooltip.function_3").formatted(Formatting.GOLD));
        tooltip.add(Text.translatable("block.extra_terracotta_utilities.silk_touching_magenta_glazed_terracotta.tooltip.function_4", BREAK_CHANCE).formatted(Formatting.GOLD));
        tooltip.add(Text.translatable("tooltip.extra_terracotta_utilities.usage").formatted(Formatting.DARK_AQUA));
        tooltip.add(Text.translatable("block.extra_terracotta_utilities.silk_touching_magenta_glazed_terracotta.tooltip.usage_1").formatted(Formatting.DARK_AQUA));
        tooltip.add(Text.translatable("block.extra_terracotta_utilities.silk_touching_magenta_glazed_terracotta.tooltip.usage_2").formatted(Formatting.DARK_AQUA));
        tooltip.add(Text.translatable("block.extra_terracotta_utilities.silk_touching_magenta_glazed_terracotta.tooltip.usage_3").formatted(Formatting.DARK_AQUA));
        tooltip.add(Text.translatable("block.extra_terracotta_utilities.silk_touching_magenta_glazed_terracotta.tooltip.usage_4").formatted(Formatting.DARK_AQUA));
    }

}

class SilkTouchingMagentaGlazedTerracottaUncovered extends SilkTouchingMagentaGlazedTerracotta{

    public SilkTouchingMagentaGlazedTerracottaUncovered(Settings settings) {
        super(settings);
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.isIn(ItemTags.WOOL) || itemStack.isIn(ItemTags.WOOL_CARPETS)) {
            if (!player.isCreative()) {
                itemStack.decrement(1);
            }
            world.setBlockState(pos, Initializer.SILK_TOUCHING_MAGENTA_GLAZED_TERRACOTTA.getDefaultState().with(Properties.FACING, state.get(FACING)), NOTIFY_ALL);
            world.playSound(null, pos, SoundEvents.BLOCK_WOOL_PLACE, SoundCategory.BLOCKS, 1.5f, .5f);
            return ActionResult.SUCCESS;
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return false;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        tooltip.add(Text.translatable("block.extra_terracotta_utilities.silk_touching_magenta_glazed_terracotta_uncovered.tooltip.add_1").formatted(Formatting.DARK_GRAY, Formatting.BOLD));
        super.appendTooltip(stack, world, tooltip, options);
    }
}
