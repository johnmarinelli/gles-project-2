package com.johnmarinelli.openglesone;

public class Square extends Object2d {
	
	static float mSquareCoords[] = {
		-0.5f,  0.5f, 0f, //top left
		-0.5f, -0.5f, 0f, //bottom left
		 0.5f, -0.5f, 0f, //bottom right
		 0.5f,  0.5f, 0f, //top right
	};
	
	/*
	 * order to draw vertices
	 */
	private static short mDrawOrder[] = {
		0, 1, 2, 0, 2, 3
	};
	
	public Square() {
		super(mSquareCoords, mDrawOrder);
	}
	
	public void draw(float[] mvpMatrix) {
		super.draw(mvpMatrix);
	}
}
