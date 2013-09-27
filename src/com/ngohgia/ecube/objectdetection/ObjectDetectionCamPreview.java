package com.ngohgia.ecube.objectdetection;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Toast;

public class ObjectDetectionCamPreview extends Activity implements CvCameraViewListener2, OnTouchListener {
	private static final String LOG_TAG = "ObjectDetection::Activity";
	public static final String CAPTURED_IMG_PATH = "com.ngohgia.ecube.objectdetection.CAPTURED_IMG_PATH"; 

	private ObjDetectCam			mOpenCvCameraView;
	private Mat						mRgba;
	
	private MenuItem				mItemPreviewRGBA;
	
	// Initialize the OpenCv Camera Manager
	private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
		@Override
		public void onManagerConnected(int status){
			switch (status) {
				case LoaderCallbackInterface.SUCCESS:
				{
					Log.i(LOG_TAG, "OpenCV loaded successfully");
					mOpenCvCameraView.enableView();
					mOpenCvCameraView.setOnTouchListener(ObjectDetectionCamPreview.this);
				} break;
				default:
				{
					super.onManagerConnected(status);
				} break;
			}
		}
	};
	
	public ObjectDetectionCamPreview(){
		Log.i(LOG_TAG, "Instantiated new " + this.getClass());
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(LOG_TAG, "called onCreate");
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		// Set the Camera Preview
		setContentView(R.layout.activity_object_detection_cam_preview);
		
		mOpenCvCameraView = (ObjDetectCam) findViewById(R.id.objection_detection_cam_preview);
		mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);		
		mOpenCvCameraView.setCvCameraViewListener(this);
	}

    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this, mLoaderCallback);
    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Log.i(LOG_TAG, "called onCreateOptionsMenu");
		mItemPreviewRGBA = menu.add("Preview RGBA");
		
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.object_detection_cam_preview, menu);
		return true;
	}

	@Override
	public void onCameraViewStarted(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCameraViewStopped() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
		mRgba = inputFrame.rgba();
		return mRgba;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// Upon touched, the preview image is captured and saved
		Log.i(LOG_TAG, "onTouch event");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String currentDateandTime = sdf.format(new Date());
		
		String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() +
				"/ObjDetect";
		
		File dir = new File(dirPath);
		if (!dir.mkdirs()){
			Log.e(LOG_TAG, "Directory not created");
		}
		
		String fileName = dirPath + "/captured_view_" + currentDateandTime + ".jpg";
		mOpenCvCameraView.takePicture(fileName);
		Toast.makeText(this, fileName + " saved", Toast.LENGTH_SHORT).show();
		
		// Call the Image Processing Activity
		Intent intent = new Intent(this, ObjDetectImgProc.class);
		intent.putExtra(CAPTURED_IMG_PATH, fileName);
		startActivity(intent);
		return false;
	}

}
