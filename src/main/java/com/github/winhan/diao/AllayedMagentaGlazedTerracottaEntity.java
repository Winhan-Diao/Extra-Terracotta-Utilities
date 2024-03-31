package com.github.winhan.diao;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;

public class AllayedMagentaGlazedTerracottaEntity extends BlockEntity {
    private String filter = "null";

    public AllayedMagentaGlazedTerracottaEntity(BlockPos pos, BlockState state) {
        super(Initializer.ALLAYED_MAGENTA_GLAZED_TERRACOTTA_ENTITY, pos, state);
    }

    /**To do:
     * remove the awkward invocation of utility (done)
     * bad box (done)
     * awful iterate (done)
     * bad break (done)**/
    public static void serverTick(World world, BlockPos pos, BlockState state, AllayedMagentaGlazedTerracottaEntity be) {
        BlockPos detectingPos = pos.offset(state.get(Properties.FACING));
        List<ItemEntity> list = world.getEntitiesByClass(ItemEntity.class, new Box(detectingPos), EntityPredicates.VALID_ENTITY);
        if (list.isEmpty()) return;
        BlockPos destineyPos = pos.offset(state.get(Properties.FACING).getOpposite());
        boolean bl = state.get(AllayedMagentaGlazedTerracotta.WHITELIST);
        for (ItemEntity itemEntity : list) {
            if (be.filter.equals(itemEntity.getStack().getTranslationKey()) == bl) {
                itemEntity.refreshPositionAfterTeleport(destineyPos.toCenterPos());
                itemEntity.setVelocity(0, .01, 0);
                world.playSound(null, pos, SoundEvents.ENTITY_ALLAY_AMBIENT_WITH_ITEM, SoundCategory.BLOCKS, 1.0f, 1.0f);
            }
        }
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
//itemStack.getTranslationKey()
//String.valueOf(Registries.ITEM.getId(item)
