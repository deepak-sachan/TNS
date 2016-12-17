package com.example.tns;

import java.util.ArrayList;

import com.example.tns.ak.MainualDrawActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class BoundaryLayoutActivity extends Activity implements OnClickListener{
	
	ListView lstBoundaryLayoutDetails;
	AccessData objAdapter;
	Button btnSubmit;	
	//ArrayList<CornerDetailsDesign> listCorDet;
	Button btnSubBoundaryDet;
	//int cornerNum;
	String IPID;
	Double latitude , longitude;
	Cursor cursorBoundary ;
	View header;
	BoundaryLayoutDetailsAdapter adapterBoundaryLayoutDetails;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		  
		        super.onCreate(savedInstanceState);
		        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		        setContentView(R.layout.boundarylayout);		        
		        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.header);
		        Bundle bCornerNumber = getIntent().getExtras();	        
		       // IPID = "VODABIHAR0002";
		        IPID = bCornerNumber.getString("ipid");
		        latitude = bCornerNumber.getDouble("latitude");
		        longitude = bCornerNumber.getDouble("longitude");
		        lstBoundaryLayoutDetails = (ListView)findViewById(R.id.lstBoundaryLayout);
		        header = getLayoutInflater().inflate(R.layout.boundarylayoutheader, null);
		        lstBoundaryLayoutDetails.addHeaderView(header);
		        btnSubmit = (Button)findViewById(R.id.btnSubmitBoundaryLayoutDetails);
		        btnSubmit.setOnClickListener(this);
		        try{
		        	
		        	objAdapter = new AccessData(this);	        	
		        	objAdapter.openDataBase();        	
		        	//Toast.makeText(this,"ipid"+IPID,Toast.LENGTH_LONG).show();
		        	cursorBoundary = objAdapter.getBoundaryLayoutDetails(IPID);
		        //	Toast.makeText(this,String.valueOf(cursorBoundary.getCount()),Toast.LENGTH_SHORT).show();
		        	//CursorLoader c = new CursorLoader(this); 
					//c.loadInBackground();
		        	
		        	//startManagingCursor(cursorBoundary);        	        	
		        	adapterBoundaryLayoutDetails = new BoundaryLayoutDetailsAdapter(BoundaryLayoutActivity.this,cursorBoundary);		        	
		        	lstBoundaryLayoutDetails.setAdapter(adapterBoundaryLayoutDetails);
		        	
		        }catch(Exception ex){
		        	Toast.makeText(this, ex.getMessage(),Toast.LENGTH_LONG).show();
		        }
		  	}
	
/*	public void onResume(){
		super.onResume();
		Cursor oldC = adapterBoundaryLayoutDetails.getCursor();
		Cursor cursornew = objAdapter.getBoundaryLayoutDetails(IPID);
		adapterBoundaryLayoutDetails.changeCursor(cursornew);
		stopManagingCursor(oldC);
		lstBoundaryLayoutDetails.setAdapter(adapterBoundaryLayoutDetails);
	}
	
	public void onPause(){
		super.onPause();
		//adapterBoundaryLayoutDetails.notifyDataSetInvalidated();
		adapterBoundaryLayoutDetails.changeCursor(null);
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
		for (int i = 0; i <lstBoundaryLayoutDetails.getCount() ; i++) {
			if(i!=0){
            View vListSortOrder;
                   vListSortOrder=lstBoundaryLayoutDetails.getChildAt(i);     
                   try{
                	   EditText edit=(EditText)vListSortOrder.findViewById(R.id.edDistance);
                   		String distance=edit.getText().toString();
                   		Float dist = Float.valueOf(distance);
                   		
                   		TextView editip=(TextView)vListSortOrder.findViewById(R.id.txtBoundaryDescription);
                   		String operator=editip.getText().toString();
                   	
                   		TextView editc=(TextView)vListSortOrder.findViewById(R.id.txtBoundaryCornerNo);
                   		String corner=editc.getText().toString();
                   		int cornerNo = Integer.valueOf(corner); 
                   		
                   		int re = objAdapter.updateBoundaryDetails(IPID,cornerNo,dist);
                   		//Toast.makeText(this,"update"+String.valueOf(re),Toast.LENGTH_LONG).show();
                   	
                   }catch(Exception ex){}
			}   
		}
		
//		Intent noOfAdjBuildings = new Intent(this , NoOfAdjacentBuildingsActivity.class);
//		Bundle b = new Bundle();		
//		b.putString("ipid",IPID);
//		b.putDouble("latitude",latitude);
//		b.putDouble("longitude",longitude);
//		noOfAdjBuildings.putExtras(b);
//		startActivity(noOfAdjBuildings);
		
		Intent in = new Intent(this , MainualDrawActivity.class);
		Bundle b = new Bundle();		
		b.putString("ipid",IPID);
		b.putDouble("latitude",latitude);
		b.putDouble("longitude",longitude);
		in.putExtras(b);
		startActivity(in);
		
	}
	
}
