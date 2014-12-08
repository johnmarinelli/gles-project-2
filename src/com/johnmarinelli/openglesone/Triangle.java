package com.johnmarinelli.openglesone;


public class Triangle extends Object2d{

	/* counterclockwise order */
	private static float TriangleCoords[] = {
		0.0f,  0.62f, 0.0f, //top
	   -0.5f, -0.31f, 0.0f, //bottom left
	    0.5f, -0.31f, 0.0f, //bottom right
	};
	
	private static short[] TriangleDrawOrder = {
		0, 1, 2
	};
	
	public Triangle() {
		super(TriangleCoords, TriangleDrawOrder);
	}
	
	public void draw(float[] mvpMatrix) {
		super.draw(mvpMatrix);
	}
}
