package com.johnmarinelli.openglesone;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;

public class MyGLRenderer implements Renderer {
	
	private Triangle mTriangle;
	private Square mSquare;

	@Override
	public void onDrawFrame(GL10 unused) {
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
		mTriangle.draw();
	}

	/*
	 * Called when geometry of view changes
	 * @see android.opengl.GLSurfaceView.Renderer#onSurfaceChanged(javax.microedition.khronos.opengles.GL10, int, int)
	 */
	@Override
	public void onSurfaceChanged(GL10 unused, int width, int height) {
		GLES20.glViewport(0, 0, width, height);
		
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
		"attribute vec4 vPosition;" +
		"void main() {" 			+
		"  gl_Position = vPosition;"+
		"}";
	
	/*
	 * Code for rendering face of shape with colors, textures, lighting, etc
	 */
	public static final String fragmentShaderCode =
		"precision mediump float;" +
		"uniform vec4 vColor;" 	   +
		"void main() {"			   +
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

}
