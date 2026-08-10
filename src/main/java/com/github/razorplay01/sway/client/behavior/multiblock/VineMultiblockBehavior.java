package com.github.razorplay01.sway.client.behavior.multiblock;

import com.github.razorplay01.sway.api.behavior.contributors.MultiBlockContributor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class VineMultiblockBehavior implements MultiBlockContributor {
	public static final VineMultiblockBehavior INSTANCE = new VineMultiblockBehavior();

	// Vines that ALWAYS hang DOWN from ceilings
	private static final Set<Block> HANGING_VINES = Set.of(
			Blocks.VINE,
			Blocks.WEEPING_VINES,
			Blocks.WEEPING_VINES_PLANT,
			Blocks.CAVE_VINES,
			Blocks.CAVE_VINES_PLANT
	);

	// Vines that ALWAYS grow UP from the ground
	private static final Set<Block> GROWING_VINES = Set.of(
			Blocks.TWISTING_VINES,
			Blocks.TWISTING_VINES_PLANT
	);

	public static boolean isHangingVine(BlockState state) {
		return HANGING_VINES.contains(state.getBlock());
	}

	public static boolean isGrowingVine(BlockState state) {
		return GROWING_VINES.contains(state.getBlock());
	}

	public static boolean isVine(BlockState state) {
		return isHangingVine(state) || isGrowingVine(state);
	}

	@Override
	public boolean appliesTo(BlockState state) {
		return isVine(state);
	}

	@Override
	public BlockPos getAnchorPosition(BlockPos currentPos, BlockState state) {
		if (!isVine(state)) return currentPos;

		ClientLevel level = Minecraft.getInstance().level;
		if (level == null) return currentPos;

		if (isHangingVine(state)) {
			// Hanging vines (VINE, WEEPING, CAVE): anchor is the TOP (attached to ceiling)
			BlockPos top = currentPos;
			while (isVine(level.getBlockState(top.above()))) {
				top = top.above();
			}
			return top;
		} else {
			// Growing vines (TWISTING): anchor is the BOTTOM
			BlockPos bottom = currentPos;
			while (isVine(level.getBlockState(bottom.below()))) {
				bottom = bottom.below();
			}
			return bottom;
		}
	}

	@Override
	public Collection<BlockPos> getLinkedBlocks(BlockPos anchorPos, BlockState state, ClientLevel level) {
		if (!isVine(state)) return List.of();

		List<BlockPos> linked = new ArrayList<>();

		if (isHangingVine(state)) {
			// Hanging vines: linked blocks are BELOW the anchor
			BlockPos current = anchorPos.below();
			while (isVine(level.getBlockState(current))) {
				linked.add(current);
				current = current.below();
			}
		} else {
			// Growing vines: linked blocks are ABOVE the anchor
			BlockPos current = anchorPos.above();
			while (isVine(level.getBlockState(current))) {
				linked.add(current);
				current = current.above();
			}
		}
		return linked;
	}

	@Override
	public boolean shouldPropagateForceToLinked() {
		return true;
	}
}
