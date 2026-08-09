package com.github.razorplay01.sway.client.behavior;

import com.github.razorplay01.sway.api.behavior.BehaviorKey;
import com.github.razorplay01.sway.client.behavior.collision.EntityCollisionBehavior;
import com.github.razorplay01.sway.client.behavior.deformation.StandardQuadraticDeformation;
import com.github.razorplay01.sway.client.behavior.force.ConfigurableMultiplierBehavior;
import com.github.razorplay01.sway.client.behavior.force.ProximityForceBehavior;
import com.github.razorplay01.sway.client.behavior.multiblock.DoublePlantMultiblockBehavior;
import com.github.razorplay01.sway.registry.BehaviorRegistry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class BuiltinBehaviors {
	private BuiltinBehaviors() {}

	private static volatile boolean registered = false;

	public static final BehaviorKey ENTITY_COLLISION_KEY = BehaviorKey.fromVanilla("entity_collision");
	public static final BehaviorKey PROXIMITY_FORCE_KEY = BehaviorKey.fromVanilla("proximity_force");
	public static final BehaviorKey DOUBLE_PLANT_MULTIBLOCK_KEY = BehaviorKey.fromVanilla("double_plant_multiblock");
	public static final BehaviorKey STANDARD_DEFORMATION_KEY = BehaviorKey.fromVanilla("standard_quadratic_deformation");

	private static final Map<Float, BehaviorKey> MULTIPLIER_KEYS = new ConcurrentHashMap<>();

	public static void ensureRegistered() {
		if (registered) return;
		synchronized (BuiltinBehaviors.class) {
			if (registered) return;
			registerBuiltins();
			registered = true;
		}
	}

	private static void registerBuiltins() {
		BehaviorRegistry.register(ENTITY_COLLISION_KEY, EntityCollisionBehavior.INSTANCE);
		BehaviorRegistry.register(PROXIMITY_FORCE_KEY, ProximityForceBehavior.INSTANCE);
		BehaviorRegistry.register(DOUBLE_PLANT_MULTIBLOCK_KEY, DoublePlantMultiblockBehavior.INSTANCE);
		BehaviorRegistry.register(STANDARD_DEFORMATION_KEY, StandardQuadraticDeformation.INSTANCE);
	}

	public static BehaviorKey multiplierKey(float multiplier) {
		return MULTIPLIER_KEYS.computeIfAbsent(multiplier, m -> {
			BehaviorKey key = BehaviorKey.fromVanilla("multiplier_" + Float.toString(m).replace('.', '_'));
			if (!BehaviorRegistry.exists(key)) {
				BehaviorRegistry.register(key, new ConfigurableMultiplierBehavior(m));
			}
			return key;
		});
	}
}
