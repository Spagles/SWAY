package com.github.razorplay01.sway.client.behavior.force;

import com.github.razorplay01.sway.api.behavior.contributors.ForceContributor;
import com.github.razorplay01.sway.api.behavior.context.ForceAccumulator;
import com.github.razorplay01.sway.api.behavior.context.SwayBehaviorContext;
import com.github.razorplay01.sway.client.behavior.multiblock.VineMultiblockBehavior;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

public class VineClimbTensionBehavior implements ForceContributor {
	public static final VineClimbTensionBehavior INSTANCE = new VineClimbTensionBehavior();

	@Override
	public boolean appliesTo(BlockState state) {
		return VineMultiblockBehavior.isVine(state);
	}

	@Override
	public void contributeForce(BlockPos pos, BlockState state, Entity entity,
	                             SwayBehaviorContext ctx, ForceAccumulator accumulator) {
		if (!VineMultiblockBehavior.isVine(state)) return;
		if (!(entity instanceof Player player)) return;

		// Only apply tension when the player is actually climbing the vine
		boolean isClimbing = player.onClimbable() && player.getDeltaMovement().y > 0.01;
		if (!isClimbing) return;

		ClientLevel level = Minecraft.getInstance().level;
		if (level == null) return;

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

		// Normalized position along the vine (0.0 at anchor, 1.0 at free end)
		float progress = totalLength > 0 ? (float) segmentIndex / totalLength : 0.0F;

		// When climbing, the anchor region tightens (tension) and the free end keeps some sway.
		// Tension factor: 0.0 at the anchor (fully tense), ramps up to 1.0 at the free end.
		float tensionFactor = progress * progress;

		// Scale the accumulated force: anchor gets heavily damped, free end keeps ~full movement
		accumulator.multiplyScale(0.15F + 0.85F * tensionFactor);
	}

	@Override
	public float clampMaxIntensity(float proposedIntensity, SwayBehaviorContext ctx) {
		// When climbing, cap the intensity lower to simulate tension
		Entity entity = ctx.triggeringEntity();
		if (entity instanceof Player player && player.onClimbable() && player.getDeltaMovement().y > 0.01) {
			return Math.min(proposedIntensity, 0.6F);
		}
		return proposedIntensity;
	}
}
