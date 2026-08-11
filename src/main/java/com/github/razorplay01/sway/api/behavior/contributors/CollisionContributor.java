package com.github.razorplay01.sway.api.behavior.contributors;

import com.github.razorplay01.sway.api.behavior.SwayBehavior;
import com.github.razorplay01.sway.api.behavior.context.SwayBehaviorContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public interface CollisionContributor extends SwayBehavior {
	default AABB contributeSearchArea(Entity entity, SwayBehaviorContext ctx) {
		return entity.getBoundingBox().inflate(1);
	}

	default boolean shouldAffectBlock(BlockPos pos, BlockState state, Entity entity, SwayBehaviorContext ctx) {
		return true;
	}
}
