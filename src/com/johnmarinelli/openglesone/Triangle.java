package com.johnmarinelli.openglesone;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.SystemClock;

public class Triangle {

	/* 
	 * Uses a ByteBuffer to pass into GL graphics pipeline
	 */
	private FloatBuffer mVertexBuffer;
	
	/* number of coordinates per vertex */
	static final int COORDS_PER_VERTEX = 3;
	
	/* counterclockwise order */
	static float mTriangleCoords[] = {
		0.0f,  0.62f, 0.0f, //top
	   -0.5f, -0.31f, 0.0f, //bottom left
	    0.5f, -0.31f, 0.0f, //bottom right
	};
	
	float color[] = {
		0.6f,
		0.7f,
		0.2f,
		1f,
	};
	
	private final int mVertexCount = mTriangleCoords.length / COORDS_PER_VERTEX;
	
	/* four bytes per vertex */
	private final int mVertexStride = COORDS_PER_VERTEX * 4;
	
	private int mProgram;
	private int mPositionHandle;
	private int mColorHandle;
	private int mMVPMatrixHandle;
	
	public Triangle() {
		/* 
		 * init vertex byte buffer for shape coords.
		 * capacity = number of coords * 4 bytes/float 
		 */
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(mTriangleCoords.length * 4);
		
		byteBuffer.order(ByteOrder.nativeOrder());
		
		/* create float buffer from bytebuffer */
		mVertexBuffer = byteBuffer.asFloatBuffer();
		/* add coordinates to float buffer */
		mVertexBuffer.put(mTriangleCoords);
		/* set buffer to read first coordinate */
		mVertexBuffer.position(0);
		
		int vertexShader = 
				MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER, MyGLRenderer.vertexShaderCode);
		int fragShader = 
				MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, MyGLRenderer.fragmentShaderCode);
		
		/* create empty OpenGLES program */
		mProgram = GLES20.glCreateProgram();
		
		/* attach vertex & fragment shaders to program */
		GLES20.glAttachShader(mProgram, vertexShader);
		GLES20.glAttachShader(mProgram, fragShader);
		
		/* create OpenGLES program executable */
		GLES20.glLinkProgram(mProgram);
	}
	
	public void draw(float[] mvpMatrix) {
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
		GLES20.glUniform4fv(mColorHandle, 1, color, 0);
		
		/* get handle to shape's transformation matrix */
		mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
		
		/* pass projection & view transformation into shader */
		GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
		
		/* draw triangle */
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, mVertexCount);
		
		/* disable vertex array */
		GLES20.glDisableVertexAttribArray(mPositionHandle);
	}
}
