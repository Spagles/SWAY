package com.github.razorplay01.sway.client.behavior.force;

import com.github.razorplay01.sway.api.behavior.contributors.ForceContributor;
import com.github.razorplay01.sway.api.behavior.context.ForceAccumulator;
import com.github.razorplay01.sway.api.behavior.context.SwayBehaviorContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;

public final class ConfigurableMultiplierBehavior implements ForceContributor {
	private final float multiplier;

	public ConfigurableMultiplierBehavior(float multiplier) {
		this.multiplier = multiplier;
	}

	public float getMultiplier() {
		return multiplier;
	}

	@Override
	public void contributeForce(BlockPos pos, BlockState state, Entity entity,
	                             SwayBehaviorContext ctx, ForceAccumulator accumulator) {
		accumulator.multiplyScale(multiplier);
	}
}
