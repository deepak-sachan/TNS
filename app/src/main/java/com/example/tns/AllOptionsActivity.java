package com.example.tns;

import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class AllOptionsActivity extends Activity {

	Button btnDownload , btnUpload , btnPending , btnCompleted,btnLogout;
	Intent iOption;
	String imei;
	int request_Code = 1;
	AccessData accessServerDataAdapter;
	TextView bytesDone;
	SharedPreferences prefs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.optionsforsite);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.header);
		String imei = Secure.getString(this.getContentResolver(),
                Secure.ANDROID_ID); 
		bytesDone = (TextView)findViewById(R.id.txtDownBytes);
		
	}
	
	public void calldownload(View view){
		
		
			if(isConnectingToInternet())
			{
				Intent iD = new Intent(this,DownloadDataActivity.class);
				startActivity(iD);
			}
			else
			{
				Toast.makeText(getApplicationContext(), "Please connect Internet first !", Toast.LENGTH_SHORT).show();
			}
			
			
		//}	
	}
	
	
	public void onActivityResult(int requestCode , int resultCode , Intent data){
		if(requestCode == request_Code){
			if(resultCode == RESULT_OK){
				//Toast.makeText(this,"Downloaded total bytes"+data.getData().toString(),Toast.LENGTH_LONG).show();
				//btnDownload.setBackgroundColor(Color.RED);
				//String dataSite = data.getStringExtra("downloadedSites");
				//bytesDone.setText("Downloaded bytes :"+dataSite);
			}
		}
	}
	public void callpending(View view){
		
		Intent iD = new Intent(this,PendingSurveyDetailsActivity.class);
		startActivity(iD);
	}

public void callcompleted(View view){
		
		Intent iD = new Intent(this,CompletedSurveyDetailsActivity.class);
		startActivity(iD);
	}
	
	public void callupload(View view){
		Intent upload = new Intent(this,Upload.class);
		startActivity(upload);
	}
	
	public void calllogout(View view){
		prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		SharedPreferences.Editor prefEditor = prefs.edit();
		prefEditor.putString("LoginSts", "");
	    prefEditor.commit();
		
	    
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		finish();
		
		
	}
	
	
	
	/*
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Toast.makeText(this,"switch",Toast.LENGTH_SHORT).show();
		
		switch(arg0.getId()){
		case R.id.btnDownload:
			Toast.makeText(this,"in down", Toast.LENGTH_SHORT).show();
			iOption = new Intent(this,DownloadDataActivity.class);			
			break;
		case R.id.btnUpload:
			iOption = new Intent(this,UploadDataToServerActivity.class);			
			break;
		case R.id.btnPendingDetails:
			iOption = new Intent(this,PendingSurveyDetailsActivity.class);			
			break;
		case R.id.btnCompletedDetails:
			//iOption = new Intent(this,Com.class);			
			break;			
		case R.id.btnLogout:
			//iOption = new Intent(this,L.class);			
			break;
		default:
			break;
		}
		startActivity(iOption);
	}*/
	
	public boolean isConnectingToInternet() {
		
		ConnectivityManager connectivity=(ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(connectivity!=null)
		{
			NetworkInfo[] info=connectivity.getAllNetworkInfo();
			if(info!=null)
			{
				for(int i=0; i<info.length; i++)
				{
					if(info[i].getState() == NetworkInfo.State.CONNECTED)
					{
						return true;
					}
				}
			}
		}
		return false;
	}
}	

