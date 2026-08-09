package com.github.razorplay01.sway.platform.neoforge.render;
//? neoforge {

/*import com.github.razorplay01.sway.client.render.VertexMutator;

//? <=1.21.1 {
public final class NeoForgePartVertexMutator implements VertexMutator {
	private final int[] vertexData;
	private final int stride;
	private boolean modified;

	public NeoForgePartVertexMutator(int[] vertexData, int stride) {
		this.vertexData = vertexData.clone();
		this.stride = stride;
		this.modified = false;
	}

	public int[] getTransformedVertexData() {
		return vertexData;
	}

	public boolean isModified() {
		return modified;
	}

	@Override
	public int vertexCount() {
		return 4;
	}

	@Override
	public float x(int idx) {
		return Float.intBitsToFloat(vertexData[idx * stride]);
	}

	@Override
	public float y(int idx) {
		return Float.intBitsToFloat(vertexData[idx * stride + 1]);
	}

	@Override
	public float z(int idx) {
		return Float.intBitsToFloat(vertexData[idx * stride + 2]);
	}

	private void markModified() { this.modified = true; }

	@Override
	public void setX(int idx, float value) {
		markModified();
		vertexData[idx * stride] = Float.floatToRawIntBits(value);
	}

	@Override
	public void setY(int idx, float value) {
		markModified();
		vertexData[idx * stride + 1] = Float.floatToRawIntBits(value);
	}

	@Override
	public void setZ(int idx, float value) {
		markModified();
		vertexData[idx * stride + 2] = Float.floatToRawIntBits(value);
	}
}
//?}
//? >1.21.1 {
/^public final class NeoForgePartVertexMutator implements VertexMutator {
	private final float[] xs = new float[4];
	private final float[] ys = new float[4];
	private final float[] zs = new float[4];
	private boolean modified;

	public NeoForgePartVertexMutator(org.joml.Vector3fc p0, org.joml.Vector3fc p1,
	                          org.joml.Vector3fc p2, org.joml.Vector3fc p3) {
		xs[0] = p0.x();
		ys[0] = p0.y();
		zs[0] = p0.z();
		xs[1] = p1.x();
		ys[1] = p1.y();
		zs[1] = p1.z();
		xs[2] = p2.x();
		ys[2] = p2.y();
		zs[2] = p2.z();
		xs[3] = p3.x();
		ys[3] = p3.y();
		zs[3] = p3.z();
		this.modified = false;
	}

	public boolean isModified() {
		return modified;
	}

	public org.joml.Vector3fc getPos(int idx) {
		return new org.joml.Vector3f(xs[idx], ys[idx], zs[idx]);
	}

	@Override
	public int vertexCount() {
		return 4;
	}

	@Override
	public float x(int idx) {
		return xs[idx];
	}

	@Override
	public float y(int idx) {
		return ys[idx];
	}

	@Override
	public float z(int idx) {
		return zs[idx];
	}

	@Override
	public void setX(int idx, float value) {
		xs[idx] = value;
		modified = true;
	}

	@Override
	public void setY(int idx, float value) {
		ys[idx] = value;
		modified = true;
	}

	@Override
	public void setZ(int idx, float value) {
		zs[idx] = value;
		modified = true;
	}
}
^///?}
*///?}
