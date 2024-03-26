package com.github.winhan.diao.mixin;

import com.github.winhan.diao.initialize.Initializer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SidedInvWrapper.class)
public class SidedInvWrapperInjector {

    /**Block clogs at the invocation of isEmpty**/
    @Redirect(method = "extractItem",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z"))
    private boolean isEmptyOrClogs2(ItemStack instance) {
        return instance == ItemStack.EMPTY || instance.getCount() <= 0 || instance.getItem() == Items.AIR || instance.is(Initializer.CLOGS);
    }

    /**Block clogs at head**/
    @Inject(method = "insertItem", at = @At("HEAD"), cancellable = true, remap = false)
    public void cancelIfClogs(int slot, @NotNull ItemStack stack, boolean simulate, CallbackInfoReturnable<ItemStack> cir) {
        if (stack.is(Initializer.CLOGS)) cir.setReturnValue(stack);
    }
}
