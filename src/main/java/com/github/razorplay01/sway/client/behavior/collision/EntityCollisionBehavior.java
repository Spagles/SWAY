package com.github.razorplay01.sway.client.behavior.collision;

import com.github.razorplay01.sway.api.behavior.contributors.CollisionContributor;
import com.github.razorplay01.sway.api.behavior.context.SwayBehaviorContext;
import com.github.razorplay01.sway.config.SwayConfig;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;

public class EntityCollisionBehavior implements CollisionContributor {
	public static final EntityCollisionBehavior INSTANCE = new EntityCollisionBehavior();

	@Override
	public AABB contributeSearchArea(Entity entity, SwayBehaviorContext ctx) {
		SwayConfig cfg = ctx.config();
		float radius = cfg.influenceRadius;
		int r = (int) Math.ceil(radius);
		return entity.getBoundingBox().inflate(r, 1, r);
	}
}
