package com.github.razorplay01.sway.client.behavior.deformation;

import com.github.razorplay01.sway.api.behavior.contributors.DeformationContributor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

public class StandardQuadraticDeformation implements DeformationContributor {
	public static final StandardQuadraticDeformation INSTANCE = new StandardQuadraticDeformation();

	@Override
	public float getVertexWeight(float vertexY, BlockState state, BlockPos pos) {
		boolean isDouble = state.hasProperty(BlockStateProperties.DOUBLE_BLOCK_HALF);
		boolean isUpper = isDouble && state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER;
		float progress = isDouble ? (isUpper ? (vertexY + 1.0F) / 2.0F : vertexY / 2.0F) : vertexY;
		return progress > 0.05F ? progress * progress : 0.0F;
	}

	@Override
	public float getDeformationScale(BlockState state, BlockPos pos) {
		return 0.45F;
	}
}
