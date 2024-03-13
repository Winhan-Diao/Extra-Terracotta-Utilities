package com.github.winhan.diao.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.EnderChestBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnderChestBlock.class)
public class MyMixin {
	@Inject(at = @At("HEAD"), method = "randomDisplayTick")
	private void init(BlockState state, World world, BlockPos pos, Random random, CallbackInfo ci) {
		return;
		// This code is injected into the start of MinecraftServer.loadWorld()V
	}
}