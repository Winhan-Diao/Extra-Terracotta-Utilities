package com.github.winhan.diao.blockentities;

import com.github.winhan.diao.blocks.AllayedMagentaGlazedTerracotta;
import com.github.winhan.diao.initialize.Initializer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class AllayedMagentaGlazedTerracottaEntity extends BlockEntity{
    private String filter = "null";
    public AllayedMagentaGlazedTerracottaEntity(BlockPos pPos, BlockState pBlockState) {
        super(Initializer.ALLAYED_MAGENTA_GLAZED_TERRACOTTA_ENTITY.get(), pPos, pBlockState);
    }

    public static void serverTick(Level pLevel, BlockPos pPos, BlockState pState, AllayedMagentaGlazedTerracottaEntity pBlockEntity) {
        BlockPos detectingPos = pPos.relative(pState.getValue(AllayedMagentaGlazedTerracotta.FACING));
        List<ItemEntity> list = pLevel.getEntitiesOfClass(ItemEntity.class, new AABB(detectingPos));
        if (list.isEmpty()) return;
        BlockPos targetPos = pPos.relative(pState.getValue(AllayedMagentaGlazedTerracotta.FACING).getOpposite());
        boolean bl = pState.getValue(AllayedMagentaGlazedTerracotta.WHITELIST);
        for (ItemEntity itemEntity : list) {
            if (pBlockEntity.filter.equals(itemEntity.getItem().getDescriptionId()) == bl) {
                itemEntity.moveTo(targetPos.getCenter());
                itemEntity.setDeltaMovement(0, .01, 0);
                pLevel.playSound(null, pPos, SoundEvents.ALLAY_AMBIENT_WITH_ITEM, SoundSource.BLOCKS, 1.0f, 1.0f);
            }
        }
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        filter = pTag.getString("filter");
    }

    @Override
    public void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putString("filter", filter);
    }

}
