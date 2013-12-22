package com.app.imageeditor.activities;

import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.ColorFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

import com.app.imageeditor.R;
import com.app.imageeditor.app.ImageEditorApp;
import com.app.imageeditor.apputils.BitmapHelper;
import com.app.imageeditor.views.TouchImageView;
import com.app.imageeditor.views.VerticalSeekBar;

public class ImageActivity extends Activity {

	private TouchImageView mImageView;
	private Button changeBrightness, mSave, mReset;
	private static float value;
	private LinearLayout mBrightnessSeekBarHolder, mContrastSeekBarHolder,
			mControlHolder;

	private float mBrightnessValue = 0, mContrastValue = 0, mValue = 0;
	private VerticalSeekBar mBrightnessSeekBar, mContrastSeekBar;
	private boolean isBrightnessChanged = false, isContrastChanged = false;

	private boolean isChangeApplied = true;
	private int mAction;

	private int height, width;

	private ColorFilter mDefaultColoFilter;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_activity);
		// setImageLoader();x
		mImageView = (TouchImageView) findViewById(R.id.xoom_view);
		// mBrightnessBar=(SeekBar)findViewById(R.id.brightness_Seekbar);
		/*
		 * changeBrightness = (Button) findViewById(R.id.changebrightness);
		 */mBrightnessSeekBar = (VerticalSeekBar) findViewById(R.id.brightness_Seekbar);
		mBrightnessSeekBarHolder = (LinearLayout) findViewById(R.id.brightness_seekbar_holder);
		mSave = (Button) findViewById(R.id.save);
		mReset = (Button) findViewById(R.id.reset);
		mContrastSeekBar = (VerticalSeekBar) findViewById(R.id.contrast_Seekbar);
		mContrastSeekBarHolder = (LinearLayout) findViewById(R.id.contrast_seekbar_holder);
		mBrightnessSeekBar.setMax(255);

		mControlHolder = (LinearLayout) findViewById(R.id.controls);

		mBrightnessSeekBar.setProgress(128);
		mContrastSeekBar.setMax(255);
		mContrastSeekBar.setProgress(128);
		mDefaultColoFilter = mImageView.getColorFilter();
		/*
		 * Display display = getWindowManager().getDefaultDisplay(); Point size
		 * = new Point(); display.getSize(size); int width = size.x; int height
		 * = size.y;
		 */
		setListener();

		// new BitmapEditor(this).execute();
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
		if (ImageEditorApp.myBitmap == null) {
			ImageEditorApp.myBitmap = BitmapHelper.decodeSampledBitmapFromResource(getResources(), 
					R.drawable.img,
					 height, width);

		}
		mImageView.setImageBitmap(ImageEditorApp.myBitmap);

		Log.i("Bitmap file", (ImageEditorApp.myBitmap == null) + " "
				+ ImageEditorApp.myBitmap.getWidth() + " "
				+ ImageEditorApp.myBitmap.getHeight());

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menus, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_brightness:
			showBrightnessController();
			return true;
		case R.id.action_contrast:
			showContrastController();
			return true;
		case R.id.action_crop:
			launchCropingActivity();
			return true;
		case R.id.action_rotate:
			launchRotateActivity();
			return true;
		case R.id.action_resetAll:
			new ResetTask().execute();
			return true;
		case R.id.action_back:
			showSavingDialog();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void launchCropingActivity() {
		Intent intent = new Intent(this, CropImageActivity.class);
		startActivity(intent);
	}

	private void launchRotateActivity() {
		Intent intent = new Intent(this, RotateImageActivity.class);
		startActivity(intent);
	}

	private void showBrightnessController() {
		if (mBrightnessSeekBarHolder.getVisibility() == View.GONE) {

			if (!isChangeApplied) {
				showConfirmDialog();
				isChangeApplied = true;
			}

			mBrightnessSeekBarHolder.setVisibility(View.VISIBLE);
			mContrastSeekBarHolder.setVisibility(View.GONE);
			mControlHolder.setVisibility(View.VISIBLE);
			mAction = 1;

		} else {
			// mControlHolder.setVisibility(View.GONE);
			isChangeApplied = true;

			resetSeekBars();
			// mBrightnessSeekBarHolder.setVisibility(View.GONE);

		}

	}

	private void showContrastController() {

		if (mContrastSeekBarHolder.getVisibility() == View.GONE) {

			if (!isChangeApplied) {
				showConfirmDialog();
				isChangeApplied = true;
			}

			mContrastSeekBarHolder.setVisibility(View.VISIBLE);
			mBrightnessSeekBarHolder.setVisibility(View.GONE);
			mControlHolder.setVisibility(View.VISIBLE);
			mAction = 2;

			/*
			 * Bitmap myBitmap = BitmapHelper .brightness(mBitmap,
			 * mBrightnessValue); mBitmap.recycle(); mBitmap = myBitmap;
			 * mImageView.setImageBitmap(myBitmap);
			 * mImageView.setColorFilter(BitmapHelper.setBrightness(0));
			 */
		} else {
			// mControlHolder.setVisibility(View.GONE);
			isChangeApplied = true;

			resetSeekBars();
			// mContrastSeekBarHolder.setVisibility(View.GONE);

		}

	}

	private void setListener() {
		mContrastSeekBar
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						// TODO Auto-generated method stub

						isContrastChanged = true;
						isChangeApplied = false;
						// mContrastValue = (300f * progress / 255f) - 255f;
						// mContrastValue=progress;
						mContrastValue = getScaledSeekBarValue(progress);
						mValue = mContrastValue;
						if (mContrastValue == 0) {
							isContrastChanged = false;
						}

						mImageView.setColorFilter(BitmapHelper
								.setContrast(mContrastValue));

					}
				});
		mBrightnessSeekBar
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						// TODO Auto-generated method stub

						isBrightnessChanged = true;
						isChangeApplied = false;
						// mBrightnessValue = (300f * progress / 255f) - 255f;
						mBrightnessValue = getScaledSeekBarValue(progress);
						mValue = mBrightnessValue;
						if (mBrightnessValue == 0) {
							isBrightnessChanged = false;
						}
						// mImageView.setColorFilter(BitmapHelper.setBrightness(mBrightnessValue));
						/*
						 * mImageView.setColorFilter(BitmapHelper
						 * .setBrightnessNContrast(mContrastValue,
						 * mBrightnessValue, isBrightnessChanged,
						 * isContrastChanged));
						 */
						mImageView.setColorFilter(BitmapHelper
								.setBrightness(mBrightnessValue));
					}
				});

		/*
		 * mImageView.setOnTouchListener(new OnTouchListener() {
		 * 
		 * @Override public boolean onTouch(View v, MotionEvent event) { // TODO
		 * Auto-generated method stub
		 * 
		 * mBrightnessSeekBarHolder.setVisibility(View.GONE);
		 * mContrastSeekBarHolder.setVisibility(View.GONE); return false; } });
		 */
		mSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				new BitmapEditor(getApplicationContext(), mValue, mAction)
						.execute(); //

				isChangeApplied = true;
				// resetImageView();

				mControlHolder.setVisibility(View.GONE);
				mBrightnessSeekBarHolder.setVisibility(View.GONE);
				mContrastSeekBarHolder.setVisibility(View.GONE);

				Toast.makeText(
						getApplicationContext(),
						"brightness = " + mBrightnessValue + " contrast = "
								+ mContrastValue, 1000).show();
			}
		});

		mReset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new ResetTask().execute();
				ImageEditorApp.isImagedChanged = false;
			}
		});
	}

	class BitmapEditor extends AsyncTask<Void, Void, Void> {

		private Context mContext;
		private float MyValue;
		private ProgressDialog pd;
		private Bitmap bitmapImage = null;

		private int action_flag;

		private float rotation_value;

		public BitmapEditor(Context context, float value, int action) {
			mContext = context;
			MyValue = value;
			action_flag = action;
		}

		public BitmapEditor(Context context, float rotation) {
			mContext = context;
			rotation_value = rotation;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			pd = ProgressDialog.show(ImageActivity.this, "ImageEditor",
					"applying changes");
			pd.setCancelable(false);
			pd.show();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub

			Options opts = new Options();
			opts.inJustDecodeBounds = true;

			BitmapFactory.decodeResource(getResources(), R.drawable.img, opts);
			int imageHeight = opts.outHeight;
			int imageWidth = opts.outWidth;

			if (!ImageEditorApp.isImageCropped
					|| (ImageEditorApp.myBitmap.getHeight()
							+ ImageEditorApp.myBitmap.getWidth() < imageHeight
							+ imageWidth)) {
				opts.inJustDecodeBounds = false;
				opts.inDither = true;
				opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
				opts.inScaled = false; /* Flag for no scalling */

				opts.inMutable = true;
				ImageEditorApp.myBitmap.recycle();
				// if image is downsampled then load real image
				bitmapImage = BitmapFactory.decodeResource(
						mContext.getResources(), R.drawable.img, opts);
				ImageEditorApp.myBitmap = bitmapImage;

			} else {
				// else if image was not downsampled then use same bitmap
				bitmapImage = ImageEditorApp.myBitmap;
				ImageEditorApp.isImageCropped = false;
			}

			switch (action_flag) {
			case 1:
				bitmapImage = BitmapHelper.changeBrightnessPixelByPixel(
						bitmapImage, mBrightnessValue);
				ImageEditorApp.isImagedChanged = true;

				break;
			case 2:
				bitmapImage = BitmapHelper.changeContrastPixelByPixel(
						bitmapImage, mContrastValue);
				ImageEditorApp.isImagedChanged = true;

				break;

			}
			// myBitmap=BrightnessController.doBrightness(mBitmap, value);

			/* Load the bitmap with the options */
			// BitmapHelper.saveBitmapToCard(getApplicationContext(),BitmapHelper.brightness(bitmapImage,
			// mBrightnessValue), "test.png");
			// BitmapHelper.saveBitmapToCard(getApplicationContext(),BitmapHelper.changeContrastPixelByPixel(BitmapHelper.changeBrightnessPixelByPixel(bitmapImage,
			// mBrightnessValue),mContrastValue), "test.jpg");

			Log.i("originalImage", "width " + bitmapImage.getWidth() + "  "
					+ bitmapImage.getHeight());

			Log.i("LoadingBitmap ", "after loading bitmap");

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			pd.dismiss();
			super.onPostExecute(result);
			ImageEditorApp.myBitmap = bitmapImage;

			if (mImageView != null) {
				mImageView.setColorFilter(mDefaultColoFilter);
				mImageView.setImageBitmap(ImageEditorApp.myBitmap);
			}

			// ` mImageView.setImageBitmap(myBitmap);
		}

	}

	private float getScaledSeekBarValue(float progress) {

		float value = 0;

		if (progress < 1) {
			return -127;
		} else if (progress == 128) {
			return 0;
		} else {
			value = progress - 128;
		}

		return value;
	}

	private void resetImageView() {

		ImageEditorApp.myBitmap.recycle();

		ImageEditorApp.myBitmap = BitmapHelper.decodeSampledBitmapFromResource(getResources(), 
				R.drawable.img,
				 height, width);

	}

	private class ResetTask extends AsyncTask<Void, Void, Void> {

		private ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = ProgressDialog.show(ImageActivity.this, "ImageEditor",
					"applying changes");
			pd.setCancelable(false);
			pd.show();

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub

			resetImageView();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			pd.dismiss();

			if (mImageView != null) {
				mImageView.setImageBitmap(ImageEditorApp.myBitmap);
			}
			ImageEditorApp.isImagedChanged = false;
			resetSeekBars();

		}

	}

	private void showConfirmDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// Add the buttons
		builder.setMessage("Would you like to apply changes ?");

		builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// User clicked OK button
				new BitmapEditor(getApplicationContext(), mValue, mAction)
						.execute();

			}
		});
		builder.setNegativeButton("cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// User cancelled the dialog
						resetSeekBars();
						ImageEditorApp.isImagedChanged = false;
					}
				});

		AlertDialog confirmDialog = builder.create();
		confirmDialog.show();
	}

	private void showSavingDialog() {
		if (ImageEditorApp.isImagedChanged) {

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			// Add the buttons
			builder.setMessage("Would you like to apply changes ?");

			builder.setPositiveButton("Save",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							// User clicked OK button
							new ImageSaverTask().execute();
						}
					});
			builder.setNegativeButton("Discard",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							// User cancelled the dialog
							ImageActivity.super.onBackPressed();
						}
					});

			AlertDialog confirmDialog = builder.create();
			confirmDialog.show();

		} else {
			super.onBackPressed();
		}
	}

	private void resetSeekBars() {

		mImageView.setColorFilter(mDefaultColoFilter);

		mBrightnessSeekBar.setProgress(128);
		mContrastSeekBar.setProgress(128);
		// mBrightnessSeekBar.invalidate();
		// mContrastSeekBar.invalidate();

		/*
		 * mSave.setVisibility(View.GONE); mReset.setVisibility(View.GONE);
		 */

		mControlHolder.setVisibility(View.GONE);
		mBrightnessSeekBarHolder.setVisibility(View.GONE);
		mContrastSeekBarHolder.setVisibility(View.GONE);

		isBrightnessChanged = false;
		isContrastChanged = false;
		isChangeApplied = true;
	}

	private class ImageSaverTask extends AsyncTask<Void, Void, Void> {

		private ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = ProgressDialog.show(ImageActivity.this, "ImageEditor",
					"applying changes");
			pd.setCancelable(false);
			pd.show();

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			long time = new Date().getTime();
			String filename = time + ".jpg";
			BitmapHelper.saveBitmapToCard(getApplicationContext(),
					ImageEditorApp.myBitmap, filename);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			pd.dismiss();

			returnResult(ImageEditorApp.absoluteFilePath);

			Toast.makeText(getApplicationContext(), "Image Saved", 2000).show();
		}
	}

	private void returnResult(String path) {
		Intent resultIntent = new Intent(this, MainActivity.class);
		resultIntent.putExtra("path", path);
		// setResult(Activity.RESULT_OK, resultIntent);
		startActivity(resultIntent);
		// Toast.makeText(this, selectedPaths.size() + "", 2000).show();
		// finish();
	}

	/*
	 * private void decodeBitmapsInBlocks(Bitmap bitmap,float value){ int height
	 * = bitmap.get }
	 */
}
