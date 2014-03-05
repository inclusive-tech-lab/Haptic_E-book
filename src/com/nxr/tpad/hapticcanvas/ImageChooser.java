package com.nxr.tpad.hapticcanvas;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


public class ImageChooser extends Activity {

    // this is the action code we use in our intent, 
    // this way we know we're looking at the response from our own action
    private static final int SELECT_PICTURE = 1;
    private String new_image = "new_image";
    private String book_id;
    private int new_page_counter = 0;
    private int old_page_counter = 0;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_chooser);
        
        book_id = this.getIntent().getExtras().getString("book_id");
        Log.w("TPAD App", "Retrieved clicked button's tag as the book_id: " + book_id);
        
        //If the book is an existing book, load up all its pages as ImageButtons
        if(!book_id.equals("new")){
        	String str_bookTitle = "error in finding book title";
        	try{
	            File book_file = new File(this.getFilesDir(), book_id + ".stn");
	            BufferedReader br = new BufferedReader(new FileReader(book_file));
	            str_bookTitle = br.readLine();
	            String line;
	            ArrayList<String> page_arr = new ArrayList<String>();
	            while ((line = br.readLine()) != null) {
			    	page_arr.add(line);
			    	LinearLayout l_layout = (LinearLayout) findViewById(R.id.linearLayout1);
			    	ImageButton page = new ImageButton(this);
			    	File page_file = new File(this.getFilesDir(), line);
			    	Log.w("TPAD App", "opened new file on page from book_file");
			    	Bitmap bmp = BitmapFactory.decodeFile(page_file.getAbsolutePath());
			    	Log.w("TPAD App", "decoded pagefile to bitmap");
			    	page.setImageBitmap(bmp);
			    	Log.w("TPAD App", "set page button to image bitmap");
			    	page.setTag(line);
			    	page.setOnClickListener(new OnClickListener() {
			            public void onClick(View arg0) {
			            	openImageEditor(arg0);
			            }});
			    	l_layout.addView(page);
	            }
	            old_page_counter = page_arr.size();
	            new_page_counter = old_page_counter;
	            Log.w("TPAD App", "ArrayList of page URIs: " + page_arr.toString());
	            br.close();
        	}catch(IOException io){
        	}
            EditText bookTitle = (EditText)findViewById(R.id.bookTitle);
            bookTitle.setText(str_bookTitle);
        }	


        ((Button)findViewById(R.id.Button01))
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View arg0) {

                        // in onCreate or any event where your want the user to
                        // select a file
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent,
                                "Select Picture"), SELECT_PICTURE);
                    }
                });
        
        Button btn = (Button) findViewById(R.id.Button02);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	saveBook(v);
            	Toast.makeText(getApplicationContext(), "Saved Book!", Toast.LENGTH_SHORT).show();
             }
         });
        
    }

    public final class SessionIdentifierGenerator {
    	  private SecureRandom random = new SecureRandom();

    	  public String nextSessionId() {
    	    return new BigInteger(130, random).toString(32);
    	  }
    	}
    

	public void openImageEditor(View view) {
	    // Do something in response to button
		Intent intent = new Intent(this, HapticCanvasActivity.class);
		Log.w("TPAD App", "intent extra from ImageChooser to HapticCanvasActivity is: " + (view).getTag().toString());
		intent.putExtra("page_fileName", (view).getTag().toString());
		startActivity(intent);
	}
    
	public void done(View view){
		Intent intent = new Intent(this, LandingScreen.class);
		startActivity(intent);
	}
	
    public void saveBook(View view){	
    	try{
		/*
		 * Get title of book from book title editText (may be different from
		 * original title loaded from file)
		 */
		EditText book_title = (EditText) findViewById(R.id.bookTitle);
		String Book_Title = book_title.getText().toString();
    	
    	/*If this is a new book, create a new book file, and save it to the Library*/
    	File sv_book_file = new File("this is here just so that sv_book_file will be initialized");
    	if(book_id.equals("new")){
    		SessionIdentifierGenerator rnd = new SessionIdentifierGenerator();
    		String new_book_id = (String)rnd.nextSessionId();
    		book_id = new_book_id;
    		sv_book_file = new File(this.getFilesDir(), new_book_id + ".stn");
    		
    		/* Write title to new book file*/
    		PrintWriter new_bookFile = new PrintWriter(new BufferedWriter(new FileWriter(sv_book_file, true))); //opens file
    		Log.w("TPAD App", "opened PrintWriter on new book file");
    		new_bookFile.println(Book_Title);
    		new_bookFile.close();
    		Log.w("TPAD App", "finished writing name of book to new book file");
    		
    		/* Save new book to the library */
    		File library_file = new File(view.getContext().getFilesDir(),"library.stn");
    		Log.w("TPAD App", "instantiated new library_file object");
    		if (library_file.isFile()!=true){
    			library_file.createNewFile();
    			Log.w("TPAD App", "created new library file");
    		}
    		PrintWriter library_os = new PrintWriter(new BufferedWriter(new FileWriter(library_file, true))); //opens file
    		Log.w("TPAD App", "opened PrintWriter on library_file");
    		library_os.println(Book_Title + "," + new_book_id); //writes "book_title, new_book_id" to library file.
    		library_os.close();
    		Log.w("TPAD App", "finished adding new book to library file");
    	}else{ //book_id does not equal "new"
    		sv_book_file = new File(this.getFilesDir(), book_id + ".stn");
    	}

		/* Find all pictures in activity */
		ArrayList<ImageView> image_views = new ArrayList<ImageView>();
		image_views = getViewsByTag((ViewGroup)view.getRootView(), new_image);
		Log.w("TPAD App", "got views by tag");

		for (ImageView image : image_views) {
			/* Convert each picture to bitmap */
			Drawable drawable = image.getDrawable();
			Log.w("TPAD App", "converted image to drawable");
			Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
			Log.w("TPAD App", "converted drawable to bitmap");
			String img_fileName = book_id + new_page_counter;
			File image_file = new File(view.getContext().getFilesDir(),img_fileName);
			new_page_counter += 1;
    		if (image_file.isFile()!=true){
    			Log.w("TPAD App", "image_file did not exist");
    			image_file.createNewFile();
    			Log.w("TPAD App", "Created new image_file");
    		}
			Log.w("TPAD App", "Made it past creating new file");
			try {
				FileOutputStream outStream = new FileOutputStream(image_file);
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
				outStream.flush();
				outStream.close();
				Log.w("TPAD App", "saved file");
			} catch (Exception e) {
				e.printStackTrace();
			}
			/* Write new page to book_file*/
    		PrintWriter book_file_os = new PrintWriter(new BufferedWriter(new FileWriter(sv_book_file, true))); 
    		Log.w("TPAD App", "opened PrintWriter on book_file");
    		book_file_os.println(img_fileName); 
    		book_file_os.close();
    		Log.w("TPAD App", "finished adding new page to book file");
		}
		
		} catch (FileNotFoundException e) { 
			Log.w("TPAD App","FileNotFoundException"); 
			e.printStackTrace(); 
		} catch (IOException e) { 
			Log.w("TPAD App", "IOException");
			e.printStackTrace();
		}
		 
    	}

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                //selectedImagePath = getPath(selectedImageUri);
                ImageView img = new ImageView(this);
                LinearLayout selectedPics_LL = (LinearLayout) findViewById(R.id.linearLayout1);
                selectedPics_LL.addView(img);
                img.setAdjustViewBounds(true);
                img.setMaxHeight(400);
                img.setMaxWidth(400);
                img.setPadding(10, 10, 10, 10);
                img.setImageURI(selectedImageUri);
                img.setTag(new_image);
                //picture_id_count += 1;
            }
        }
    }
    
    private static ArrayList<ImageView> getViewsByTag(ViewGroup root, String tag){
        ArrayList<ImageView> views = new ArrayList<ImageView>();
        final int childCount = root.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = root.getChildAt(i);
            if (child instanceof ViewGroup) {
                views.addAll(getViewsByTag((ViewGroup) child, tag));
            } 

            final Object tagObj = child.getTag();
            if (tagObj != null && tagObj.equals(tag)) {
                views.add((ImageView)child);
            }

        }
        return views;
    }
}