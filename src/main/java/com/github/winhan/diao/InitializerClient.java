package com.github.winhan.diao;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;
@Environment(EnvType.CLIENT)
public class InitializerClient implements ClientModInitializer {
	public void onInitializeClient() {
		BlockRenderLayerMap.INSTANCE.putBlock(Initializer.STICKY_MAGENTA_GLAZED_TERRACOTTA, RenderLayer.getTranslucent());
	}
}