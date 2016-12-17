package com.example.tns;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import android.hardware.Camera.Size;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class CamCorMainActivity extends Activity implements SurfaceHolder.Callback,SensorEventListener{


	 Camera camera;
	 SurfaceView mSurfaceView;
	 SurfaceHolder mSurfaceHolder;
	 boolean mPreviewRunning = false;
	 SensorManager sensorManager;
	 SensorEventListener listner;
	 private Sensor accSensor;
     private Sensor magnetSensor;
 	private float[] gravity;
 	private float[] geoMagnetic;
 	private float azimut;
 	private CompassView drawableView;
TextView txtaz;

	 static final int sensor = SensorManager.SENSOR_MAGNETIC_FIELD;
	 
	 String stringPath = "/sdcard/samplevideo.3gp";	 
		private SurfaceView surface_view;  
        private Camera mCamera;
        SurfaceHolder.Callback sh_ob = null;
        SurfaceHolder surface_holder        = null;
        SurfaceHolder.Callback sh_callback  = null;
        PictureCallback rawCallback;
        ShutterCallback shutterCallback;
        PictureCallback jpegCallback;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            getWindow().setFormat(PixelFormat.TRANSLUCENT);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                     WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            setContentView(R.layout.activity_cam_cor_main);
           mSurfaceView = (SurfaceView) findViewById(R.id.surfaceview);
           
            mSurfaceHolder = mSurfaceView.getHolder();

            mSurfaceHolder.addCallback(this);
            Button buttonStartCameraPreview = (Button)findViewById(R.id.buttonStartCameraPreview);
           txtaz = (TextView)findViewById(R.id.txtView);
            mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
            
    		accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    		magnetSensor = sensorManager
    				.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    		drawableView = (CompassView)findViewById(R.id.customView);
    		drawableView.letdraw();
            buttonStartCameraPreview.setOnClickListener(new Button.OnClickListener(){

            	   @Override
            	   public void onClick(View v) {
            	    // TODO Auto-generated method stub
                       mSurfaceHolder = mSurfaceView.getHolder();
                       mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
                       

            startCam(mSurfaceHolder);
            	   }});
            Log.w("create","done");
            }

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		try{
		if (mPreviewRunning)
        {
            mCamera.stopPreview();
        }

        Parameters parameters = mCamera.getParameters();
        Display display = ((WindowManager)this.getSystemService(WINDOW_SERVICE)).getDefaultDisplay();

        if(display.getRotation() == Surface.ROTATION_0)
        {
            parameters.setPreviewSize(height, width);                           
            mCamera.setDisplayOrientation(90);
        }

        if(display.getRotation() == Surface.ROTATION_90)
        {
            parameters.setPreviewSize(width, height);                           
        }

        if(display.getRotation() == Surface.ROTATION_180)
        {
            parameters.setPreviewSize(height, width);               
        }

        if(display.getRotation() == Surface.ROTATION_270)
        {
            parameters.setPreviewSize(width, height);
            mCamera.setDisplayOrientation(180);
        }

        mCamera.setParameters(parameters);
        Log.w("surface","change");
        previewCamera();            
		}catch(Exception ex){
			Toast.makeText(getBaseContext(), ex.getMessage(),Toast.LENGTH_SHORT).show();
		}

	}
	
	public void previewCamera()
	{        
	    try 
	    {           
	        mCamera.setPreviewDisplay(mSurfaceHolder);          
	        mCamera.startPreview();
	        mPreviewRunning = true;
	    }
	    catch(Exception e)
	    {
	        Toast.makeText(getBaseContext(),"cannot start preview",Toast.LENGTH_SHORT).show();    
	    }
	}
	
	

	private void startCamera(SurfaceHolder sh, int width, int height) {
		Camera.Parameters parameters = camera.getParameters();
	    List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();



	    
	    Size optimalPreviewSize = getOptimalPreviewSize(previewSizes, width, height);
	    if (optimalPreviewSize != null) {
	        parameters.setPreviewSize(optimalPreviewSize.width, optimalPreviewSize.height);
	        camera.setParameters(parameters);
	        camera.startPreview();
	    }

	 //   parameters.setPreviewSize(previewSize.width, previewSize.height);
	  //  camera.setParameters(parameters);
	   // camera.startPreview();
	}
	
	 
	 @Override
	 public void onResume() {
	   super.onResume();
		sensorManager.registerListener(this, accSensor,
				SensorManager.SENSOR_DELAY_UI);
		sensorManager.registerListener(this, magnetSensor,
				SensorManager.SENSOR_DELAY_UI);
	   
	 }

	 // unregister
	 @Override
	 public void onPause() {
	   super.onPause();
	   sensorManager.unregisterListener(this);
	 }
	
	private Size getOptimalPreviewSize(List<Size> previewSizes, int width,
			int height) {
		// TODO Auto-generated method stub
		return null;
	}

	public void startCam(SurfaceHolder holder){
		
		if(!mPreviewRunning){
		     camera = Camera.open();
		     //camera.unlock();
		     if (camera != null){
		      try {
		    	  if (this.getResources().getConfiguration().orientation !=Configuration.ORIENTATION_LANDSCAPE)
		          {

		              camera.setDisplayOrientation(90);
		              	              
		          }
		          else
		          {

		              camera.setDisplayOrientation(0);
		              
		          }
		       camera.setPreviewDisplay(mSurfaceHolder);
		       
		       camera.startPreview();		       
		       mPreviewRunning = true;
		       Log.w("surfacecreate","done");

		      } catch (IOException e) {
		       // TODO Auto-generated catch block
		       Toast.makeText(this,"cam"+e.getMessage(),Toast.LENGTH_SHORT).show();
		      }
		     }
		}
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		/*if(!mPreviewRunning){
		     camera = Camera.open();
		     //camera.unlock();
		     if (camera != null){
		      try {
		    	  if (this.getResources().getConfiguration().orientation !=Configuration.ORIENTATION_LANDSCAPE)
		          {

		              camera.setDisplayOrientation(90);
		              	              
		          }
		          else
		          {

		              camera.setDisplayOrientation(0);
		              
		          }
		       camera.setPreviewDisplay(mSurfaceHolder);
		       
		       camera.startPreview();		       
		       mPreviewRunning = true;
		       Log.w("surfacecreate","done");

		      } catch (IOException e) {
		       // TODO Auto-generated catch block
		       Toast.makeText(this,"cam"+e.getMessage(),Toast.LENGTH_SHORT).show();
		      }
		     }
		}*/
	       startCam(holder);	
		 rawCallback = new PictureCallback() {
	            public void onPictureTaken(byte[] data, Camera camera) {
	                Log.d("Log", "onPictureTaken - raw");
	            }
	        };

	        /** Handles data for jpeg picture */
	        shutterCallback = new ShutterCallback() {
	            public void onShutter() {
	                Log.i("Log", "onShutter'd");
	            }
	        };
	        jpegCallback = new PictureCallback() {
	            public void onPictureTaken(byte[] data, Camera camera) {
	                FileOutputStream outStream = null;
	                mPreviewRunning = false;
	                try {
	                    outStream = new FileOutputStream(String.format(
	                            "/sdcard/%d.jpg", System.currentTimeMillis()));
	                    outStream.write(data);
	                    outStream.close();
	                    Log.d("Log", "onPictureTaken - wrote bytes: " + data.length);
	                } catch (FileNotFoundException e) {
	                    e.printStackTrace();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                } finally {
	                	camera.release();
	                }
	                Log.d("Log", "onPictureTaken - jpeg");
	            }
	        };
	 
	}

	
	public void capture(View view){
		Toast.makeText(this,"cheese!",Toast.LENGTH_SHORT).show();
		   camera.takePicture(shutterCallback, rawCallback, jpegCallback);
		   
	}
	public void stopCam(SurfaceHolder holder){
		if(mCamera!= null)
			mCamera.release();		
		
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		//if(mCamera!= null)
		//mCamera.release();
		stopCam(holder);
		
	}

	
	public void onOrientationChanged(int orientation) {
	     
	 }
	
	@Override public void onBackPressed()
	{    
	  // Restart camera
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        startCam(mSurfaceHolder);
		
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
			gravity = event.values;
		if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
			geoMagnetic = event.values;
		if (gravity != null && geoMagnetic != null) {
			float R[] = new float[9];
			float I[] = new float[9];
			boolean success = SensorManager.getRotationMatrix(R, I, gravity,
					geoMagnetic);
			if (success) {
				/* Orientation has azimuth, pitch and roll */
				Toast.makeText(this,"success!",Toast.LENGTH_SHORT).show();
				float orientation[] = new float[3];
				SensorManager.getOrientation(R, orientation);
				azimut = orientation[0];
				float azimuthInDegress = (float)Math.toDegrees(azimut);
				if (azimuthInDegress < 0.0f) {
				    azimuthInDegress += 360.0f;
				}
				int az = (int)azimuthInDegress;
				txtaz.setText("Azimuth"+String.valueOf(az));
				drawableView.takeAzi(azimut);
			}
			//Toast.makeText(this,String.valueOf(azimut), Toast.LENGTH_SHORT).show();
		}
		

	}
}