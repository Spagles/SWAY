package com.github.razorplay01.sway.mixin;

import com.github.razorplay01.sway.SwayLevelRendererExtension;
import com.github.razorplay01.sway.client.SwayEngine;
import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin implements SwayLevelRendererExtension {
	//? >=26.2{
	@Inject(method = "render", at = @At("HEAD"))
	private void sway$render(
			com.mojang.blaze3d.resource.GraphicsResourceAllocator resourceAllocator, net.minecraft.client.DeltaTracker deltaTracker, boolean renderOutline, net.minecraft.client.renderer.state.level.CameraRenderState cameraState, org.joml.Matrix4fc modelViewMatrix, com.mojang.blaze3d.buffers.GpuBufferSlice terrainFog, org.joml.Vector4f fogColor, boolean shouldRenderSky, CallbackInfo ci
	) {
		sway$markedSections.clear();
		sway$regionCache = new net.minecraft.client.renderer.chunk.RenderRegionCache();

		SwayEngine.update();
	}
	//?}
	//? <26.2{
	/*@Inject(method = "renderLevel", at = @At("HEAD"))
	private void sway$renderLevel(CallbackInfo ci) {
		SwayEngine.update();
	}
	*///?}

	//? >=26.2{
	@org.spongepowered.asm.mixin.Shadow
	private net.minecraft.client.renderer.state.level.LevelRenderState levelRenderState;

	@org.spongepowered.asm.mixin.Unique
	private static net.minecraft.client.renderer.chunk.RenderRegionCache sway$regionCache = new net.minecraft.client.renderer.chunk.RenderRegionCache();

	@org.spongepowered.asm.mixin.Unique
	private static final java.util.Set<Long> sway$markedSections = java.util.Collections.newSetFromMap(new java.util.concurrent.ConcurrentHashMap<>());

	//?}

	@Override
	public void sway$markBlockForRerender(net.minecraft.client.multiplayer.ClientLevel level, net.minecraft.core.BlockPos pos) {
		//? >=26.2{
		if (level == null || this.levelRenderState == null) return;

		int sx = net.minecraft.core.SectionPos.blockToSectionCoord(pos.getX());
		int sy = net.minecraft.core.SectionPos.blockToSectionCoord(pos.getY());
		int sz = net.minecraft.core.SectionPos.blockToSectionCoord(pos.getZ());
		long sectionNode = net.minecraft.core.SectionPos.asLong(sx, sy, sz);

		if (!sway$markedSections.add(sectionNode)) return;

		net.minecraft.client.renderer.chunk.RenderSectionRegion region = sway$regionCache.createRegion(level, sectionNode);
		this.levelRenderState.sectionUpdateRenderStates.add(
				new net.minecraft.client.renderer.state.level.SectionUpdateRenderState(sectionNode, false, region)
		);
		//?}
	}
}
