package com.nxr.tpad.hapticcanvas;

import nxr.tpadioio.lib.TPadIOIOTabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class HapticCanvasActivity extends TPadIOIOTabActivity {

	// Used to initialize our screenview object for
	// drawing on to

	public static TabHost mTabHost;

	public static HapticCanvasView myHapticView;

	public static String page_fileName_frmIntent;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// set the content view to our layout .xml
		setContentView(R.layout.hapticcanvas);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		
		/*---------------Added by Scott Neaves 2-28-2014 ----------------*/
		page_fileName_frmIntent = this.getIntent().getExtras().getString("page_fileName");
		Log.w("TPAD App", "filname received by HapticCanvasActivity, sent by ImageChooser: " + page_fileName_frmIntent);
		/*---------------End Added by Scott Neaves 2-28-2014 ----------------*/
		
		mTabHost = getTabHost();
		
		/*------------Moved this from end of onCreate to here by Scott Neaves 2-28-2014 -----------------*/
		
		// initialize screenview class object
		myHapticView = (HapticCanvasView) findViewById(R.id.hapticCanvasView);
		// Start communication with TPad
		
		/*------------End Moved this from end of onCreate to here by Scott Neaves 2-28-2014 -----------------*/

		TabSpec FileSpec = mTabHost.newTabSpec("File");
		FileSpec.setIndicator("Save/Load");
		Intent fileIntent = new Intent(this, FileOptions.class);
		/*---------------Added by Scott Neaves 2-28-2014 ----------------*/
		//fileIntent.putExtra("page_fileName_frmIntent", page_fileName_frmIntent);
		/*---------------End Added by Scott Neaves 2-28-2014 ----------------*/
		FileSpec.setContent(fileIntent);
		
		TabSpec BrushSpec = mTabHost.newTabSpec("Brush");
		BrushSpec.setIndicator("Brush Options");
		Intent brushIntent = new Intent(this, BrushOptions.class);
		BrushSpec.setContent(brushIntent);
		
		TabSpec EditSpec = mTabHost.newTabSpec("Edit");
		EditSpec.setIndicator("Edit");
		Intent editIntent = new Intent(this, EditScreen.class);
		EditSpec.setContent(editIntent);
		
		TabSpec FeelSpec = mTabHost.newTabSpec("Feel");
		FeelSpec.setIndicator("Feel");
		Intent feelIntent = new Intent(this, FeelScreen.class);
		FeelSpec.setContent(feelIntent);
		
		mTabHost.addTab(FileSpec);
		mTabHost.addTab(BrushSpec);
		mTabHost.addTab(EditSpec);
		mTabHost.addTab(FeelSpec);
				
		myHapticView.setContext(this);

	}

	// Following code modified from

	@Override
	protected void onPause() {
		// Pauses our surfaceview thread
		super.onPause();
		// Stop our drawing thread (which runs in the screenview object) when
		// the screen is paused
		myHapticView.pause();

	}

	@Override
	protected void onDestroy() {
		
		myHapticView.destroy();
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// resume the drawing thread (which runs in the screenview object) when
		// screen is resumed.
		myHapticView.resume();

	}

}
