package com.example.tns;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

import org.xmlpull.v1.XmlPullParserException;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.telephony.TelephonyManager;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Toast;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;

public class TNSMainActivity extends Activity{
	
	String vAndroidDeviceId;
	HttpClient httpclient;
	HttpGet httpGet;
	AccessData accessServerDataAdapter;
	boolean registered = false;
	SharedPreferences prefs;
	int value =0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.tnsstart_main);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.header);
		prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		//Check if already registered
		accessServerDataAdapter = new AccessData(this);
		try{
			accessServerDataAdapter.createDataBase();
			accessServerDataAdapter.openDataBase();
			Cursor c = accessServerDataAdapter.getLoginDetails();
			if(c!= null){				
				if(c.moveToNext()){
					
					String loginSts=prefs.getString("LoginSts", "");
					registered = true;
					if(loginSts.equals("on"))
					{
						Intent i = new Intent(this,AllOptionsActivity.class);
						startActivity(i);
						TNSMainActivity.this.finish();
					}
					else
					{
						Intent i = new Intent(this,LoginActivity.class);
						startActivity(i);
						TNSMainActivity.this.finish();
					}
				//	}
				}
			}
		}catch(Exception ex){
			
		}
		//Toast.makeText(this,"no login details....",Toast.LENGTH_SHORT).show();
		TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		if(!registered){		
		if(!isNetworkAvailable()){
			 AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Network not available...!");
			    builder.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							Intent intent = new Intent(Intent.ACTION_MAIN);
							intent.addCategory(Intent.CATEGORY_HOME);
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(intent);
							dialog.dismiss();
						}
					});
				
			    builder.create();
	  		    builder.show();
		}
		else{
		//	android:layout_weight=".50" (this,"network available...",Toast.LENGTH_SHORT).show();

			String id =tm.getDeviceId();
		value =	checkRegistrationTest(id);
		//Toast.makeText(this,"You have regstered"+String.valueOf(value)+String.valueOf(value),Toast.LENGTH_SHORT).show();
		int v = value;
        if(value > 0){
        	//Intent iRegisterPopUp = new Intent(this, RegisterPopUpActivity.class);
			//startActivity(iRegisterPopUp);
			//CHECK in database			
			try{				
				accessServerDataAdapter.openDataBase();
				Cursor c = accessServerDataAdapter.getLoginDetails();
				if(c!= null && c.moveToNext()){
				//goto options form
					Intent i = new Intent(this,LoginActivity.class);
					startActivity(i);
				}
				else{
					 boolean isValuePresent = checkForUID(tm.getDeviceId());
					 if(isValuePresent){
						 //call login form
						// Toast.makeText(this,"goto login",Toast.LENGTH_SHORT).show();
						 Intent i = new Intent(this,LoginActivity.class);
							startActivity(i);
					 }
					 else{					
					//	 Toast.makeText(this,"waiting for approval...",Toast.LENGTH_LONG).show();
						 AlertDialog.Builder builder = new AlertDialog.Builder(this);
							builder.setTitle("waiting for approval...!");
						    builder.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface dialog, int which) {
										// TODO Auto-generated method stub
										Intent intent = new Intent(Intent.ACTION_MAIN);
										intent.addCategory(Intent.CATEGORY_HOME);
										intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
										startActivity(intent);
										dialog.dismiss();
									}
								});
							
						    builder.create();
				  		    builder.show();
						 
					 }
				}
			}catch(Exception ex){
				Toast.makeText(this,ex.getMessage(),Toast.LENGTH_LONG).show();
			}
		}
		else{
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        //Title
	        builder.setTitle("Register");
	        builder.setMessage("Do you want to register?");
	        builder.setPositiveButton("ok",new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Intent iRegisterPopUp = new Intent(getBaseContext(), RegisterPopUpActivity.class);
					startActivity(iRegisterPopUp);
					dialog.dismiss();
				}
			});
	        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					Intent intent = new Intent(Intent.ACTION_MAIN);
					intent.addCategory(Intent.CATEGORY_HOME);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
				}
				
			});
	        builder.create();
	        builder.show();
			
		}
		}
		}
		
	}	
	
	private	boolean isNetworkAvailable() {
	ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
	NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	// return activeNetworkInfo.isConnected();
	return activeNetworkInfo != null;
	}



	
	public boolean checkForUID(String imei){
		
		//Toast.makeText(this,"inside checkUID", Toast.LENGTH_LONG).show();
		httpclient = new DefaultHttpClient();		
		boolean value = false;
		URI uri;
		try {		
			String URL = "http://www.tnssofts.com/wcf_get/RestServiceImpl.svc/getuidpass/";
			URL += imei;		
		//	Toast.makeText(this,"URL"+URL, Toast.LENGTH_LONG).show();
			httpGet = new HttpGet(URL);		          
			//Toast.makeText(this,"GETUID pass", Toast.LENGTH_LONG).show();           
        	HttpResponse objHttpResponse = httpclient.execute(httpGet);             
        	InputStream objInputStream = objHttpResponse.getEntity().getContent();
        	InputStreamReader objInputStreamReader = new InputStreamReader(objInputStream);
        	BufferedReader objBufferedReader = new BufferedReader(objInputStreamReader);        	
	         	StringBuilder sbr = new StringBuilder();
	         	String line = null;
	         	while((line = objBufferedReader.readLine())!=null){
	         		sbr.append(line + "\n");
	         	}
	        	
	         	String result = sbr.toString();
	         	JSONObject objJson = new JSONObject(result);
	         	JSONArray arr =  objJson.getJSONArray("GetUidPassResult");
	         	String pass="",username="";
	         	//ArrayList<String> arrList = new ArrayList<String>(arr.length());
	         	for(int i=0;i<arr.length();i++){
	         			JSONObject objJ = arr.getJSONObject(i);
	         			pass = objJ.getString("vPassword");
	         			username = objJ.getString("vUserID");	         			
	         	}
	      
	         if(pass != null && username != null){
	        	 if(pass.length() > 0 && username.length() > 0){
	        		 value = true;
	        		 accessServerDataAdapter.insertLoginDetails(username, pass);
	        	 }
	         }
		}catch(Exception ex)
		{
			//Toast.makeText(this,"Ex :"+ex.getMessage(),Toast.LENGTH_LONG).show();
		}		
		
		return value;
	}
	
	public int checkRegistrationTest(String imei){
	//	Toast.makeText(this,"inside checkRegistrationTest", Toast.LENGTH_LONG).show();
		httpclient = new DefaultHttpClient();		
		int value = 0;
		URI uri;
		try {		
			String URL = "http://www.tnssofts.com/wcf_get/RestServiceImpl.svc/checkIMEI/";
			URL += imei;		
			//Toast.makeText(this,"URL"+URL, Toast.LENGTH_LONG).show();
			httpGet = new HttpGet(URL);		          
        	           
        	HttpResponse objHttpResponse = httpclient.execute(httpGet);             
        	InputStream objInputStream = objHttpResponse.getEntity().getContent();
        	InputStreamReader objInputStreamReader = new InputStreamReader(objInputStream);
        	BufferedReader objBufferedReader = new BufferedReader(objInputStreamReader);        	
	         	StringBuilder sbr = new StringBuilder();
	         	String line = null;
	         	while((line = objBufferedReader.readLine())!=null){
	         		sbr.append(line + "\n");
	         	}
	        	
	         	String result = sbr.toString();
	         	JSONObject objJson = new JSONObject(result);
	         	value = objJson.getInt("CheckIMEIResult");
	         	//JSONArray arr =  objJson.getJSONArray("CheckIMEIResult");
	         	//JSONArray arr = new JSONArray(sbr.toString());
	         	//ArrayList<String> arrList = new ArrayList<String>(arr.length());
	         	/*for(int i=0;i<arr.length();i++){
	         			JSONObject objJ = arr.getJSONObject(i);
	         			value = objJ.getInt("CheckIMEIResult");
	         			Toast.makeText(this,String.valueOf(value), Toast.LENGTH_SHORT).show();
	         	}*/
	      //   	Toast.makeText(this,String.valueOf(value), Toast.LENGTH_SHORT).show();
        	  return value;
        	  
        	 
		}catch(Exception ex)
		{
			Toast.makeText(this,"Ex :"+ex.getMessage(),Toast.LENGTH_LONG).show();
		}
		return value;
	
	}

}
