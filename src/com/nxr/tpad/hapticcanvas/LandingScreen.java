package com.nxr.tpad.hapticcanvas;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class LandingScreen extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.landingscreen);
	    // TODO Auto-generated method stub
	    TextView textview = (TextView)findViewById(R.id.textView1);
	    textview.setText("Touch & Feel E-Books", TextView.BufferType.EDITABLE);
	    textview.setTextSize(40);
	}

	/** Called when the user clicks the Send button */
	public void openBookChooser(View view) {
	    // Do something in response to button
		Intent intent = new Intent(this, BookChooser.class);
		startActivity(intent);
	}
	public void ReadBook(View view) {
	    // Do something in response to button
		Intent intent = new Intent(this, HapticCanvasActivity.class);
		startActivity(intent);
	}
	
}
