package com.github.winhan.diao.mixin;

import com.github.winhan.diao.initialize.Initializer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(HopperBlockEntity.class)
public class HopperBlockEntityInjector {
    /**Block clogs at the invocation of isEmpty**/
    @Redirect(method = "addItem(Lnet/minecraft/world/Container;Lnet/minecraft/world/Container;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/core/Direction;)Lnet/minecraft/world/item/ItemStack;",
              at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z"))
    private static boolean redirectIsEmpty4p(ItemStack instance) {
        return instance == ItemStack.EMPTY || instance.getCount() <= 0 || instance.getItem() == Items.AIR || instance.is(Initializer.CLOGS);
    }

}
