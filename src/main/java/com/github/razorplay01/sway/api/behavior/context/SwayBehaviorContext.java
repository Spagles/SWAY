package com.github.razorplay01.sway.api.behavior.context;

import com.github.razorplay01.sway.config.SwayConfig;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;

public record SwayBehaviorContext(ClientLevel level, Entity triggeringEntity, SwayConfig config, float partialTick) {
}
