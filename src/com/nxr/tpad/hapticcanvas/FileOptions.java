package com.nxr.tpad.hapticcanvas;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class FileOptions extends Activity {

	private static Button LoadBackgroundButton;

	private static View loadView;

	private static final int REQ_CODE_PICK_IMAGE = 100;

	private boolean hapticLoad = true;
	
	public static final String dl = ";";
	

	// TPad myTPad;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.filelayout);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		// myTPad = HapticCanvasActivity.myTPad;

		LoadBackgroundButton = (Button) findViewById(R.id.loadBackgroundButton);
		loadView = (View) findViewById(R.id.loadView);
		
		// Action to take when picture selection button is clicked
		LoadBackgroundButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				hapticLoad = false;
				// We must start a new intent to request data from the system's
				// image picker
				Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
				photoPickerIntent.setType("image/*");
				startActivityForResult(photoPickerIntent, REQ_CODE_PICK_IMAGE);
			}
		});
		
		loadView.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				hapticLoad = true;
				// We must start a new intent to request data from the system's
				// image picker
				Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
				photoPickerIntent.setType("image/*");
				startActivityForResult(photoPickerIntent, REQ_CODE_PICK_IMAGE);
				
				return false;
			}
		});
		
		/*--------------------Added by Scott Neaves 2-28-2014, may be incorrect--------------------------*/
		//String bg_img_FileName = this.getIntent().getExtras().getString("page_fileName_frmIntent");
		//Log.w("TPAD App", "filename received by FileOptions, sent by HapticCanvasActivity: " + bg_img_FileName);
		//setBackgroundImage_fromImageChooser(bg_img_FileName);
		/*--------------------End Added by Scott Neaves 2-28-2014, may be incorrect--------------------------*/

		/*
		// Action to take when save picture button is clicked
		SaveButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				saveFile();
			}
		});
		*/
	}
/*
	protected void saveFile() {
		
		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath()+"/Haptic Images");

		String file_path = mediaStorageDir.getPath();
		Log.i("Path: ", file_path);
		OutputStream fOut = null;

		File dir = new File(file_path);

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yy.MM.dd. h:mm:ss a");
		String formattedDate = sdf.format(date);
		Log.i("Date:", formattedDate);

		if (!dir.exists()) {
			dir.mkdirs();
		}
		dir = new File(file_path, formattedDate + " " + FileName.getText().toString() + ".png");

		Log.i("Dir:", dir.toString());
		try {
			fOut = new FileOutputStream(dir);
			HapticCanvasActivity.myHapticView.getDrawBitmap().compress(Bitmap.CompressFormat.PNG, 100, fOut);
			fOut.flush();
			fOut.close();
			//sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(dir)));
		} catch (IOException e) {
			Log.i("Fail:", " didn't flush stream");
			e.printStackTrace();
		}
		
	}
	*/
	
	/*--------------------Added by Scott Neaves 2-28-2014, may be incorrect--------------------------
	protected void setBackgroundImage_fromImageChooser(String bg_img_FileName){
		hapticLoad = false;
		//Get URI of image to be texturized from intent here:
		File file = new File(this.getFilesDir(), bg_img_FileName);
		Log.w("TPAD App", "File name to be opened in texturizer:" + bg_img_FileName);
		HapticCanvasActivity.myHapticView.writeToLog(timestamp() + dl + "BackgroundLoaded" + dl + bg_img_FileName);
		HapticCanvasActivity.myHapticView.backgroundName = bg_img_FileName;
		Log.w("TPAD App", "Got past both myHapticViews");
				
		try {
			// set the display bitmap based on the bitmap we just got
			// back from our intent
			Bitmap b = BitmapFactory.decodeFile(file.getAbsolutePath());
			Log.w("TPAD App", "is b null? " + b.toString());
			//Bitmap b = Media.getBitmap(getContentResolver(), bitmapUri);
			
			if (hapticLoad){
				HapticCanvasActivity.myHapticView.setDrawBitmap(b);
				Log.w("TPAD App", "hapticLoad == true, did setDrawBitmap");
			}else{
				HapticCanvasActivity.myHapticView.setBackgroundBitmap(b);
				Log.w("TPAD App", "hapticLoad == false, did setBackgroundBitmap");
			}
			
		}finally{			
		}
	}
	--------------------End Added by Scott Neaves 2-28-2014, may be incorrect--------------------------*/
	
	
	
	// http://stackoverflow.com/questions/9564644/null-pointer-exception-while-loading-images-from-gallery
	protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
		super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
		Log.w("TPAD App", "In onActivityResult");
    	// Parse the activity result
		switch (requestCode) {
		case REQ_CODE_PICK_IMAGE:
			if (resultCode == RESULT_OK) {
				Log.w("TPAD App", "Result is OK");
				Uri bitmapUri = imageReturnedIntent.getData();
				String fileName = null;
				String scheme = bitmapUri.getScheme();
				if (scheme.equals("file")) {
				    fileName = bitmapUri.getLastPathSegment();
				}
				else if (scheme.equals("content")) {
				    String[] proj = { MediaStore.Images.Media.TITLE };
				    Cursor cursor = this.getContentResolver().query(bitmapUri, proj, null, null, null);
				    if (cursor != null && cursor.getCount() != 0) {
				        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE);
				        cursor.moveToFirst();
				        fileName = cursor.getString(columnIndex);
				    }
				}

				HapticCanvasActivity_Scott.myHapticView.writeToLog(timestamp() + dl + "BackgroundLoaded" + dl + fileName);
				HapticCanvasActivity_Scott.myHapticView.backgroundName = fileName;
				Log.w("TPAD App", "Set backgroundName to fileName");
				
				try {

					// set the display bitmap based on the bitmap we just got
					// back from our intent
					Bitmap b = Media.getBitmap(getContentResolver(), bitmapUri);
					
					if (hapticLoad){
						HapticCanvasActivity_Scott.myHapticView.setDrawBitmap(b);
					}else{
						HapticCanvasActivity_Scott.myHapticView.setBackgroundBitmap(b);
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				break;

			}
		}
	}

	@Override
	protected void onResume() {
		HapticCanvasActivity_Scott.myHapticView.setDrawing(true);
		super.onResume();
	}
	private String timestamp(){
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yy.MM.dd h:mm:ss:SSS a");
		String formattedDate = sdf.format(date);
		return formattedDate;
	}
}
