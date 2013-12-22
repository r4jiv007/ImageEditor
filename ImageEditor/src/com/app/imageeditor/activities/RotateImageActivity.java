package com.app.imageeditor.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.app.imageeditor.R;
import com.app.imageeditor.app.ImageEditorApp;
import com.app.imageeditor.apputils.BitmapHelper;
import com.app.imageeditor.views.TouchImageView;
import com.edmodo.cropper.CropImageView;

public class RotateImageActivity extends Activity {

	private CropImageView myImageView;
	private Button myButton,mSubmit,mReset;
	private static final int ROTATE_COUNTER_CLOCKWISE = -90;
	private int mRotation;
	
	int height ;
	int width ;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.rotate_activity_layout);

		initView();
		setListener();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		 height = displaymetrics.heightPixels;
		 width = displaymetrics.widthPixels;

		/*
		 * String imageUri = "drawable://" + R.drawable.img;
		 * imageLoader.displayImage(imageUri, mImageView);
		 */

		Toast.makeText(this, "width = " + width + " height = " + height, 2000)
				.show();
		if(ImageEditorApp.myBitmap==null){
			ImageEditorApp.myBitmap = BitmapHelper.decodeSampledBitmapFromResource(getResources(), 
					R.drawable.img,
					 height, width);
				
		}
			myImageView.setImageBitmap(ImageEditorApp.myBitmap);
			myImageView.hideCropOverlayView();

		
		Log.i("Bitmap file", (ImageEditorApp.myBitmap == null) + " "
				+ ImageEditorApp.myBitmap.getWidth() + " "
				+ ImageEditorApp.myBitmap.getHeight());



	}

	private void initView() {

		myImageView = (CropImageView) findViewById(R.id.rotateImageView);
		
		myButton = (Button) findViewById(R.id.rotate_btn);
		mSubmit=(Button)findViewById(R.id.submit_button_rotate);
		mReset=(Button)findViewById(R.id.cancel_button_rotate);

	}

	private void setListener() {
		myButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				/*ImageEditorApp.myBitmap=BitmapHelper.rotate(ImageEditorApp.myBitmap, mRotation);
				myImageView.setImageBitmap(ImageEditorApp.myBitmap);
*/				
				myImageView.rotateImage(ROTATE_COUNTER_CLOCKWISE);
				int height =myImageView.getBitmap().getHeight();
				int width = myImageView.getBitmap().getWidth();

				ImageEditorApp.isImagedChanged=true;
				Toast.makeText(getApplicationContext(), width+"  "+height, 1000).show();
				mSubmit.setVisibility(View.VISIBLE);
				mReset.setVisibility(View.VISIBLE);
			}
		});
		
		
		mSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			
				ImageEditorApp.myBitmap=myImageView.getBitmap();
				Toast.makeText(getApplicationContext(), ImageEditorApp.myBitmap.getWidth()+"  "+ImageEditorApp.myBitmap.getHeight(), 1000).show();
				
				Intent intent = new Intent(RotateImageActivity.this,ImageActivity.class);
				startActivity(intent);
			}
		});
		
		mReset.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new ResetTask().execute();
			}
		});
	}
	
	
	private class ResetTask extends AsyncTask<Void,Void,Void>{
		private ProgressDialog pd;
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = ProgressDialog.show(RotateImageActivity.this, "ImageEditor",
					"applying changes");
			pd.setCancelable(false);
			pd.show();
		
		}
		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			
			ImageEditorApp.myBitmap.recycle();
			ImageEditorApp.myBitmap = BitmapHelper.decodeSampledBitmapFromResource(getResources(), 
					R.drawable.img,
					 height, width);

			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pd.dismiss();
			
			if(myImageView!=null){
				myImageView.setImageBitmap(ImageEditorApp.myBitmap);
			}
			mSubmit.setVisibility(View.GONE);
			mReset.setVisibility(View.GONE);
			ImageEditorApp.isImagedChanged=false;	
		
		}
	}

}
