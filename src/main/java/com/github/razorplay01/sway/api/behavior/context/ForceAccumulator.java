package com.github.razorplay01.sway.api.behavior.context;

import com.github.razorplay01.sway.client.SwayData;

public class ForceAccumulator {
	private float nx;
	private float nz;
	private float intensity;
	private boolean anyContribution;
	private float scaleMultiplier;

	public ForceAccumulator() {
		this.nx = 0;
		this.nz = 0;
		this.intensity = 0;
		this.anyContribution = false;
		this.scaleMultiplier = 1.0F;
	}

	public enum CombineStrategy {
		ADD,
		VECTOR_SUM,
		MAX,
		REPLACE
	}

	public void contribute(float dirX, float dirZ, float intensity, CombineStrategy strategy) {
		if (intensity <= 0.001F) return;
		float len = (float) Math.sqrt(dirX * dirX + dirZ * dirZ);
		if (len < 0.001F) return;
		float nDirX = dirX / len;
		float nDirZ = dirZ / len;

		switch (strategy) {
			case REPLACE -> {
				this.nx = nDirX;
				this.nz = nDirZ;
				this.intensity = intensity;
			}
			case MAX -> {
				if (intensity > this.intensity) {
					this.nx = nDirX;
					this.nz = nDirZ;
					this.intensity = intensity;
				}
			}
			case VECTOR_SUM -> {
				float vx = this.nx * this.intensity + nDirX * intensity;
				float vz = this.nz * this.intensity + nDirZ * intensity;
				float vLen = (float) Math.sqrt(vx * vx + vz * vz);
				if (vLen > 0.001F) {
					this.nx = vx / vLen;
					this.nz = vz / vLen;
					this.intensity = vLen;
				}
			}
			case ADD -> {
				float combinedIntensity = this.intensity + intensity;
				if (combinedIntensity > 0.001F) {
					float ratio = this.intensity / combinedIntensity;
					this.nx = (this.nx * ratio + nDirX * (1 - ratio));
					this.nz = (this.nz * ratio + nDirZ * (1 - ratio));
					float newLen = (float) Math.sqrt(this.nx * this.nx + this.nz * this.nz);
					if (newLen > 0.001F) {
						this.nx /= newLen;
						this.nz /= newLen;
					}
					this.intensity = combinedIntensity;
				}
			}
		}
		this.anyContribution = true;
	}

	public void multiplyScale(float factor) {
		this.scaleMultiplier *= factor;
	}

	public boolean hasAnyContribution() {
		return anyContribution;
	}

	public float getNx() {
		return nx;
	}

	public float getNz() {
		return nz;
	}

	public float getIntensity() {
		return intensity * scaleMultiplier;
	}

	public SwayData toSwayData() {
		return new SwayData(nx, nz, getIntensity());
	}

	public void updateSwayData(SwayData existing) {
		existing.update(nx, nz, getIntensity());
	}

	public void reset() {
		this.nx = 0;
		this.nz = 0;
		this.intensity = 0;
		this.anyContribution = false;
		this.scaleMultiplier = 1.0F;
	}
}
