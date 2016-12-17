package com.example.tns;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Filter;
import android.widget.Toast;


public class PendingSurveyDetailsAdapter extends CursorAdapter {
	 
	Context context;
	Cursor filterCursor;
	String type;
	
	
		public PendingSurveyDetailsAdapter(Context context, Cursor c,String type) {
			super(context, c);
			//acc = new AccessData(context);
			//filterCursor = c;
			// TODO Auto-generated constructor stub
			this.context=context;
			this.type=type;
		}

		@Override
		public void bindView(final View view, Context arg1, Cursor cursor) {
			// TODO Auto-generated method stub
			
			    TextView textvSiteIdCode = (TextView) view.findViewById(R.id.txtSiteID);
			    textvSiteIdCode.setText(cursor.getString(cursor.getColumnIndex("vSiteIdCode")));	        
		        
			    TextView texttxtSiteName = (TextView) view.findViewById(R.id.txtSiteName);
			    texttxtSiteName.setText(cursor.getString(cursor.getColumnIndex("vSiteName")));
			    
			    TextView textvAddress = (TextView) view.findViewById(R.id.txtSiteAddress);
			    textvAddress.setText(cursor.getString(cursor.getColumnIndex("vAddress")));  
		        
			    TextView textCircle = (TextView) view.findViewById(R.id.txtCircle);
			    textCircle.setText(cursor.getString(cursor.getColumnIndex("vCircle")));
		        
			    TextView texthid = (TextView) view.findViewById(R.id.txtHidden);
			    texthid.setText(cursor.getString(cursor.getColumnIndex("vSiteId")));
			    int s=cursor.getColumnIndex("vSiteId");
			    Button btnSaveData = (Button) view.findViewById(R.id.btnDataSave);
			    Button btnSaveImg = (Button) view.findViewById(R.id.btnImgSave);
			    
			    if(type.equalsIgnoreCase("compelete"))
			    {
			    	
			    
			    btnSaveData.setVisibility(View.VISIBLE);
			    btnSaveImg.setVisibility(View.VISIBLE);
			    
			    btnSaveData.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						TextView siteID = (TextView) view.findViewById(R.id.txtSiteID);
						if(isConnectingToInternet()){
							  Intent uploaddata = new Intent(context,UploadToServerActivity.class);
							  uploaddata.putExtra("ipid",siteID.getText().toString());
							  context.startActivity(uploaddata);
						  }
						  else{
							  AlertDialog.Builder builder = new AlertDialog.Builder(context);
								builder.setTitle("Network not available...!");
							    builder.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
										
										@Override
										public void onClick(DialogInterface dialog, int which) {
											// TODO Auto-generated method stub
//											Intent intent = new Intent(context,AllOptionsActivity.class);     							
//											context.startActivity(intent);
											dialog.dismiss();
										}
									});
								
							    builder.create();
					  		    builder.show();
						  }
					}
				});
			    
			    
				btnSaveImg.setOnClickListener(new OnClickListener() {
									
									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										TextView siteID = (TextView) view.findViewById(R.id.txtSiteID);
										if(isConnectingToInternet()){
											  Intent uploaddata = new Intent(context,UploadImageDataToServerActivity.class);
											  uploaddata.putExtra("ipid",siteID.getText().toString());
											  context.startActivity(uploaddata);
										  }
										  else{
											  AlertDialog.Builder builder = new AlertDialog.Builder(context);
												builder.setTitle("Network not available...!");
											    builder.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
														
														@Override
														public void onClick(DialogInterface dialog, int which) {
															// TODO Auto-generated method stub
//															Intent intent = new Intent(context,AllOptionsActivity.class);     							
//															context.startActivity(intent);
															dialog.dismiss();
														}
													});
												
											    builder.create();
									  		    builder.show();
										  }
									}
								});
			
			    }  
		}
	

		@Override
		public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			 LayoutInflater inflater = LayoutInflater.from(arg2.getContext());
		        View retView = inflater.inflate(R.layout.pendingsurveydetailsonerow,arg2, false);		        
		        return retView;
		}
		private	boolean isNetworkAvailable() {
			ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
			NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
			// return activeNetworkInfo.isConnected();
			return activeNetworkInfo != null;
			}
	 
		public boolean isConnectingToInternet() {
			
			ConnectivityManager connectivity=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
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
 
 	



