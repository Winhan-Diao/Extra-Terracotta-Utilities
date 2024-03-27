package com.github.winhan.diao.utilities;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraftforge.event.ForgeEventFactory;

public class Utilities {
    public static <T extends Mob> void entityCreateAndSpawn (EntityType<T> entityType, ServerLevel level, BlockPos blockPos) {
        T entity = entityType.create(level);
        entity.moveTo(blockPos.getX()+.5, blockPos.getY(), blockPos.getZ()+.5);
        ForgeEventFactory.onFinalizeSpawn(entity, level, new DifficultyInstance(level.getDifficulty(),0, 0, 0), MobSpawnType.MOB_SUMMONED, null, null);
        level.addFreshEntity(entity);
        level.playSound(null, blockPos, SoundEvents.ZOMBIE_VILLAGER_CURE, SoundSource.BLOCKS, 1.0f, 1.0f);
        level.destroyBlock(blockPos, false);
    }
}
