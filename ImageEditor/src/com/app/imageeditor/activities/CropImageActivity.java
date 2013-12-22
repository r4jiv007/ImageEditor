package com.app.imageeditor.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.imageeditor.R;
import com.app.imageeditor.app.ImageEditorApp;
import com.app.imageeditor.apputils.DpiConvertor;
import com.edmodo.cropper.CropImageView;

public class CropImageActivity extends Activity {

	// Static final constants
	private static final int DEFAULT_ASPECT_RATIO_VALUES = 10;
	private static final int ROTATE_NINETY_DEGREES = 90;
	private static final String ASPECT_RATIO_X = "ASPECT_RATIO_X";
	private static final String ASPECT_RATIO_Y = "ASPECT_RATIO_Y";
	private static final int ON_TOUCH = 1;

	// Instance variables
	private int mAspectRatioX = DEFAULT_ASPECT_RATIO_VALUES;
	private int mAspectRatioY = DEFAULT_ASPECT_RATIO_VALUES;

	// views:-

	private CropImageView cropImageView;
	private  ImageView croppedImageView;
	
	Bitmap croppedImage = null;

	// Saves the state upon rotating the screen/restarting the activity
	@Override
	protected void onSaveInstanceState(Bundle bundle) {
		super.onSaveInstanceState(bundle);
		bundle.putInt(ASPECT_RATIO_X, mAspectRatioX);
		bundle.putInt(ASPECT_RATIO_Y, mAspectRatioY);
	}

	// Restores the state upon rotating the screen/restarting the activity
	@Override
	protected void onRestoreInstanceState(Bundle bundle) {
		super.onRestoreInstanceState(bundle);
		mAspectRatioX = bundle.getInt(ASPECT_RATIO_X);
		mAspectRatioY = bundle.getInt(ASPECT_RATIO_Y);
	}

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.crop_activvity);

		// Sets fonts for all
		Typeface mFont = Typeface.createFromAsset(getAssets(),
				"Roboto-Thin.ttf");
		ViewGroup root = (ViewGroup) findViewById(R.id.mylayout);
		// setFont(root, mFont);

		// Initialize components of the app
		cropImageView = (CropImageView) findViewById(R.id.CropImageView);
		Spinner showGuidelinesSpin = (Spinner) findViewById(R.id.showGuidelinesSpin);
		final Button cropButton = (Button) findViewById(R.id.Button_crop);
		final Button submitButton = (Button)findViewById(R.id.submit_button);
		final Button resetButton =(Button)findViewById(R.id.cancel_button);
		croppedImageView = (ImageView) findViewById(R.id.croppedImageView);
		croppedImageView.setImageResource(R.drawable.ic_launcher);
		
		Log.i("Crop file", (ImageEditorApp.myBitmap == null) + " "
				+ ImageEditorApp.myBitmap.getWidth() + " "
				+ ImageEditorApp.myBitmap.getHeight());

		if (ImageEditorApp.myBitmap != null) {
			 cropImageView.setImageBitmap(ImageEditorApp.myBitmap);
		} else {
			DisplayMetrics displaymetrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
			int height = displaymetrics.heightPixels;
			int width = displaymetrics.widthPixels;

		}

			// Sets initial aspect ratio to 10/10, for demonstration purposes
		cropImageView.setAspectRatio(DEFAULT_ASPECT_RATIO_VALUES,
				DEFAULT_ASPECT_RATIO_VALUES);

		// Sets up the Spinner
		showGuidelinesSpin
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> adapterView,
							View view, int i, long l) {
						cropImageView.setGuidelines(i);
					}

					public void onNothingSelected(AdapterView<?> adapterView) {
						return;
					}
				});

		
		cropButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				croppedImage = cropImageView.getCroppedImage();
				
				Log.i("imageCroped",croppedImage.getWidth()+" "+croppedImage.getHeight());
			//	ImageView croppedImageView = (ImageView) findViewById(R.id.croppedImageView);
				croppedImageView.setMaxHeight(DpiConvertor.dpToPx(cropImageView.getHeight()/4, CropImageActivity.this));
				croppedImageView.setMaxWidth(DpiConvertor.dpToPx(croppedImage.getHeight()/4,CropImageActivity.this));
				croppedImageView.setMinimumHeight(DpiConvertor.dpToPx(cropImageView.getHeight()/4, CropImageActivity.this));
				croppedImageView.setMinimumWidth(DpiConvertor.dpToPx(croppedImage.getHeight()/4,CropImageActivity.this));
				Toast.makeText(getApplicationContext(), "croppedImage dim width = "+croppedImage.getWidth()+" height = "+croppedImage.getHeight(), 1000).show();
				croppedImageView.setImageBitmap(Bitmap.createScaledBitmap(croppedImage, croppedImage.getWidth()/4, croppedImage.getHeight()/4, true));
				ImageEditorApp.isImageCropped=true;
				ImageEditorApp.isImagedChanged=true;
				submitButton.setVisibility(View.VISIBLE);
				resetButton.setVisibility(View.VISIBLE);
				//showCroppedImageDialog(croppedImage);
			}
		});
		
		
		submitButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				ImageEditorApp.myBitmap=croppedImage;
				Intent in = new Intent(getApplicationContext(),ImageActivity.class);
				startActivity(in);
				CropImageActivity.this.finish();
				
				
			}
		});
		
		resetButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				croppedImageView.setImageResource(R.drawable.ic_launcher);
				croppedImage.recycle();
				croppedImage=null;
				ImageEditorApp.isImageCropped=false;
				ImageEditorApp.isImagedChanged=false;
				submitButton.setVisibility(View.GONE);
				resetButton.setVisibility(View.GONE);
			}
		});

	}
	
	
	private void showCroppedImageDialog(final Bitmap bitmap){
		  final Dialog dialog = new Dialog(this);
          // Include dialog.xml file
          dialog.setContentView(R.layout.cropped_dialog);
          // Set dialog title
          dialog.setTitle("Cropped Image");

                  
          Button resetButton = (Button) dialog.findViewById(R.id.declineButton);
          Button submitButton = (Button) dialog.findViewById(R.id.acceptButton);
          
          submitButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ImageEditorApp.myBitmap=bitmap;
				CropImageActivity.this.finish();
			}
		});
          // if decline button is clicked, close the custom dialog
          resetButton.setOnClickListener(new OnClickListener() {
              @Override
              public void onClick(View v) {
                  // Close dialog
                  dialog.dismiss();
                  bitmap.recycle();
              }
          });
          
          dialog.show();


	}

	/*
	 * Sets the font on all TextViews in the ViewGroup. Searches recursively for
	 * all inner ViewGroups as well. Just add a check for any other views you
	 * want to set as well (EditText, etc.)
	 */
	public void setFont(ViewGroup group, Typeface font) {
		int count = group.getChildCount();
		View v;
		for (int i = 0; i < count; i++) {
			v = group.getChildAt(i);
			if (v instanceof TextView || v instanceof EditText
					|| v instanceof Button) {
				((TextView) v).setTypeface(font);
			} else if (v instanceof ViewGroup)
				setFont((ViewGroup) v, font);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
