package com.github.razorplay01.sway.platform.fabric.render;
//? fabric {

import com.github.razorplay01.sway.client.render.VertexMutator;
//? <26 {
/*import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
*///?}
//? >=26 {
import net.fabricmc.fabric.api.client.renderer.v1.mesh.MutableQuadView;
//?}

public final class FabricVertexMutator implements VertexMutator {
	private final MutableQuadView quad;

	public FabricVertexMutator(Object quad) {
		this.quad = (MutableQuadView) quad;
	}

	@Override
	public int vertexCount() {
		return 4;
	}

	@Override
	public float x(int idx) {
		return quad.x(idx);
	}

	@Override
	public float y(int idx) {
		return quad.y(idx);
	}

	@Override
	public float z(int idx) {
		return quad.z(idx);
	}

	@Override
	public void setX(int idx, float value) {
		quad.pos(idx, value, y(idx), z(idx));
	}

	@Override
	public void setY(int idx, float value) {
		quad.pos(idx, x(idx), value, z(idx));
	}

	@Override
	public void setZ(int idx, float value) {
		quad.pos(idx, x(idx), y(idx), value);
	}

	@Override
	public void pos(int idx, float x, float y, float z) {
		quad.pos(idx, x, y, z);
	}
}
//?}
