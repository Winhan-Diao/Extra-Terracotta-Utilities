package com.github.winhan.diao.mixin;

import com.github.winhan.diao.Initializer;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(DispenserBlockEntity.class)
public class DispenserBlockEntityInjector {
    @Redirect(method = "chooseNonEmptySlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z"))
    private boolean redirectIsEmpty(ItemStack instance) {
        return instance == ItemStack.EMPTY || instance.getItem() == Items.AIR || instance.getCount() <= 0 || instance.isIn(Initializer.CLONGS);
    }
}
