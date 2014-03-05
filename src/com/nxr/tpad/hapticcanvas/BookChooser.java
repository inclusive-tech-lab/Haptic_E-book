package com.nxr.tpad.hapticcanvas;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;


public class BookChooser extends Activity {
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.bookchooserscreen);
	    
        
	    ImageButton new_book_button = (ImageButton)findViewById(R.id.imgbtn_NewBook);
	    new_book_button.setTag("new");
	    Log.w("TPAD App", "Set new book button's tag to: " + new_book_button.getTag().toString());
	    
	    /* Find books in library.stn file */
	    File library_file = new File(this.getFilesDir(),"library.stn");
	    try{
	    	BufferedReader br = new BufferedReader(new FileReader(library_file));
		    String line;
		    String[] book;
		    while ((line = br.readLine()) != null) {
		    	book = line.split(",");
		    	   // for each book, create a new Button containing the name of the book, which has a tag containing the id of the book.
		    	LinearLayout l_layout = (LinearLayout) findViewById(R.id.LL_books); 
		    	Button new_book = new Button(this);
		    	new_book.setText(book[0]);
		    	new_book.setTag(book[1]);
		    	new_book.setWidth(200);
		    	new_book.setHeight(200);
		    	new_book.setGravity(Gravity.CENTER);
		    	new_book.setOnClickListener(new OnClickListener() {
		            public void onClick(View arg0) {
		            	openImageChooser(arg0);
		            }});
		    	l_layout.addView(new_book);
		    	}
		    br.close();
	    }catch(FileNotFoundException fne){
	    }catch(IOException io){
	    }   
	}

	/** Called when the user clicks the Send button */
	public void openImageChooser(View view) {
	    // Do something in response to button
		Intent intent = new Intent(this, ImageChooser.class);
		intent.putExtra("book_id", (view).getTag().toString());
		startActivity(intent);
	}
	
}
