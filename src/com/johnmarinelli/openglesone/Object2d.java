package com.johnmarinelli.openglesone;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.opengl.GLES20;
import android.opengl.Matrix;

public abstract class Object2d {
	
	/* 
	 * Uses a ByteBuffer to pass into GL graphics pipeline
	 */
	private FloatBuffer mVertexBuffer;
	private ShortBuffer mDrawListBuffer;
	
	/* number of coordinates per vertex */
	static final int COORDS_PER_VERTEX = 3;
	
	/* orientation matrices */
	private Vector3d mPosition = new Vector3d(0, 0, 0);
	private Vector3d mRotationAxis = new Vector3d(0, 0, 0);
	private Vector3d mScale = new Vector3d(1, 1, 1);
	
	private float[] mPositionMatrix = new float[16];
	private float[] mRotationMatrix = new float[16];
	private float mRotationAngle = 0f;
	private float[] mScaleMatrix = new float[16];
	
	/* model matrix */
	private float[] mModelMatrix = new float[16];
	
	/* model view matrix */
	private float[] mModelViewMatrix = new float[16];
	
	/* model-view-projection matrix */
	private float[] mMVPMatrix = new float[16];
	
	/* float arrays holding coordinates & color */
	private float[] mCoords;
	private float[] mColor = { 1f, .5f,	.25f, 1	};
	
	/* order in which to draw vertices */
	private short[] mDrawOrder;
	
	/* for manipulating the OpenGL program */
	private int mProgram;
	private int mPositionHandle;
	private int mColorHandle;
	private int mMVPMatrixHandle;
	
	/* four bytes per vertex */
	private final int mVertexStride = COORDS_PER_VERTEX * 4;
	
	private int mVertexCount = 0;
	
	public Object2d(float[] coords, short[] drawOrder) {
		mCoords = coords;
		mDrawOrder = drawOrder;
		mVertexCount = mCoords.length / COORDS_PER_VERTEX;
		
		/* 
		 * init vertex byte buffer for shape coords.
		 * capacity = number of coords * 4 bytes/float 
		 */
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(mCoords.length * 4);
		
		byteBuffer.order(ByteOrder.nativeOrder());
		
		/* create float buffer from bytebuffer */
		mVertexBuffer = byteBuffer.asFloatBuffer();
		/* add coordinates to float buffer */
		mVertexBuffer.put(mCoords);
		/* set buffer to read first coordinate */
		mVertexBuffer.position(0);
		
		/*
		 * Draw list buffer
		 */
		ByteBuffer drawListByteBuffer = ByteBuffer.allocate(mDrawOrder.length * 2);
		drawListByteBuffer.order(ByteOrder.nativeOrder());
		
		mDrawListBuffer = drawListByteBuffer.asShortBuffer();
		mDrawListBuffer.put(mDrawOrder);
		mDrawListBuffer.position(0);
		
		int vertexShader = 
				MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER, MyGLRenderer.mVertexShaderCode);
		int fragShader = 
				MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, MyGLRenderer.mFragmentShaderCode);
		
		/* create empty OpenGLES program */
		mProgram = GLES20.glCreateProgram();
		
		/* attach vertex & fragment shaders to program */
		GLES20.glAttachShader(mProgram, vertexShader);
		GLES20.glAttachShader(mProgram, fragShader);
		
		/* create OpenGLES program executable */
		GLES20.glLinkProgram(mProgram);
	}
	
	public void draw(Camera camera) {
		/* set position vector */
		mPosition.mX = 0f;
		mPosition.mY = 0f;
		mPosition.mZ = 0f;
		
		/* set rotation vector */
		mRotationAxis.mX = 0f;
		mRotationAxis.mY = 0f;
		mRotationAxis.mZ = 1f;
		
		/* set scale vector */
		mScale.mX = 1f;
		mScale.mY = 1f;
		mScale.mZ = 1f;
		
		float[] projectionMatrix = camera.getProjectionMatrix();
		float[] viewMatrix = camera.getViewMatrix();
		
		/* build translation matrix */
		Matrix.setIdentityM(mPositionMatrix, 0);
		Matrix.translateM(mPositionMatrix, 0, mPosition.mX, mPosition.mY, mPosition.mZ);
		
		/* build rotation matrix */
		Matrix.setIdentityM(mRotationMatrix, 0);
		Matrix.rotateM(mRotationMatrix, 0, 
				   mRotationAngle++, 
				   mRotationAxis.mX, 
				   mRotationAxis.mY, 
				   mRotationAxis.mZ);
		
		/* build scale matrix */
		Matrix.setIdentityM(mScaleMatrix, 0);
		Matrix.scaleM(mScaleMatrix, 0, mScale.mX, mScale.mY, mScale.mZ);
		
		float[] tempMatrix = new float[16];
   
		/* rotate object around axis, then translate */
		Matrix.multiplyMM(tempMatrix, 0, mPositionMatrix, 0, mRotationMatrix, 0);
		
		/* build scale matrix */
		Matrix.setIdentityM(mModelMatrix, 0);

		/* scale then store in model matrix */
		Matrix.multiplyMM(mModelMatrix, 0, tempMatrix, 0, mScaleMatrix, 0);
		
		/* add program to gles environment */
		GLES20.glUseProgram(mProgram);
		
		/* get handle to vertex shader's vPosition member */
		mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
		
		/* enable handle to triangle vertices */
		GLES20.glEnableVertexAttribArray(mPositionHandle);
		
		/* prepare triangle coordinate data */
		GLES20.glVertexAttribPointer(mPositionHandle, 
				COORDS_PER_VERTEX, 
				GLES20.GL_FLOAT, 
				false, 
				mVertexStride, 
				mVertexBuffer);
		
		/* get handle to fragment shader's vColor member */
		mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
		
		/* set color for drawing triangle */
		GLES20.glUniform4fv(mColorHandle, 1, mColor, 0);
		
		/* get handle to shape's transformation matrix */
		mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

		/* build model view matrix */
	    Matrix.multiplyMM(mModelViewMatrix, 0, viewMatrix, 0, mModelMatrix, 0);
	     
	    /* build MVP matrix */
	    Matrix.multiplyMM(mMVPMatrix, 0, projectionMatrix, 0, mModelViewMatrix, 0);
	    
		/* calculate projection & view transformation */
		//Matrix.multiplyMM(mMVPMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
		
		/* pass projection & view transformation into shader */
		GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
		
		/* draw object */
		/*GLES20.glDrawElements(GLES20.GL_TRIANGLES, mDrawOrder.length,
				GLES20.GL_UNSIGNED_SHORT, mDrawListBuffer);*/
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, mVertexCount);
		
		/* disable vertex array */
		GLES20.glDisableVertexAttribArray(mPositionHandle);
	}
}
