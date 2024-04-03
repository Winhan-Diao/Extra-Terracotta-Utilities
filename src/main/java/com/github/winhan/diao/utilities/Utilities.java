package com.github.winhan.diao.utilities;

import com.github.winhan.diao.initialize.Initializer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.ForgeEventFactory;

public class Utilities {
    public static <T extends Mob> void mobCreateAndSpawn(EntityType<T> entityType, ServerLevel level, BlockPos blockPos) {
        T entity = entityType.create(level);
        entity.moveTo(blockPos.getX()+.5, blockPos.getY(), blockPos.getZ()+.5);
        ForgeEventFactory.onFinalizeSpawn(entity, level, new DifficultyInstance(level.getDifficulty(),0, 0, 0), MobSpawnType.MOB_SUMMONED, null, null);
        level.addFreshEntity(entity);
        level.playSound(null, blockPos, SoundEvents.ZOMBIE_VILLAGER_CURE, SoundSource.BLOCKS, 1.0f, 1.0f);
        level.destroyBlock(blockPos, false);
    }

    public static void chanceToDestroyBlock (BlockPos pPos, Level pLevel, int pChance, boolean pDropBlock, RandomSource pRandom) {
        int randInt = pRandom.nextInt(100);
        if (randInt < pChance) {
            pLevel.destroyBlock(pPos, pDropBlock);
        }
    }

    public static boolean checkItemEntityValidityForPlacement(ItemEntity pItemEntity) {
        return (pItemEntity.getItem().getCount() == 1 && pItemEntity.getItem().getItem() instanceof BlockItem);
    }

    public static boolean isEmptyOrClogs(ItemStack itemStack) {
        return itemStack == ItemStack.EMPTY || itemStack.getCount() <= 0 || itemStack.getItem() == Items.AIR || itemStack.is(Initializer.CLOGS);
    }

}
