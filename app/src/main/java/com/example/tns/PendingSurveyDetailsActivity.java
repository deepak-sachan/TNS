package com.example.tns;

import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



@SuppressLint("NewApi")
public class PendingSurveyDetailsActivity extends Activity implements OnItemClickListener {

	ListView lstsurveyDetails;
	AccessData accessData;
	PendingSurveyDetailsAdapter surveyAdapter;
	Cursor cursorSurvey;
	Filter siteFilter;
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
        		cursorSurvey = accessData.getPendingDetails();        		        		
        		startManagingCursor(cursorSurvey);        	        	
        		//autoSites.setThreshold(5);
        		surveyAdapter = new PendingSurveyDetailsAdapter(PendingSurveyDetailsActivity.this,cursorSurvey,"pending");        		
        		lstsurveyDetails.setTextFilterEnabled(true);
        		lstsurveyDetails.setFastScrollEnabled(true);
        		lstsurveyDetails.setAdapter(surveyAdapter);
        		//autoSites.setAdapter(surveyAdapter);
        		surveyAdapter.setFilterQueryProvider(new FilterQueryProvider() {

        		        @Override
        		        public Cursor runQuery(CharSequence constraint) {
        		            String partialValue = constraint.toString();
        		            Log.w("set",partialValue);
        		            return accessData.getSuggestions(partialValue);
        		            

        		        }
        		    });
        	//	autoSites.setAdapter(surveyAdapter);
        		autoSites.addTextChangedListener(new TextWatcher() {
					
					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {
						// TODO Auto-generated method stub
						
						//surveyAdapter.getFilterQueryProvider();
						updateList(s.toString());
						
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
        		 // Set the FilterQueryProvider, to run queries for choices
                // that match the specified input.
                
        	/*	autoSites.setAdapter(surveyAdapter);
        		autoSites.setOnClickListener(new View.OnClickListener() {
					
					public void onClick(View v) {
						// TODO Auto-generated method stub
						autoSites.setText("");
					}
				});
        		
        	
        		autoSites.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> listView, View view,
	                        int position, long id) {
						// TODO Auto-generated method stub
						Toast.makeText(getBaseContext(), "auto item click",Toast.LENGTH_SHORT).show();
						  Cursor cursor = (Cursor) listView.getItemAtPosition(position);
						  
			                // Get the state's capital from this row in the database.
			                String vSiteIdCode = 
			                    cursor.getString(cursor.getColumnIndexOrThrow("vSiteIdCode"));
			 
			                // Update the parent class's TextView
			                autoSites.setText(vSiteIdCode);
					}
				});*/
        		
     }catch(Exception ex){
     	//Toast.makeText(this, ex.getMessage(),Toast.LENGTH_LONG).show();
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
		Intent iGPSData = new Intent(this,GPSData.class);
		Bundle b = new Bundle();
		b.putString("ipid",siteID.getText().toString());
		iGPSData.putExtras(b);
		startActivity(iGPSData);
		
	}
	
	public void updateList(String str)
	{
		try {
			accessData.createDataBase();
		
		accessData.openDataBase();
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cursorSurvey = accessData.getPendingDetailsSearch(str);        		        		
		startManagingCursor(cursorSurvey);        	        	
		//autoSites.setThreshold(5);
		lstsurveyDetails.setVisibility(View.GONE);
		surveyAdapter = new PendingSurveyDetailsAdapter(PendingSurveyDetailsActivity.this,cursorSurvey,"pending");        		
		lstsurveyDetails.setTextFilterEnabled(true);
		lstsurveyDetails.setFastScrollEnabled(true);
		lstsurveyDetails.setVisibility(View.VISIBLE);
		lstsurveyDetails.setAdapter(surveyAdapter);
	}


	
}	
