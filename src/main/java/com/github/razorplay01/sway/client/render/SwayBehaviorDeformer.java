package com.github.razorplay01.sway.client.render;

import com.github.razorplay01.sway.api.behavior.BehaviorPipeline;
import com.github.razorplay01.sway.api.behavior.contributors.DeformationContributor;
import com.github.razorplay01.sway.client.SwayData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public final class SwayBehaviorDeformer {
	private SwayBehaviorDeformer() {}

	public static void deform(VertexMutator mutator, SwayData interpolatedData,
	                          BlockState state, BlockPos pos, BehaviorPipeline pipeline) {
		if (mutator == null || interpolatedData == null || interpolatedData.intensity < 0.01F) return;
		if (pipeline == null || pipeline.getDeformationContributors().isEmpty()) return;

		for (DeformationContributor contributor : pipeline.getDeformationContributors()) {
			if (!contributor.appliesTo(state)) continue;
			contributor.transformQuad(mutator, interpolatedData, state, pos);
		}
	}
}
