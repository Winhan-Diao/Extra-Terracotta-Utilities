package com.github.winhan.diao.mixin;

import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(HopperBlockEntity.class)
public interface HopperBlockEntityInvoker {
    @Invoker("canInsert")
    public static boolean invokeCanInsert(Inventory inventory, ItemStack stack, int slot, @Nullable Direction side) {
        throw new AssertionError();
    }
}
