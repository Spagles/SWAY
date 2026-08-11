package com.github.razorplay01.sway.api.behavior;

import com.github.razorplay01.sway.api.behavior.contributors.CollisionContributor;
import com.github.razorplay01.sway.api.behavior.contributors.DeformationContributor;
import com.github.razorplay01.sway.api.behavior.contributors.ForceContributor;
import com.github.razorplay01.sway.api.behavior.contributors.MultiBlockContributor;

import java.util.ArrayList;
import java.util.List;

public final class BehaviorPipeline {
	private final List<SwayBehavior> behaviors;
	private final List<CollisionContributor> collisionContributors;
	private final List<ForceContributor> forceContributors;
	private final List<MultiBlockContributor> multiBlockContributors;
	private final List<DeformationContributor> deformationContributors;

	public BehaviorPipeline(List<SwayBehavior> behaviors) {
		this.behaviors = List.copyOf(behaviors);
		this.collisionContributors = new ArrayList<>();
		this.forceContributors = new ArrayList<>();
		this.multiBlockContributors = new ArrayList<>();
		this.deformationContributors = new ArrayList<>();
		for (SwayBehavior b : behaviors) {
			if (b instanceof CollisionContributor c) collisionContributors.add(c);
			if (b instanceof ForceContributor c) forceContributors.add(c);
			if (b instanceof MultiBlockContributor c) multiBlockContributors.add(c);
			if (b instanceof DeformationContributor c) deformationContributors.add(c);
		}
	}

	public List<SwayBehavior> getBehaviors() {
		return behaviors;
	}

	public List<CollisionContributor> getCollisionContributors() {
		return collisionContributors;
	}

	public List<ForceContributor> getForceContributors() {
		return forceContributors;
	}

	public List<MultiBlockContributor> getMultiBlockContributors() {
		return multiBlockContributors;
	}

	public List<DeformationContributor> getDeformationContributors() {
		return deformationContributors;
	}

	public boolean isEmpty() {
		return behaviors.isEmpty();
	}
}
