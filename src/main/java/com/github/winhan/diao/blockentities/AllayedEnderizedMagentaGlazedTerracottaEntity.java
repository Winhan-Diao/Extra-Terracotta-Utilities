package com.github.winhan.diao.blockentities;

import com.github.winhan.diao.initialize.Initializer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class AllayedEnderizedMagentaGlazedTerracottaEntity extends BlockEntity {
    private String filter = "null";
    public AllayedEnderizedMagentaGlazedTerracottaEntity(BlockPos pPos, BlockState pBlockState) {
        super(Initializer.ALLAYED_ENDERIZED_MAGENTA_GLAZED_TERRACOTTA_ENTITY.get(), pPos, pBlockState);
    }

    public String getFilter() {
        return filter;
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
