package com.nxr.tpad.hapticcanvas;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;

public class FeelScreen extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.feellayout);
		
		
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		HapticCanvasActivity_Scott.myHapticView.saveFile();
		HapticCanvasActivity_Scott.myHapticView.setDrawing(false);
		HapticCanvasActivity_Scott.myHapticView.writeToLog(timestamp()+";"+"SwitchedToFeelMode"+";"+HapticCanvasActivity_Scott.myHapticView.backgroundName);
		
	}

	
	private String timestamp(){
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yy.MM.dd h:mm:ss:SSS a");
		String formattedDate = sdf.format(date);
		return formattedDate;
	}
	
}
