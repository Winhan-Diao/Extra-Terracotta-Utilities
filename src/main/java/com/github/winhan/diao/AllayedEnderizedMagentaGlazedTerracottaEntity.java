package com.github.winhan.diao;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
/**To do:
 * directory organization ( )**/
public class AllayedEnderizedMagentaGlazedTerracottaEntity extends BlockEntity {
    private String filter = "null";
    public AllayedEnderizedMagentaGlazedTerracottaEntity(BlockPos pos, BlockState state) {
        super(Initializer.ALLAYED_ENDERIZED_MAGENTA_GLAZED_TERRACOTTA_ENTITY, pos, state);
    }



    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putString("filter", filter);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        filter = nbt.getString("filter");
    }



}
