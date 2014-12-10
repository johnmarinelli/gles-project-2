package com.johnmarinelli.openglesone;


import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;
import android.util.Log;

public class MyGLRenderer implements Renderer {
	
	private Triangle mTriangle;
	private Square mSquare;

	private Camera mCamera;
	
	private float[] mProjectionMatrix = new float[16];
	private float[] mViewMatrix = new float[16];
	private float[] mMVPMatrix = new float[16];
	private float[] mRotationMatrix = new float[16];
	
	private float mViewportWidth = 0;
	private float mViewportHeight = 0;
	
	private float mAngle = 0f;
	
	public static String mVertexShaderCode;
	public static String mFragmentShaderCode;
	
	private Context mContext;
	
	public MyGLRenderer(Context ctx) {
		mContext = ctx;
		initShaders(ctx);
	}
	
	private void setCamera() {
		Vector3d eye = new Vector3d(0, 0, 0);
		Vector3d center = new Vector3d(-3, 0, 0);
		Vector3d up = new Vector3d(0, 1, 0);

		float ratio = (float) mViewportWidth / mViewportHeight;
		Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
		
		mCamera = new Camera(eye, center, up,
								-ratio, ratio,
								-1, 1,
								 3, 7);
	}
	
	private void initShaders(Context ctx) {		
		mVertexShaderCode = Utilities.readFile(ctx, R.raw.vertexshader);
		mFragmentShaderCode = Utilities.readFile(ctx, R.raw.fragshader);
	}

	@Override
	public void onDrawFrame(GL10 unused) {
		/*
		 * set camera matrix
		 * matrix to store in, offset, eyex, eyey, eyez, centerx, centery, centerz, upx, upy, upz
		 */
		//Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1f, 0f);
		Vector3d eye = new Vector3d(0, 0, -3);
		Vector3d center = new Vector3d(-0, 0, 0);
		Vector3d up = new Vector3d(0, 1, 0);
		
		mCamera.setCameraView(eye, center, up);
		
		/*
		 * calculate projection & view transformation
		 */
		//Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

		
		/* create rotation transformation */
		//float[] scratch = new float[16]; 
		
		/* rotate around the (0, 0, -1) axis */
		//Matrix.setRotateM(mRotationMatrix, 0, mAngle, 0, 0, -1f);
		
		/* combine rotation matrix with projection & view matrices */
		//Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);
		
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
		
//		mTriangle.draw(scratch);
		mSquare.draw(mCamera);
	}
	
	public void setAngle(float angle) { 
		mAngle = -angle;
	}
	
	public float getAngle() {
		return mAngle;
	}

	/*
	 * Called when geometry of view changes
	 * @see android.opengl.GLSurfaceView.Renderer#onSurfaceChanged(javax.microedition.khronos.opengles.GL10, int, int)
	 */
	@Override
	public void onSurfaceChanged(GL10 unused, int width, int height) {
		GLES20.glViewport(0, 0, width, height);
		mViewportWidth = width;
		mViewportHeight = height;
		
		setCamera();
		mTriangle = new Triangle();
		mSquare = new Square();
	}

	/*
	 * Called once to set up OpenGLES environment
	 * @see android.opengl.GLSurfaceView.Renderer#onSurfaceCreated(javax.microedition.khronos.opengles.GL10, javax.microedition.khronos.egl.EGLConfig)
	 */
	@Override
	public void onSurfaceCreated(GL10 arg0, EGLConfig config) {
		GLES20.glClearColor(0, 0, 0, 1);
	}
	
	public static int loadShader(int type, String shaderCode) {
		/*
		 * create vertex shader type (GLES20.GL_VERTEX_SHADER)
		 * or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
		 */
		int shader = GLES20.glCreateShader(type);
		
		/*
		 * add source code to shader & compile it
		 */
		GLES20.glShaderSource(shader, shaderCode);
		GLES20.glCompileShader(shader);
		
		return shader;
	}

	public static void checkGlError(String glOp) {
		int error;
		while((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
			Log.e("GLRENDERER", glOp + ": glError " + error);
			throw new RuntimeException(glOp + ": glError" + error);
		}
	}
}
