package com.example.tns;

import com.example.tns.ak.BuildingDrawActivity;

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

public class NoOfAdjacentBuildingsActivity extends Activity implements View.OnClickListener{
	
	ListView lstNoOfAdjBuildings;
	AccessData objAdapter;
	Button btnSubmit;
	String IPID;
	Double latitude ,longitude;
	Cursor cursorNoofBldgDetails;
	View header;
	Intent bldgDetails ;
	NoOfAdjacentBuildingsAdapter  noofadjbldgDetailsAdapter;
	  @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
	        setContentView(R.layout.numberofadjacentbuildings);
	        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.header);
	        Bundle bCornerNumber = getIntent().getExtras();	        
	        IPID = bCornerNumber.getString("ipid");
	        latitude = bCornerNumber.getDouble("latitude");
	        longitude = bCornerNumber.getDouble("longitude");
	        objAdapter = new AccessData(this);
	        btnSubmit = (Button)findViewById(R.id.btnSubmitAdjNoOfBuildings);
	        btnSubmit.setOnClickListener(this);
	        lstNoOfAdjBuildings = (ListView)findViewById(R.id.lstNumberOfAdjBuildings);
	        header = getLayoutInflater().inflate(R.layout.numberofadjbldgheader, null);
	        lstNoOfAdjBuildings.addHeaderView(header);
	        try {
	        	 
	        	
	        	objAdapter.openDataBase();	        	
	        	cursorNoofBldgDetails = objAdapter.getNoOfBldgDetails(IPID);
	        	if(cursorNoofBldgDetails!=null){
	        //	startManagingCursor(cursorNoofBldgDetails);
	        	noofadjbldgDetailsAdapter = new NoOfAdjacentBuildingsAdapter(this,cursorNoofBldgDetails);
	        	lstNoOfAdjBuildings.setAdapter(noofadjbldgDetailsAdapter);
	        	}
	        	
	        }catch(Exception ex){
	        	Toast.makeText(this,"T:"+ex.getMessage(),Toast.LENGTH_LONG).show();
	        }
	        
	  }
	  
	 /* public void onResume(){
			super.onResume();
			Cursor oldC = noofadjbldgDetailsAdapter.getCursor();
			Cursor cursornew = objAdapter.getNoOfBldgDetails(IPID);
			noofadjbldgDetailsAdapter.changeCursor(cursornew);
			stopManagingCursor(oldC);
			lstNoOfAdjBuildings.setAdapter(noofadjbldgDetailsAdapter);
			
			
		}
		
		public void onPause(){
			super.onPause();
			//noofadjbldgDetailsAdapter.notifyDataSetInvalidated();
			noofadjbldgDetailsAdapter.changeCursor(null);
		}*/
	  
	  public void onResume(){
			super.onResume();
			
		}
		
		public void onPause(){
			super.onPause();
		}
		
		
		public void onDestroy(){
			super.onDestroy();
		//	cursorNoofBldgDetails.close();
			objAdapter.close();
		}
	  
	  
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
		
		int adjBuildingsNm = -1;
		String tempip ="";
				for (int i = 0; i <lstNoOfAdjBuildings.getCount() ; i++) {
					if(i!=0){
		            View vListSortOrder;
		                   vListSortOrder=lstNoOfAdjBuildings.getChildAt(i);     
		                   try{
		                   EditText edit=(EditText)vListSortOrder.findViewById(R.id.edNoAdjBuildings);
		                   	String numberCorners=edit.getText().toString();
		                   	if(numberCorners!= "")		                   		
		                   		adjBuildingsNm = Integer.valueOf(numberCorners);
		                   	else
		                   		adjBuildingsNm = 0;
		                   	
		                   
		                   	TextView editip=(TextView)vListSortOrder.findViewById(R.id.txtIPIDN);
		                   	tempip=editip.getText().toString();
		                   
		                   int r =	objAdapter.updateNumberOfBldg(adjBuildingsNm, tempip,this);
		                  // Toast.makeText(this,"after update"+String.valueOf(r),Toast.LENGTH_LONG).show();	                   
		                   	                   	
		                   }catch(Exception ex){
		                	   Toast.makeText(getBaseContext(),"ed"+ ex.getMessage(), Toast.LENGTH_LONG).show();
		                   }
					}  
				}
				Bundle b = new Bundle();
				if(adjBuildingsNm > 0){
				bldgDetails = new Intent(this , BuildingDetailsActivity.class);				
				b.putInt("numberAdjBldg",adjBuildingsNm);
				b.putString("ipid",IPID);
				b.putDouble("latitude",latitude);
				b.putDouble("longitude",longitude);
				
				}
				else{
					bldgDetails = new Intent(this , BuildingDrawActivity.class);
					b.putString("ipid",IPID);
					b.putInt("numberAdjBldg",adjBuildingsNm);
				}
				bldgDetails.putExtras(b);
				startActivity(bldgDetails);

		
	}    
}
