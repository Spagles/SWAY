package com.github.razorplay01.sway.client.behavior.force;

import com.github.razorplay01.sway.api.SwayAPI;
import com.github.razorplay01.sway.api.behavior.contributors.ForceContributor;
import com.github.razorplay01.sway.api.behavior.context.ForceAccumulator;
import com.github.razorplay01.sway.api.behavior.context.SwayBehaviorContext;
import com.github.razorplay01.sway.api.behavior.contributors.MultiBlockContributor;
import com.github.razorplay01.sway.config.SwayConfig;
import com.github.razorplay01.sway.api.behavior.BehaviorPipeline;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class ProximityForceBehavior implements ForceContributor {
	public static final ProximityForceBehavior INSTANCE = new ProximityForceBehavior();

	@Override
	public void contributeForce(BlockPos pos, BlockState state, Entity entity,
	                             SwayBehaviorContext ctx, ForceAccumulator accumulator) {
		if (entity == null) return;

		SwayConfig cfg = ctx.config();
		float radius = cfg.influenceRadius;
		float baseIntensity = cfg.intensity;

		// Calculate distance from the entity to the current block being processed,
		// not the anchor, so tall multi-block structures (like sugar cane) react
		// even when the entity is near the top of the stalk.
		Vec3 entityPos = entity.position();

		double dx = (pos.getX() + 0.5) - entityPos.x;
		double dz = (pos.getZ() + 0.5) - entityPos.z;
		double distSq = dx * dx + dz * dz;

		if (distSq < radius * radius) {
			double d = Math.sqrt(distSq);
			float force = (float) (1.0 - d / radius) * baseIntensity;

			if (force > 0.01F) {
				float nx = d > 0.001 ? (float) (dx / d) : 1.0F;
				float nz = d > 0.001 ? (float) (dz / d) : 0.0F;
				accumulator.contribute(nx, nz, force, ForceAccumulator.CombineStrategy.ADD);
			}
		}
	}

	private BlockPos resolveAnchor(BlockPos pos, BlockState state, SwayBehaviorContext ctx) {
		BehaviorPipeline pipeline = resolvePipeline(state);
		if (pipeline == null) return pos;
		BlockPos current = pos;
		for (MultiBlockContributor mb : pipeline.getMultiBlockContributors()) {
			current = mb.getAnchorPosition(current, state);
		}
		return current;
	}

	private BehaviorPipeline resolvePipeline(BlockState state) {
		try {
			return SwayAPI.getBehaviorPipeline(state.getBlock());
		} catch (Exception e) {
			return null;
		}
	}
}
