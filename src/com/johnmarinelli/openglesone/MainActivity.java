package com.johnmarinelli.openglesone;

import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;

public class MainActivity extends ActionBarActivity {
	
	private GLSurfaceView mGLView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mGLView = new MyGLSurfaceView(this);
		this.setContentView(mGLView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}

class MyGLSurfaceView extends GLSurfaceView {

	private MyGLRenderer mRenderer;
	private float mPreviousX = 0f, mPreviousY = 0f;
	private final float TOUCH_SCALE_FACTOR = 180f / 320;
	
	public MyGLSurfaceView(Context context) {
		super(context);
		this.setEGLContextClientVersion(2);
		mRenderer = new MyGLRenderer();
		this.setRenderer(mRenderer);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent e) {
		float x = e.getX();
		float y = e.getY();
		
		switch(e.getAction()) {
		case MotionEvent.ACTION_MOVE:
			float dx = x - mPreviousX;
			float dy = y - mPreviousY;
			
			/* reverse direction of rotation above midline */
			if(y > getHeight() / 2) {
				dx *= -1;
			}
			/* reverse direction of rotation to left of midline */
			if(x < getWidth() / 2) {
				dy *= -1;
			}
			
			mRenderer.setAngle(mRenderer.getAngle() +
					((dx+dy) * TOUCH_SCALE_FACTOR));
		}
		
		mPreviousX = x;
		mPreviousY = y;
		return true;
	}
	
}
