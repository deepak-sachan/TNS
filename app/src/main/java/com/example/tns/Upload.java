package com.example.tns;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class Upload extends Activity{
	String IPID;
	  @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
	        setContentView(R.layout.upload);
	       Bundle ipidB = getIntent().getExtras();	        
		       // IPID = "VODABIHAR0002";
		     IPID = ipidB.getString("ipid");
	        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.header);
	  }
	  
	  public void uploadData(View view){
		  if(isNetworkAvailable()){
			  Intent uploaddata = new Intent(this,UploadToServerActivity.class);
			  uploaddata.putExtra("ipid",IPID);
			  startActivity(uploaddata);
		  }
		  else{
			  AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Network not available...!");
			    builder.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							Intent intent = new Intent(getBaseContext(),AllOptionsActivity.class);     							
							startActivity(intent);
							dialog.dismiss();
						}
					});
				
			    builder.create();
	  		    builder.show();
		  }
	  }
	  
	  private	boolean isNetworkAvailable() {
			ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
			NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
			// return activeNetworkInfo.isConnected();
			return activeNetworkInfo != null;
			}
	  
	  public void uploadImageData(View view){
		  if(isNetworkAvailable()){
			  Intent uploadImg = new Intent(this,UploadImageDataToServerActivity.class);
			  uploadImg.putExtra("ipid",IPID);
			  startActivity(uploadImg);
		  }
		  else{
			  AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Network not available...!");
			    builder.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							Intent intent = new Intent(getBaseContext(),AllOptionsActivity.class);     							
							startActivity(intent);
							dialog.dismiss();
						}
					});
				
			    builder.create();
	  		    builder.show();
		  }
	  }

}
