package com.github.winhan.diao;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.command.CommandOutput;
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
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AllayedEnderizedMagentaGlazedTerracotta extends AllayedMagentaGlazedTerracotta {
    public static final BooleanProperty WHITELIST = BooleanProperty.of("whitelist");
    public AllayedEnderizedMagentaGlazedTerracotta(Settings settings) {
        super(settings);
        this.setDefaultState(((this.stateManager.getDefaultState()).with(FACING, Direction.SOUTH).with(WHITELIST, true)));
    }
    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new AllayedEnderizedMagentaGlazedTerracottaEntity(pos, state);
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        world.scheduleBlockTick(pos, world.getBlockState(pos).getBlock(), 20);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, WHITELIST);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
            world.scheduleBlockTick(pos, world.getBlockState(pos).getBlock(), 20);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        world.markDirty(pos);
        ItemStack itemStack = player.getStackInHand(hand);
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof AllayedEnderizedMagentaGlazedTerracottaEntity) {
            AllayedEnderizedMagentaGlazedTerracottaEntity aemgtEntity = (AllayedEnderizedMagentaGlazedTerracottaEntity) blockEntity;
            if (!player.getStackInHand(hand).isEmpty()) {
                if (itemStack.getItem() instanceof BlockItem) {
                    world.playSound(null, pos, SoundEvents.ENTITY_ENDERMAN_AMBIENT, SoundCategory.BLOCKS, 1, 1);
                    NbtCompound nbtToRead = new NbtCompound();
                    nbtToRead.putString("filter", itemStack.getTranslationKey());
                    aemgtEntity.readNbt(nbtToRead);
                    state.updateNeighbors(world, pos, NOTIFY_ALL);
                } else {
                    if (!world.isClient) {
                        player.sendMessage(Text.translatable("block.extra_terracotta_utilities.allayed_magenta_glazed_terracotta.warn"));
                    }
                    return ActionResult.SUCCESS;
                }
            } else if (player.isSneaking()) {
                world.setBlockState(pos, state.with(WHITELIST, !state.get(WHITELIST)), NOTIFY_ALL);
                if (!world.isClient) {
                    if (!state.get(WHITELIST)) {
                        player.sendMessage(Text.translatable("block.extra_terracotta_utilities.allayed_enderized_magenta_glazed_terracotta.whitelist"));
                    } else {
                        player.sendMessage(Text.translatable("block.extra_terracotta_utilities.allayed_enderized_magenta_glazed_terracotta.blacklist"));
                    }
                    world.playSound(null, pos, SoundEvents.BLOCK_METAL_PRESSURE_PLATE_CLICK_OFF, SoundCategory.BLOCKS, 1, 1);
                }
                return  ActionResult.SUCCESS;
            }
            NbtCompound nbtToWrite = new NbtCompound();
            aemgtEntity.writeNbt(nbtToWrite);
            if (!world.isClient) {
                if (state.get(WHITELIST)) {
                    player.sendMessage(Text.translatable("block.extra_terracotta_utilities.allayed_enderized_magenta_glazed_terracotta.whitelist_info").append(Text.translatable(nbtToWrite.getString("filter")).styled(style -> style.withColor(0xFF6F8A7A))));
                } else {
                    player.sendMessage(Text.translatable("block.extra_terracotta_utilities.allayed_enderized_magenta_glazed_terracotta.blacklist_info").append(Text.translatable(nbtToWrite.getString("filter")).styled(style -> style.withColor(0xFF6F8A7A))));
                }
            }
            return  ActionResult.SUCCESS;
        }
        return  ActionResult.PASS;
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        BlockPos resultPos = pos.offset(state.get(Properties.FACING).getOpposite());
        BlockPos targetPos = pos.offset(state.get(Properties.FACING));
        BlockState targetState = world.getBlockState(targetPos);
        BlockState resultState = world.getBlockState(resultPos);
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof AllayedEnderizedMagentaGlazedTerracottaEntity) {
            AllayedEnderizedMagentaGlazedTerracottaEntity aemgtEntity = (AllayedEnderizedMagentaGlazedTerracottaEntity) blockEntity;
            NbtCompound nbt = new NbtCompound();
            aemgtEntity.writeNbt(nbt);
            String filterKey = nbt.getString("filter");
            if (filterKey.equals(targetState.getBlock().getTranslationKey()) == state.get(WHITELIST) && !targetState.isAir() && resultState.isAir()) {
                world.getServer().getCommandManager().executeWithPrefix(world.getServer().getCommandSource().withOutput(CommandOutput.DUMMY),
                        String.format("clone %d %d %d %d %d %d %d %d %d", targetPos.getX(), targetPos.getY(), targetPos.getZ(), targetPos.getX(), targetPos.getY(), targetPos.getZ(), resultPos.getX(), resultPos.getY(), resultPos.getZ()));
                world.removeBlockEntity(targetPos);
                world.setBlockState(targetPos, Blocks.AIR.getDefaultState(), Block.field_31022);
                world.playSound(null, pos, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.BLOCKS, 1.0F, 1.0F);
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
        tooltip.add(Text.translatable("block.extra_terracotta_utilities.allayed_enderized_magenta_glazed_terracotta.tooltip.function_1", Text.translatable(Initializer.ENDERIZED_MAGENTA_GLAZED_TERRACOTTA.getTranslationKey())).formatted(Formatting.GOLD));
        tooltip.add(Text.translatable("block.extra_terracotta_utilities.allayed_enderized_magenta_glazed_terracotta.tooltip.function_2").formatted(Formatting.GOLD));
        tooltip.add(Text.translatable("tooltip.extra_terracotta_utilities.usage").formatted(Formatting.DARK_AQUA));
        tooltip.add(Text.translatable("block.extra_terracotta_utilities.allayed_enderized_magenta_glazed_terracotta.tooltip.usage").formatted(Formatting.DARK_AQUA));
    }


}
