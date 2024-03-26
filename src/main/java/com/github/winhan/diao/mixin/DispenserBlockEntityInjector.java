package com.github.winhan.diao.mixin;

import com.github.winhan.diao.initialize.Initializer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(DispenserBlockEntity.class)
public class DispenserBlockEntityInjector {
    /**Block clogs at the invocation of isEmpty**/
    @Redirect(method = "getRandomSlot",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z"))
    public boolean redirectIsEmpty(ItemStack instance) {
        return instance == ItemStack.EMPTY || instance.getCount() <= 0 || instance.getItem() == Items.AIR || instance.is(Initializer.CLOGS);
    }

}
