package com.app.imageeditor.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.imageeditor.R;
import com.app.imageeditor.app.ImageEditorApp;

public class MainActivity extends Activity {

	EditText mEditext;
	TextView mAbsPath;
	Button mButton;
	String file_path;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mEditext = (EditText) findViewById(R.id.img_path);
		mButton = (Button) findViewById(R.id.submit);
		mAbsPath=(TextView)findViewById(R.id.savedImagePath);
		
		setListener();
		
	} 
	
	

	private void setListener() {

		mButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (!mEditext.getText().toString().equalsIgnoreCase("")) {
					Intent imageIntent = new Intent(getApplicationContext(),
							ImageActivity.class);
					ImageEditorApp.originalImagePath=mEditext.getText().toString();
					startActivity(imageIntent);

				}else{
					Toast.makeText(getApplicationContext(), "Please provide Image Path", 2000).show();
				}
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub

		if (ImageEditorApp.absoluteFilePath != null) {
			mAbsPath.setText(ImageEditorApp.absoluteFilePath);
		}
		
		/*file_path=(String)getIntent().getStringExtra("path");
		if(file_path!=null){
			mAbsPath.setText(file_path);
		}
		*/super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}

}
