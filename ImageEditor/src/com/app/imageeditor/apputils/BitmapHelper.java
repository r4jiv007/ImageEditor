package com.app.imageeditor.apputils;

import java.io.File;
import java.io.FileOutputStream;

import com.app.imageeditor.app.ImageEditorApp;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class BitmapHelper {

	public static Bitmap brightness(Bitmap original, float brightness) {
		Bitmap newBitmap = Bitmap.createBitmap(original.getWidth(),original.getHeight(), original.getConfig());
		int width = original.getWidth();
		int height = original.getHeight();
		
		int[] argb = new int[width * height];
		original.getPixels(argb, 0, width, 0, 0,width, height);
		//original.recycle();
		for (int i = argb.length - 1; i >= 0; --i) {
			int alpha = argb[i] >> 24;
			int red = (argb[i] >> 16) & 0xFF;
			int green = (argb[i] >> 8) & 0xFF;
			int blue = argb[i] & 0xFF;
			int red2 = (int)(red + brightness);
			if (red2 > 0xFF)
				red2 = 0xFF;
			if (red2 < 0)
				red2 = 0;
			int green2 = (int)(green + brightness);
			if (green2 > 0xFF)
				green2 = 0xFF;
			if (green2 < 0)
				green2 = 0;
			int blue2 = (int)(blue + brightness);
			if (blue2 > 0xFF)
				blue2 = 0xFF;
			if (blue2 < 0)
				blue2 = 0;
			int composite = (alpha << 24) | (red2 << 16) | (green2 << 8)
					| blue2;
			argb[i] = composite;
		}
		newBitmap.setPixels(argb, 0, width, 0, 0, width,height);
		return newBitmap;
	}

	public static Bitmap contrast(Bitmap original, float value) {
		value = (100.0f + value) / 100.0f;
		value *= value;
		Bitmap newBitmap = Bitmap.createBitmap(original.getWidth(),
				original.getHeight(), original.getConfig());
		int[] argb = new int[original.getWidth() * original.getHeight()];
		original.getPixels(argb, 0, original.getWidth(), 0, 0,
				original.getWidth(), original.getHeight());
		for (int i = argb.length - 1; i >= 0; --i) {
			int alpha = argb[i] >> 24;
			int _red = (argb[i] >> 16) & 0xFF;
			int _green = (argb[i] >> 8) & 0xFF;
			int _blue = argb[i] & 0xFF;
			float red = _red / 255.0f;
			float green = _green / 255.0f;
			float blue = _blue / 255.0f;
			red = (((red - 0.5f) * value) + 0.5f) * 255.0f;
			green = (((green - 0.5f) * value) + 0.5f) * 255.0f;
			blue = (((blue - 0.5f) * value) + 0.5f) * 255.0f;
			int iR = (int) red;
			iR = iR > 255 ? 255 : iR;
			iR = iR < 0 ? 0 : iR;
			int iG = (int) green;
			iG = iG > 255 ? 255 : iG;
			iG = iG < 0 ? 0 : iG;
			int iB = (int) blue;
			iB = iB > 255 ? 255 : iB;
			iB = iB < 0 ? 0 : iB;
			int composite = (alpha << 24) | (iR << 16) | (iG << 8) | iB;
			argb[i] = composite;
		}
		newBitmap.setPixels(argb, 0, original.getWidth(), 0, 0,
				original.getWidth(), original.getHeight());
		return newBitmap;
	}
	
	
	

	public static ColorMatrixColorFilter setBrightness(float value1){
		float[] colorMatrix = { 
				1, 0, 0, 0, value1, // red
				0, 1, 0, 0, value1, // green
				0, 0, 1, 0, value1, // blue
				0, 0, 0, 1, 0 // alpha
		};
		ColorMatrix matrix = new ColorMatrix(colorMatrix);
		ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
		return filter;


	}

	public static ColorMatrixColorFilter setContrast(float contrast) {
		// (100.0f + contrast) / 100.0f;
		float scale = (100.0f + contrast) / 100.0f;// contrast /*+ 1.f*/;
		scale *= scale;
		float translate = (-.5f * scale + .5f) * 255.f;
		float[] array = new float[] { 
				scale, 0, 0, 0, translate,
				0, scale, 0, 0, translate,
				0, 0, scale, 0, translate, 
				0, 0, 0, 1, 0 };
		ColorMatrix matrix = new ColorMatrix(array);
		ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
		return filter;
	}

	public static ColorMatrixColorFilter setBrightnessNContrast(float contrast,float brightness,boolean brighten,boolean conts){
		
		float scale = (100.0f + contrast) / 100.0f;// contrast /*+ 1.f*/;
		scale *= scale;
		ColorMatrixColorFilter filter ;
		float myScale =0;
		float translate = (-.5f * scale + .5f) * 255.f;
		
		float [] default_array= new float[]{
				1, 0, 0, 0, 0, // red
				0, 1, 0, 0, 0, // green
				0, 0, 1, 0, 0, // blue
				0, 0, 0, 1, 0 // alpha
	
		};
		
		float[] Contrast_Array = new float[] { 
				scale, 0, 0, 0, translate,
				0, scale, 0, 0, translate,
				0, 0, scale, 0, translate, 
				0, 0, 0, 1, 0 };
		
		float[] Brightness_Array = { 
				1, 0, 0, 0, brightness, // red
				0, 1, 0, 0, brightness, // green
				0, 0, 1, 0, brightness, // blue
				0, 0, 0, 1, 0 // alpha
		};
		int size = Brightness_Array.length;
	/*	float [] composite_matrix= new float[size];
		
		for(int i=0;i<size;i++){
			composite_matrix[i]=Contrast_Array[i]+Brightness_Array[i];
		}
	*/	
		float [] composite_matrix = new float[]{
				scale, 0, 0, 0, translate+brightness,
				0, scale, 0, 0, translate+brightness,
				0, 0, scale, 0, translate+brightness, 
				0, 0, 0, 1, 0
		};
		
		if(brighten && conts){
			filter = new ColorMatrixColorFilter(composite_matrix);
				
		}else if(conts && !brighten){
			filter = new ColorMatrixColorFilter(Contrast_Array);
			
		}else if(!conts && brighten){
			filter = new ColorMatrixColorFilter(Brightness_Array);
			
		}else{
			filter=new ColorMatrixColorFilter(default_array);
		}
		return filter;
		
	}
	
	/*public static int calculateInSampleSize(BitmapFactory.Options options,
			int screenWidth, int screenHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int reqHeight = 0;
		int reqWidth = 0;

		// for tackling orientation change
		if (screenWidth > screenHeight) {
			reqHeight = screenWidth;
			reqWidth = screenHeight;

		} else {
			reqHeight = screenHeight;
			reqWidth = screenWidth;
		}
		
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

	public static Bitmap decodeSampledBitmapFromPath(String pathName, int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(pathName,  options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		options.inMutable=true;
		//return Bitmap.createScaledBitmap(src, dstWidth, dstHeight, filter)
		return BitmapFactory.decodeFile(pathName,options);
	}

*/
	

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int screenWidth, int screenHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int reqHeight = 0;
		int reqWidth = 0;

		// for tackling orientation change
		if (screenWidth > screenHeight) {
			reqHeight = screenWidth;
			reqWidth = screenHeight;

		} else {
			reqHeight = screenHeight;
			reqWidth = screenWidth;
		}
		
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

	public static Bitmap decodeSampledBitmapFromResource(Resources res,
			int resId, int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		//return Bitmap.createScaledBitmap(src, dstWidth, dstHeight, filter)
		return BitmapFactory.decodeResource(res, resId, options);
	}

	
	
	
	
	
	public static void saveBitmapToCard(Context context,Bitmap myBitmap,String fileName){
		
		Log.i("saving file",(myBitmap==null)+" "+myBitmap.getWidth()+" "+myBitmap.getHeight());
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
		{
		   // sd card mounted
			File direct = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ "/ImageEditor");

			if(!direct.exists())
			{
			    if(direct.mkdir()) 
			      {
			    	try {
			    		   File f = new File( direct + File.separator + fileName);
			    		   ImageEditorApp.absoluteFilePath=f.getAbsolutePath();
			    		   f.createNewFile();
			    	       FileOutputStream out = new FileOutputStream(f);
			    	       myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
			    	       out.flush();
			    	       out.close();
			    		
			    		
			    	} catch (Exception e) {
			    	       e.printStackTrace();
			    	}
			      }

		}else{
			try {
	    		   File f = new File( direct + File.separator + fileName);
	    		   ImageEditorApp.absoluteFilePath=f.getAbsolutePath();
		    		  
	    		   f.createNewFile();
	    	       FileOutputStream out = new FileOutputStream(f);
	    	       myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
	    	       out.flush();
	    	       out.close();
	    		
	    		
	    	} catch (Exception e) {
	    	       e.printStackTrace();
	    	}
	   
		}
			
		}else{
			Toast.makeText(context, "SDCard Not Mounted", 2000).show();
		}
	}
	
	
	public static Bitmap rotate(Bitmap src, float degree) {
	    // create new matrix
	    Matrix matrix = new Matrix();
	    // setup rotation degree
	    matrix.postRotate(degree);
	 
	    // return new bitmap rotated using matrix
	    return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
	}
	
	
	public static Bitmap changeBrightnessPixelByPixel(Bitmap bitmap, float value){
		
		int height = bitmap.getHeight();
		int width = bitmap.getWidth();
		int color=0;
		for (int i =0;i<width;i++){
			for(int j=0;j<height;j++){
				color = getNewBrightnessForPixel(bitmap.getPixel(i, j),value);
				bitmap.setPixel(i, j, color);
				
			}
		}
		
		return bitmap;
		
	}
	
	public static int getNewBrightnessForPixel(int pixelValue, float brightness){
		int composite=0;
		
		int alpha = pixelValue >> 24;
		int red = (pixelValue >> 16) & 0xFF;
		int green = (pixelValue >> 8) & 0xFF;
		int blue = pixelValue & 0xFF;
		int red2 = (int)(red + brightness);
		if (red2 > 0xFF)
			red2 = 0xFF;
		if (red2 < 0)
			red2 = 0;
		int green2 = (int)(green + brightness);
		if (green2 > 0xFF)
			green2 = 0xFF;
		if (green2 < 0)
			green2 = 0;
		int blue2 = (int)(blue + brightness);
		if (blue2 > 0xFF)
			blue2 = 0xFF;
		if (blue2 < 0)
			blue2 = 0;
		 composite = (alpha << 24) | (red2 << 16) | (green2 << 8)
				| blue2;

		return composite;
	}
	
	public static Bitmap changeContrastPixelByPixel(Bitmap bitmap,float value){
		
		int height = bitmap.getHeight();
		int width = bitmap.getWidth();
		int color=0;
		for (int i =0;i<width;i++){
			for(int j=0;j<height;j++){
				color = getNewContrastForPixel(bitmap.getPixel(i, j),value);
				bitmap.setPixel(i, j, color);
				
			}
		}
		
		return bitmap;
	
	}
	
	
	
	public static int getNewContrastForPixel(int pixelValue,float contrast){
		int alpha = pixelValue >> 24;
		int _red = (pixelValue >> 16) & 0xFF;
		int _green = (pixelValue >> 8) & 0xFF;
		int _blue = pixelValue & 0xFF;
		float red = _red / 255.0f;
		float green = _green / 255.0f;
		float blue = _blue / 255.0f;
		red = (((red - 0.5f) * contrast) + 0.5f) * 255.0f;
		green = (((green - 0.5f) * contrast) + 0.5f) * 255.0f;
		blue = (((blue - 0.5f) * contrast) + 0.5f) * 255.0f;
		int iR = (int) red;
		iR = iR > 255 ? 255 : iR;
		iR = iR < 0 ? 0 : iR;
		int iG = (int) green;
		iG = iG > 255 ? 255 : iG;
		iG = iG < 0 ? 0 : iG;
		int iB = (int) blue;
		iB = iB > 255 ? 255 : iB;
		iB = iB < 0 ? 0 : iB;
		return  (alpha << 24) | (iR << 16) | (iG << 8) | iB;
		
	}
	
	

}
