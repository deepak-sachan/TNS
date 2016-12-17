package com.example.tns;

import java.util.ArrayList;

import com.example.tns.ak.MainualDrawActivity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.CUPCAKE)
@SuppressLint("NewApi")
public class CornerDetailsActivity extends Activity implements View.OnClickListener,SensorEventListener{
	
	ListView lstNoOfCornerDetails;
	private float[] gravity;
	private float[] geoMagnetic;
	private float azimut;
	AccessData objAdapter;
	//TextView txtaz;
	Button btnSubmit;
	private Sensor accSensor;
	   private Sensor magnetSensor;
	ListView lstCornerDet;
	View header;
	SensorManager sensorManager;
	 @SuppressLint("NewApi")
	SensorEventListener listner;
	Double latitude ,longitude;
	ArrayList<CornerDetailsDesign> listCorDet;
	Button btnSubCorDet;
	int cornerNum=2;
	String IPID;
	AccessData accessData;
	CornerDetailsAdapter adapter;
	CornerDetailsDesign objCorDetDesign;
	public static final double[] latid = new double[]{};
	public static final double[] longi = new double[]{};
	public static final double[] dist = new double[]{};
	public static final int[] number = new int[]{};
	

	
	
	NoOfCornerDetailsAdapter noofcornerDetailsAdapter;
	  @SuppressLint("NewApi")
	@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
	        setContentView(R.layout.cornerdetails);
	        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.header);
	        Bundle bCornerNumber = getIntent().getExtras();
	        cornerNum = bCornerNumber.getInt("numberCorner");	        
	        IPID = bCornerNumber.getString("ipid");
	        latitude = bCornerNumber.getDouble("latitude");
	        longitude = bCornerNumber.getDouble("longitude");	    
	        //IPID = "VODABIHAR0002";
	        btnSubCorDet = (Button)findViewById(R.id.btnSubmitCornerDetails);
	        btnSubCorDet.setOnClickListener(this);
	        lstCornerDet = (ListView)findViewById(R.id.lstCornerDetails);
	        header = getLayoutInflater().inflate(R.layout.cornerdetailsheader, null);
			 lstCornerDet.addHeaderView(header);
			 sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
			 accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		 		magnetSensor = sensorManager
		 				.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		 		//txtaz = (TextView)findViewById(R.id.txtAzi);
			 objAdapter = new AccessData(this);
	        Log.w("calling","setlist");
	        if(cornerNum > 0){
	        setListDetails();
	        }else{
	        //	Toast.makeText(this,"No Corner Details",Toast.LENGTH_SHORT).show();
	        	Intent noOfAdjBuildings = new Intent(this , MainualDrawActivity.class);
	    		Bundle b = new Bundle();		
	    		b.putString("ipid",IPID);
	    		b.putDouble("latitude",latitude);
	    		b.putDouble("longitude",longitude);
	    		noOfAdjBuildings.putExtras(b);
	    		startActivity(noOfAdjBuildings);
	        }
	 }
	  
	  /*public void onResume(){
			super.onResume();
			setListDetails();
		}
		
		public void onPause(){
			super.onPause();
			//adapter.notifyDataSetInvalidated();
			
			Log.w("pause-cornerdetails","called");
			
		}*/
		
	  @TargetApi(Build.VERSION_CODES.CUPCAKE)
	@SuppressLint("NewApi")
	public void onResume(){
			super.onResume();
//			sensorManager.registerListener(this, accSensor,
//					SensorManager.SENSOR_DELAY_FASTEST);
//			sensorManager.registerListener(this, magnetSensor,
//					SensorManager.SENSOR_DELAY_FASTEST);
		}
		
		public void onPause(){
			super.onPause();
			 sensorManager.unregisterListener(this);
		}
		
		public void onDestroy(){
			objAdapter.close();
			super.onDestroy();
			
			
		}
		
	  
	  
	  public void setListDetails(){
			 
			// Toast.makeText(this,"set list",Toast.LENGTH_LONG).show();
			 listCorDet = new ArrayList<CornerDetailsDesign>();
		//	 cornerNum = 2;
			 for(int i=0;i<cornerNum ;i++){
			//	 Log.w("inside setlist",String.valueOf(i));
				 objCorDetDesign = new CornerDetailsDesign(i+1,0,0,0);
				 listCorDet.add(objCorDetDesign);
			 }		 
			 if(listCorDet.size() > 0){
				// latitude = 0.0;
				// longitude = 0.0;
			 adapter = new CornerDetailsAdapter(this,R.layout.cornerdetailsonerow,listCorDet,latitude,longitude);			 
			 lstCornerDet.setAdapter(adapter);
			 
			 }else{
				// Toast.makeText(this,"No corner details!",Toast.LENGTH_LONG).show();
			 }
		 }
 
	  
	  
	@TargetApi(Build.VERSION_CODES.CUPCAKE)
	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub		
		objAdapter.openDataBase();
		
		for (int i = 0; i <lstCornerDet.getCount() ; i++) {
			if(i!=0){
            View vListSortOrder;
                   vListSortOrder=lstCornerDet.getChildAt(i);     
                   
                   try{
                   EditText edit=(EditText)vListSortOrder.findViewById(R.id.edDistance);
                   String temp=edit.getText().toString();
                   double distance = Double.valueOf(temp);
                  
                   
                   TextView txtCD=(TextView)vListSortOrder.findViewById(R.id.txtCornerDetails);
                   temp=txtCD.getText().toString();
                   int cNo = Integer.valueOf(temp);
                   
                   edit=(EditText)vListSortOrder.findViewById(R.id.edLatitude);
                   temp=edit.getText().toString();
                   double lati = Double.valueOf(temp);
                   
                   edit=(EditText)vListSortOrder.findViewById(R.id.edLongitude);
                   temp=edit.getText().toString();
                   
                   double longi = Double.valueOf(temp);
                   
                  // Toast.makeText(this,String.valueOf(cNo)+String.valueOf(distance)+
                		  // String.valueOf(lati)+String.valueOf(longi)+IPID, Toast.LENGTH_LONG).show();
                   int re =objAdapter.updateCornerDetailsLatLong(cNo, distance, lati, longi, IPID,this);
                 //  Toast.makeText(this,"update corner details",Toast.LENGTH_SHORT).show();
                   }catch(Exception ex){
                	  // Toast.makeText(getBaseContext(),":"+ex.getMessage(), Toast.LENGTH_LONG).show();
                   }
			} 
		}
		
		Bundle b = new Bundle();		
		b.putString("ipid",IPID);		
		b.putDouble("latitude",latitude);
		b.putDouble("longitude",longitude);
		Intent cornerMappingDetails = new Intent(this ,BoundaryLayoutActivity.class);
		cornerMappingDetails.putExtras(b);
		startActivity(cornerMappingDetails);

		
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@TargetApi(Build.VERSION_CODES.CUPCAKE)
	@SuppressLint("NewApi")
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
				//txtaz.setText("N:"+String.valueOf(az));
			}
		}
		
	}
}	  