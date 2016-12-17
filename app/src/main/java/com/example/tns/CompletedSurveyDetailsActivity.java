package com.example.tns;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



public class CompletedSurveyDetailsActivity extends Activity implements OnItemClickListener {

	ListView lstsurveyDetails;
	AccessData accessData;
	PendingSurveyDetailsAdapter surveyAdapter;
	Cursor cursorSurvey;

	EditText autoSites;
	//SitesAutoCompleteView objAutoCompete;
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.pendingsurveydetails);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.header);
        lstsurveyDetails = (ListView)findViewById(R.id.lstSurveyList);  
        autoSites = (EditText)findViewById(R.id.autoSite);
        lstsurveyDetails.setOnItemClickListener(this);
        try{
     	
        		accessData = new AccessData(this);
        		accessData.createDataBase();
        		accessData.openDataBase();        	
        		cursorSurvey = accessData.getCompletedDetails();        		        		
        		startManagingCursor(cursorSurvey);        	        	
        		//autoSites.setThreshold(5);
        		surveyAdapter = new PendingSurveyDetailsAdapter(CompletedSurveyDetailsActivity.this,cursorSurvey,"Compelete");        		
        		lstsurveyDetails.setAdapter(surveyAdapter);
        		surveyAdapter.setFilterQueryProvider(new FilterQueryProvider() {

    		        @Override
    		        public Cursor runQuery(CharSequence constraint) {
    		            String partialValue = constraint.toString();
    		            Log.w("set",partialValue);
    		            return accessData.getCompletedSuggestions(partialValue);
    		            

    		        }
    		    });
    	//	autoSites.setAdapter(surveyAdapter);
    		autoSites.addTextChangedListener(new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					// TODO Auto-generated method stub
					
					//surveyAdapter.getFilterQueryProvider();
					if(s.toString() == null || s.toString() == ""){
						
					}
					
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					Log.w(s.toString(),"1seq");
					surveyAdapter.getFilter().filter(s.toString());
					
				}
			});

        	//	autoSites.setAdapter(surveyAdapter);
        		
        		
     }catch(Exception ex){
     	Toast.makeText(this, ex.getMessage(),Toast.LENGTH_LONG).show();
     }
	
  }
	
	public void onResume(){
		super.onResume();
		
	}
	
	public void onPause(){
		super.onPause();
	}
	
	public void onDestroy(){
		super.onDestroy();
		cursorSurvey.close();
		accessData.close();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		TextView siteID = (TextView) arg1.findViewById(R.id.txtSiteID);
		//Toast.makeText(this,siteID.getText().toString(),Toast.LENGTH_SHORT).show();
		Intent uploadData = new Intent(this,Upload.class);
		Bundle b = new Bundle();
		b.putString("ipid",siteID.getText().toString());
		uploadData.putExtras(b);
		startActivity(uploadData);
		
	}
}	
