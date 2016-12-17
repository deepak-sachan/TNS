package com.example.tns;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.DateTimeKeyListener;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

//URL Site_Survey-http://www.tnssofts.com/Ashwani/ReqService.svc/uploadsiteSurvey
public class UploadToServerActivity extends Activity{
	
	AccessData objAdapter;
	Cursor dataUpload;
	JSONObject jObj;
	String postString, postData;
	int count=0;
	String[] urls = new String[]{};
	JSONArray jArray;
	JSONObject jMain;
	ArrayList<String> resData;
	ArrayList<String> resLink;
	ProgressDialog progressDialog;
	int countImages =0;
	String ipid="";
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);     
        setContentView(R.layout.up);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.header);
        ipid = getIntent().getStringExtra("ipid");
        objAdapter = new AccessData(this);
        objAdapter.openDataBase();
      //  Toast.makeText(this,ipid,Toast.LENGTH_SHORT).show();
	    //postData = jMain.toString();		
        resData = new ArrayList<String>();
        resLink = new ArrayList<String>();
		//Toast.makeText(this,"posting data",Toast.LENGTH_SHORT).show();
	
		progressDialog = new ProgressDialog(UploadToServerActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);        
        
      //  progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Uploading data....");
        String res1=getSiteSurveyDetails();
        System.out.println(" siteres= "+res1);
        resData.add(res1);		
		resLink.add("http://www.tnssofts.com/Ashwani/ReqService.svc/uploadsitesurveymaster");
        
        
        
		String res  = getCornerDetails();
		resData.add(res);		
		resLink.add("http://www.tnssofts.com/Ashwani/ReqService.svc/uploadsitecorner");
		res = getBuildingDetails();
		resData.add(res);
		resLink.add("http://www.tnssofts.com/Ashwani/ReqService.svc/uploadsitebuilding");
		res = getBoundaryLayoutDetails();
		resData.add(res);
		resLink.add("http://www.tnssofts.com/Ashwani/ReqService.svc/uploadsiteboundarydetails");
		//progressDialog.setMax(resLink.size());
		new SendDataToServerTask().execute("upload");
	//	new SendDataToServerTask().execute("http://www.tnssofts.com/Ashwani/ReqService.svc/uploadsiteSurveymaster");
		//new SendDataToServerTask().execute(param);
		
//		new SendDataToServerTask().execute("http://www.tnssofts.com/Ashwani/ReqService.svc/uploadsitebuilding",res);
	}
		

//Get master survey details
public String getSiteSurveyDetails(){
	dataUpload = objAdapter.getSiteSurveyMasterDetails(ipid);
    jMain = new JSONObject();
	try {
		jArray = new JSONArray();			
		 while(dataUpload.moveToNext() && dataUpload!=null){
			 jObj = new JSONObject();
			  // Toast.makeText(this,"creating objs",Toast.LENGTH_SHORT).show();		        	
	        	jObj.put("vSiteIDCode",dataUpload.getString(dataUpload.getColumnIndex("vSiteIdCode")));
	        //	jObj.put("vOpratorSiteID", dataUpload.getString(1));
	        	//jObj.put("vSiteID", dataUpload.getString(2));
	        	jObj.put("nLat",dataUpload.getString(dataUpload.getColumnIndex("nLat")));	        	
			 	jObj.put("nLong",dataUpload.getString(dataUpload.getColumnIndex("nLong")));
	        	jObj.put("vTowerType", dataUpload.getString(dataUpload.getColumnIndex("vTowerType")));
	        	SimpleDateFormat dateFormat = new SimpleDateFormat("d/MMM/yyyy"); 
	        	String datee=dataUpload.getString(dataUpload.getColumnIndex("vSiteSurveyDate"));
	        	String []ar=datee.split("/");
	        	String monthe=getMonthName(Integer.parseInt(ar[1]));
	        	jObj.put("dSurveyDate", ar[0]+"-"+monthe+"-"+ar[2]);
	        	jObj.put("nTowerHeight", dataUpload.getString(dataUpload.getColumnIndex("nTowerHeight")));
	        	
	        	
	        	jArray.put(jObj);
	        }
		 	jMain.put("obj",jArray);
	}catch (Exception e) {
		// TODO Auto-generated catch block
		Toast.makeText(this,"exc"+ e.getMessage(),Toast.LENGTH_SHORT).show();
	}
	//Toast.makeText(this,jMain.toString(),Toast.LENGTH_SHORT).show();
	Log.w("jmain",jMain.toString());
	return jMain.toString(); 
}

//Fetch Building details.
	private String getBuildingDetails(){
		
	 Cursor dataUpload = objAdapter.getAllBuildingDetails();
	 try {
		 jMain = new JSONObject();
		 if(dataUpload!=null){
			 jArray = new JSONArray();
		 while(dataUpload.moveToNext()){			 
			 String ip = dataUpload.getString(dataUpload.getColumnIndex("vSiteIdCode"));
			 Log.w("ip",ip);
				if(ip.equalsIgnoreCase(ipid)){
				 	jObj = new JSONObject();
				 	jObj.put("vSiteIDCode",dataUpload.getString(dataUpload.getColumnIndex("vSiteIdCode")));		
				 	jObj.put("vOpratorSiteID",dataUpload.getString(dataUpload.getColumnIndex("vOpratorSiteId")));
				 	jObj.put("nAnteenaNo",dataUpload.getString(dataUpload.getColumnIndex("nAnteenaNo")));
				 	jObj.put("nBuildingNo",dataUpload.getString(dataUpload.getColumnIndex("nBldgNo")));
				 	jObj.put("nNoofFloor",dataUpload.getString(dataUpload.getColumnIndex("nFloors")));
				 	jObj.put("nAntBuildingDistance",dataUpload.getString(dataUpload.getColumnIndex("nDistance")));
				 	jObj.put("nAziAngle",dataUpload.getString(dataUpload.getColumnIndex("nAzimuth")));
				 	jObj.put("nLat",dataUpload.getString(dataUpload.getColumnIndex("nLat")));	        	
				 	jObj.put("nLong",dataUpload.getString(dataUpload.getColumnIndex("nLong")));
				 	jObj.put("nBuildingHeight",dataUpload.getString(dataUpload.getColumnIndex("nBldgHeight")));
				 	jArray.put(jObj);
				 
			 }				
		 }
			jMain.put("obj",jArray);
		 }
	    }
      catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
	// Toast.makeText(this,jMain.toString(),Toast.LENGTH_SHORT).show();
		return jMain.toString();
	}
	
	//Fetch Boundary Layout Details
	private String getBoundaryLayoutDetails(){
		 Cursor dataUpload = objAdapter.getAllBoundaryLayoutDetails();
		 jMain = new JSONObject();
		 try {
			 if(dataUpload != null){
				 jArray = new JSONArray();			
				 Log.w("bcursor","notnull");
			 while(dataUpload.moveToNext()){
				 jObj = new JSONObject();
				 String str = dataUpload.getString(dataUpload.getColumnIndex("vSiteIdCode"));
				 if(str.equalsIgnoreCase(ipid)){
				// Log.w("vsiteid",dataUpload.getString(dataUpload.getColumnIndex("vSiteIdCode")));
					jObj.put("vSiteIdCode",dataUpload.getString(dataUpload.getColumnIndex("vSiteIdCode")));
					jObj.put("nDistance",dataUpload.getString(dataUpload.getColumnIndex("nDistance")));
					jObj.put("vDescription",dataUpload.getString(dataUpload.getColumnIndex("vDescription")));
					jObj.put("nCornerNo",dataUpload.getString(dataUpload.getColumnIndex("nCornerNo")));		        
		        	jArray.put(jObj);
				}
			 }
			  	jMain.put("obj",jArray);
			 }
		  }			 
	      catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			}
		// Toast.makeText(this,jMain.toString(),Toast.LENGTH_SHORT).show();
			return jMain.toString();
		}
	
	//Fetch Corner details
	public String getCornerDetails(){
		
		 Cursor dataUpload = objAdapter.getAllAntenaDetailsUpload();
		// Toast.makeText(this,"caled curs",Toast.LENGTH_SHORT).show();
		 jMain = new JSONObject();
		 try {
			 jArray = new JSONArray();			
			 if(dataUpload!=null ){
				 while(dataUpload.moveToNext()){
					// Toast.makeText(this,"notnull",Toast.LENGTH_SHORT).show();
					 String ip = dataUpload.getString(dataUpload.getColumnIndex("vSiteIdCode"));
					 Log.w("ip",ip);
					if(ip.equalsIgnoreCase(ipid)){
						 jObj = new JSONObject();
						 	jObj.put("vSiteIDCode",dataUpload.getString(dataUpload.getColumnIndex("vSiteIdCode")));
						 	jObj.put("vOpratorSiteID",dataUpload.getString(dataUpload.getColumnIndex("vOpratorSiteId")));
						 	jObj.put("nAnteenaNo",dataUpload.getString(dataUpload.getColumnIndex("nAnteenaNo")));
						 	jObj.put("nCornerNo",dataUpload.getString(dataUpload.getColumnIndex("nCornerNo")));
						 	jObj.put("nDistance",dataUpload.getString(dataUpload.getColumnIndex("nDistance")));
						 	jObj.put("nCornerHeight",dataUpload.getString(dataUpload.getColumnIndex("nCornerHeight")));
						 	jObj.put("nLat",dataUpload.getString(dataUpload.getColumnIndex("nLat")));
						 	jObj.put("nLong",dataUpload.getString(dataUpload.getColumnIndex("nLong")));				 		           
						 	jArray.put(jObj);	
					}
				 }
				 jMain.put("obj",jArray);
				}
		 }
	     catch (JSONException e) {
					// TODO Auto-generated catch block
					Log.w(e.getMessage(),"ex");
			}
		 	//Toast.makeText(this,jMain.toString(),Toast.LENGTH_SHORT).show();
		 	//Toast.makeText(this,jMain.toString(),Toast.LENGTH_SHORT).show();
			return jMain.toString();
		}
	
	
	//Obtained Data
	private void obtainedData(String data){
		System.out.println("ResDataUpload"+data);
		String res="";
		//{"RequestSiteBoundaryDetailsResult":"ok"}
		try {
			JSONObject obj=new JSONObject(data);
			res=obj.getString("RequestSiteBoundaryDetailsResult");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
			if(res.equalsIgnoreCase("ok"))
			{
				builder.setTitle("Uploaded site data");
			}
			else
			{
			builder.setTitle("Failed to upload site data");
			}
			//builder.setMessage(data);
			builder.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						UploadToServerActivity.this.finish();
//						Intent intent = new Intent(getBaseContext(),AllOptionsActivity.class);     							
//						startActivity(intent);
//						dialog.dismiss();
					}
				});
			
		    builder.create();
		    builder.show();
	}

	@SuppressLint("NewApi")
	private class SendDataToServerTask extends AsyncTask<String ,Void,String>{
		ArrayList<String> data = null;
		String result1,result2,result3,result;
		int c=0;
		
		 @Override
		    protected void onPreExecute() {
		        super.onPreExecute();
		        progressDialog.show();
		    }
		
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String datapost="";
			Log.w("params[o]",params[0]);
			String URL ="";
			for(int i=0;i<resLink.size();i++){
			Log.w("in","do in");
			
			try{			
				URL = resLink.get(i);
				datapost = resData.get(i);
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(URL);
			httppost.setHeader("Accept","application/json");    	    
    	    httppost.setHeader("Content-type", "application/json");
    	    StringEntity se = new StringEntity(datapost);
            se.setContentEncoding((new BasicHeader(HTTP.CONTENT_TYPE, "application/json")));
            httppost.setEntity(se);
    	    HttpResponse objHttpResponse = httpclient.execute(httppost); 
    		int stat = objHttpResponse.getStatusLine().getStatusCode();
    		
    		InputStream objInputStream = objHttpResponse.getEntity().getContent();
        	InputStreamReader objInputStreamReader = new InputStreamReader(objInputStream);
        	BufferedReader objBufferedReader = new BufferedReader(objInputStreamReader);        	
	         	StringBuilder sbr = new StringBuilder();
	         	String line = null;
	         	long total =0;
	         	while((line = objBufferedReader.readLine())!=null){
	         		total =objBufferedReader.read();
	         		sbr.append(line + "\n");
	         	   // publishProgress((int)(total));
	         	}
	        	
	         	 result = sbr.toString();
	         	 
	         	 System.out.println("result"+result);
			
			}catch(Exception ex){
				
				AlertDialog.Builder builder = new AlertDialog.Builder(UploadToServerActivity.this);
				builder.setTitle("Error :"+ex.getMessage());
				builder.setMessage("Please try again...");
				builder.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
							UploadToServerActivity.this.finish();
						}
					});
				
			    builder.create();
			    builder.show();
				
			}
			}
			return result;
		}
		
		
		
		
		
		protected void onPostExecute(String data){
			progressDialog.dismiss();
			UploadToServerActivity.this.obtainedData(data);
			
		}	

	}
	
	public String  getMonthName(int month)
	{
		String monthString;
		switch (month) {
        case 1:  monthString = "Jan";       break;
        case 2:  monthString = "Feb";      break;
        case 3:  monthString = "Mar";         break;
        case 4:  monthString = "Apr";         break;
        case 5:  monthString = "May";           break;
        case 6:  monthString = "Jun";          break;
        case 7:  monthString = "Jul";          break;
        case 8:  monthString = "Aug";        break;
        case 9:  monthString = "Sep";     break;
        case 10: monthString = "Oct";       break;
        case 11: monthString = "Nov";      break;
        case 12: monthString = "Dec";      break;
        default: monthString = "Invalid month"; break;
    }
    
		return monthString;
		
	}
	
}