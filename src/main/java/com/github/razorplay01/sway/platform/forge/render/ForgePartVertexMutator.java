package com.github.razorplay01.sway.platform.forge.render;
//? forge {

/*import com.github.razorplay01.sway.client.render.VertexMutator;

public final class ForgePartVertexMutator implements VertexMutator {
	// Minecraft's block models store quad positions in a 0..16 unit box.
	// The deformation pipeline works in normalized 0..1 space, so we need to
	// scale positions on read and undo that scaling when writing them back.
	private static final float TO_BLOCK_SPACE = 1.0F / 16.0F;
	private static final float TO_MODEL_SPACE = 16.0F;

	private final int[] vertexData;
	private final int stride;
	private boolean modified;

	public ForgePartVertexMutator(int[] vertexData, int stride) {
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
		return Float.intBitsToFloat(vertexData[idx * stride]) * TO_BLOCK_SPACE;
	}

	@Override
	public float y(int idx) {
		return Float.intBitsToFloat(vertexData[idx * stride + 1]) * TO_BLOCK_SPACE;
	}

	@Override
	public float z(int idx) {
		return Float.intBitsToFloat(vertexData[idx * stride + 2]) * TO_BLOCK_SPACE;
	}

	private void markModified() { this.modified = true; }

	@Override
	public void setX(int idx, float value) {
		markModified();
		vertexData[idx * stride] = Float.floatToRawIntBits(value * TO_MODEL_SPACE);
	}

	@Override
	public void setY(int idx, float value) {
		markModified();
		vertexData[idx * stride + 1] = Float.floatToRawIntBits(value * TO_MODEL_SPACE);
	}

	@Override
	public void setZ(int idx, float value) {
		markModified();
		vertexData[idx * stride + 2] = Float.floatToRawIntBits(value * TO_MODEL_SPACE);
	}
}

*///?}
