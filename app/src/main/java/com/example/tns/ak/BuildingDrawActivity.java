package com.example.tns.ak;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import com.example.tns.AccessData;
import com.example.tns.CamTestActivity;
import com.example.tns.R;
import com.example.tns.compass.RotationVectorCompass;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;


public class BuildingDrawActivity extends Activity implements
		OnClickListener, OnTouchListener {

	BuildingCustomView customView;
	float runningX = 0, runningY = 0;
	boolean actionMove = false;
	Display display;
	int screenWidth,screenHeight;
	RelativeLayout relaLayout;
	ArrayList<Image> imgList;
	public static int uniqID=1;
	int imgposID = -1;
	String IPID;
	int bldgNum;
	Button btnSave;
	AccessData objAdapter;
	String curLayout = "Adjbuildinglayout";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		//requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.building_view);
        display=getWindowManager().getDefaultDisplay();
        screenWidth=display.getWidth();
        screenHeight=display.getHeight();
        relaLayout=(RelativeLayout) findViewById(R.id.RelaView);
        //relaLayout.setLayoutParams(new LayoutParams(screenWidth-200, screenHeight-5));
		customView = (BuildingCustomView) findViewById(R.id.buildCustomView);
		//customView.setLayoutParams(new LayoutParams(screenWidth-200, screenHeight-5));
		customView.setOnTouchListener(this);
		customView.setDrawingCacheEnabled(true);
		btnSave=(Button) findViewById(R.id.btnBuildSave);
		btnSave.setOnClickListener(this);
		Bundle bBldgNumber = getIntent().getExtras();
       IPID = bBldgNumber.getString("ipid");
       bldgNum = bBldgNumber.getInt("numberAdjBldg");
		
		imgList=new ArrayList<Image>();
		initializeImage(bldgNum);
		customView.initializeLine(150, (screenHeight/2)-25,imgList);
		

	}



	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		runningX = event.getX();
		runningY = event.getY();
		
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			
			checkList();
			actionMove = false;

		}

		else if (event.getAction() == MotionEvent.ACTION_MOVE) {
			if (runningX != 0 && runningY != 0)
				customView.mooveImg(runningX,runningY,imgposID, imgList);
			//	customView.drawImage(runningX, runningY, imgposID, imgList);
			actionMove = true;
		}

		else if (event.getAction() == MotionEvent.ACTION_UP) {
			if (runningX != 0 && runningY != 0) {
				if (actionMove) {
					// 9thfeb
					//imgposID = -1;
				}

				

				actionMove = false;
			}
		}

		return true;

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnBuildSave:
			
			saveBitmapFile();
			//Intent in =new Intent(getApplicationContext(), CamTestActivity.class);
			Intent in =new Intent(getApplicationContext(), RotationVectorCompass.class);
			in.putExtra("ipid",IPID);
			startActivity(in);

			break;
		
		
		
		
	/*	case R.id.btnUndoText:
			if (writeList != null) {
				if (writeList.size() > 0) {
					writeList.remove(writeList.size() - 1);
				}
			}
			customView.writeText(writeList);

			break;*/

/*		case R.id.btnText:

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			// Title
			builder.setTitle("Please enter text:");
			LayoutInflater inflater = this.getLayoutInflater();
			final View mychildview = inflater.inflate(R.layout.writetextdialog,
					null);
			builder.setView(mychildview);
			builder.setPositiveButton("Ok",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub

							iswrite = true;
							EditText ed = (EditText) mychildview
									.findViewById(R.id.textlayout);
							textoncanvas = ed.getText().toString();

							if (writeList == null) {
								writeList = new ArrayList<WriteTextLayout>();
							}
							arg0.dismiss();
						}
					});
			// To show negative button
			builder.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
							arg0.dismiss();

						}
					});
			builder.create();
			builder.show();
			Log.w("outof", "switchmain");
			break;*/

		}

	}
	
	
	@SuppressLint("NewApi")
	public void saveBitmapFile(){
		String imgName = IPID +"_"+curLayout;
		File imgFile = new  File(Environment.getExternalStorageDirectory()+"/"+imgName+".png");	 
		 FileOutputStream outStream;
		 customView.buildDrawingCache(true);
	//	 imgTest = (ImageView)findViewById(R.id.test);
		 Bitmap bmp = customView.getDrawingCache();
	        try {
	   //     	outStream = openFileOutput(imgName, MODE_PRIVATE);
	        	outStream = new FileOutputStream(imgFile);
	        bmp.compress(Bitmap.CompressFormat.PNG, 85, outStream);
	        outStream.flush();
	        outStream.close();
	        //String imgName = ipid +"_"+curLayout;
	       // File imgFile = new  File(Environment.getExternalStorageDirectory()+ "/"+imgName+".png");
	            objAdapter = new AccessData(this);
	        	objAdapter.openDataBase();
	            //String path =imgName;	          
	           long v= objAdapter.saveImagePathToDatabase(imgFile.getAbsolutePath(), IPID, imgName,"ADJ_BL");
	        		   //"ADJ_BL", ipid, imgName,"ADJ_BL");
	        
	        	
	        
	        }catch (Exception e) {	        
	        	Toast.makeText(getBaseContext(), e.getMessage(),Toast.LENGTH_LONG).show();
	       } 
		 
		 Toast.makeText(this,"Build Lay saved",Toast.LENGTH_SHORT).show();
/*		Intent iAdjBldg = new Intent(this,CameraMainActivity.class);
        Intent iAdjBldg = new Intent(this,CamTestActivity.class);
         iAdjBldg.putExtra("ipid",ipid);
         startActivity(iAdjBldg);*/

	}

	/*@TargetApi(Build.VERSION_CODES.DONUT)
	public void saveBitmapFile() {

		FileOutputStream outStream;
		customView.buildDrawingCache(true);
		File imgFile = new File(Environment.getExternalStorageDirectory() + "/"
				+IPID+"_BUILIMG "+".png");
		// imgTest = (ImageView)findViewById(R.id.test);
		Bitmap bmp = customView.getDrawingCache();
		try {
			// outStream = openFileOutput(imgName, MODE_PRIVATE);
			outStream = new FileOutputStream(imgFile);
			bmp.compress(Bitmap.CompressFormat.PNG, 85, outStream);
			outStream.flush();
			outStream.close();

			objAdapter = new AccessData(this);
			objAdapter.openDataBase();
//			 long v= objAdapter.saveImagePathToDatabase("TOWER_BL", ipid,
//			 imgName,"TOWER_BL");
			long v = objAdapter.saveImagePathToDatabase(
					imgFile.getAbsolutePath(), IPID, IPID+"_BUILIMG", "TOWER_BL");

		} catch (Exception e) {
			Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG)
					.show();
		}
		Toast.makeText(this, "Layout stored", Toast.LENGTH_SHORT).show();
//	 Intent iAdjBldg = new Intent(this,CameraMainActivity.class);
//		Intent iAdjBldg = new Intent(this, CamTestActivity.class);
//		iAdjBldg.putExtra("ipid", ipid);
//		startActivity(iAdjBldg);

	}*/
	
	public void initializeImage(int imgCount)
	{
		float a=50,b=50;
		int newID = 0;
		for(int i=1;i<=imgCount;i++)
		{
			Bitmap newImg=null;
			
			if(i==1)
			{
			newImg = BitmapFactory.decodeResource(getResources(),R.drawable.b1);
			newID = R.drawable.b1;
			}
			if(i==2)
			{
			newImg = BitmapFactory.decodeResource(getResources(),R.drawable.b2);
			newID = R.drawable.b2;
			}
			if(i==3)
			{
			newImg = BitmapFactory.decodeResource(getResources(),R.drawable.b3);
			newID = R.drawable.b3;
			}
			if(i==4)
			{
			newImg = BitmapFactory.decodeResource(getResources(),R.drawable.b4);
			newID = R.drawable.b4;
			}
			if(i==5)
			{
			newImg = BitmapFactory.decodeResource(getResources(),R.drawable.b5);
			newID = R.drawable.b5;
			}
			if(i==6)
			{
			newImg = BitmapFactory.decodeResource(getResources(),R.drawable.b6);
			newID = R.drawable.b6;
			}
			if(i==7)
			{
			newImg = BitmapFactory.decodeResource(getResources(),R.drawable.b7);
			newID = R.drawable.b7;
			}
			if(i==8)
			{
			newImg = BitmapFactory.decodeResource(getResources(),R.drawable.b8);
			newID = R.drawable.b8;
			}
			if(i==9)
			{
			newImg = BitmapFactory.decodeResource(getResources(),R.drawable.b9);
			newID = R.drawable.b9;
			}
			if(i==10)
			{
			newImg = BitmapFactory.decodeResource(getResources(),R.drawable.b10);
			newID = R.drawable.b10;
			}
			if(i==11)
			{
			newImg = BitmapFactory.decodeResource(getResources(),R.drawable.b11);
			newID = R.drawable.b11;
			}
			if(i==12)
			{
			newImg = BitmapFactory.decodeResource(getResources(),R.drawable.b12);
			newID = R.drawable.b12;
			}
			int uniID = imgList.size();
			Image item;
			item = new Image(newID ,a,b,newImg.getWidth(),newImg.getHeight(),uniqID,"");
			uniqID=uniqID+1;
			a=a+35;;
			imgList.add(item);	
		}
	}
	
	@SuppressLint("NewApi")
	public void checkList()
	{
		//Toast.makeText(this,"checklist", Toast.LENGTH_LONG).show();
		for(int i=0;i<imgList.size();i++){
			//if(i!=0){
			 Image img = imgList.get(i);
			 if((img.getuniqueID()!= img.getID()) && (img.getName().isEmpty())){
			 Bitmap bitMoved = BitmapFactory.decodeResource(getResources(),img.getID());
			 }
			 float x = img.getXCoordinate();
			 float y = img.getYCoordinate();
			 float dx = img.getW() + x;
			 float dy = img.getH() + y;
			 if(runningX < dx && runningX > x){
				 if(runningY < dy && runningY > y){
					 imgposID = img.getuniqueID();
					 img.setXCoordinate(runningX);
					 img.setYCoordinate(runningY);
					 
				 }
			 }
			 
		}
		
	}

}