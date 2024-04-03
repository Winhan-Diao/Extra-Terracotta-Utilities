package com.github.winhan.diao.blockentities;

import com.github.winhan.diao.blocks.BufferingMagentaGlazedTerracotta;
import com.github.winhan.diao.initialize.Initializer;
import com.github.winhan.diao.utilities.Utilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.function.Supplier;
import java.util.stream.IntStream;

import static com.github.winhan.diao.blocks.BufferingMagentaGlazedTerracotta.*;

public class BufferingMagentaGlazedTerracottaEntity extends RandomizableContainerBlockEntity {
    private final Vec3 popoutVec;
    private final Vec3 popoutVelocity;
    private NonNullList<ItemStack> items = NonNullList.withSize(27, ItemStack.EMPTY);
    private final ContainerOpenersCounter openersCounter = new ContainerOpenersCounter() {
        protected void onOpen(Level p_155062_, BlockPos p_155063_, BlockState p_155064_) {
            BufferingMagentaGlazedTerracottaEntity.this.playSound(p_155064_, SoundEvents.BARREL_OPEN);
            BufferingMagentaGlazedTerracottaEntity.this.updateBlockState(p_155064_, true);
        }

        protected void onClose(Level p_155072_, BlockPos p_155073_, BlockState p_155074_) {
            BufferingMagentaGlazedTerracottaEntity.this.playSound(p_155074_, SoundEvents.BARREL_CLOSE);
            BufferingMagentaGlazedTerracottaEntity.this.updateBlockState(p_155074_, false);
        }

        protected void openerCountChanged(Level p_155066_, BlockPos p_155067_, BlockState p_155068_, int p_155069_, int p_155070_) {
        }

        protected boolean isOwnContainer(Player p_155060_) {
            if (p_155060_.containerMenu instanceof ChestMenu) {
                Container container = ((ChestMenu)p_155060_.containerMenu).getContainer();
                return container == BufferingMagentaGlazedTerracottaEntity.this;
            } else {
                return false;
            }
        }
    };


    public BufferingMagentaGlazedTerracottaEntity(BlockPos pPos, BlockState pBlockState) {
        super(Initializer.BUFFERING_MAGENTA_GLAZED_TERRACOTTA_ENTITY.get(), pPos, pBlockState);
        this.popoutVec = pPos.relative(pBlockState.getValue(FACING)).getCenter().relative(pBlockState.getValue(FACING), -.3).subtract(0, .2, 0);
        this.popoutVelocity = new Vec3(pBlockState.getValue(FACING).getStepX()*.18, pBlockState.getValue(FACING).getStepY()*.18, pBlockState.getValue(FACING).getStepZ()*.18);
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> pItemStacks) {
        this.items = pItemStacks;
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.extra_terracotta_utilities.buffering_magenta_glazed_terracotta");
    }

    @Override
    protected AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory) {
        return ChestMenu.threeRows(pContainerId, pInventory, this);
    }

    @Override
    public int getContainerSize() {
        return 27;
    }

    public void startOpen(Player pPlayer) {
        if (!this.remove && !pPlayer.isSpectator()) {
            this.openersCounter.incrementOpeners(pPlayer, this.getLevel(), this.getBlockPos(), this.getBlockState());
        }

    }

    public void stopOpen(Player pPlayer) {
        if (!this.remove && !pPlayer.isSpectator()) {
            this.openersCounter.decrementOpeners(pPlayer, this.getLevel(), this.getBlockPos(), this.getBlockState());
        }

    }

    void updateBlockState(BlockState pState, boolean pOpen) {
        this.level.setBlock(this.getBlockPos(), pState.setValue(BufferingMagentaGlazedTerracotta.OPEN, Boolean.valueOf(pOpen)), 3);
    }

    public void playSound(BlockState pState, SoundEvent pSound) {
        Vec3i vec3i = pState.getValue(FACING).getNormal();
        double d0 = (double)this.worldPosition.getX() + 0.5D + (double)vec3i.getX() / 2.0D;
        double d1 = (double)this.worldPosition.getY() + 0.5D + (double)vec3i.getY() / 2.0D;
        double d2 = (double)this.worldPosition.getZ() + 0.5D + (double)vec3i.getZ() / 2.0D;
        this.level.playSound(null, d0, d1, d2, pSound, SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        if (!this.trySaveLootTable(pTag)) {
            ContainerHelper.saveAllItems(pTag, this.items);
        }

    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(pTag)) {
            ContainerHelper.loadAllItems(pTag, this.items);
        }

    }

    public static void constantTick(Level pLevel, BlockPos pPos, BlockState pState, BufferingMagentaGlazedTerracottaEntity pBlockEntity) {
        if (pState.getValue(TRIGGERED) && (!pState.getValue(IMPULSE) || !pState.getValue(POWERED))) {
            pLevel.setBlock(pPos, pState.setValue(TRIGGERED, false), UPDATE_ALL);
        }       //Update block to un-triggered if it's not in impulse mod or doesn't receive redstone signal anymore.
        if (!pLevel.isClientSide && pState.getValue(POWERED) && ((!pState.getValue(TRIGGERED) && pState.getValue(IMPULSE)) || (!pState.getValue(IMPULSE)))) {
            Supplier<IntStream> streamSupplier = () -> IntStream.range(0, pBlockEntity.items.size()).filter(i -> !Utilities.isEmptyOrClogs(pBlockEntity.items.get(i)));
            if (streamSupplier.get().findAny().isPresent()) {
                pBlockEntity.playSound(pState, SoundEvents.BARREL_OPEN);
                streamSupplier.get().forEach(i -> itemPopout(pBlockEntity, pBlockEntity.items.get(i), pLevel));
                streamSupplier.get().forEach(i -> pBlockEntity.items.set(i, ItemStack.EMPTY));
            }
            if (pState.getValue(IMPULSE)) pLevel.setBlock(pPos, pState.setValue(TRIGGERED, true), UPDATE_ALL);
        }       //Execute when ##1.ServerSide## ##2.Redstone received## ##3.(Impulse but un-triggered) OR (Constant)##
    }

    public static void itemPopout(BufferingMagentaGlazedTerracottaEntity be, ItemStack itemStack, Level world) {
        ItemEntity itemEntity = new ItemEntity(world, be.popoutVec.x, be.popoutVec.y, be.popoutVec.z, itemStack, be.popoutVelocity.x, be.popoutVelocity.y, be.popoutVelocity.z);
        world.addFreshEntity(itemEntity);
    }

}
