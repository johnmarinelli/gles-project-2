package com.johnmarinelli.openglesone;

import android.opengl.Matrix;

public class Camera {

	private float[] mViewMatrix = new float[16];
	private float[] mProjectionMatrix = new float[16];
	
	/* 
	 * Camera location & orientation
	 */
	private Vector3d mEye = new Vector3d(0, 0, 0);
	private Vector3d mCenter = new Vector3d(0, 0, 0);
	private Vector3d mUp = new Vector3d(0, 0, 0);
	
	float mProjLeft, mProjRight, 
		mProjBottom, mProjTop, 
		mProjNear, mProjFar;
	
	public Camera(Vector3d eye, 
			Vector3d center,
			Vector3d up,
			float projLeft, float projRight,
			float projBottom, float projTop,
			float projNear, float projFar) {
		mEye = eye;
		mCenter = center;
		mUp = up;
		
		setCameraProjection(projLeft, projRight, 
				projBottom, projTop, 
				projNear, projFar);
	}

	
	/*
	 * set projection matrix
	 */
	public void setCameraProjection(float projLeft, float projRight, 
			float projBottom, float projTop,
			float projNear, float projFar) {
		mProjLeft = projLeft;
		mProjRight = projRight;
		mProjBottom = projBottom;
		mProjTop = projTop;
		mProjNear = projNear;
		mProjFar = projFar;
		
		Matrix.frustumM(mProjectionMatrix, 0, 
				projLeft, projRight, 
				projBottom, projTop, 
				projNear, projFar);
	}
	
	public void setCameraView(Vector3d eye,
							Vector3d center,
							Vector3d up) {
		Matrix.setLookAtM(mViewMatrix, 0, 
				eye.mX, eye.mY, eye.mZ, 
				center.mX, center.mY, center.mZ, 
				up.mX, up.mY, up.mZ);
	}
	
	public float[] getViewMatrix() { return mViewMatrix; }
	public float[] getProjectionMatrix() { return mProjectionMatrix; }
}
