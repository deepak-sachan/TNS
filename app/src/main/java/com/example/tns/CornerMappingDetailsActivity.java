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

public class CornerMappingDetailsActivity extends Activity implements View.OnClickListener{
	
	ListView lstNoOfCornerDetails;
	AccessData objAdapter;
	Button btnSubmit;
	ListView lstCornerMappingDet;
	ArrayList<CornerDetailsDesign> listCorDet;
	Button btnSubCorDet;
	int cornerNum;
	String IPID;
	Double latitude ,longitude;
	Cursor cursor ;
	View header;
	AccessData accessData;
	CornerMappingDetailsAdapter adapterCornerMappingDetails;
		
	NoOfCornerDetailsAdapter noofcornerDetailsAdapter;
	  @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);	   
	        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
	        setContentView(R.layout.cornermappingdetails);
	        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.header);
	        Bundle bCornerNumber = getIntent().getExtras();	        
	       // IPID = "VODABIHAR0002";
	        IPID = bCornerNumber.getString("ipid");
	        latitude = bCornerNumber.getDouble("latitude");
	        longitude = bCornerNumber.getDouble("longitude");
	        lstCornerMappingDet = (ListView)findViewById(R.id.lstCornerMappingDetails);
	        header = getLayoutInflater().inflate(R.layout.cornermappingheader, null);
	        lstCornerMappingDet.addHeaderView(header);
	        btnSubmit = (Button)findViewById(R.id.btnSubmitCornerMappingDetails);
	        btnSubmit.setOnClickListener(this);
	        try{
	        	
	        	accessData = new AccessData(this);	        	
	        	accessData.openDataBase();        	
	        	//Toast.makeText(this,"ipid"+IPID,Toast.LENGTH_LONG).show();
	        	cursor = accessData.getCornerMappingDetails(IPID);
	        
	        	//CursorLoader c = new CursorLoader(this); 
				//c.loadInBackground();
	        	
	        	//startManagingCursor(cursor);        	        	
	        	adapterCornerMappingDetails = new CornerMappingDetailsAdapter(CornerMappingDetailsActivity.this,cursor);
	        	lstCornerMappingDet.setAdapter(adapterCornerMappingDetails);
	        	
	        	
	        }catch(Exception ex){
	        	Toast.makeText(this, ex.getMessage(),Toast.LENGTH_LONG).show();
	        }
	  	}
	  
	 /* public void onResume(){
			super.onResume();
			Cursor oldC = adapterCornerMappingDetails.getCursor();
			Cursor cursornew = accessData.getCornerMappingDetails(IPID);
			adapterCornerMappingDetails.changeCursor(cursornew);
			stopManagingCursor(oldC);
			lstCornerMappingDet.setAdapter(adapterCornerMappingDetails);
		}
		
		public void onPause(){
			super.onPause();
			//adapterCornerMappingDetails.notifyDataSetInvalidated();
			adapterCornerMappingDetails.changeCursor(null);
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
		accessData.openDataBase();
		for (int i = 0; i <lstCornerMappingDet.getCount() ; i++) {
			if(i!=0){
            View vListSortOrder;
                   vListSortOrder=lstCornerMappingDet.getChildAt(i);     
                   try{
                	   EditText edit=(EditText)vListSortOrder.findViewById(R.id.edDistance);
                   		String distance=edit.getText().toString();
                   		Float dist = Float.valueOf(distance);
                   		EditText edit1=(EditText)vListSortOrder.findViewById(R.id.edHeight);
                   		String height=edit1.getText().toString();
                   		Float heightCorner = Float.valueOf(height);
                   		TextView editip=(TextView)vListSortOrder.findViewById(R.id.txtOperator);
                   		String operator=editip.getText().toString();
                   	
                   		editip=(TextView)vListSortOrder.findViewById(R.id.txtAntennaNo);
                   		int antena = Integer.valueOf(editip.getText().toString());
                   	
                   		editip=(TextView)vListSortOrder.findViewById(R.id.txtCornerNo);
                   		int cNo=Integer.valueOf(editip.getText().toString());
                   		
                   		//Toast.makeText(this,"Values:"+operator+":"+IPID+":"+String.valueOf(antena),Toast.LENGTH_LONG).show();
                   		int re = accessData.updateCornerMappingDetails(IPID,operator,antena,cNo,dist,heightCorner);
                   		//Toast.makeText(this,"update"+String.valueOf(re),Toast.LENGTH_LONG).show();
                   	
                   }catch(Exception ex){}
			}   
		}     
		
		Intent noOfAdjBuildings = new Intent(this , BoundaryLayoutActivity.class);
		Bundle b = new Bundle();		
		b.putString("ipid",IPID);
		b.putDouble("latitude",latitude);
		b.putDouble("longitude",longitude);
		noOfAdjBuildings.putExtras(b);
		startActivity(noOfAdjBuildings);
	}
}	  
