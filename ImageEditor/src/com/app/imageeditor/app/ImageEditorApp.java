package com.app.imageeditor.app;

import android.app.Application;
import android.graphics.Bitmap;

public class ImageEditorApp extends Application{
	
	public static Bitmap myBitmap;
	
	public static boolean isImageCropped=false;
	
	public static boolean isImagedChanged=false;
	
	public static String originalImagePath=null;
	public static String absoluteFilePath;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
		
		if(myBitmap!=null){
			myBitmap.recycle();
		}
	}

	
}
