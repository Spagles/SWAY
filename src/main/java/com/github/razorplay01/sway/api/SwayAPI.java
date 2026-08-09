package com.github.razorplay01.sway.api;

import com.github.razorplay01.sway.api.behavior.BehaviorKey;
import com.github.razorplay01.sway.api.behavior.BehaviorPipeline;
import com.github.razorplay01.sway.api.behavior.SwayBehavior;
import com.github.razorplay01.sway.client.behavior.BuiltinBehaviors;
import com.github.razorplay01.sway.registry.BehaviorRegistry;
import com.github.razorplay01.sway.registry.BlockPipelineRegistry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public final class SwayAPI {
	private SwayAPI() {
	}

	private static final Map<Block, Float> LEGACY_REGISTRY = Collections.synchronizedMap(new IdentityHashMap<>());

	public static void register(Block block, float multiplier) {
		LEGACY_REGISTRY.put(block, multiplier);
		BuiltinBehaviors.ensureRegistered();
		BlockPipelineRegistry.setPipeline(block, List.of(
				BuiltinBehaviors.ENTITY_COLLISION_KEY,
				BuiltinBehaviors.PROXIMITY_FORCE_KEY,
				BuiltinBehaviors.DOUBLE_PLANT_MULTIBLOCK_KEY,
				BuiltinBehaviors.STANDARD_DEFORMATION_KEY,
				BuiltinBehaviors.multiplierKey(multiplier)
		));
	}

	public static boolean isInteractive(Block block) {
		return LEGACY_REGISTRY.containsKey(block) || BlockPipelineRegistry.hasPipeline(block);
	}

	public static float getMultiplier(BlockState state) {
		Float base = LEGACY_REGISTRY.get(state.getBlock());
		return base != null ? base : 0.0F;
	}

	public static Map<Block, Float> getRegistry() {
		return LEGACY_REGISTRY;
	}

	public static void registerBehavior(BehaviorKey key, SwayBehavior behavior) {
		BehaviorRegistry.register(key, behavior);
	}

	public static void addBehaviorToBlock(Block block, BehaviorKey key, int priority) {
		BuiltinBehaviors.ensureRegistered();
		BlockPipelineRegistry.addBehavior(block, key, priority);
	}

	public static void removeBehaviorFromBlock(Block block, BehaviorKey key) {
		BlockPipelineRegistry.removeBehavior(block, key);
	}

	public static void setBlockPipeline(Block block, List<BehaviorKey> keys) {
		BuiltinBehaviors.ensureRegistered();
		BlockPipelineRegistry.setPipeline(block, keys);
	}

	public static BehaviorPipeline getBehaviorPipeline(Block block) {
		return BlockPipelineRegistry.getPipeline(block);
	}

	public static void registerGlobalBehavior(BehaviorKey key, int priority, Predicate<Block> applyTo) {
		BlockPipelineRegistry.registerGlobalBehavior(key, priority, applyTo);
	}
}
