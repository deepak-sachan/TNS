package com.example.tns;

import java.util.ArrayList;

import com.example.tns.ak.BuildingDrawActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class BuildingDetailsActivity extends Activity implements View.OnClickListener{
	
	ListView lstNoOfBldgDetails;
	AccessData objAdapter;
	Button btnSubmit;
	ListView lstBldgDet;
	ArrayList<BuildingDetailsDesign> listBldgDet;
	Button btnSubBldgDet;
	int bldgNum;
	Double latitude ,longitude;
	String IPID;
	AccessData accessData;
	View header;
	BuildingDetailsAdapter adapter;
	BuildingDetailsDesign objBldgDetDesign;
	public static final float[] latid = new float[]{};
	public static final float[] longi = new float[]{};
	public static final float[] dist = new float[]{};
	public static final float[] azimuth = new float[]{};
	public static final int[] floors = new int[]{};
	public static final int[] number = new int[]{};
	

	
	
	NoOfCornerDetailsAdapter noofcornerDetailsAdapter;
	  @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
	        setContentView(R.layout.buildingdetails);
	        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.header);
	        Bundle bBldgNumber = getIntent().getExtras();
	        bldgNum = bBldgNumber.getInt("numberAdjBldg");
	      //  Toast.makeText(this,String.valueOf(bldgNum),Toast.LENGTH_LONG).show();
	        IPID = bBldgNumber.getString("ipid");
	        latitude = bBldgNumber.getDouble("latitude");
	        longitude = bBldgNumber.getDouble("longitude");
	        btnSubBldgDet = (Button)findViewById(R.id.btnSubmitBldgDetails);
	        btnSubBldgDet.setOnClickListener(this);
	        lstNoOfBldgDetails = (ListView)findViewById(R.id.lstBldgDetails);
	        header = getLayoutInflater().inflate(R.layout.bldgdetailsheader, null);
	        lstNoOfBldgDetails.addHeaderView(header);
	        accessData = new AccessData(this);
	        setListDetails();
	 }
	  
	  public void onResume(){
			super.onResume();
			
		}
		
		public void onPause(){
			super.onPause();
		}
		
		
		public void onDestroy(){
			super.onDestroy();
			accessData.close();
		}
	  
	  public void setListDetails(){
		  
			 listBldgDet = new ArrayList<BuildingDetailsDesign>();
			 for(int i=0;i<bldgNum ;i++){
				 Log.w("inside setlist",String.valueOf(i));
				 objBldgDetDesign = new BuildingDetailsDesign(i+1,0,0,0,0, 0,0);
				 listBldgDet.add(objBldgDetDesign);
			 }		 
			 adapter = new BuildingDetailsAdapter(this,R.layout.buildingdetailsonerow,listBldgDet,latitude,longitude);
			 lstNoOfBldgDetails.setAdapter(adapter);
		 }
 
	  
	  
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub		
		accessData.openDataBase();
		for (int i = 0; i <lstNoOfBldgDetails.getCount() ; i++) {
			if(i!=0){
            View vListSortOrder;
                   vListSortOrder=lstNoOfBldgDetails.getChildAt(i);     
                   try{
                	TextView txtBldNo = (TextView)vListSortOrder.findViewById(R.id.txtBldgDetails);   
                	   int bldgNo = Integer.valueOf(txtBldNo.getText().toString());
                   EditText edit=(EditText)vListSortOrder.findViewById(R.id.edDistance);
                   String temp=edit.getText().toString();
                   Double distance = Double.valueOf(temp);
                   
                   edit=(EditText)vListSortOrder.findViewById(R.id.edLatitude);
                   temp=edit.getText().toString();
                   Double lati = Double.valueOf(temp);
                   
                   edit=(EditText)vListSortOrder.findViewById(R.id.edLongitude);
                   temp=edit.getText().toString();
                   Double longi = Double.valueOf(temp);
                   
                   edit=(EditText)vListSortOrder.findViewById(R.id.edAzimuth);
                   temp=edit.getText().toString();
                   int azimuth = Integer.valueOf(temp);
                   
                   edit=(EditText)vListSortOrder.findViewById(R.id.edFloor);
                   temp=edit.getText().toString();
                   int floors = Integer.valueOf(temp);
                   

                   edit=(EditText)vListSortOrder.findViewById(R.id.edBldgHeight);
                   temp=edit.getText().toString();
                   Double bldgHeight = Double.valueOf(temp);
                   
                  //nBldgHeight
                  int re= accessData.updateBldgDetails(i,distance,lati,longi,azimuth,floors,IPID,bldgHeight,this);
                //  Toast.makeText(this,"Update"+String.valueOf(re),Toast.LENGTH_LONG).show();
                   }catch(Exception ex){
                	   Toast.makeText(getBaseContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                   }
			}
		}
		Intent bldgMappingDetails = new Intent(this ,BuildingDrawActivity.class);
		Bundle b = new Bundle();
		b.putString("ipid",IPID);
		b.putInt("numberAdjBldg",bldgNum);
		//b.putDouble("latitude",latitude);
		//b.putDouble("longitude",longitude);
		bldgMappingDetails.putExtras(b);
		startActivity(bldgMappingDetails);


	}
}	
		
				
	
