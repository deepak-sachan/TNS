package com.example.tns;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CornerDetailsAdapter extends ArrayAdapter<CornerDetailsDesign> implements LocationListener{
	
	Context context;
	Double lat , lng;
	ArrayList<CornerDetailsDesign> cornerDetails;
	LocationManager locationManager;
	double mainLatitude , mainLongitude;
    Location location; // location
    double latitude=0.0,latitude1=0; // latitude
    double longitude=0.0,longitude1=0; // longitude
    double distanceVal;
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 3; 
 
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
 
    String provider;
    
    
	
	public CornerDetailsAdapter(Context context, int res,ArrayList<CornerDetailsDesign> objects,double lat , double lng) {
		super(context, res,objects);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.cornerDetails = objects;
		this.mainLatitude = lat;
		this.mainLongitude = lng; 
		getLocation();
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
		TextView  txtCornerNo;
		EditText edLat ,edLong ,edDist;
		Button btnSubmit;
	}

    public View getView(int position, View convertView, ViewGroup parent) {
       
        CornerDetailsDesign rowItem = getItem(position);
        
        ViewHolder vh = new ViewHolder(); 
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.cornerdetailsonerow, null);
       
         vh.txtCornerNo = (TextView) convertView.findViewById(R.id.txtCornerDetails);
         vh.edLat = (EditText) convertView.findViewById(R.id.edLatitude);  
         vh.edLong = (EditText)convertView.findViewById(R.id.edLongitude);
         vh.edDist = (EditText)convertView.findViewById(R.id.edDistance);
         vh.btnSubmit = (Button)convertView.findViewById(R.id.btnGetDistance);
         vh.btnSubmit.setOnClickListener(new View.OnClickListener() {
     		
     		public void onClick(View v) {
     			// TODO Auto-generated method stub
     			
     			 LinearLayout vwParentRow = (LinearLayout)v.getParent();
     			 Location loc = getLocation();
     			 if(loc!= null){
     				 latitude = loc.getLatitude();
     				 longitude = loc.getLongitude();
     				// Toast.makeText(context,"Location available",Toast.LENGTH_SHORT).show();
     			 }
     			 else{
     				//Toast.makeText(context,"Location NOT available",Toast.LENGTH_SHORT).show();
     			 }
     		        EditText child = (EditText)vwParentRow.getChildAt(3);
     		        if(latitude == 0.0)
     		        	child.setText(String.valueOf("0.0"));
     		        else
     		        	child.setText(String.valueOf(latitude));
     		       child = (EditText)vwParentRow.getChildAt(5);
     		      if(longitude == 0.0)
   		        	child.setText(String.valueOf("0.0"));
   		        else
   		        	child.setText(String.valueOf(longitude));
     		       
     		      /* distanceVal =  distance(mainLatitude, mainLongitude, latitude, longitude);
     		      child = (EditText)vwParentRow.getChildAt(7);
     		      if(distanceVal == 0.0)
    		       child.setText(String.valueOf("0.0"));
     		      else
     		    	  child.setText(String.valueOf(distanceVal));*/
     		}
     	});

            convertView.setTag(vh);
        
        } else
        {
            vh = (ViewHolder)convertView.getTag();
        }
        
        vh.txtCornerNo.setText(String.valueOf(rowItem.getcornerNumber()));
        vh.edLat.setText("0");    
        vh.edLong.setText("0");
        vh.edDist.setText("0");
        vh.btnSubmit.setText("Find");
        return convertView;
    }

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String arg0) {
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
	//	Toast.makeText(context, "calc distance",Toast.LENGTH_SHORT).show();
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



}
