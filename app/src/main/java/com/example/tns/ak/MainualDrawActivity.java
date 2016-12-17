	package com.example.tns.ak;
	
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import com.example.tns.AccessData;
import com.example.tns.AdjBldgLayoutActivity;
import com.example.tns.NoOfAdjacentBuildingsActivity;
import com.example.tns.R;
import com.example.tns.compass.RotationVectorCompass;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
	
	public class MainualDrawActivity extends Activity implements OnClickListener,OnTouchListener
	{
		Display display;
		int screenWidth,screenHeight;
		MyView myView;
		String[] strTower = new String[]{"Rectangle","Triangle","Circle"};
		Button btnDrawLine,btnDrawBuild,btnText,btnDelete,btnRemoveLine;
		Button btnCaution,btnWarning,btnDanger,btnDG,btnSave,btnTextUndo,btnShelter;
		ArrayList<Image> imgList;
		ArrayList<WriteTextLayout> writeList;
		String textoncanvas="";
		int[] images = new int[]{R.drawable.tower_new};
		float[] imagesdx = new float[]{50,180,130,150};
		float[] imagesdy = new float[]{50,90,140,75};
		String shape , btnClickImage;
		float runningX=0 , runningY=0;
		int imgposID = 0;
		ArrayList<Float> linesdraw = new ArrayList<Float>();
		boolean actionMove = false;
		Boolean boolDelete=false;
		public static boolean drwline = false,iswrite=false;
		float tempx = 0 , tempy = 0;
		
		public static int uniqID=1;
		AlertDialog.Builder builder;
		String curTower;
		
		String IPID;
		Double latitude , longitude;
		AccessData objAdapter;
		String curLayout = "towerlayout";
		
		public static float ln1x1,ln2x1,ln3x1,ln4x1,ln1y1,ln2y1,ln3y1,ln4y1;
		public static float ln1x2,ln2x2,ln3x2,ln4x2,ln1y2,ln2y2,ln3y2,ln4y2;
		private static String[] twrList = new String[] {"Rectangle","Triangle","Circle"};
		public static String twrType="";
		
	  @Override
	protected void onCreate(Bundle savedInstanceState) {
	    // TODO Auto-generated method stub
	    super.onCreate(savedInstanceState);
	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		//requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
	    setContentView(R.layout.my_view);
	    myView=(MyView) findViewById(R.id.customView);
	    myView.setDrawingCacheEnabled(true);
	    myView.setOnTouchListener(this);
	    btnDrawBuild=(Button) findViewById(R.id.btnBuild);
	    btnDrawLine=(Button) findViewById(R.id.btnLine);
	    btnText=(Button) findViewById(R.id.btnText);
	    btnDelete=(Button) findViewById(R.id.btnDelete);
	    btnRemoveLine=(Button) findViewById(R.id.btnRemoveLine);
	    display=getWindowManager().getDefaultDisplay();
        screenWidth=display.getWidth();
        screenHeight=display.getHeight();
	    
	    Bundle bCornerNumber = getIntent().getExtras();	        
        IPID = bCornerNumber.getString("ipid");
        latitude = bCornerNumber.getDouble("latitude");
        longitude = bCornerNumber.getDouble("longitude");
        objAdapter = new AccessData(this);
	    
	    btnCaution=(Button) findViewById(R.id.btnCaution);
	    btnWarning=(Button) findViewById(R.id.btnWarning);
	    btnDanger=(Button) findViewById(R.id.btnDanger);
	    btnDG=(Button) findViewById(R.id.btnDG);
	    btnSave=(Button) findViewById(R.id.btnSave);
	    btnTextUndo=(Button) findViewById(R.id.btnTextUndo);
	    btnShelter=(Button) findViewById(R.id.btnSheter);
	    btnDrawBuild.setOnClickListener(this);
	    btnDrawLine.setOnClickListener(this);
	    btnText.setOnClickListener(this);
	    btnDelete.setOnClickListener(this);
	    btnRemoveLine.setOnClickListener(this);
	    btnCaution.setOnClickListener(this);
	    btnWarning.setOnClickListener(this);
	    btnDanger.setOnClickListener(this);
	    btnDG.setOnClickListener(this);
	    btnSave.setOnClickListener(this);
	    btnTextUndo.setOnClickListener(this);
	    btnShelter.setOnClickListener(this);
	    imgList=new ArrayList<Image>();
	    imgOption();
	    uniqID=1;
	   // initList();
	   // initLine();
	    
	    }
	  
	  @Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		  this.finish();
		super.onDestroy();
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()==R.id.btnLine)
		{
			MyView.boolLineDraw=true;
			MyView.x1=0;
			MyView.y1=0;
			MyView.x2=0;
			MyView.y2=0;
			drwline=true;
			Toast.makeText(getApplicationContext(), "Line Selected", Toast.LENGTH_SHORT).show();
		}
		
		if(v.getId()==R.id.btnRemoveLine)
		{
			btnClickImage = "removeLine";
			if(linesdraw!=null){
				int cnt = 4;
				if(linesdraw.size() > 0){
					while(cnt > 0){
						linesdraw.remove(linesdraw.size() - 1);
						cnt--;
					}
					
				}
				myView.drawline(linesdraw);
			}
		}
		
		
		if(v.getId()==R.id.btnDelete)
		{
			//myView.addNewImg("Tower");
			btnClickImage = "delete";
			Toast.makeText(getApplicationContext(), "Delete selected", Toast.LENGTH_SHORT).show();
			boolDelete=true;
		}
		
		if(v.getId()==R.id.btnBuild)
		{
			//myView.addNewImg("Build");
			btnClickImage = "build";
			addToImages();
		}
		
		if(v.getId()==R.id.btnText)
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        //Title
	        builder.setTitle("Please enter text:");
	        final EditText edtText=new EditText(getApplicationContext());
	        builder.setView(edtText);
	        builder.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					
					iswrite = true;
					textoncanvas = edtText.getText().toString();
								
					//lines = null;	
					if(writeList == null){
						writeList = new ArrayList<WriteTextLayout>();
						
					}
					arg0.dismiss();		
					
				}
			});
	        //To show negative button
	        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					arg0.dismiss();
					
				}
			});
	        builder.create();
	        builder.show();
		}
		
		if(v.getId()==R.id.btnCaution)
		{
			//myView.addNewImg("Build");
			btnClickImage = "caution";
			addToImages();
		}
		
		if(v.getId()==R.id.btnWarning)
		{
			//myView.addNewImg("Build");
			btnClickImage = "warning";
			addToImages();
		}
		
		if(v.getId()==R.id.btnDanger)
		{
			//myView.addNewImg("Build");
			btnClickImage = "danger";
			addToImages();
		}
		if(v.getId()==R.id.btnDG)
		{
			//myView.addNewImg("Build");
			btnClickImage = "dg";
			addToImages();
		}
		
		if(v.getId()==R.id.btnSheter)
		{
			//myView.addNewImg("Build");
			btnClickImage = "shelter";
			addToImages();
		}
		
		if(v.getId()==R.id.btnSave)
		{
			//myView.addNewImg("Build");
			btnClickImage = "save";
			saveBitmapFile();
			Intent noOfAdjBuildings = new Intent(this , NoOfAdjacentBuildingsActivity.class);
			Bundle b = new Bundle();		
			b.putString("ipid",IPID);
			b.putDouble("latitude",latitude);
			b.putDouble("longitude",longitude);
			noOfAdjBuildings.putExtras(b);
			startActivity(noOfAdjBuildings);
			startActivity(noOfAdjBuildings);
		}
		
		if(v.getId()==R.id.btnTextUndo)
		{
			//myView.addNewImg("Build");
			btnClickImage = "textundo";
			if(writeList!=null){				
				if(writeList.size() > 0){					
					writeList.remove(writeList.size() - 1);					
					}					
				}
				myView.writeText(writeList);
			
		}
		
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		//Toast.makeText(this,"fired",Toast.LENGTH_SHORT).show();
		
		
		runningX = event.getX();
		runningY = event.getY();
		if(event.getAction() == MotionEvent.ACTION_DOWN)
		{	
			if(!drwline)
				{
					checkList();
				}
			if(MyView.x1==0 && drwline)
			{
			Toast.makeText(this,"line start",Toast.LENGTH_SHORT).show();
			MyView.x1=runningX;
			MyView.y1=runningY;
			}
			if(MyView.x1 !=0 && drwline)
			{
				if(tempx ==0 && tempy == 0){
					tempx = runningX;
					tempy = runningY;	
				}
				else{
					linesdraw.add(runningX);
					linesdraw.add(runningY);
					linesdraw.add(tempx);
					linesdraw.add(tempy);
					myView.drawline(linesdraw);
					Toast.makeText(this,"line end",Toast.LENGTH_SHORT).show();
					tempx =0;
					tempy =0;
					drwline = false;
					MyView.x1=0;
					MyView.y1=0;
				}
			}
			
			if(boolDelete)
			{
				deleteImg();
			}
			
			actionMove = false;
			
		}
		else if(event.getAction() == MotionEvent.ACTION_MOVE){
				if(runningX != 0 && runningY!=0 )
					if(!boolDelete && !drwline)
					{
					myView.drawImage(runningX,runningY,imgposID,imgList);
				    actionMove = true;	
					}
				
		}
		else if(event.getAction() == MotionEvent.ACTION_UP){
			if(runningX != 0 && runningY!=0 ){			
				if(actionMove){
				for(int i = 0 ; i <imgList.size() ; i++){
					Image imgUP = imgList.get(i);
					if(imgUP.getuniqueID() == imgposID){
						imgUP.setXCoordinate(runningX);
						imgUP.setYCoordinate(runningY);
					}
				}
				imgposID = -1;
			  }
				
				if(MyView.x1==0 && drwline){
					//Toast.makeText(this,"in list",Toast.LENGTH_SHORT).show();
					
//						if(tempx ==0 && tempy == 0){
//							tempx = runningX;
//							tempy = runningY;	
//						}
//						else{
							linesdraw.add(MyView.x1);
							linesdraw.add(MyView.y2);
							linesdraw.add(runningX);
							linesdraw.add(runningY);
							myView.drawline(linesdraw);
							Toast.makeText(this,"line endd",Toast.LENGTH_SHORT).show();
							tempx =0;
							tempy =0;
							MyView.x1=0;
							MyView.y2=0;
							drwline = false;
						//}
					
							
				/*	if(tempx ==0 && tempy == 0){
						tempx = runningX;
						tempy = runningY;	
					}
					else{
						linesdraw.add(runningX);
						linesdraw.add(runningY);
						linesdraw.add(tempx);
						linesdraw.add(tempy);
						myView.drawline(linesdraw);
						Toast.makeText(this,"line endd",Toast.LENGTH_SHORT).show();
						tempx =0;
						tempy =0;
						drwline = false;
					}*/
					
				}
				
				if(!actionMove && iswrite){
					WriteTextLayout wobj = new WriteTextLayout(writeList.size() +1, runningX,runningY,textoncanvas);
					writeList.add(wobj);
					myView.writeText(writeList);
					iswrite = false;
				}
				
				actionMove = false;				
			}
	}
		
		return true;
		 
	}
	
	public void addToImages(){
		Bitmap newImg=null;
		int newID = 0;
		
		if(btnClickImage.equalsIgnoreCase("build")){
			newImg = BitmapFactory.decodeResource(getResources(),R.drawable.entry);
			newID = R.drawable.entry;
		}
		
		else if(btnClickImage.equalsIgnoreCase("tower")){
			
			if(curTower.equalsIgnoreCase("Rectangle"))
			{
			newImg = BitmapFactory.decodeResource(getResources(),R.drawable.tower_new);
			newID = R.drawable.tower_new;
			}
			if(curTower.equalsIgnoreCase("Triangle"))
			{
			newImg = BitmapFactory.decodeResource(getResources(),R.drawable.towert);
			newID = R.drawable.towert;
			}
			if(curTower.equalsIgnoreCase("Circle"))
			{
			newImg = BitmapFactory.decodeResource(getResources(),R.drawable.towercauto);
			newID = R.drawable.towercauto;
			}
		}
		
		else if(btnClickImage.equalsIgnoreCase("caution")){
			newImg = BitmapFactory.decodeResource(getResources(),R.drawable.caution_new);
			newID = R.drawable.caution_new;
		}
		
		else if(btnClickImage.equalsIgnoreCase("warning")){
			newImg = BitmapFactory.decodeResource(getResources(),R.drawable.warning_new);
			newID = R.drawable.warning_new;
		}
		else if(btnClickImage.equalsIgnoreCase("danger")){
			newImg = BitmapFactory.decodeResource(getResources(),R.drawable.danger_new);
			newID = R.drawable.danger_new;
		}
		
		else if(btnClickImage.equalsIgnoreCase("dg")){
			newImg = BitmapFactory.decodeResource(getResources(),R.drawable.dg_new);
			newID = R.drawable.dg_new;
		}
		else if(btnClickImage.equalsIgnoreCase("north")){
			newImg = BitmapFactory.decodeResource(getResources(),R.drawable.direction_new);
			newID = R.drawable.direction_new;
		}
		else if(btnClickImage.equalsIgnoreCase("shelter")){
			newImg = BitmapFactory.decodeResource(getResources(),R.drawable.shelter_new);
			newID = R.drawable.shelter_new;
		}
		
		
		/*else if(btnClickImage.equalsIgnoreCase("writetext")){
			customView.writeText(textoncanvas);
			newImg = BitmapFactory.decodeFile(textoncanvas+".png");
		}*/		
		int uniID = imgList.size();
		Image item;
		if(btnClickImage.equalsIgnoreCase("writetext"))
			 item = new Image(uniID +1 ,20,15,newImg.getWidth(),newImg.getHeight(),uniID+1,textoncanvas);
		else
			 item = new Image(newID ,20,15,newImg.getWidth(),newImg.getHeight(),uniqID,"");
		uniqID=uniqID+1;
		imgList.add(item);		
		
		if(!btnClickImage.equalsIgnoreCase("writetext")){
			myView.drawNewImage(imgList);
		}
		else{
			myView.copyList(imgList);
		}
		
		btnClickImage = "";
	}
	
	public void initList(){
		imgList = new ArrayList<Image>();
		Bitmap newImg=null;
		int newID = 0;
		if(twrType.equalsIgnoreCase("Rectangle"))
		{
		newImg = BitmapFactory.decodeResource(getResources(),R.drawable.rectangle_img);
		newID = R.drawable.rectangle_img;
		}
		else if(twrType.equalsIgnoreCase("Circle"))
		{
			newImg = BitmapFactory.decodeResource(getResources(),R.drawable.circle_img);
			newID = R.drawable.circle_img;	
		}
		else
		{
			newImg = BitmapFactory.decodeResource(getResources(),R.drawable.trangle_img);
			newID = R.drawable.trangle_img;
		}
		
		Image item = new Image(newID ,screenWidth/2-100,screenHeight/2-40,newImg.getWidth(),newImg.getHeight(),uniqID,"");
		uniqID=uniqID+1;
		imgList.add(item);
		
		newImg = BitmapFactory.decodeResource(getResources(),R.drawable.direction_new);
		newID = R.drawable.direction_new;
		Image item1 = new Image(newID ,screenWidth-270,10,newImg.getWidth(),newImg.getHeight(),uniqID,"");
		uniqID=uniqID+1;
		imgList.add(item1);
		
		
		Image img=imgList.get(0);
		if(twrType.equalsIgnoreCase("Triangle"))
		{
		ln1x1=19;ln1y1=17;ln1x2=screenWidth/2-100+(img.getW()/2);ln1y2=screenHeight/2-40;
		ln2x1=24;ln2y1=screenHeight-71;ln2x2=screenWidth/2-100;ln2y2=screenHeight/2-40+(img.getH());
		ln3x1=screenWidth-175;ln3y1=22;ln3x2=screenWidth/2-100+(img.getW()/2);ln3y2=screenHeight/2-40;
		ln4x1=screenWidth-175;ln4y1=screenHeight-72;ln4x2=screenWidth/2-100+(img.getW());ln4y2=screenHeight/2-40+(img.getH());
		}
		else
		{
			ln1x1=19;ln1y1=17;ln1x2=screenWidth/2-100;ln1y2=screenHeight/2-40;
			ln2x1=24;ln2y1=screenHeight-71;ln2x2=screenWidth/2-100;ln2y2=screenHeight/2-40+(img.getH());
			ln3x1=screenWidth-175;ln3y1=22;ln3x2=screenWidth/2-100+(img.getW());ln3y2=screenHeight/2-40;
			ln4x1=screenWidth-175;ln4y1=screenHeight-72;ln4x2=screenWidth/2-100+(img.getW());ln4y2=screenHeight/2-40+(img.getH());
		}
		myView.drawNewImage(imgList);
		
	 }
	
	
	public void deleteImg()
	{
		for(int i=0;i<imgList.size();i++){
			 Image img = imgList.get(i);
			 
			 float x = img.getXCoordinate();
			 float y = img.getYCoordinate();
			 float dx = img.getW() + x;
			 float dy = img.getH() + y;
			 if(runningX < dx && runningX >= x){
				 if(runningY < dy && runningY >= y){
					 
					 System.out.println("Deletee");
					 imgposID = 0;
					 imgList.remove(i);
					 myView.drawNewImage(imgList);
					 boolDelete=false;
					 
				 }
			 }
			 
		}	
	}
	
	@SuppressLint("NewApi")
	public void checkList()
	{
		//Toast.makeText(this,"checklist", Toast.LENGTH_LONG).show();
		imgposID=0;
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
	
	public void chooseTower()
	{
		builder = new AlertDialog.Builder(this);
		builder.setTitle("Choose Tower :");
		builder.setCancelable(true);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				btnClickImage = "tower";
				addToImages();
			}
		});
		builder.setSingleChoiceItems(strTower, -1, new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
				curTower = strTower[which];
			}
		});
        builder.show();
		
	}
	
	
	@SuppressLint("NewApi")
	public void saveBitmapFile(){
	    String imgName = IPID +"_"+curLayout;
	    Toast.makeText(this,imgName,Toast.LENGTH_SHORT).show();
		File imgFile = new  File(Environment.getExternalStorageDirectory()+"/"+imgName+".png");		 
		 FileOutputStream outStream;
		 myView.buildDrawingCache(true);
		// imgTest = (ImageView)findViewById(R.id.test);
		 Bitmap bmp = myView.getDrawingCache();
	        try {
	        //outStream = openFileOutput(imgName, MODE_PRIVATE);
	        	outStream = new FileOutputStream(imgFile);
	        bmp.compress(Bitmap.CompressFormat.PNG, 85, outStream);
	        outStream.flush();
	        outStream.close();
	    
	        //File imgFile = new  File(Environment.getExternalStorageDirectory()+ "/"+imgName+".png");
	        
	        	objAdapter.openDataBase();
	           // String path = imgName;	          
	          //  long v=objAdapter.saveImagePathToDatabase("TOWER_BL", ipid, imgName,"TOWER_BL");
	            long v=objAdapter.saveImagePathToDatabase(imgFile.getAbsolutePath(), IPID, imgName,"TOWER_BL");
	            Toast.makeText(this,"L:"+String.valueOf(v),Toast.LENGTH_LONG).show();
	        
	        }catch (Exception e) {	        
	        	Toast.makeText(getBaseContext(), e.getMessage(),Toast.LENGTH_LONG).show();
	       } 
	       /* Toast.makeText(this,"Layout stored",Toast.LENGTH_SHORT).show();
            Intent iAdjBldg = new Intent(this,AdjBldgLayoutActivity.class);
            iAdjBldg.putExtra("ipid",ipid);
            startActivity(iAdjBldg);*/

	}
	
/*	@SuppressLint("NewApi")
	public void saveBitmapFile(){
	    String imgName = IPID +"_"+"tower";
	    Toast.makeText(this,imgName,Toast.LENGTH_SHORT).show();
		File imgFile = new  File(Environment.getExternalStorageDirectory()+"/"+imgName+".png");		 
		 FileOutputStream outStream;
		 myView.buildDrawingCache(true);
		// imgTest = (ImageView)findViewById(R.id.test);
		 Bitmap bmp = myView.getDrawingCache();
	        try {
	        //outStream = openFileOutput(imgName, MODE_PRIVATE);
	        	outStream = new FileOutputStream(imgFile);
	        bmp.compress(Bitmap.CompressFormat.PNG, 85, outStream);
	        outStream.flush();
	        outStream.close();
	    
	        //File imgFile = new  File(Environment.getExternalStorageDirectory()+ "/"+imgName+".png");
	        
	        	objAdapter.openDataBase();
	            String path = imgName;	          
	          //  long v=objAdapter.saveImagePathToDatabase("TOWER_BL", ipid, imgName,"TOWER_BL");
	            long v=objAdapter.saveImagePathToDatabase(imgFile.getAbsolutePath(), IPID, imgName,"TOWER_BL");
	          //  Toast.makeText(this,"L:"+String.valueOf(v),Toast.LENGTH_LONG).show();
	        
	        }catch (Exception e) {	        
	        	Toast.makeText(getBaseContext(), e.getMessage(),Toast.LENGTH_LONG).show();
	       } 

	}*/
	
	public void imgOption()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(
				MainualDrawActivity.this);
		// Title
		builder.setTitle("Select tower type :");
		builder.setSingleChoiceItems(strTower, -1,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						twrType=strTower[which];
						dialog.cancel();
						initList();
						
					}
				});
	
		
		builder.create();
		builder.show();
	}
	
 }