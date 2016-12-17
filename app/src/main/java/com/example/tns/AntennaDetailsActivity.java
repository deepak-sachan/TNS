package com.example.tns;

import android.os.Bundle;
import android.provider.Settings.Secure;
import android.app.Activity;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AntennaDetailsActivity extends Activity implements View.OnClickListener {

	AccessData objAccessData;
	ListView lstAntennaDetails;
	Button btnSubmit;
	Cursor cursor;
	String IPID;
	Double latitude ,longitude;
	View header;
	AntennaDetailsCursorAdapter cursorAntennaDetailsAdapter;
    @SuppressWarnings("deprecation")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_antenna_details);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.header);   
        
       String vAndroidDeviceId = Secure.getString(this.getContentResolver(),
                Secure.ANDROID_ID);
      
        Bundle bBldgNumber = getIntent().getExtras();        
        IPID = bBldgNumber.getString("ipid");
       // Toast.makeText(this,IPID,Toast.LENGTH_SHORT).show();
        latitude = bBldgNumber.getDouble("latitude");
        longitude = bBldgNumber.getDouble("longitude");
        lstAntennaDetails = (ListView)findViewById(R.id.lstAntennaList);        
        header = getLayoutInflater().inflate(R.layout.antennadetailsheader, null);        
        lstAntennaDetails.addHeaderView(header);
        btnSubmit = (Button)findViewById(R.id.btnSub);
        btnSubmit.setOnClickListener(this);
        try{
        	
        	objAccessData = new AccessData(this);        
        	objAccessData.openDataBase();
        	cursor = objAccessData.getAntennaDetails(IPID);
        	
        	//startManagingCursor(cursor);        	        	
        	cursorAntennaDetailsAdapter = new AntennaDetailsCursorAdapter(AntennaDetailsActivity.this,cursor);
        	lstAntennaDetails.setAdapter(cursorAntennaDetailsAdapter);
        	
        	
        }catch(Exception ex){
        //	Toast.makeText(this,"Ex:"+ex.getMessage(),Toast.LENGTH_LONG).show();
        }
        
    }
    
    
  /*  public void onResume(){
		
		Cursor oldC = cursorAntennaDetailsAdapter.getCursor();
		Cursor cursornew = objAccessData.getAntennaDetails(IPID);
		cursorAntennaDetailsAdapter.changeCursor(cursornew);
		stopManagingCursor(oldC);
		lstAntennaDetails.setAdapter(cursorAntennaDetailsAdapter);
		super.onResume();
	}
    
    public void onRestart(){
		Log.w("re","start");
 		Cursor oldC = cursorAntennaDetailsAdapter.getCursor();
 		Cursor cursornew = objAccessData.getAntennaDetails(IPID);
 		cursorAntennaDetailsAdapter.changeCursor(cursornew);
 		stopManagingCursor(oldC);
 		lstAntennaDetails.setAdapter(cursorAntennaDetailsAdapter);
 		super.onRestart();
 	}
	
	public void onPause(){
		super.onPause();
		//cursorAntennaDetailsAdapter.notifyDataSetInvalidated();
		cursorAntennaDetailsAdapter.changeCursor(null);
		Log.w("anteonres","pause");
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
		objAccessData.close();
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       return true;
    }
    

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		objAccessData.openDataBase();
		int antennaNm =-1;
		String ipid , operatorID;
		//Toast.makeText(this,"fetching..."+String.valueOf(lstAntennaDetails.getCount()), Toast.LENGTH_SHORT).show();
				for (int i = 0; i <lstAntennaDetails.getCount() ; i++) {
					if(i!=0){
		            View vListSortOrder;
		                   vListSortOrder=lstAntennaDetails.getChildAt(i);     
		                   try{	                	
		                	   
		                   EditText edit=(EditText)vListSortOrder.findViewById(R.id.edAntennaNo);
		                   	String numberAntenna=edit.getText().toString();
		                   	
		                    antennaNm = Integer.valueOf(numberAntenna);
		                    TextView txtOperatorID = (TextView) vListSortOrder.findViewById(R.id.hiddenOpratorID);
		                    operatorID = txtOperatorID.getText().toString();
		                   	TextView editip=(TextView)vListSortOrder.findViewById(R.id.txtIPID);
		                   	ipid=IPID;
		                //Toast.makeText(this,"calling access data"+numberAntenna+":"+operatorID+":"+ipid, Toast.LENGTH_SHORT).show();
		                    objAccessData.checkForAntennaDetails(antennaNm, IPID, operatorID,this);
		                                     	
		                   }
		                   catch(Exception ex){
		                	   //Toast.makeText(getBaseContext(),"Exception:"+ ex.getMessage(), Toast.LENGTH_LONG).show();
		                   }
					}   
				}
		
		
		Intent numberOfCornerDetails = new Intent(this,NoOfCornerDetailsActivity.class);
		Bundle b = new Bundle();
		b.putString("ipid",IPID);
		b.putDouble("latitude",latitude);
		b.putDouble("longitude",longitude);
		numberOfCornerDetails.putExtras(b);
		startActivity(numberOfCornerDetails);
	}
	
	
	//<View android:background="@drawable/listverticalline"/>
	
	/*public void insertToBuilding(){
		try{
			objAccessData.openDataBase();        	
			
		}catch(Exception ex){
			Toast.makeText(this,ex.getMessage(),Toast.LENGTH_LONG).show();
		}
	}
	Cursor oldCursor = mAdapter.getCursor();
mAdapter.changeCursor(newCursor);
stopManagingCursor(oldCursor);
	*
	*/
    
	

	
}

