package com.example.tns;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class BuildingMappingDetailsActivity extends Activity implements View.OnClickListener{ 
	
	ListView lstNoOfBldgDetails;
	AccessData objAdapter;
	Button btnSubmit;
	ListView lstBldgMappingDet;
	View header;
	Button btnSubBldgDet;
	Double latitude ,longitude;
	int BldgNum;
	String IPID;
	Cursor cursor;
	AccessData accessData;
	BuildingMappingDetailsAdapter adapterBldgMappingDetails;
		
	NoOfCornerDetailsAdapter noofcornerDetailsAdapter;
	  @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
	        setContentView(R.layout.buildingmappingdetails);
	        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.header);
	        Bundle bBldgNumber = getIntent().getExtras();
	        IPID = bBldgNumber.getString("ipid");
	        latitude = bBldgNumber.getDouble("latitude");
	        longitude = bBldgNumber.getDouble("longitude");
	        lstBldgMappingDet = (ListView)findViewById(R.id.lstBuildingMappingDetails);
	        header = getLayoutInflater().inflate(R.layout.bldgmappingdetailsheader, null);
	        lstBldgMappingDet.addHeaderView(header);
	        btnSubBldgDet = (Button)findViewById(R.id.btnSubmitBuildingMappingDetails);
	        btnSubBldgDet.setOnClickListener(this);
	        try{
	        	
	        	accessData = new AccessData(this);	        	
	        	accessData.openDataBase();        	
	        	cursor = accessData.getBldgMappingDetails(IPID);
	        	
	        	//CursorLoader c = new CursorLoader(this); 
				//c.loadInBackground();
	        	//startManagingCursor(cursor);        	        	
	        	adapterBldgMappingDetails = new BuildingMappingDetailsAdapter(this,cursor,IPID);
	        	lstBldgMappingDet.setAdapter(adapterBldgMappingDetails);
	        	
	        }catch(Exception ex){
	        	Toast.makeText(this, ex.getMessage(),Toast.LENGTH_LONG).show();
	        }
	  	}
	  
	/*  public void onResume(){
			super.onResume();
			Cursor oldC = adapterBldgMappingDetails.getCursor();
			Cursor cursornew = objAdapter.getBldgMappingDetails(IPID);
			adapterBldgMappingDetails.changeCursor(cursornew);
			stopManagingCursor(oldC);
			lstBldgMappingDet.setAdapter(adapterBldgMappingDetails);
		}
		
		public void onPause(){
			super.onPause();
		//	adapterBldgMappingDetails.notifyDataSetInvalidated();
			adapterBldgMappingDetails.changeCursor(null);
		}*/
	  
	  public void onResume(){
			super.onResume();
			
		}
		
		public void onPause(){
			super.onPause();
		}
		
		
		public void onDestroy(){
			super.onDestroy();
			cursor.close();
			accessData.close();
		}
	  
	  
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		for (int i = 0; i <lstBldgMappingDet.getCount() ; i++) {
			if(i!=0){
            View vListSortOrder;
                   vListSortOrder=lstBldgMappingDet.getChildAt(i);     
                   try{
                	   String distance="";
                	   Float dist=0.0f;
                	   EditText edit=(EditText)vListSortOrder.findViewById(R.id.edDistance);
                	   if(edit.getText().toString() != null){
                   		 distance=edit.getText().toString();
                   		}
                	   if(distance!= null)
                   		dist = Float.valueOf(distance);                   		
                   		
                   		TextView editip=(TextView)vListSortOrder.findViewById(R.id.hiddenOpratorID);
                   		String operator=editip.getText().toString();	
                   	
                   		editip=(TextView)vListSortOrder.findViewById(R.id.txtAntennaNo);
                   		int antena = Integer.valueOf(editip.getText().toString());
                   	
                   		editip=(TextView)vListSortOrder.findViewById(R.id.txtBldgNo);
                   		int cNo=Integer.valueOf(editip.getText().toString());
                   		//Toast.makeText(this,operator + IPID+String.valueOf(dist)+String.valueOf(antena)+String.valueOf(cNo), Toast.LENGTH_SHORT).show();
                   		int re = accessData.updateBldgMappingDetails(IPID,operator,antena,cNo,dist,this);
                   		//Toast.makeText(this,"Update"+String.valueOf(re),Toast.LENGTH_LONG).show();
                   	
                   	}catch(Exception ex){
                   		ex.printStackTrace();
                   		Toast.makeText(this,"Exce"+ex.getMessage(),Toast.LENGTH_LONG).show();
                   }
			}
			}

		Bundle b = new Bundle();
		b.putString("ipid",IPID);
		Intent tower = new Intent(this,MainDrawActivity.class);
		tower.putExtras(b);
		startActivity(tower);
		
	}
}	  
