package com.github.razorplay01.sway.api.behavior.contributors;

import com.github.razorplay01.sway.api.behavior.SwayBehavior;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Collection;
import java.util.Collections;

public interface MultiBlockContributor extends SwayBehavior {
	default BlockPos getAnchorPosition(BlockPos currentPos, BlockState state) {
		return currentPos;
	}

	default Collection<BlockPos> getLinkedBlocks(BlockPos anchorPos, BlockState state, ClientLevel level) {
		return Collections.emptyList();
	}

	default boolean shouldPropagateForceToLinked() {
		return true;
	}
}
