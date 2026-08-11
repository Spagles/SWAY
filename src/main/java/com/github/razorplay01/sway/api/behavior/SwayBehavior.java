package com.github.razorplay01.sway.api.behavior;

import net.minecraft.world.level.block.state.BlockState;

public interface SwayBehavior {
	default boolean appliesTo(BlockState state) {
		return true;
	}

	default Float getDecayRateOverride() {
		return null;
	}

	default Float getSmoothnessOverride() {
		return null;
	}
}
