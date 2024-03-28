package com.github.winhan.diao.mixin;

import com.github.winhan.diao.blocks.AllayedMagentaGlazedTerracotta;
import com.mojang.logging.LogUtils;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(BlockEntityType.class)
public class BlockEntityTypeDebugger {
    @Shadow @Final
    private Set<Block> validBlocks;

    @Inject(method = "isValid", at = @At("HEAD"))
    public void validBlocksDebug(BlockState pState, CallbackInfoReturnable<Boolean> cir) {
//        Logger logger = LogUtils.getLogger();
//        logger.debug("isValid called");
//        validBlocks.stream().map(Block::toString).forEach(System.out::println);
        if (pState.getBlock() instanceof AllayedMagentaGlazedTerracotta) {
            validBlocks.stream().map(Block::toString).forEach(System.out::println);
        }
    }
}
