package com.github.winhan.diao.blockentities;

import com.github.winhan.diao.blocks.EntropyReducingMagentaGlazedTerracotta;
import com.github.winhan.diao.initialize.Initializer;
import com.github.winhan.diao.utilities.Utilities;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.slf4j.Logger;

import java.util.List;

public class EntropyReducingMagentaGlazedTerracottaEntity extends BlockEntity {

    public static Logger logger = LogUtils.getLogger();

    public static final int ENTROPY_COOLDOWN = 400;
    private String target = "null";
    private int cooldown = ENTROPY_COOLDOWN;

    public String getTarget() {
        return target;
    }

    public int getCooldown() {
        return cooldown;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public EntropyReducingMagentaGlazedTerracottaEntity(BlockPos pPos, BlockState pBlockState) {
        super(Initializer.ENTROPY_REDUCING_MAGENTA_GLAZED_TERRACOTTA_ENTITY.get(), pPos, pBlockState);
    }

    public static void constantTick(Level pLevel, BlockPos pPos, BlockState pState, EntropyReducingMagentaGlazedTerracottaEntity pBlockEntity) {
        BlockPos targetPos = pPos.relative(pState.getValue(EntropyReducingMagentaGlazedTerracotta.FACING).getOpposite());
        List<ItemEntity> list = pLevel.getEntitiesOfClass(ItemEntity.class, new AABB(targetPos), Entity::isAlive);
        if (list.size() != 1 || list.stream().noneMatch(Utilities::checkItemEntityValidityForPlacement)) {
            pBlockEntity.target = "null";
            pBlockEntity.cooldown = ENTROPY_COOLDOWN;
            return;
        }       //↑ block invalid circumstances, set target to null and initialize cooldown
        if (pBlockEntity.cooldown-- >= 0) {
            if (!pBlockEntity.target.equals("not_null")) {
                pBlockEntity.target = "not_null";
                pLevel.playSound(null, pPos, SoundEvents.POWDER_SNOW_STEP, SoundSource.BLOCKS, 1.0f, 1.0f);
            }
            pBlockEntity.setChanged();
            if (pLevel.isClientSide) {
                RandomSource random = pLevel.getRandom();
                pLevel.addParticle(ParticleTypes.SNOWFLAKE,
                        targetPos.getX() + .5 + Mth.randomBetween(random, -.4f, .4f),
                        targetPos.getY() + .5 + Mth.randomBetween(random, -.4f, .4f),
                        targetPos.getZ() + .5 + Mth.randomBetween(random, -.4f, .4f),
                        0, 0,0);
            }
        } else {        //↑ try reducing cooldown first if passed validity check
            list.forEach(itemEntity -> pLevel.setBlock(targetPos, ((BlockItem)itemEntity.getItem().getItem()).getBlock().defaultBlockState(), Block.UPDATE_ALL));
            list.forEach(Entity::kill);
            pLevel.playSound(null, pPos, SoundEvents.POWDER_SNOW_STEP, SoundSource.BLOCKS, 1.0f, 1.0f);
            pBlockEntity.cooldown = ENTROPY_COOLDOWN;       /*Just to make sure*/
        }       //↑ if cooldown is finished place the block
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.target = pTag.getString("target");
        this.cooldown = pTag.getInt("cooldown");
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putString("target", this.target);
        pTag.putInt("cooldown", this.cooldown);
    }
}
/*
if (pLevel.getBlockState(pPos.above()).is(Blocks.CHAIN_COMMAND_BLOCK)) logger.debug(list.toString());

logger.debug("##################");
logger.debug("There's a NBT load to the blockEntity");
logger.debug("  at "+this.worldPosition);
logger.debug("  class "+this.getClass().getSimpleName());
logger.debug("  nbt in parameter "+pTag);
logger.debug("##################");


logger.debug("==================");
logger.debug("something's saved to the NBT in the parameter");
logger.debug("  at "+this.worldPosition);
logger.debug("  class "+this.getClass().getSimpleName());
logger.debug("  nbt in parameter "+pTag);
logger.debug("==================");
*/

