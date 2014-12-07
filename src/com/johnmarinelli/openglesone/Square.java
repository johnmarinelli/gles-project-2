package com.johnmarinelli.openglesone;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.opengl.GLES20;

public class Square {
	private FloatBuffer mVertexBuffer;
	private ShortBuffer mDrawListBuffer;
	
	static final int COORDS_PER_VERTEX = 3;
	static float mSquareCoords[] = {
		-0.5f,  0.5f, 0f, //top left
		-0.5f, -0.5f, 0f, //bottom left
		 0.5f, -0.5f, 0f, //bottom right
		 0.5f,  0.5f, 0f, //top right
	};
	
	/*
	 * order to draw vertices
	 */
	private short mDrawOrder[] = {
		0, 1, 2, 0, 2, 3
	};
	
	private float color[] = {
		.75f, .5f, .25f, 1f
	};
	
	private int mVertexCount = mSquareCoords.length / COORDS_PER_VERTEX;
	
	private int mVertexStride = COORDS_PER_VERTEX * 4;
	
	private int mProgram;
	private int mPositionHandle;
	private int mColorHandle;
	
	public Square() {
		/*
		 * Vertex Buffer
		 */	
		/* length * bytes per float */
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(mSquareCoords.length * 4);
		byteBuffer.order(ByteOrder.nativeOrder());
		
		mVertexBuffer = byteBuffer.asFloatBuffer();
		mVertexBuffer.put(mSquareCoords);
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
		
		/* draw triangle */
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, mVertexCount);
		
		/* disable vertex array */
		GLES20.glDisableVertexAttribArray(mPositionHandle);
	}
}
