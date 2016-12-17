package com.example.tns;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CameraMainActivity extends Activity implements SensorEventListener{

	private Camera mCam;
	private Click myClick;
	Button btnCapture;
	Camera again;
	SensorManager sensorManager;
	 SensorEventListener listner;
	 private Sensor accSensor;
   private Sensor magnetSensor;
	private float[] gravity;
	private float[] geoMagnetic;
	private float azimut;
	byte[] dataImg;
	TextView txtaz;
	CompassView drawableView;
	Spinner spnImages;
	private static String[] imagesList = new String[]{};
	private static String imgName="";
	static String ipid = "test123";
	ArrayAdapter<String> adapter;
	AccessData objAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_main);
        ipid = getIntent().getStringExtra("ipid");
        mCam = getcameraInstance();
        myClick = new Click(this,mCam);
 sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        drawableView = (CompassView)findViewById(R.id.compass);
        
 		accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
 		magnetSensor = sensorManager
 				.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        txtaz = (TextView)findViewById(R.id.txtAzi);
        FrameLayout frame = (FrameLayout)findViewById(R.id.camera_preview);
        frame.addView(myClick);
        
     /*   spnImages = (Spinner)findViewById(R.id.spnImages);
		imagesList = getResources().getStringArray(R.array.Tower);
		adapter = 
		        new ArrayAdapter<String> (this, 
		        android.R.layout.simple_spinner_item,imagesList);
		spnImages.setAdapter(adapter);
		spnImages.setVisibility(View.GONE);
		spnImages.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		        int item = spnImages.getSelectedItemPosition();
//		         spnImages.getSelectedItem().toString();
		        Toast.makeText(getBaseContext(), 
		        		spnImages.getSelectedItem().toString(), 
		            Toast.LENGTH_SHORT).show();
		    }

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});*/
        
        btnCapture = (Button)findViewById(R.id.btnCapture);
        btnCapture.setOnClickListener( new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mCam.takePicture(null, null, mPicture);
			}
		});
        
        
    }
    
    /** Create a File for saving the image */
	private static File getOutputMediaFile(){
		//Toast.makeText(cnt, "inside output",Toast.LENGTH_SHORT).show();
	/*	imagesList = cnt.getResources().getStringArray(R.array.Tower);
		String selectedName = "";
		 AlertDialog.Builder builder = new AlertDialog.Builder(cnt);
	        //Title
	        builder.setTitle("Choose image name :");
	        builder.setSingleChoiceItems(imagesList, -1, new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					imgName = imagesList[which];
					dialog.dismiss();
				}
			});

	        builder.create();
	        builder.show();*/

	  /*  File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
	              Environment.DIRECTORY_PICTURES), "EMF");
		File mediaStorageDir = new File(Environment.getExternalStorageDirectory(),"EMF");
		
		File mediaStorageDir = new File("EMF");
		
	    if (! mediaStorageDir.exists()){
	        if (! mediaStorageDir.mkdirs()){
	            Log.d("MyCameraApp", "failed to create directory");
	            return null;
	        }
	    }*/

	    // Create a media file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    File mediaFile;
	        //mediaFile = new File(mediaStorageDir.getPath() + File.separator +ipid +"_"+imgName+ "_"+
	    mediaFile = new File(ipid +"_"+imgName+ "_"+
	        imgName + ".png");
	        String path = mediaFile.getAbsolutePath();
	       
	    return mediaFile;
	}
	
	 @Override
	 public void onResume() {
	   super.onResume();
		sensorManager.registerListener(this, accSensor,
				SensorManager.SENSOR_DELAY_FASTEST);
		sensorManager.registerListener(this, magnetSensor,
				SensorManager.SENSOR_DELAY_FASTEST);
		//camera.lock();
	   
	 }

	 // unregister
	 @Override
	 public void onPause() {
	   super.onPause();
	  // mCamera.unlock();
	 sensorManager.unregisterListener(this);
	 }
	 
	 public void callexit(View view){
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
					Intent exitAct = new Intent(getBaseContext(),AllOptionsActivity.class);
					 //builder.setPositiveButton(text, listener)
					 startActivity(exitAct);
				}
			});
	        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub					
					Intent exitAct = new Intent(getBaseContext(),AllOptionsActivity.class);
					 //builder.setPositiveButton(text, listener)
					 startActivity(exitAct);
				}
				
			});
	        builder.create();
	        builder.show();
		 
	 }

    
    
	PictureCallback mPicture = new PictureCallback() {

		@Override
		public void onPictureTaken( byte[] data, Camera camera) {
			Toast.makeText(getBaseContext(), "Clicked!",Toast.LENGTH_LONG).show();
			dataImg = new byte[data.length];
			
			System.arraycopy(data, 0,dataImg,0,data.length);
			imagesList = getBaseContext().getResources().getStringArray(R.array.Tower);
			AlertDialog.Builder builder = new AlertDialog.Builder(CameraMainActivity.this);
	        //Title
	        builder.setTitle("Choose image name :");
	        builder.setSingleChoiceItems(imagesList, -1, new DialogInterface.OnClickListener() {
				
			public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					imgName = imagesList[which];
					
					saveimg();
					dialog.dismiss();
					
				}
			});

	        builder.create();
	        builder.show();
			// File pictureFile = getOutputMediaFile();
	        //File pictureFile = openFileOutput(ipid +"_"+imgName+ "_"+
	        //imgName + ".png",Context.MODE_PRIVATE);
		       /* if (pictureFile == null){
		            return;
		        }*/

/*	        try {
	        	File f = new File(ipid +"_"+imgName+ "_"+imgName + ".png");
	        	Log.w(imgName,"name");
	        	objAdapter = new AccessData(getBaseContext());
	        	objAdapter.openDataBase();
	        	Toast.makeText(getBaseContext(), f.getAbsolutePath(),Toast.LENGTH_SHORT).show();
	         	objAdapter.saveImagePathToDatabase(f.getAbsolutePath(), ipid, imgName, imgName);
	            FileOutputStream fos = new FileOutputStream(f);
	            fos.write(data);
	            fos.close();
	            objAdapter.close();
	        } catch (FileNotFoundException e) {

	        } catch (IOException e) {
	        
	        }
	        finally{
	        	camera.stopPreview();
	        	camera.startPreview();
	        }*/
	        
	        camera.stopPreview();
	        camera.startPreview();
			
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
    
    private Camera getcameraInstance(){
    	Camera camera = null;
    	try{
    		camera = Camera.open();
    	}catch(Exception ex){
    		
    	}
    	return camera;
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
				//Orientation has azimuth, pitch and roll 
				//Toast.makeText(this,"success!",Toast.LENGTH_SHORT).show();
				float orientation[] = new float[3];
				SensorManager.getOrientation(R, orientation);
				azimut = orientation[0];
				float azimuthInDegress = (float)Math.toDegrees(azimut);
				if (azimuthInDegress < 0.0f) {
				    azimuthInDegress += 360.0f;
				}
				int az = (int)azimuthInDegress;
			//	Toast.makeText(getBaseContext(), String.valueOf(az),Toast.LENGTH_SHORT).show();
				txtaz.setText("N:"+String.valueOf(az));
				drawableView.takeAzi(azimut);
			}
		
	}
	}
}

	
   
    

