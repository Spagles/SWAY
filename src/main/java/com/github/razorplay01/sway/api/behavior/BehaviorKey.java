package com.github.razorplay01.sway.api.behavior;

import net.minecraft.resources./*? >= 1.21.11 {*/ Identifier /*?} else {*/ /*ResourceLocation *//*?} */;

import java.util.Objects;

public final class BehaviorKey {
	private final /*? >= 1.21.11 {*/ Identifier /*?} else {*/ /*ResourceLocation *//*?} */ id;

	private BehaviorKey(/*? >= 1.21.11 {*/ Identifier /*?} else {*/ /*ResourceLocation *//*?} */ id) {
		this.id = Objects.requireNonNull(id, "id");
	}

	public static BehaviorKey create(String namespace, String path) {
		return new BehaviorKey(/*? >= 1.21.11 {*/ Identifier /*?} else {*/ /*ResourceLocation *//*?} */./*? >1.20.1 {*/parse/*?} else { */ /*tryParse*//*?} */(namespace + ":" + path));
	}

	public static BehaviorKey fromVanilla(String path) {
		return create("sway", path);
	}

	public /*? >= 1.21.11 {*/ Identifier /*?} else {*/ /*ResourceLocation *//*?} */ getId() {
		return id;
	}

	public String getNamespace() {
		return id./*? >= 1.21.11 {*/getNamespace/*?} else {*/ /*getNamespace*//*?} */();
	}

	public String getPath() {
		return id./*? >= 1.21.11 {*/getPath/*?} else {*/ /*getPath*//*?} */();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof BehaviorKey that)) return false;
		return id.equals(that.id);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public String toString() {
		return id.toString();
	}
}
