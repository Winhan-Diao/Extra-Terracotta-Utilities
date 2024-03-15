package com.github.winhan.diao;

import com.github.winhan.diao.utility.BlockFacingUtility;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
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

    public static void serverTick(World world, BlockPos pos, BlockState state, AllayedMagentaGlazedTerracottaEntity be) {
        BlockFacingUtility blockFacingUtility = BlockFacingUtility.getByDirection(state.get(Properties.FACING));
        BlockPos targetPos = pos.add(blockFacingUtility.getVec3i());
        List<ItemEntity> list = world.getEntitiesByClass(ItemEntity.class, new Box(pos).offset(blockFacingUtility.getVec3dOpposite()), EntityPredicates.VALID_ENTITY);
        List<String> list2 = list.stream().map(ItemEntity::getStack).map(ItemStack::getTranslationKey).toList();
        for (String translationKey : list2) {
            if (translationKey.equals(be.filter) == state.get(AllayedMagentaGlazedTerracotta.WHITELIST)) {
                ItemEntity filteredItemEntity = list.get(list2.indexOf(translationKey));
                filteredItemEntity.updatePosition(targetPos.getX()+.5, targetPos.getY()+.5, targetPos.getZ()+.5);
                filteredItemEntity.setVelocity(0, 0.01, 0);
                world.playSound(null, pos, SoundEvents.ENTITY_ALLAY_AMBIENT_WITH_ITEM, SoundCategory.BLOCKS, 1, 1);
                break;
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
