package com.example.tns;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class NoOfCornerDetailsActivity extends Activity implements View.OnClickListener{
	
	ListView lstNoOfCornerDetails;
	AccessData objAdapter;
	Button btnSubmit;
	String IPID;
	View header;
	Double latitude ,longitude;
	Cursor cursorNoofCornerDetails;
	NoOfCornerDetailsAdapter noofcornerDetailsAdapter;
	  @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
	        setContentView(R.layout.numberofcornerdetails);
	        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.header);
	        Bundle bBldgNumber = getIntent().getExtras();	        
	        IPID = bBldgNumber.getString("ipid");
	        latitude = bBldgNumber.getDouble("latitude");
	        longitude = bBldgNumber.getDouble("longitude");
	    //    IPID = "VODABIHAR0002";
	        objAdapter = new AccessData(this);
	        btnSubmit = (Button)findViewById(R.id.btnSubmit);
	        btnSubmit.setOnClickListener(this);
	        header = getLayoutInflater().inflate(R.layout.numofcornerdetailsheader, null);	        
	        lstNoOfCornerDetails = (ListView)findViewById(R.id.lstNumberOfCorners);
	        lstNoOfCornerDetails.addHeaderView(header);
	        try {
	        	 
	        	//objAdapter.createDataBase();
	        	objAdapter.openDataBase();	        	
	        	cursorNoofCornerDetails = objAdapter.getNoOfCornerDetails(IPID);	        	
	       // 	startManagingCursor(cursorNoofCornerDetails);
	        	noofcornerDetailsAdapter = new NoOfCornerDetailsAdapter(this,cursorNoofCornerDetails);
	        	lstNoOfCornerDetails.setAdapter(noofcornerDetailsAdapter);
	        	
	        }catch(Exception ex){
	        	//Toast.makeText(this,"T:"+ex.getMessage(),Toast.LENGTH_LONG).show();
	        }
	        
	  }
	  
	  
	  
	  /*public void onRestart(){
		  super.onRestart();
		  Cursor oldC = noofcornerDetailsAdapter.getCursor();
			Cursor cursornew = objAdapter.getNoOfCornerDetails(IPID);
			noofcornerDetailsAdapter.changeCursor(cursornew);
			stopManagingCursor(oldC);
			lstNoOfCornerDetails.setAdapter(noofcornerDetailsAdapter);
			Log.w("cornereonres","onrestart");
	  }
	  
	  public void onResume(){
			super.onResume();
			/*Cursor oldC = noofcornerDetailsAdapter.getCursor();
			Cursor cursornew = objAdapter.getNoOfCornerDetails(IPID);
			noofcornerDetailsAdapter.changeCursor(cursornew);
			stopManagingCursor(oldC);
			lstNoOfCornerDetails.setAdapter(noofcornerDetailsAdapter);
			Log.w("cornereonres","onres");
		}
		
		public void onPause(){
			super.onPause();
	//		noofcornerDetailsAdapter.notifyDataSetInvalidated();
			noofcornerDetailsAdapter.changeCursor(null);
			Log.w("cornereonres","onpuase");
		}*/
	  public void onResume(){
			super.onResume();
			
		}
		
		public void onPause(){
			super.onPause();
		}
		
		public void onDestroy(){
			super.onDestroy();
			objAdapter.close();
			
		}
		
		
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
		//objAdapter.openDataBase();
		int cornerNm = -1;
		String tempip ="";
				for (int i = 0; i <lstNoOfCornerDetails.getCount() ; i++) {
					if(i!=0){
		            View vListSortOrder;
		            float heightV =0;
		                   vListSortOrder=lstNoOfCornerDetails.getChildAt(i);
		                   
		                   try{
		                   EditText edit=(EditText)vListSortOrder.findViewById(R.id.edNoCorners);
		                   	String numberCorners=edit.getText().toString();
		                   	if(numberCorners!= "")
		                   cornerNm = Integer.valueOf(numberCorners);
		                   	else
		                   		cornerNm = 0;
		                   	EditText edit1=(EditText)vListSortOrder.findViewById(R.id.edHeight);
		                   	String height=edit1.getText().toString();
		                   	if(height!= ""){
		                   		 heightV =  Float.valueOf(height);
		                   	}
		                   	else
		                   		heightV = 0;
		                   	TextView editip=(TextView)vListSortOrder.findViewById(R.id.txtIPIDN);
		                   	tempip=editip.getText().toString();
		                   //	Toast.makeText(this,numberCorners+"::"+height+IPID,Toast.LENGTH_SHORT).show();
		                   objAdapter.updateNumberOfCorners(cornerNm,heightV, IPID,this);
		                                     	
		                   }catch(Exception ex){
		                	   //Toast.makeText(getBaseContext(),"ed"+ ex.getMessage(), Toast.LENGTH_LONG).show();
		                   }
					}  
				}
				
				
				Intent cornerDetails = new Intent(this , CornerDetailsActivity.class);
				Bundle b = new Bundle();
				b.putInt("numberCorner",cornerNm);
				b.putString("ipid",IPID);
				b.putDouble("latitude",latitude);
				b.putDouble("longitude",longitude);
				cornerDetails.putExtras(b);
				startActivity(cornerDetails);

		
	}    
}
