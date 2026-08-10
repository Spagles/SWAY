package com.github.razorplay01.sway.client.behavior.deformation;

import com.github.razorplay01.sway.api.behavior.contributors.DeformationContributor;
import com.github.razorplay01.sway.client.behavior.multiblock.VineMultiblockBehavior;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class VineDeformation implements DeformationContributor {
	public static final VineDeformation INSTANCE = new VineDeformation();

	@Override
	public boolean appliesTo(BlockState state) {
		return VineMultiblockBehavior.isVine(state);
	}

	@Override
	public float getVertexWeight(float vertexY, BlockState state, BlockPos pos) {
		if (!VineMultiblockBehavior.isVine(state)) {
			return vertexY > 0.05F ? vertexY * vertexY : 0.0F;
		}

		ClientLevel level = Minecraft.getInstance().level;
		if (level == null) return 0.0F;

		boolean hanging = VineMultiblockBehavior.isHangingVine(state);

		// Find the anchor: top for hanging vines, bottom for growing vines
		BlockPos anchor = pos;
		if (hanging) {
			while (VineMultiblockBehavior.isVine(level.getBlockState(anchor.above()))) {
				anchor = anchor.above();
			}
		} else {
			while (VineMultiblockBehavior.isVine(level.getBlockState(anchor.below()))) {
				anchor = anchor.below();
			}
		}

		// Measure total length of the vine structure
		int totalLength = 0;
		BlockPos current = anchor;
		if (hanging) {
			while (VineMultiblockBehavior.isVine(level.getBlockState(current))) {
				totalLength++;
				current = current.below();
			}
		} else {
			while (VineMultiblockBehavior.isVine(level.getBlockState(current))) {
				totalLength++;
				current = current.above();
			}
		}

		int segmentIndex = Math.abs(pos.getY() - anchor.getY());

		// Absolute distance from the anchor (0.0 at anchor, totalLength at free end).
		// For hanging vines, vertexY=1 is the anchored side (top of block) and
		// vertexY=0 is the free side (bottom of block), so we invert it.
		// For growing vines, vertexY=0 is the anchored side (bottom of block) and
		// vertexY=1 is the free side (top of block), so we use it directly.
		float absoluteDistance = hanging
				? segmentIndex + (1.0F - vertexY)
				: segmentIndex + vertexY;

		// Normalized progress (0.0 at anchor, 1.0 at free end)
		float progress = totalLength > 0 ? absoluteDistance / totalLength : 0.0F;

		// Rope-like behavior: the anchor stays nearly still, the free end swings.
		// Use a cubic curve so the anchor stays almost fixed while the tip moves freely.
		// For short vines (1-2 blocks), reduce the maximum weight to avoid exaggerated bending.
		float weight = progress > 0.05F ? progress * progress * progress : 0.0F;

		// Scale down the weight for short vines to prevent exaggerated inclination
		if (totalLength <= 1) {
			weight *= 0.4F;
		} else if (totalLength == 2) {
			weight *= 0.6F;
		} else if (totalLength == 3) {
			weight *= 0.8F;
		}

		return weight;
	}

	@Override
	public float getDeformationScale(BlockState state, BlockPos pos) {
		return 0.5F;
	}
}
