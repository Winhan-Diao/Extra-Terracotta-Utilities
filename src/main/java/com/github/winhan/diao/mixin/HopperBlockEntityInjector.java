package com.github.winhan.diao.mixin;

import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HopperBlockEntity.class)
public class HopperBlockEntityInjector {
    @Inject(method = "readNbt", at = @At("HEAD"))
    public void injectReadNbt(NbtCompound nbt, CallbackInfo ci) {
        HopperBlockEntity thisHbe = (HopperBlockEntity)(Object)this;
        thisHbe.getWorld().getPlayers().forEach(player -> player.sendMessage(Text.literal("[Hopper] nbt -> field")));
    }

    @Inject(method = "writeNbt", at = @At("HEAD"))
    public void injectWriteNbt(NbtCompound nbt, CallbackInfo ci) {
        HopperBlockEntity thisHbe = (HopperBlockEntity)(Object)this;
        thisHbe.getWorld().getPlayers().forEach(player -> player.sendMessage(Text.literal("[Hopper] field -> nbt")));
    }


}
