package com.github.razorplay01.sway.api.behavior.contributors;

import com.github.razorplay01.sway.api.behavior.SwayBehavior;
import com.github.razorplay01.sway.api.behavior.context.ForceAccumulator;
import com.github.razorplay01.sway.api.behavior.context.SwayBehaviorContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;

public interface ForceContributor extends SwayBehavior {
	default void contributeForce(BlockPos pos, BlockState state, Entity entity,
	                              SwayBehaviorContext ctx, ForceAccumulator accumulator) {
	}

	default float clampMaxIntensity(float proposedIntensity, SwayBehaviorContext ctx) {
		return proposedIntensity;
	}
}
