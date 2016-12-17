package com.example.tns;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
public class MainDrawActivity extends Activity {

	String[] layouts = new String[]{"Manual","Auto Draw"};
	String curLayout;
	Intent drawLayout;
	AlertDialog.Builder builder;
	String IPID;
	int bldgNum;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_draw);
		
		Bundle bBldgNumber = getIntent().getExtras();
		//IPID = "DU-1116";
       IPID = bBldgNumber.getString("ipid");
       bldgNum = bBldgNumber.getInt("numberAdjBldg");
		//IPID = "DU-1116";
        
		//bldgNum= 15;
		 builder = new AlertDialog.Builder(this);
	        //Title
	        builder.setTitle("Choose :");
	        builder.setSingleChoiceItems(layouts, -1, new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					curLayout = layouts[which];
					Bundle b = new Bundle();		
					b.putString("ipid",IPID);
					
					if(curLayout.equalsIgnoreCase("Manual")){
						
						
						drawLayout = new Intent(getBaseContext(), TowerBuildingLayoutActivity.class);
						drawLayout.putExtra("ipid",IPID);
						//drawLayout.putExtras(b);
	        			startActivity(drawLayout);
					}						
					else{
						
						drawLayout = new Intent(getBaseContext(), AutoDrawActivity.class);
						drawLayout.putExtra("ipid",IPID);
						drawLayout.putExtra("bldgnum",bldgNum);
	        			startActivity(drawLayout);
					}
					
				}
			});

	        builder.create();
	        builder.show();
	        
	}
	
	public void onResume(){
		super.onResume();
		
	}
	
	public void onPause(){
		super.onPause();
	}
	
	public void onDestroy(){
		super.onDestroy();

		
	}
	
	

}
