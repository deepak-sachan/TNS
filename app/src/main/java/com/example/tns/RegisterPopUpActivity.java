package com.example.tns;

import android.os.Bundle;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterPopUpActivity extends Activity{

	EditText editDispName;
	HttpGet httpGet;
	HttpClient httpclient;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);		
		setContentView(R.layout.registerpopup);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.header);
	}
	
	//If Save is clicked then send the displayName to server
		public void Save(View view){
			try{
			
			editDispName = (EditText)findViewById(R.id.txtDisplayName);		
			String vDispName = editDispName.getText().toString();
			String vManufacturer = android.os.Build.MANUFACTURER;
		    String vModel = android.os.Build.MODEL;
		    String vDevicename = android.os.Build.PRODUCT;
		    //String vDevicename = android.os.Build.DEVICE;
		    //Toast.makeText(this,"vModel"+vModel +"vManufacturer:"+ vManufacturer,Toast.LENGTH_LONG).show();
		    TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		    int vOSVersion = android.os.Build.VERSION.SDK_INT;
		  //  String imei =Secure.getString(this.getContentResolver(),
	        //        Secure.ANDROID_ID);
		    String imei = tm.getDeviceId();
		    //Toast.makeText(this,"passing all values",Toast.LENGTH_LONG).show();
		    
		    checkRegistrationAll(vDispName, vManufacturer, vModel, vDevicename,vOSVersion,imei);
			}catch(Exception ex){
				Toast.makeText(this,ex.getMessage(),Toast.LENGTH_LONG).show();
			}
			
		}
		
		public void  checkRegistrationAll(String vDispName,String vManufacturer,String vModel,String vDevicename, int vOSVersion,String imei){
			httpclient = new DefaultHttpClient();
			//Toast.makeText(this,"chec reg"
				//	, Toast.LENGTH_LONG).show();			
			try {
				
				String uri = "http://www.tnssofts.com/wcf_get/RestServiceImpl.svc/InsertDevicemst/" +			
				 imei + "~" +vDevicename
						+ "~" + vModel + "~"+ String.valueOf(vOSVersion) + "~"+  vDispName;
			//	Toast.makeText(this,imei,Toast.LENGTH_LONG).show();			
				httpGet = new HttpGet(uri);
				Log.w("uri","posted");
	        	  HttpResponse objHttpResponse = httpclient.execute(httpGet);             
	        	  InputStream objInputStream = objHttpResponse.getEntity().getContent();
	        	  int status = objHttpResponse.getStatusLine().getStatusCode();
	        	//  Toast.makeText(this, "Status"+String.valueOf(status), Toast.LENGTH_LONG).show();	        	  
	        	  InputStreamReader objInputStreamReader = new InputStreamReader(objInputStream);
	        	  BufferedReader objBufferedReader = new BufferedReader(objInputStreamReader);
	        	  String value = objBufferedReader.readLine();
	        	//  Toast.makeText(this,"Registration done, please wait for approval!",Toast.LENGTH_LONG).show();
	        	  //Toast.makeText(this,"value fro server"+value, Toast.LENGTH_LONG).show();
	          }catch(Exception ex){
	        	  AlertDialog.Builder builder = new AlertDialog.Builder(this);
	  			builder.setTitle("Some error in registration. Please try again!");
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
			
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Registration done, please wait for approval!");
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
			/*Intent intentDD = new Intent(this,DownloadDataActivity.class);
			Bundle b = new Bundle();
			b.putString("imei", imei);
			intentDD.putExtras(b);
			startActivity(intentDD);*/
		}
		

	
}	