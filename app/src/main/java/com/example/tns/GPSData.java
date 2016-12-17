package com.example.tns;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.Criteria;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

@SuppressLint("NewApi")
public class GPSData  extends Activity implements LocationListener,GpsStatus.Listener{
	double longitude=0,latitude=0;
	double lat =0, lng=0;
	float acc=0;
	float accuracy;
	EditText edLatitude , edLongitude,edAccuracy,edLastUpdated;
	String provider,IPID;
	LocationManager locationManager;
	AccessData objAdapter;
	
	private int year;
    private int month;
    private int day;
    static final int DATE_PICKER_ID = 1111; 
    String strGpsStatus="";
    TextView txtSetelite;
    Spinner spinTwrType;
    List<String> spinTwrArray;
    EditText edtTwrHt;
	
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.gpsactivity);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.header);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

       // IPID = "VODABIHAR002";
        Bundle bBldgNumber = getIntent().getExtras();        
        IPID = bBldgNumber.getString("ipid");
        edLatitude = (EditText)findViewById(R.id.edLatitude);
        edLongitude = (EditText)findViewById(R.id.edLongitude);
        edAccuracy = (EditText)findViewById(R.id.edAccuracy);
        edLastUpdated = (EditText)findViewById(R.id.edLastUpdated); 
        edtTwrHt = (EditText)findViewById(R.id.tyrHight); 
        txtSetelite=(TextView) findViewById(R.id.txtSeteLite);
        spinTwrType=(Spinner) findViewById(R.id.spinner1);
        spinTwrArray=new ArrayList<String>();
        spinTwrArray.add("RTT");
        spinTwrArray.add("GBT");
        spinTwrArray.add("RTP");
        spinTwrArray.add("POLE MOUNT");
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, spinTwrArray);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    spinTwrType.setAdapter(adapter);
	    
        edLastUpdated.setHint("Click to set date");
        
        getLocationData();
        locationManager.addGpsStatusListener(this);
        final Calendar c = Calendar.getInstance();
        year  = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day   = c.get(Calendar.DAY_OF_MONTH);
        edLastUpdated.setText(new StringBuilder().append(day)
                .append("/").append(month+1).append("/").append(year)
                .append(" "));
        edLastUpdated.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(DATE_PICKER_ID);
			}
		});

    }

	public void getLocationData(){

	    locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
	    
	    //locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	    // Define the criteria how to select the locatioin provider -> use
	    // default
	    
	    if (!(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))) 
		{
		    AlertDialog.Builder builder = new AlertDialog.Builder(GPSData.this);
		    builder.setTitle("Location Manager");
		    builder.setMessage("Would you like to enable GPS?");
		    builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
		        @Override
		        public void onClick(DialogInterface dialog, int which) {
		            //Launch settings, allowing user to make a change
		            Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		            startActivity(i);
		        }
		    });
		    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
		        @Override
		        public void onClick(DialogInterface dialog, int which) {
		            //No location service, no Activity
		            finish();
		        }
		    });
		    builder.create().show();
			
		}
	    
	    try {
			
		
	    Criteria criteria = new Criteria();
	    criteria.setAccuracy(Criteria.ACCURACY_FINE);
	    provider = locationManager.getBestProvider(criteria, true);
	    Location location = locationManager.getLastKnownLocation(provider);
	    
	    // Initialize the location fields
	    if (location != null) {
	      System.out.println("Provider " + provider + " has been selected.");
	      onLocationChanged(location);
	    } else {
	  //  Toast.makeText(getBaseContext(), "Location NOT available", Toast.LENGTH_LONG).show();
	    
	    }
	    
		} catch (Exception e) {
			// TODO: handle exception
		}
	    
	  }

	  /* Request updates at startup */
	  @Override
	  protected void onResume() {
	    super.onResume();
	    if (!(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))) 
		{
		    AlertDialog.Builder builder = new AlertDialog.Builder(GPSData.this);
		    builder.setTitle("Location Manager");
		    builder.setMessage("Would you like to enable GPS?");
		    builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
		        @Override
		        public void onClick(DialogInterface dialog, int which) {
		            //Launch settings, allowing user to make a change
		            Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		            startActivity(i);
		        }
		    });
		    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
		        @Override
		        public void onClick(DialogInterface dialog, int which) {
		            //No location service, no Activity
		            finish();
		        }
		    });
		    builder.create().show();
			
		}
	    else
	    {
	    locationManager.requestLocationUpdates(provider, 0, 0, this); 
	    }
	  }

	  /* Remove the locationlistener updates when Activity is paused */
	  @Override
	  protected void onPause() {
	    super.onPause();
	    locationManager.removeUpdates(this);
	  }

	  @Override
	  public void onLocationChanged(Location location) {
	    lat = (double) (location.getLatitude());
	    lng = (double) (location.getLongitude());
	    acc = (float)(location.getAccuracy());
	    setValues();
	  }

	  
	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		//Toast.makeText(this,"provider not available!",Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		//Toast.makeText(this,"provider available!",Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	
	public String roundUp(double value){
		DecimalFormat df = new DecimalFormat("##.######");
		return df.format(value);		
	}
	
	public void setValues(){
		edLatitude.setText(String.valueOf(roundUp(lat)));
	    edLongitude.setText(String.valueOf(roundUp(lng)));
	   // edAccuracy.setText(String.valueOf(acc));
	    
	    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		   //get current date time with Date()
		   Date date = new Date();
		  // edLastUpdated.setText(dateFormat.format(date));
	}
	
	
	public void getGPSValues(View view){
		try{
		//Toast.makeText(this,"inside get gps",Toast.LENGTH_LONG).show();
		/*Log.w("lat",String.valueOf(lat));
		Log.w("lng",String.valueOf(lng));
		Log.w("acc",String.valueOf(acc));
		Toast.makeText(this,String.valueOf(lat),Toast.LENGTH_LONG).show();
		Toast.makeText(this,String.valueOf(lng),Toast.LENGTH_LONG).show();
		edLatitude.setText(String.valueOf(roundUp(lat)));
	    edLongitude.setText(String.valueOf(roundUp(lng)));
	    edAccuracy.setText(String.valueOf(acc));
	    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");*/
		   //get current date time with Date()
		   //Date date = new Date();
		   //edLastUpdated.setText(dateFormat.format(date));
			setValues();
		}catch(Exception ex){
			//Toast.makeText(this,"Ex:"+ex.getMessage(),Toast.LENGTH_LONG).show();
		}
	}
	
	public void savenext(View view){
		try{
		objAdapter = new AccessData(this);
        objAdapter.openDataBase();
        String dateSurvey = edLastUpdated.getText().toString();
		int i= objAdapter.insertGPSData(lat,lng,IPID,dateSurvey,spinTwrType.getSelectedItem().toString(),Double.parseDouble(edtTwrHt.getText().toString()));
		//Toast.makeText(this,"GPS data saved!",Toast.LENGTH_SHORT).show();
		}catch(Exception ex){
			//Toast.makeText(this,"GPSex"+ex.getMessage(),Toast.LENGTH_SHORT).show();
		}
		//Toast.makeText(this,String.valueOf(i),Toast.LENGTH_SHORT).show();
		Intent antenna = new Intent(this ,AntennaDetailsActivity.class);
		Bundle b = new Bundle();
		b.putString("ipid",IPID);
		b.putDouble("latitude",lat);
		b.putDouble("longitude",lng);
		antenna.putExtras(b);
		startActivity(antenna);
		
	}
	
	  @Override
	    protected Dialog onCreateDialog(int id) {
	        switch (id) {
	        case DATE_PICKER_ID:
	             
	            // open datepicker dialog. 
	            // set date picker for current date 
	            // add pickerListener listner to date picker
	            return new DatePickerDialog(this, pickerListener, year, month,day);
	        }
	        return null;
	    }
	 
	 private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {
		 
	        // when dialog box is closed, below method will be called.
	        @Override
	        public void onDateSet(DatePicker view, int selectedYear,
	                int selectedMonth, int selectedDay) {
	             
	            year  = selectedYear;
	            month = selectedMonth;
	            day   = selectedDay;
	 
	            // Show selected date 
	            edLastUpdated.setText(new StringBuilder().append(day)
	                    .append("/").append(month+1).append("/").append(year)
	                    .append(" "));
	     
	           }

	        };


	@Override
	public void onGpsStatusChanged(int event) {
		// TODO Auto-generated method stub
		
	     GpsStatus gpsStatus = locationManager.getGpsStatus(null);
	        if(gpsStatus != null) {
	            Iterable<GpsSatellite>satellites = gpsStatus.getSatellites();
	            Iterator<GpsSatellite>sat = satellites.iterator();
	            int i=0;
	            while (sat.hasNext()) {
	                GpsSatellite satellite = sat.next();
	                i++;
	               // strGpsStatus+= (i++) + ": " + satellite.getPrn() + "," + satellite.usedInFix() + "," + satellite.getSnr() + "," + satellite.getAzimuth() + "," + satellite.getElevation()+ "\n\n";
	            }
	            txtSetelite.setText(Integer.toString(i));
	        }
		
	}
	
}
