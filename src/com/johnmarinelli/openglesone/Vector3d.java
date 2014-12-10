package com.johnmarinelli.openglesone;

/*
 * 3 dimensional vector
 */
public class Vector3d {
	public float mX;
	public float mY;
	public float mZ;
	
	Vector3d() {
		mX = mY = mZ = 0;
	}
	
	Vector3d(float x, float y, float z) {
		mX = x;
		mY = y;
		mZ = z;
	}
	
	void add(Vector3d v) {
		mX += v.mX;
		mY += v.mY;
		mZ += v.mZ;
	}
	
	void subtract(Vector3d v) {
		mX -= v.mX;
		mY -= v.mY;
		mZ -= v.mZ;
	}
	
	void multiply(float s) {
		mX *= s;
		mY *= s;
		mZ *= s;
	}
	
	void divide(float s) {
		if(s == 0f) return;
		mX /= s;
		mY /= s;
		mZ /= s;
	}
	
	float length() {
		return (float)Math.sqrt(mX*mX + mY*mY + mZ*mZ);
	}
	
	void normalize() {
		float length = length();
		divide(length);
	}
}
