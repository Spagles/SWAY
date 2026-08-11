package com.github.razorplay01.sway.api.behavior.contributors;

import com.github.razorplay01.sway.api.behavior.SwayBehavior;
import com.github.razorplay01.sway.client.SwayData;
import com.github.razorplay01.sway.client.render.VertexMutator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public interface DeformationContributor extends SwayBehavior {
	default float getVertexWeight(float vertexY, BlockState state, BlockPos pos) {
		return vertexY > 0.05F ? vertexY * vertexY : 0.0F;
	}

	default float getDeformationScale(BlockState state, BlockPos pos) {
		return 0.45F;
	}

	default void transformQuad(VertexMutator v, SwayData data, BlockState state, BlockPos pos) {
		float scale = getDeformationScale(state, pos);
		float dx = data.nx * data.intensity * scale;
		float dz = data.nz * data.intensity * scale;
		if (dx == 0 && dz == 0) return;

		for (int i = 0; i < v.vertexCount(); i++) {
			float y = v.y(i);
			float weight = getVertexWeight(y, state, pos);
			if (weight > 0) {
				v.addOffset(i, dx * weight, 0, dz * weight);
			}
		}
	}
}
