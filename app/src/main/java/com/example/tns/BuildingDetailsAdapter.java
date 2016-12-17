package com.example.tns;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class BuildingDetailsAdapter extends ArrayAdapter<BuildingDetailsDesign> implements LocationListener,SensorListener{
	
	Context context;
	Double latitude =0.0 , longitude=0.0;
	ArrayList<BuildingDetailsDesign> bldgDetails;
	double mainLatitude , mainLongitude;
	LocationManager locationManager;
	Double distanceVal = 0.0;
    Location location; // location
    double latitude1; // latitude
    double longitude1; // longitude
 
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 3; 
 
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
 
    SensorManager sensorManager;
    String provider;
    
    int orientation=0;
    static final int sensor = SensorManager.SENSOR_ORIENTATION;
	
	public BuildingDetailsAdapter(Context context, int res,ArrayList<BuildingDetailsDesign> objects,double lat,double lng) {
		super(context, res,objects);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.bldgDetails = objects;
		this.mainLatitude = lat;
		this.mainLongitude = lng;
		getLocation();
		getSensor();
	}
	
	private void getSensor(){
		sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
	}
private Location getLocation(){
		
		locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
			Criteria criteria = new Criteria();
		   String provider = locationManager.getBestProvider(criteria, false);
		    Location location = locationManager.getLastKnownLocation(provider);		 
		    locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
            
            if (locationManager != null) {
                location = locationManager
                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    latitude1 = location.getLatitude();
                    longitude1 = location.getLongitude();
                }
            }
            
            return location;
	}
	
	
	private class ViewHolder{
		TextView  txtBldgNo;
		EditText edLat ,edLong ,edDist ,edFloors ,edAzimuth,edBldgHeight;
		Button btnSubmit;
	}

    public View getView(int position, View convertView, ViewGroup parent) {
       
        BuildingDetailsDesign rowItem = getItem(position);
        
        ViewHolder vh = new ViewHolder(); 
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.buildingdetailsonerow, null);
       
         vh.txtBldgNo = (TextView) convertView.findViewById(R.id.txtBldgDetails);
         vh.edLat = (EditText) convertView.findViewById(R.id.edLatitude);  
         vh.edLong = (EditText)convertView.findViewById(R.id.edLongitude);
         vh.edDist = (EditText)convertView.findViewById(R.id.edDistance);
         vh.edAzimuth = (EditText)convertView.findViewById(R.id.edAzimuth);
         vh.edFloors = (EditText)convertView.findViewById(R.id.edFloor);
         vh.edBldgHeight = (EditText)convertView.findViewById(R.id.edBldgHeight);
         vh.btnSubmit = (Button)convertView.findViewById(R.id.btnGetDistance);
         vh.btnSubmit.setOnClickListener(new View.OnClickListener() {
     		
     		public void onClick(View v) {
     			// TODO Auto-generated method stub
     		
     			 LinearLayout vwParentRow = (LinearLayout)v.getParent();
     			 Location loc = getLocation();
     			 if(loc!=null){
     			latitude = loc.getLatitude();
     	        longitude = loc.getLongitude();
     	        Toast.makeText(context,"Provider available", Toast.LENGTH_SHORT).show();
     			 }
     	      //  Toast.makeText(context, String.valueOf(latitude),Toast.LENGTH_LONG).show();
     	       //Toast.makeText(context, String.valueOf(longitude),Toast.LENGTH_LONG).show();
     			 EditText child = (EditText)vwParentRow.getChildAt(3);
     			 if(latitude > 0.0)
     				 child.setText(String.valueOf(latitude));
     			 else
     				child.setText(String.valueOf("0.0"));
     			child = (EditText)vwParentRow.getChildAt(5);
     			if(longitude > 0.0)     				
     				child.setText(String.valueOf(longitude));
     			else
     				child.setText(String.valueOf("0.0"));     			
     		      
    		       	child = (EditText)vwParentRow.getChildAt(9);    
    		       	if(orientation > 0)
    		       	child.setText(String.valueOf(orientation));
    		       	else
    		       		child.setText(String.valueOf("0"));
    		       	if(latitude > 0.0 && longitude > 0.0)
    		       	 distanceVal =  distance(mainLatitude, mainLongitude, latitude, longitude);    		       	
    		      	child = (EditText)vwParentRow.getChildAt(13);
    		      	if(distanceVal > 0.0)
   		       		child.setText(String.valueOf(distanceVal));
    		      	else
    		      		child.setText(String.valueOf("0.0"));
     		        
     			
     		}
     	});

            convertView.setTag(vh);
        
        } else
        {
            vh = (ViewHolder)convertView.getTag();
        }
        
        vh.txtBldgNo.setText(String.valueOf(rowItem.getbldgNumber()));
        vh.edLat.setText("0");    
        vh.edLong.setText("0");
        vh.edDist.setText("0");
        vh.edFloors.setText("0");
        vh.edAzimuth.setText("0");
        vh.btnSubmit.setText("Find");
        return convertView;
    }

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	
	private double distance(double lat1, double lon1, double lat2, double lon2) {
	      double theta = lon1 - lon2;
	      double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
	      dist = Math.acos(dist);
	      dist = rad2deg(dist);
	      dist = dist * 60 * 1.1515;
	       return (dist);
	    }

	   private double deg2rad(double deg) {
	      return (deg * Math.PI / 180.0);
	    }
	   private double rad2deg(double rad) {
	      return (rad * 180.0 / Math.PI);
	    }


	@Override
	public void onAccuracyChanged(int sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onSensorChanged(int sensor, float[] values) {
		// TODO Auto-generated method stub
		if (sensor != BuildingDetailsAdapter.sensor)
		      return;
		 
		   float azimut = values[0];
			float azimuthInDegress = (float)Math.toDegrees(azimut);
			if (azimuthInDegress < 0.0f) {
			    azimuthInDegress += 360.0f;
			}
			orientation = (int)azimuthInDegress;
	}



}
