package com.johnmarinelli.openglesone;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

public class MyGLRenderer implements Renderer {
	
	private Triangle mTriangle;
	private Square mSquare;
	
	private float[] mProjectionMatrix = new float[16];
	private float[] mViewMatrix = new float[16];
	private float[] mMVPMatrix = new float[16];
	private float[] mRotationMatrix = new float[16];
	
	private float mAngle = 0f;

	@Override
	public void onDrawFrame(GL10 unused) {
		/*
		 * set camera matrix
		 * matrix to store in, offset, eyex, eyey, eyez, centerx, centery, centerz, upx, upy, upz
		 */
		Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1f, 0f);
		
		/*
		 * calculate projection & view transformation
		 */
		Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

		
		/* create rotation transformation */
		float[] scratch = new float[16]; 
		
		/* rotate around the (0, 0, -1) axis */
		Matrix.setRotateM(mRotationMatrix, 0, mAngle, 0, 0, -1f);
		
		/* combine rotation matrix with projection & view matrices */
		Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);
		
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
		
		mTriangle.draw(scratch);
		//mSquare.draw(mMVPMatrix);
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
		
		float ratio = (float) width / height;
		
		/*
		 * projection matrix is applied to object coordinates in onDrawFrame
		 */
		Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
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
	
	
	/*
	 * Code to rendering vertices of a shape
	 */
	public static final String vertexShaderCode = 
		/* provides hook to manipulate coords of objects using this vertex shader */
		"uniform mat4 uMVPMatrix;"    +
		"attribute vec4 vPosition;"  +
		"void main() {" 			 +
		/* matrix must be included as modifier of gl_Position */
		"  gl_Position = uMVPMatrix*vPosition;" +
		"}";
	
	/*
	 * Code for rendering face of shape with colors, textures, lighting, etc
	 */
	public static final String fragmentShaderCode =
		"precision mediump float;"  +
		"uniform vec4 vColor;" 	    +
		"void main() {"			    +
		"  gl_FragColor = vColor;"  +
		"}";
	
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
