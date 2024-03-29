package com.github.winhan.diao.mixin;

import com.mojang.logging.LogUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntity.class)
public class BlockEntityDebugger {
    @Inject(method = "load", at = @At("TAIL"))
    public void loadDebug(CompoundTag pTag, CallbackInfo ci) {
        System.out.println("##################");
        System.out.println("There's a NBT load to the blockEntity");
        System.out.println("  nbt in parameter "+pTag);
        System.out.println("##################");
    }

    @Inject(method = "saveAdditional", at = @At("TAIL"))
    public void saveAdditionalDebug(CompoundTag pTag, CallbackInfo ci) {
        System.out.println("==================");
        System.out.println("something's saved to the NBT in the parameter");
        System.out.println("  nbt in parameter "+pTag);
        System.out.println("==================");
    }

}
