package com.github.razorplay01.sway.registry;

import com.github.razorplay01.sway.api.behavior.BehaviorKey;
import com.github.razorplay01.sway.api.behavior.BehaviorPipeline;
import com.github.razorplay01.sway.api.behavior.SwayBehavior;
import net.minecraft.world.level.block.Block;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

public final class BlockPipelineRegistry {
	private static final Map<Block, List<PrioritizedEntry>> PIPELINE_ENTRIES = new ConcurrentHashMap<>();
	private static final Map<Block, BehaviorPipeline> CACHED_PIPELINES = new ConcurrentHashMap<>();
	private static final List<GlobalEntry> GLOBAL_ENTRIES = Collections.synchronizedList(new ArrayList<>());

	private BlockPipelineRegistry() {}

	private record PrioritizedEntry(int priority, BehaviorKey key) {}

	private record GlobalEntry(int priority, BehaviorKey key, Predicate<Block> predicate) {}

	public static void addBehavior(Block block, BehaviorKey key, int priority) {
		PIPELINE_ENTRIES.computeIfAbsent(block, k -> Collections.synchronizedList(new ArrayList<>()))
				.add(new PrioritizedEntry(priority, key));
		CACHED_PIPELINES.remove(block);
	}

	public static void removeBehavior(Block block, BehaviorKey key) {
		List<PrioritizedEntry> entries = PIPELINE_ENTRIES.get(block);
		if (entries != null) {
			entries.removeIf(e -> e.key.equals(key));
			CACHED_PIPELINES.remove(block);
		}
	}

	public static void setPipeline(Block block, List<BehaviorKey> keys) {
		List<PrioritizedEntry> entries = Collections.synchronizedList(new ArrayList<>());
		for (int i = 0; i < keys.size(); i++) {
			entries.add(new PrioritizedEntry((i + 1) * 100, keys.get(i)));
		}
		PIPELINE_ENTRIES.put(block, entries);
		CACHED_PIPELINES.remove(block);
	}

	public static void registerGlobalBehavior(BehaviorKey key, int priority, Predicate<Block> applyTo) {
		GLOBAL_ENTRIES.add(new GlobalEntry(priority, key, applyTo));
		CACHED_PIPELINES.clear();
	}

	public static BehaviorPipeline getPipeline(Block block) {
		return CACHED_PIPELINES.computeIfAbsent(block, BlockPipelineRegistry::buildPipeline);
	}

	public static boolean hasPipeline(Block block) {
		List<PrioritizedEntry> entries = PIPELINE_ENTRIES.get(block);
		if (entries != null && !entries.isEmpty()) return true;
		for (GlobalEntry g : GLOBAL_ENTRIES) {
			if (g.predicate.test(block)) return true;
		}
		return false;
	}

	private static BehaviorPipeline buildPipeline(Block block) {
		List<PrioritizedEntry> all = new ArrayList<>();

		List<PrioritizedEntry> blockEntries = PIPELINE_ENTRIES.get(block);
		if (blockEntries != null) all.addAll(blockEntries);

		for (GlobalEntry g : GLOBAL_ENTRIES) {
			if (g.predicate.test(block)) {
				all.add(new PrioritizedEntry(g.priority, g.key));
			}
		}

		all.sort(Comparator.comparingInt(PrioritizedEntry::priority));

		List<SwayBehavior> behaviors = new ArrayList<>();
		for (PrioritizedEntry e : all) {
			BehaviorRegistry.get(e.key).ifPresent(behaviors::add);
		}
		return new BehaviorPipeline(behaviors);
	}

	public static void clearCache() {
		CACHED_PIPELINES.clear();
	}
}
