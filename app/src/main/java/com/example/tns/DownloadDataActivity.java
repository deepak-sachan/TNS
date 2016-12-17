package com.example.tns;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.BreakIterator;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;



import android.R.bool;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;

import android.net.Uri;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings.Secure;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class DownloadDataActivity extends Activity{
	
	 Button btn_start;
	 ProgressBar progressBar;
	 TextView txt_percentage;
	 AccessData objAdapter;
	 ProgressDialog progressDialog;
	 String imei;
	 InsertData iData;
	 int count =0;
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
	        setContentView(R.layout.activity_download_async_task);
	        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.header);
	       // Toast.makeText(this,"Downloading data...",Toast.LENGTH_SHORT).show();
	        AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Starting download...");
		    builder.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
						String url ="http://tnssofts.com/wcf_get/RestServiceImpl.svc/RetSite/" +
								tm.getDeviceId();
						new ReadJSONFeedTask().execute(url);
						dialog.dismiss();
					}
				});
			
		    builder.create();
  		    builder.show();
	        progressDialog = new ProgressDialog(DownloadDataActivity.this);
	        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	        progressDialog.setIndeterminate(false);
	        progressDialog.setMessage("Downloading data....");
	        
	    }
	    
	    private void obtainedData(String data){
	   // 	Toast.makeText(this,data,Toast.LENGTH_LONG).show();
	    	objAdapter =  new AccessData(this);
	    //	Toast.makeText(this,"after downloading data..."+String.valueOf(data.length()),Toast.LENGTH_SHORT).show();
	    	if( data !=null && data.length() > 0){
	    	try{
	    		objAdapter.createDataBase();
	    		objAdapter.openDataBase();
				JSONObject objJson = new JSONObject(data);
				// Toast.makeText(this,"Parsing data...",Toast.LENGTH_LONG).show();
	         	JSONArray arr =  objJson.getJSONArray("RetSiteResult");
	         	//Toast.makeText(this,"to array"+String.valueOf(arr.length()),Toast.LENGTH_LONG).show();
         	for(int i=0;i<arr.length();i++){
     			JSONObject objJ = arr.getJSONObject(i);
     			String value = objJ.getString("vOpratorName");     			
	         	
	         			//value = objJ.getString("VIMEI");
	         			float nATPCFactor = (float) objJ.getDouble("nATPCFactor");
	         			float nAnteenaGain= (float) objJ.getDouble("nAnteenaGain");
	         			float nAnteenaHeightAGL= (float) objJ.getDouble("nAnteenaHeightAGL");
	         			float nBuildingHeightAGL= (float) objJ.getDouble("nBuildingHeightAGL");
	         			int nCarrier_Sector= objJ.getInt("nCarrier_Sector");
	         			float nChannelFreq = (float) objJ.getDouble("nChannelFreq");
	         			float nCominerLoss= (float) objJ.getDouble("nCominerLoss");
	         			float nDTXFactor= (float) objJ.getDouble("nDTXFactor");
	         			float nLat= (float) objJ.getDouble("nLat");
	         			float nLong= (float) objJ.getDouble("nLong");
	         			float nRFCableLength= (float) objJ.getDouble("nRFCableLength");
	         			float nTotalTilt= (float) objJ.getDouble("nTotalTilt");
	         			float nTxPower= (float) objJ.getDouble("nTxPower");
	         			float nUnitLoss= (float) objJ.getDouble("nUnitLoss");
	         			float nVerticalBeamWidth= (float) objJ.getDouble("nVerticalBeamWidth");
	         			float nsideLoabAttenuation= (float) objJ.getDouble("nsideLoabAttenuation");
	         			String vAddress = objJ.getString("vAddress");
	         			String vCircle= objJ.getString("vCircle");
	         			String vIMEI= objJ.getString("vIMEI");
	         			String vMakeModal= objJ.getString("vMakeModal");
	         			String vOpratorName= objJ.getString("vOpratorName");
	         			String vOpratorSiteId= objJ.getString("vOpratorSiteId");
	         			String vSiteId= objJ.getString("vSiteId");
	         			String vSiteIdCode= objJ.getString("vSiteIdCode");
	         			String vSiteName= objJ.getString("vSiteName");
	         			String vSurveyor= objJ.getString("vSurveyor");
	         			String vSysTemType= objJ.getString("vSysTemType");
	         			String vTowerType= objJ.getString("vTowerType");
	         			String vOpratorId= objJ.getString("vOpratorId");
	         			String bcom = objJ.getString("bComplete");
	         			int bcomplete = 0;
	         			if(bcom.equalsIgnoreCase("true")){
	         				bcomplete =1; 
	         			}
	         		long	id =objAdapter.insertSiteSurveyMaster(nATPCFactor, nAnteenaGain, nAnteenaHeightAGL, nBuildingHeightAGL, nCarrier_Sector, nChannelFreq, nCominerLoss, nDTXFactor, nLat, nLong, nRFCableLength, nTotalTilt, nTxPower, nUnitLoss, nVerticalBeamWidth, nsideLoabAttenuation, vAddress, vCircle, vIMEI, vMakeModal, vOpratorName, vOpratorSiteId, vSiteId, vSiteIdCode, vSiteName, vSurveyor, vSysTemType, vTowerType,vOpratorId,bcomplete);
	         		//Toast.makeText(this,"after downloading data..."+String.valueOf(id),Toast.LENGTH_SHORT).show();
     			//Log.w(String.valueOf(id),"id");
     			if(id > 0){     				
     				//Toast.makeText(this,"Data Downloaded",Toast.LENGTH_LONG).show();
     				AlertDialog.Builder builder = new AlertDialog.Builder(this);
     				builder.setTitle("Total sites downloaded :"+String.valueOf(id));
     			    builder.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
     						
     						@Override
     						public void onClick(DialogInterface dialog, int which) {
     							// TODO Auto-generated method stub
//     							Intent intent = new Intent(getBaseContext(),AllOptionsActivity.class);     							
//     							startActivity(intent);
     							dialog.dismiss();
     							DownloadDataActivity.this.finish();
     							
     						}
     					});
     				
     			    builder.create();
     	  		    builder.show();
     			}
     			
         	
			}}catch(Exception ex){
				Toast.makeText(getApplicationContext(),"Some error in download...."+ex.getMessage(),Toast.LENGTH_LONG).show();
				ex.printStackTrace();
				DownloadDataActivity.this.finish();
				return;
				
			}	
	    	}else{
	    		Toast.makeText(getApplicationContext(),"Site Data Not Present!",Toast.LENGTH_SHORT).show();
	    		DownloadDataActivity.this.finish();
	    	}
	    	
	    }
	       
	  @SuppressLint("NewApi")
	@TargetApi(Build.VERSION_CODES.CUPCAKE)
	private class ReadJSONFeedTask extends AsyncTask<String ,Integer,String>{
		  
		  int myProgress;
		  
		  @SuppressLint("NewApi")
		@Override
		    protected void onPreExecute() {
		        super.onPreExecute();
		        progressDialog.show();
		    }
		  
			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				StringBuilder sbr = new StringBuilder();
		    	HttpClient httpclient = new DefaultHttpClient();
		    	HttpGet httpGet = new HttpGet(params[0]);
		    	try{
		    		HttpResponse objHttpResponse = httpclient.execute(httpGet); 
		    		int status = objHttpResponse.getStatusLine().getStatusCode();
		    		//Log.w(String.valueOf(status),"status");
		    		if(status == 200){
					InputStream objInputStream = objHttpResponse.getEntity().getContent();
					InputStreamReader objInputStreamReader = new InputStreamReader(objInputStream);
					BufferedReader objBufferedReader = new BufferedReader(objInputStreamReader);
					int total=0;
		         	String line = null;
		         	while((line = objBufferedReader.readLine())!=null){
		         		sbr.append(line + "\n");
		         		
		         	}
		    	}
		    	}catch(Exception ex){
		    		return null;

		    	}
		    	
		    	return sbr.toString();
			}
			
			/* @Override
			    protected void onProgressUpdate(Integer... progress) {
			        super.onProgressUpdate(progress);
			        progressDialog.setProgress(progress[0]);
			    }*/
			
			protected void onPostExecute(String data){
				
				progressDialog.dismiss();
				DownloadDataActivity.this.obtainedData(data);
				
				
			}
	    }
	

	}


