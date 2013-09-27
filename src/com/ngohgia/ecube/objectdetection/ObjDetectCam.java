package com.ngohgia.ecube.objectdetection;

import java.io.FileOutputStream;

import org.opencv.android.JavaCameraView;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.util.AttributeSet;
import android.util.Log;

public class ObjDetectCam extends JavaCameraView implements PictureCallback{
	private static final String LOG_TAG = "ObjectDetect::Cam";
	private String mPictureFileName;
	
	public ObjDetectCam(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void takePicture(final String fileName){
		Log.i(LOG_TAG, "Taking picture");
		this.mPictureFileName = fileName;
		
		mCamera.setPreviewCallback(null);
		
		mCamera.takePicture(null, null, this);
	}
	
	@Override
	public void onPictureTaken(byte[] data, Camera camera) {
		Log.i(LOG_TAG, "Saving a bitmap to file");
		// The camera preview was automatically stopped. Start it again.
		mCamera.startPreview();
		mCamera.setPreviewCallback(this);
		
		try {
			FileOutputStream fos = new FileOutputStream(mPictureFileName);
			
			fos.write(data);
			fos.close();
		} catch (java.io.IOException e){
			Log.e("LOG_TAG", "Exception in photoCallback", e);
		}
		
	}

}
