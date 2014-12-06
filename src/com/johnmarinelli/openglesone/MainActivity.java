package com.johnmarinelli.openglesone;

import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

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
	
	public MyGLSurfaceView(Context context) {
		super(context);
		this.setEGLContextClientVersion(2);
		mRenderer = new MyGLRenderer();
		this.setRenderer(mRenderer);
	}
	
}
