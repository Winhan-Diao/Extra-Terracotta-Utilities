package com.github.winhan.diao;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class AllayedEnderizedMagentaGlazedTerracotta extends AllayedMagentaGlazedTerracotta {
    public AllayedEnderizedMagentaGlazedTerracotta(Settings settings) {
        super(settings);
    }
    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new AllayedEnderizedMagentaGlazedTerracottaEntity(pos, state);
    }
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        world.markDirty(pos);
        ItemStack itemStack = player.getStackInHand(hand);
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof AllayedEnderizedMagentaGlazedTerracottaEntity) {
            AllayedEnderizedMagentaGlazedTerracottaEntity aemgtEntity = (AllayedEnderizedMagentaGlazedTerracottaEntity) blockEntity;
            if (!player.getStackInHand(hand).isEmpty()) {
                world.playSound(null, pos, SoundEvents.ENTITY_ENDERMAN_AMBIENT, SoundCategory.BLOCKS, 1, 1);
                NbtCompound nbtToRead = new NbtCompound();
                nbtToRead.putString("filter", itemStack.getTranslationKey());
                aemgtEntity.readNbt(nbtToRead);
            }
            NbtCompound nbtToWrite = new NbtCompound();
            aemgtEntity.writeNbt(nbtToWrite);
            if (!world.isClient) {
                player.sendMessage(Text.translatable("block.extra_terracotta_utilities.allayed_enderized_magenta_glazed_terracotta.message").append(Text.translatable(nbtToWrite.getString("filter")).formatted(Formatting.AQUA)));
            }
            return  ActionResult.SUCCESS;
        }
        return  ActionResult.PASS;
    }

}
