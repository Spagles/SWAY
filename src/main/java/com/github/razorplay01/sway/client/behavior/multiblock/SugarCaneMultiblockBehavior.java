package com.github.razorplay01.sway.client.behavior.multiblock;

import com.github.razorplay01.sway.api.behavior.contributors.MultiBlockContributor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SugarCaneMultiblockBehavior implements MultiBlockContributor {
	public static final SugarCaneMultiblockBehavior INSTANCE = new SugarCaneMultiblockBehavior();

	@Override
	public boolean appliesTo(BlockState state) {
		return state.getBlock() == Blocks.SUGAR_CANE;
	}

	@Override
	public BlockPos getAnchorPosition(BlockPos currentPos, BlockState state) {
		if (state.getBlock() != Blocks.SUGAR_CANE) return currentPos;

		ClientLevel level = Minecraft.getInstance().level;
		if (level == null) return currentPos;

		// Walk down to find the bottom-most sugar cane block of the stalk
		BlockPos bottom = currentPos;
		while (level.getBlockState(bottom.below()).getBlock() == Blocks.SUGAR_CANE) {
			bottom = bottom.below();
		}
		return bottom;
	}

	@Override
	public Collection<BlockPos> getLinkedBlocks(BlockPos anchorPos, BlockState state, ClientLevel level) {
		if (state.getBlock() != Blocks.SUGAR_CANE) return List.of();

		// Collect all sugar cane blocks above the anchor as linked blocks
		List<BlockPos> linked = new ArrayList<>();
		BlockPos current = anchorPos.above();
		while (level.getBlockState(current).getBlock() == Blocks.SUGAR_CANE) {
			linked.add(current);
			current = current.above();
		}
		return linked;
	}
}
