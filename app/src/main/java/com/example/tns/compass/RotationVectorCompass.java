package com.example.tns.compass;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.adamratana.rotationvectorcompass.camera.CameraPreviewLayer;
import com.adamratana.rotationvectorcompass.camera.CameraUtil;
import com.adamratana.rotationvectorcompass.math.Matrix4;
import com.adamratana.rotationvectorcompass.rotation.MagAccelListener;
import com.adamratana.rotationvectorcompass.rotation.RotationUpdateDelegate;
import com.example.tns.AccessData;
import com.example.tns.AllOptionsActivity;
import com.example.tns.CamTestActivity;
import com.example.tns.R;

@TargetApi(Build.VERSION_CODES.CUPCAKE)
@SuppressLint("NewApi")
public class RotationVectorCompass extends Activity implements RotationUpdateDelegate, CameraPreviewLayer.FOVUpdateDelegate,OnClickListener {
	private static final boolean FLAG_DEBUG = true;
	private static final String TAG = "RotationVectorCompass";

	private static final float MAX_FOV = 175f;
	private static final float MIN_FOV = 10f;
	private static final float DEFAULT_FOV = 40f;

	private static final float MAX_ORTHO_SCALE = 7.0f;
	private static final float DEFAULT_SCALE = 1.0f;
	private static final float MIN_ORTHO_SCALE = 0.25f;

	private OverlayView mOverlayView;
	private Matrix4 mRotationMatrix = new Matrix4();
	private SensorManager mSensorManager;

	private MagAccelListener mMagAccel;
	//private RotationVectorListener mRotationVector;

	private int mDisplayRotation;
	//private boolean mUseRotationVector = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD);

	private float mFOV = DEFAULT_FOV;
	private float mOrthographicScale = DEFAULT_SCALE;
	private SeekBar mScaleAndFOVSlider = null;

	/** camera stuff **/
	//private boolean mCameraOn = false;
	private LinearLayout mCameraViewHolder;
	private LayoutParams mCameraViewLayoutParams;
	private CameraPreviewLayer mPreview;
	private Camera mCamera;
	private int mNumCameras;
	private int mDefaultCameraID = -1; // The first rear facing camera
	private boolean mFullScreen = false;
	private boolean mPerspectiveProjection = true;
	private boolean mOrientationLocked = false;
	
	Button btnNext,btnCapture;
	byte[] dataImg;
	AccessData objAdapter;
	private String imgName="";
	static String ipid;
	public static String filePath="";
	private static String[] imagesList = new String[] {};

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// setup window decorations
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		final Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		mDisplayRotation = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) ? display.getRotation() : display.getOrientation();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

			log("display: " + display.getWidth() + " x " + display.getHeight());
		} else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			mFullScreen = true;
			mOrientationLocked = true;
		}

	/*	if (mFullScreen) {
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}
*/
		setContentView(R.layout.compass_main);
		ipid=getIntent().getStringExtra("ipid");
		btnNext=(Button) findViewById(R.id.btnNext);
		btnCapture=(Button) findViewById(R.id.btnCapture);
		//btnCompelete=(Button) findViewById(R.id.btnCompelet);
		
		btnNext.setOnClickListener(this);
	//	btnCompelete.setOnClickListener(this);
		btnCapture.setOnClickListener(this);

		// sensor listeners
		mMagAccel = new MagAccelListener(this);
		//mRotationVector = new RotationVectorListener(this);
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

		// overlay view
		mOverlayView = (OverlayView) findViewById(R.id.rotateView2);
		mOverlayView.setFOVView((TextView) findViewById(R.id.txtDegrees));
		//mOverlayView.setPerspectiveProjection(mPerspectiveProjection);
		mOverlayView.setFOV(DEFAULT_FOV);

	
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
			// Find the total number of cameras available
			mNumCameras = Camera.getNumberOfCameras();

			// Find the ID of the back-facing camera
			CameraInfo cameraInfo = new CameraInfo();
			for (int i = 0; i < mNumCameras; i++) {
				Camera.getCameraInfo(i, cameraInfo);
				if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK) {
					mDefaultCameraID = i;
				}
			}
		} else {
			// to work on non-froyo
			mDefaultCameraID = 1;
		}
		mCameraViewHolder = (LinearLayout) findViewById(R.id.cameraViewHolder);
	}

	@TargetApi(Build.VERSION_CODES.CUPCAKE)
	@SuppressLint("NewApi")
	private void applySensors(boolean useRV) {
		mSensorManager.unregisterListener(mMagAccel);
		//mSensorManager.unregisterListener(mRotationVector);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD && useRV) {
			//mSensorManager.registerListener(mRotationVector, mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR), SensorManager.SENSOR_DELAY_GAME);
		} else {
			mSensorManager.registerListener(mMagAccel, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
			mSensorManager.registerListener(mMagAccel, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_GAME);
		}
	}

	private void updateViews() {
		mOverlayView.rotateView(mRotationMatrix);
	}

	private void stopCamera() {
		if (mCamera != null) {
			mPreview.setCamera(null);
			mCamera.release();
			mCamera = null;

			mCameraViewHolder.removeView(mPreview);
			mPreview = null;
		}
	}

	@SuppressLint("NewApi")
	@TargetApi(Build.VERSION_CODES.DONUT)
	private void startCamera() {
		if (mCamera != null) {
			return;
		}
		/** camera stuff **/
		// Open the default i.e. the first rear facing camera.
		try {
			if (mDefaultCameraID != -1) {
				// new
				mCameraViewLayoutParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
				mPreview = new CameraPreviewLayer(this, this);

				// create preview and attach to view -- will be removed when
				// pausing, will be recreated when resuming
				PackageManager pm = getPackageManager();
				if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA) && pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_AUTOFOCUS)) {
					mPreview.setCanAutoFocus(true);
				} else {
					mPreview.setCanAutoFocus(false);
				}

				mCameraViewHolder.addView(mPreview, mCameraViewLayoutParams);

				mCamera = Camera.open();

				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
					CameraUtil.setCameraDisplayOrientation(this, mDefaultCameraID, mCamera);
				}
				mPreview.setCamera(mCamera);
			}
		} catch (Exception e) {
			log("startCamera(): exception caught: " + e);
		}
	}

	/**
	 * logging if debug enabled
	 * 
	 * @param msg
	 */
	private void log(String msg) {
		if (FLAG_DEBUG) {
			Log.e(TAG, msg);
		}
	}

	// RotationUpdateDelegate methods
	@TargetApi(Build.VERSION_CODES.CUPCAKE)
	@Override
	public void onRotationUpdate(float[] newMatrix) {
		// remap matrix values according to display rotation, as in
		// SensorManager documentation.
		switch (mDisplayRotation) {
		case Surface.ROTATION_0:
		case Surface.ROTATION_180:
			break;
		case Surface.ROTATION_90:
			SensorManager.remapCoordinateSystem(newMatrix, SensorManager.AXIS_Y, SensorManager.AXIS_MINUS_X, newMatrix);
			break;
		case Surface.ROTATION_270:
			SensorManager.remapCoordinateSystem(newMatrix, SensorManager.AXIS_MINUS_Y, SensorManager.AXIS_X, newMatrix);
			break;
		default:
			break;
		}
		mRotationMatrix.set(newMatrix);
		updateViews();
	}

	// CameraPreviewLayer.FOVDelegate methods
	@Override
	public void onFOVUpdate(int width, int height, float fovH, float fovV, float adjustedFOVH, float adjustedFOVV) {
		log("adjusted FOV for " + width + " x " + height + " h: " + fovH + " v: " + fovV + " adjH: " + adjustedFOVH + " adjV: " + adjustedFOVV);
		if (width > height) {
			mFOV = adjustedFOVH;
		} else {
			mFOV = adjustedFOVV;
		}
		mOverlayView.setFOV(mFOV);
//		if (mPerspectiveProjection) {
//			mScaleAndFOVSlider.setProgress((int) ((mFOV - MIN_FOV) / (MAX_FOV - MIN_FOV) * 1000f));
//		}
	}

	// Other Activity life-cycle methods
	@Override
	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(mMagAccel);
		/*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD && mUseRotationVector) {
			mSensorManager.unregisterListener(mRotationVector);
		}*/
			stopCamera();
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		applySensors(false);
			startCamera();
		
	}
	
	ShutterCallback shutterCallback = new ShutterCallback() {
		public void onShutter() {
			// Log.d(TAG, "onShutter'd");
		}
	};

	PictureCallback rawCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			// Log.d(TAG, "onPictureTaken - raw");
		}
	};

	PictureCallback jpegCallback = new PictureCallback() {
		
		public void onPictureTaken(byte[] data, Camera camera) {

			String imgType="";
			if(imgName.startsWith("T")&& imgName.endsWith("0"))
			{
				imgType = "TOWER";
				Log.w("tw","T");
			}
			else if(imgName.startsWith("SE"))
			{
				imgType = "TOWER_MAIN";
				Log.w("gr","S");
			}
			else if(imgName.startsWith("T"))
			{
				imgType = "TOWER_MAIN";
				Log.w("gr","S");
			}
			else if(imgName.startsWith("TOWER Bu"))
			{
				imgType = "TOWER_MAIN";
				Log.w("gr","S");
			}
			else if(imgName.startsWith("CAU"))
			{
				imgType = "GROUND";
				Log.w("cr","C");
			}
			else if(imgName.startsWith("C1"))
			{
				imgType = "CORNER";
				Log.w("cr","C");
			}
			else if(imgName.startsWith("C2"))
			{
				imgType = "CORNER";
				Log.w("cr","C");
			}
			else if(imgName.startsWith("C3"))
			{
				imgType = "CORNER";
				Log.w("cr","C");
			}
			else if(imgName.startsWith("C4"))
			{
				imgType = "CORNER";
				Log.w("cr","C");
			}
			
			else if(imgName.startsWith("DANG"))
			{
				imgType = "GROUND";
				Log.w("cr","C");
			}
			
			else if(imgName.startsWith("ADJ"))
			{
				imgType = "ADJ_GPS";
				Log.w("cr","C");
			}
			else if(imgName.startsWith("Wa"))
			{
				imgType = "GROUND";
				Log.w("cr","C");
			}
			else if(imgName.startsWith("SITE"))
			{
				imgType = "GROUND";
				Log.w("cr","C");
			}
			else if(imgName.startsWith("BUIL"))
			{
				imgType="BUILDING";
			}
			else
			{
				imgType="";	
			}
			
			dataImg = new byte[data.length];
	        Intent imageIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
	        File imagesFolder = new File(Environment.getExternalStorageDirectory(), "/TNS_IMG");
	        imagesFolder.mkdirs(); 
	        Date d = new Date();
	        
	        String fileName =ipid +"_"+imgName;
	        filePath="/sdcard/TNS_IMG/"+fileName;
	        File output = new File(imagesFolder, fileName);
	        Uri uriSavedImage = Uri.fromFile(output);
            imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
	        OutputStream imageFileOS;
			try {
				imageFileOS = getContentResolver().openOutputStream(uriSavedImage);
				//thumbnail.compress(Bitmap.CompressFormat.JPEG, 1000, out);
				Bitmap original = BitmapFactory.decodeByteArray(data , 0, data.length);
				Bitmap resized = Bitmap.createScaledBitmap(original, original.getWidth(), original.getHeight(), true);
		         
			    ByteArrayOutputStream blob = new ByteArrayOutputStream();
			    resized.compress(Bitmap.CompressFormat.PNG, 100, blob);
			 
	            imageFileOS.write(blob.toByteArray());
	            imageFileOS.flush();
	            imageFileOS.close();
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
        	File f = new File(filePath);

			objAdapter = new AccessData(getBaseContext());
        	objAdapter.openDataBase();
        //	File imgCamera = new  File(Environment.getExternalStorageDirectory()+imgName );
        	//Toast.makeText(getBaseContext(), f.getAbsolutePath(),Toast.LENGTH_SHORT).show();
         	long i =objAdapter.saveImagePathToDatabase(f.getAbsolutePath(), ipid, fileName, imgType);
         	Toast.makeText(getApplicationContext(),String.valueOf(i),Toast.LENGTH_SHORT).show();
			
			
			//saveimg();
			stopCamera();
			startCamera();
			Log.d(TAG, "onPictureTaken - jpeg");
		}
	};
	
	
	public void saveimg(){
		try {
			String imgType="";
			if(imgName.startsWith("T"))
			{
				imgType = "TOWER";
				Log.w("tw","T");
			}
			else if(imgName.startsWith("S"))
			{
				imgType = "GROUND";
				Log.w("gr","S");
			}
			else
			{
				imgType = "BUILDING";
				Log.w("blsg","B");
			}
        	File f = new File(Environment.getExternalStorageDirectory() +ipid +"_"+imgName+ "_"+imgName + ".png");
        	//Log.w(imgName,"name");
        	objAdapter = new AccessData(getBaseContext());
        	objAdapter.openDataBase();
        //	File imgCamera = new  File(Environment.getExternalStorageDirectory()+imgName );
        	//Toast.makeText(getBaseContext(), f.getAbsolutePath(),Toast.LENGTH_SHORT).show();
         	long i =objAdapter.saveImagePathToDatabase(f.getAbsolutePath(), ipid, imgName, imgType);
         	Toast.makeText(this,String.valueOf(i),Toast.LENGTH_SHORT).show();
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(dataImg);
            fos.close();
            objAdapter.close();
            Log.w("done","camera");
        } catch (FileNotFoundException e) {

        } catch (IOException e) {
        
        }
        
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()==R.id.btnNext)
		{
			//Intent in = new = new Intent(getApplicationContext(), al)
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        //Title	        
	        builder.setMessage("Mark site as complete?");
	        builder.setPositiveButton("ok",new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					objAdapter = new AccessData(getBaseContext());
					objAdapter.openDataBase();
					int r =objAdapter.markSiteAsComplete(ipid);
					Toast.makeText(getBaseContext(),String.valueOf(r),Toast.LENGTH_LONG).show();
					RotationVectorCompass.this.finish();
					Intent in=new Intent(getApplicationContext(), AllOptionsActivity.class);
					in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(in);
				}
			});
	        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub					
					RotationVectorCompass.this.finish();
					Intent in=new Intent(getApplicationContext(), AllOptionsActivity.class);
					in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(in);
				}
				
			});
	        builder.create();
	        builder.show();
			
			
		}
//		if(v.getId()==R.id.btnCompelet)
//		{
//			RotationVectorCompass.this.finish();
//			Intent in=new Intent(Intent.ACTION_MAIN);
//			in.addCategory(Intent.CATEGORY_HOME);
//			in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(in);
//		}
		if(v.getId()==R.id.btnCapture)
		{
			imgOption();
			//mCamera.takePicture(shutterCallback, rawCallback, jpegCallback);
		}
		
	}
	
	public void imgOption()
	{
		imagesList = getBaseContext().getResources().getStringArray(
				R.array.Tower);
		AlertDialog.Builder builder = new AlertDialog.Builder(
				RotationVectorCompass.this);
		// Title
		builder.setTitle("Choose image name :");
		builder.setSingleChoiceItems(imagesList, -1,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						imgName = imagesList[which];
						AlertDialog.Builder builder1 = new AlertDialog.Builder(
								RotationVectorCompass.this);
						builder1.setTitle("You are selected "+imgName);
						builder1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
								mCamera.takePicture(shutterCallback, rawCallback, jpegCallback);
								//saveimg();
							}
						});
                        builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						});
						
						builder1.create();
						builder1.show();
						
						dialog.dismiss();
					}
				});
        
		
		builder.create();
		builder.show();
	}
}