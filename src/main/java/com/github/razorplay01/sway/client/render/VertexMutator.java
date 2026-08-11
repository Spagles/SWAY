package com.github.razorplay01.sway.client.render;

public interface VertexMutator {
	int vertexCount();

	float x(int idx);

	float y(int idx);

	float z(int idx);

	void setX(int idx, float value);

	void setY(int idx, float value);

	void setZ(int idx, float value);

	default void pos(int idx, float x, float y, float z) {
		setX(idx, x);
		setY(idx, y);
		setZ(idx, z);
	}

	default void addOffset(int idx, float dx, float dy, float dz) {
		pos(idx, x(idx) + dx, y(idx) + dy, z(idx) + dz);
	}
}
