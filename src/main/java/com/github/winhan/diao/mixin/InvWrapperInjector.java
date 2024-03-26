package com.github.winhan.diao.mixin;

import com.github.winhan.diao.initialize.Initializer;
import com.mojang.logging.LogUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(InvWrapper.class)
public class InvWrapperInjector {
    /**Block clogs at the invocation of isEmpty**/
    @Redirect(method = "extractItem",
    at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z"))
    private boolean isEmptyOrClogs2(ItemStack instance) {
        return instance == ItemStack.EMPTY || instance.getCount() <= 0 || instance.getItem() == Items.AIR || instance.is(Initializer.CLOGS);
    }

    /**Block clogs at head**/
    @Inject(method = "insertItem", at = @At(value = "HEAD"), cancellable = true, remap = false)
    public void cancelIfClogs(int slot, @NotNull ItemStack stack, boolean simulate, CallbackInfoReturnable<ItemStack> cir) {
        if (stack.is(Initializer.CLOGS)) cir.setReturnValue(stack);
    }
}
