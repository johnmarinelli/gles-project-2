package com.johnmarinelli.openglesone;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

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
	}
}
