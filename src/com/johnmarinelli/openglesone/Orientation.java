package com.johnmarinelli.openglesone;

import android.opengl.Matrix;

public class Orientation {

	/*
	 * transformation vectors
	 */
	private Vector3d mPosition = new Vector3d(0, 0, 0);
	private Vector3d mRotationAxis = new Vector3d(0, 0, 1);
	private Vector3d mScale = new Vector3d(1, 1, 1);
	
	/*
	 * transformation matrices
	 */
	private float[] mPositionMatrix = new float[16];
	private float[] mRotationMatrix = new float[16];
	private float mRotationAngle = 0f;
	private float[] mScaleMatrix = new float[16];
	
	private float[] mOrientationMatrix = new float[16];
	
	/*
	 * angle for rotation
	 */
	private float mAngle = 0f;
	
	public Orientation() {
		Matrix.setIdentityM(mRotationMatrix, 0);
		Matrix.setIdentityM(mOrientationMatrix, 0);
	}
	
	/*
	 * SETTING TRANSFORMATION VECTORS
	 */
	public void setPosition(Vector3d position) { 
		mPosition = position; 
	}
	
	public void setPosition(float x, float y, float z) {
		mPosition.mX = x;
		mPosition.mY = y;
		mPosition.mZ = z;
	}
	
	public void setRotationAxis(Vector3d rotationAxis) {
		mRotationAxis = rotationAxis;
	}
	
	public void setRotationAngle(float angle) {
		mAngle = angle;
	}
	
	public void setRotationAxis(float x, float y, float z) {
		mRotationAxis.mX = x;
		mRotationAxis.mY = y;
		mRotationAxis.mZ = z;
	}
	
	public void setScale(Vector3d scale) {
		mScale = scale;
	}
	
	public void setScale(float x, float y, float z) {
		mScale.mX = x;
		mScale.mY = y;
		mScale.mZ = z;
	}
	
	/*
	 * SETTING TRANSFORMATION MATRICES 
	 */
	public void setPositionMatrix() {
		Matrix.setIdentityM(mPositionMatrix, 0);
		Matrix.translateM(mPositionMatrix, 0, mPosition.mX, mPosition.mY, mPosition.mZ);
	}
	
	public void setRotationMatrix() {
		Matrix.setIdentityM(mRotationMatrix, 0);
		Matrix.rotateM(mRotationMatrix, 0, mAngle, 
				mRotationAxis.mX, mRotationAxis.mY, mRotationAxis.mZ);
	}
	
	public void setScaleMatrix() {
		Matrix.setIdentityM(mScaleMatrix, 0);
		Matrix.scaleM(mScaleMatrix, 0, mScale.mX, mScale.mY, mScale.mZ);
	}
	
	float[] getOrientationMatrix() {
		/* build all our matrices */
		setPositionMatrix();
		setRotationMatrix();
		setScaleMatrix();
		
		float[] temp = new float[16];

		/* rotate & translate */
		Matrix.multiplyMM(temp, 0, mPositionMatrix, 0, mRotationMatrix, 0);

		/* scale */
		Matrix.multiplyMM(mOrientationMatrix, 0, temp, 0, mScaleMatrix, 0);
		
		return mOrientationMatrix;
	}
}
